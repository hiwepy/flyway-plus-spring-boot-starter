package org.flywaydb.spring.boot;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FlywayModularizedMigrationProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class FlywayModularizedMigrationPropertiesTest {

    @Test
    void defaultValues() {
        FlywayModularizedMigrationProperties props = new FlywayModularizedMigrationProperties();
        assertThat(props.getModules()).isEmpty();
        assertThat(props.isModuleable()).isFalse();
    }

    @Test
    void prefix() {
        assertThat(FlywayModularizedMigrationProperties.PREFIX).isEqualTo("spring.flyway");
    }

    @Test
    void settersAndGetters() {
        FlywayModularizedMigrationProperties props = new FlywayModularizedMigrationProperties();
        props.setModuleable(true);
        assertThat(props.isModuleable()).isTrue();

        org.flywaydb.spring.boot.ext.FlywayModularizedProperties module = new org.flywaydb.spring.boot.ext.FlywayModularizedProperties();
        module.setModule("test");
        props.setModules(Arrays.asList(module));
        assertThat(props.getModules()).hasSize(1);
        assertThat(props.getModules().get(0).getModule()).isEqualTo("test");
    }
}
