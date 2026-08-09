package org.flywaydb.spring.boot.ext;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FlywayFluentConfiguration}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class FlywayFluentConfigurationTest {

    @Test
    void constructorSetsModule() {
        FlywayFluentConfiguration config = new FlywayFluentConfiguration("mymodule", "Baseline", "1");
        assertThat(config.getModule()).isEqualTo("mymodule");
    }

    @Test
    void constructorSetsBaselineDescription() {
        FlywayFluentConfiguration config = new FlywayFluentConfiguration("mymodule", "Custom Baseline", "1");
        assertThat(config.getBaselineDescription()).isEqualTo("Custom Baseline");
    }

    @Test
    void constructorWithClassLoader() {
        FlywayFluentConfiguration config = new FlywayFluentConfiguration(
                Thread.currentThread().getContextClassLoader(), "mymodule", "Baseline", "1");
        assertThat(config.getModule()).isEqualTo("mymodule");
    }

    @Test
    void locationsResolvesModulePlaceholder() {
        FlywayFluentConfiguration config = new FlywayFluentConfiguration("auth", "Baseline", "1");
        config.locations("classpath:db/migration/{module}/{vendor}");
        List<String> locationAsStrings = config.getLocationAsStrings();
        assertThat(locationAsStrings).isNotEmpty();
        assertThat(locationAsStrings.get(0)).contains("auth");
    }

    @Test
    void getTableReturnsTableName() {
        FlywayFluentConfiguration config = new FlywayFluentConfiguration("mymodule", "Baseline", "1");
        String table = config.getTable();
        assertThat(table).isNotNull();
        // Table should contain the module name from the default pattern
        assertThat(table).containsIgnoringCase("module");
    }

    @Test
    void getLocationAsStringsReturnsConfiguredLocations() {
        FlywayFluentConfiguration config = new FlywayFluentConfiguration("mymodule", "Baseline", "1");
        List<String> locations = config.getLocationAsStrings();
        assertThat(locations).isNotEmpty();
    }

    @Test
    void defaultModuleIsModule() {
        FlywayFluentConfiguration config = new FlywayFluentConfiguration("module", "Baseline", "1");
        assertThat(config.getModule()).isEqualTo("module");
    }

    @Test
    void multipleLocations() {
        FlywayFluentConfiguration config = new FlywayFluentConfiguration("auth", "Baseline", "1");
        config.locations(
                "classpath:db/migration/{module}/{vendor}",
                "classpath:db/data/{module}"
        );
        List<String> locationAsStrings = config.getLocationAsStrings();
        assertThat(locationAsStrings).hasSize(2);
    }
}
