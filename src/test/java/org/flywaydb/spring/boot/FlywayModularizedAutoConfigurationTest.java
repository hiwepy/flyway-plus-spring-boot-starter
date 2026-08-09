package org.flywaydb.spring.boot;

import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.TypeDescriptor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for inner classes of {@link FlywayModularizedAutoConfiguration}.
 *
 * @author Loong Wan (https://github.com/loong10k)
 * @since 1.0.0
 */
class FlywayModularizedAutoConfigurationTest {

    @Test
    void stringOrNumberConverterConvertsString() throws Exception {
        Class<?> converterClass = Class.forName(
                "org.flywaydb.spring.boot.FlywayModularizedAutoConfiguration$StringOrNumberToMigrationVersionConverter");
        java.lang.reflect.Constructor<?> ctor = converterClass.getDeclaredConstructor();
        ctor.setAccessible(true);
        Object converter = ctor.newInstance();

        TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(MigrationVersion.class);

        java.lang.reflect.Method convertMethod = converterClass.getMethod("convert", Object.class, TypeDescriptor.class, TypeDescriptor.class);
        convertMethod.setAccessible(true);
        Object result = convertMethod.invoke(converter, "1.0", sourceType, targetType);

        assertThat(result).isEqualTo(MigrationVersion.fromVersion("1.0"));
    }

    @Test
    void stringOrNumberConverterConvertsNumber() throws Exception {
        Class<?> converterClass = Class.forName(
                "org.flywaydb.spring.boot.FlywayModularizedAutoConfiguration$StringOrNumberToMigrationVersionConverter");
        java.lang.reflect.Constructor<?> ctor = converterClass.getDeclaredConstructor();
        ctor.setAccessible(true);
        Object converter = ctor.newInstance();

        TypeDescriptor sourceType = TypeDescriptor.valueOf(Number.class);
        TypeDescriptor targetType = TypeDescriptor.valueOf(MigrationVersion.class);

        java.lang.reflect.Method convertMethod = converterClass.getMethod("convert", Object.class, TypeDescriptor.class, TypeDescriptor.class);
        convertMethod.setAccessible(true);
        Object result = convertMethod.invoke(converter, 42, sourceType, targetType);

        assertThat(result).isEqualTo(MigrationVersion.fromVersion("42"));
    }

    @Test
    void stringOrNumberConverterGetConvertibleTypes() throws Exception {
        Class<?> converterClass = Class.forName(
                "org.flywaydb.spring.boot.FlywayModularizedAutoConfiguration$StringOrNumberToMigrationVersionConverter");
        java.lang.reflect.Constructor<?> ctor = converterClass.getDeclaredConstructor();
        ctor.setAccessible(true);
        Object converter = ctor.newInstance();

        java.lang.reflect.Method getTypesMethod = converterClass.getMethod("getConvertibleTypes");
        getTypesMethod.setAccessible(true);
        java.util.Set<?> types = (java.util.Set<?>) getTypesMethod.invoke(converter);

        assertThat(types).hasSize(2);
    }

    @Test
    void autoConfigurationClassExists() {
        assertThat(FlywayModularizedAutoConfiguration.class).isNotNull();
    }

    @Test
    void innerConfigurationClassExists() throws Exception {
        Class<?> configClass = Class.forName(
                "org.flywaydb.spring.boot.FlywayModularizedAutoConfiguration$FlywayModularizedConfiguration");
        assertThat(configClass).isNotNull();
    }
}
