package org.flywaydb.spring.boot;

import org.flywaydb.spring.boot.ext.FlywayModularizedProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.time.Duration;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FlywayPropertiesBindingTest {

    @Test
    void bindsAggregateAndModuleProperties() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("spring.flyway.moduleable", "true");
        values.put("spring.flyway.modules[0].module", "orders");
        values.put("spring.flyway.modules[0].enabled", "false");
        values.put("spring.flyway.modules[0].url", "jdbc:h2:mem:orders");
        values.put("spring.flyway.modules[0].connect-retries", "3");
        values.put("spring.flyway.modules[0].connect-retries-interval", "15s");
        values.put("spring.flyway.modules[0].schemas", "orders,audit");
        values.put("spring.flyway.modules[0].locations", "classpath:db/{module}/{vendor}");
        values.put("spring.flyway.modules[0].placeholders.tenant", "acme");
        values.put("spring.flyway.modules[0].ignore-migration-patterns", "*:missing");

        FlywayModularizedMigrationProperties aggregate = new Binder(
                new MapConfigurationPropertySource(values))
                .bind(FlywayModularizedMigrationProperties.PREFIX,
                        Bindable.of(FlywayModularizedMigrationProperties.class)).get();

        assertThat(aggregate.isModuleable()).isTrue();
        assertThat(aggregate.getModules()).hasSize(1);
        FlywayModularizedProperties module = aggregate.getModules().get(0);
        assertThat(module.getModule()).isEqualTo("orders");
        assertThat(module.isEnabled()).isFalse();
        assertThat(module.isCreateDataSource()).isTrue();
        assertThat(module.getConnectRetries()).isEqualTo(3);
        assertThat(module.getConnectRetriesInterval()).isEqualTo(Duration.ofSeconds(15));
        assertThat(module.getSchemas()).containsExactly("orders", "audit");
        assertThat(module.getLocations()).containsExactly("classpath:db/{module}/{vendor}");
        assertThat(module.getPlaceholders()).containsEntry("tenant", "acme");
        assertThat(module.getIgnoreMigrationPatterns()).containsExactly("*:missing");
    }

    @Test
    void exposesSafeDefaultsAndMutableAggregate() {
        FlywayModularizedMigrationProperties aggregate = new FlywayModularizedMigrationProperties();
        assertThat(aggregate.isModuleable()).isFalse();
        aggregate.setModuleable(true);
        aggregate.setModules(java.util.Collections.singletonList(new FlywayModularizedProperties()));
        assertThat(aggregate.isModuleable()).isTrue();
        assertThat(aggregate.getModules()).hasSize(1);

        FlywayModularizedProperties module = aggregate.getModules().get(0);
        assertThat(module.getModule()).isEqualTo("module");
        assertThat(module.getEncoding().name()).isEqualTo("UTF-8");
        assertThat(module.getConnectRetriesInterval()).isEqualTo(Duration.ofSeconds(120));
        assertThat(module.getTable()).isEqualTo("flyway_{module}_schema_history");
        assertThat(module.getBaselineDescription()).isEqualTo("<< Flyway Modularized Baseline >>");
        assertThat(module.isCreateDataSource()).isFalse();
        module.setUser("sa");
        assertThat(module.isCreateDataSource()).isTrue();
    }

    @Test
    void everyConfigurationPropertySupportsBeanBinding() throws Exception {
        FlywayModularizedProperties properties = new FlywayModularizedProperties();
        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(
                FlywayModularizedProperties.class, Object.class).getPropertyDescriptors()) {
            if (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
                continue;
            }
            Object value = sampleValue(descriptor.getPropertyType(), descriptor.getName());
            descriptor.getWriteMethod().invoke(properties, value);
            assertThat(descriptor.getReadMethod().invoke(properties))
                    .as(descriptor.getName()).isEqualTo(value);
        }
    }

    private Object sampleValue(Class<?> type, String name) {
        if (type == String.class) {
            return name + "-value";
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.TRUE;
        }
        if (type == int.class) {
            return 7;
        }
        if (type == java.nio.charset.Charset.class) {
            return StandardCharsets.ISO_8859_1;
        }
        if (type == Duration.class) {
            return Duration.ofSeconds(7);
        }
        if (type == File.class) {
            return new File("target/" + name);
        }
        if (type == String[].class) {
            return new String[] {name + "-value"};
        }
        if (List.class.isAssignableFrom(type)) {
            return new ArrayList<>(java.util.Collections.singletonList(name + "-value"));
        }
        if (Map.class.isAssignableFrom(type)) {
            Map<String, String> value = new HashMap<>();
            value.put(name, "value");
            return value;
        }
        throw new IllegalArgumentException("Unsupported property type: " + type);
    }
}
