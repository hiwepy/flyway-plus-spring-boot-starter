package org.flywaydb.spring.boot.ext.resolver;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link TableModuleResolver}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class TableModuleResolverTest {

    @Test
    void replacesModulePlaceholder() {
        TableModuleResolver resolver = new TableModuleResolver("mymodule");
        String table = resolver.resolveTable("flyway_{module}_schema_history");
        assertThat(table).isEqualTo("flyway_mymodule_schema_history");
    }

    @Test
    void noPlaceholderReturnsOriginal() {
        TableModuleResolver resolver = new TableModuleResolver("mymodule");
        String table = resolver.resolveTable("flyway_schema_history");
        assertThat(table).isEqualTo("flyway_schema_history");
    }

    @Test
    void emptyModuleReplacesWithEmpty() {
        TableModuleResolver resolver = new TableModuleResolver("");
        String table = resolver.resolveTable("flyway_{module}_schema_history");
        assertThat(table).isEqualTo("flyway__schema_history");
    }

    @Test
    void nullTableThrowsNPE() {
        TableModuleResolver resolver = new TableModuleResolver("mymodule");
        assertThatThrownBy(() -> resolver.resolveTable(null))
                .isInstanceOf(NullPointerException.class);
    }
}
