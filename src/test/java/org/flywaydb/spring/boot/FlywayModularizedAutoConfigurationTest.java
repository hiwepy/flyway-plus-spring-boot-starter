package org.flywaydb.spring.boot;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.spring.boot.ext.FlywayFluentConfiguration;
import org.flywaydb.spring.boot.ext.FlywayMigrationProvider;
import org.flywaydb.spring.boot.ext.FlywayModularizedMigrationInitializer;
import org.flywaydb.spring.boot.ext.FlywayModularizedSchemaManagementProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FlywayModularizedAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    DataSourceAutoConfiguration.class,
                    FlywayModularizedAutoConfiguration.class))
            .withPropertyValues(
                    "spring.datasource.url=jdbc:h2:mem:autoconfig;DB_CLOSE_DELAY=-1",
                    "spring.datasource.username=sa",
                    "spring.datasource.password=",
                    "spring.flyway.moduleable=true",
                    "spring.flyway.modules[0].module=orders",
                    "spring.flyway.modules[0].check-location=false",
                    "spring.flyway.modules[0].locations=classpath:db/migration/test");

    @Test
    void createsFlywaysInitializerProviderAndConverterFromProperties() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(FlywayMigrationProvider.class);
            assertThat(context).hasSingleBean(FlywayModularizedMigrationInitializer.class);
            assertThat(context).hasSingleBean(FlywayModularizedSchemaManagementProvider.class);
            assertThat(context).hasBean("flywayDatasource");
            assertThat(context).hasBean("flyways");
            List<?> flyways = context.getBean("flyways", List.class);
            assertThat(flyways).hasSize(1);
            Flyway flyway = (Flyway) flyways.get(0);
            assertThat(flyway.getConfiguration().getTable()).isEqualTo("flyway_orders_schema_history");
            assertThat(flyway.info().current()).isNotNull();

            GenericConverter converter = context.getBean(GenericConverter.class);
            assertThat(converter.getConvertibleTypes()).hasSize(2);
            assertThat(converter.convert("5", TypeDescriptor.valueOf(String.class),
                    TypeDescriptor.valueOf(MigrationVersion.class))).isEqualTo(MigrationVersion.fromVersion("5"));
            assertThat(converter.convert(6, TypeDescriptor.valueOf(Number.class),
                    TypeDescriptor.valueOf(MigrationVersion.class))).isEqualTo(MigrationVersion.fromVersion("6"));
        });
    }

    @Test
    void alsoBuildsUserProvidedFluentConfigurations() {
        contextRunner.withBean(FlywayFluentConfiguration.class, () -> {
            FlywayFluentConfiguration configuration = new FlywayFluentConfiguration(
                    "catalog", "Catalog baseline", "1");
            configuration.dataSource("jdbc:h2:mem:fluent_autoconfig;DB_CLOSE_DELAY=-1", "sa", "");
            configuration.locations("classpath:db/migration/test");
            return configuration;
        }).run(context -> {
            List<?> flyways = context.getBean("flyways", List.class);
            assertThat(flyways).hasSize(2);
            assertThat(flyways).allMatch(Flyway.class::isInstance);
        });
    }

    @Test
    void backsOffWhenModularizedMigrationIsDisabled() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(FlywayModularizedAutoConfiguration.class))
                .withPropertyValues("spring.flyway.moduleable=false")
                .run(context -> assertThat(context).doesNotHaveBean(FlywayMigrationProvider.class));
    }
}
