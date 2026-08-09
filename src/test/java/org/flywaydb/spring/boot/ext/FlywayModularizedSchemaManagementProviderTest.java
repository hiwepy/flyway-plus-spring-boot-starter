package org.flywaydb.spring.boot.ext;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.SchemaManagement;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link FlywayModularizedSchemaManagementProvider}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class FlywayModularizedSchemaManagementProviderTest {

    @Test
    void returnsManagedWhenDataSourceMatches() {
        DataSource dataSource = mock(DataSource.class);
        Flyway flyway = mock(Flyway.class);
        Configuration config = mock(Configuration.class);
        when(flyway.getConfiguration()).thenReturn(config);
        when(config.getDataSource()).thenReturn(dataSource);

        FlywayModularizedSchemaManagementProvider provider =
                new FlywayModularizedSchemaManagementProvider(Collections.singletonList(flyway));

        assertThat(provider.getSchemaManagement(dataSource)).isEqualTo(SchemaManagement.MANAGED);
    }

    @Test
    void returnsUnmanagedWhenDataSourceDoesNotMatch() {
        DataSource dataSource = mock(DataSource.class);
        DataSource otherDataSource = mock(DataSource.class);
        Flyway flyway = mock(Flyway.class);
        Configuration config = mock(Configuration.class);
        when(flyway.getConfiguration()).thenReturn(config);
        when(config.getDataSource()).thenReturn(dataSource);

        FlywayModularizedSchemaManagementProvider provider =
                new FlywayModularizedSchemaManagementProvider(Collections.singletonList(flyway));

        assertThat(provider.getSchemaManagement(otherDataSource)).isEqualTo(SchemaManagement.UNMANAGED);
    }

    @Test
    void returnsUnmanagedWhenNoFlyways() {
        DataSource dataSource = mock(DataSource.class);

        FlywayModularizedSchemaManagementProvider provider =
                new FlywayModularizedSchemaManagementProvider(Collections.emptyList());

        assertThat(provider.getSchemaManagement(dataSource)).isEqualTo(SchemaManagement.UNMANAGED);
    }

    @Test
    void checksMultipleFlyways() {
        DataSource dataSource1 = mock(DataSource.class);
        DataSource dataSource2 = mock(DataSource.class);

        Flyway flyway1 = mock(Flyway.class);
        Configuration config1 = mock(Configuration.class);
        when(flyway1.getConfiguration()).thenReturn(config1);
        when(config1.getDataSource()).thenReturn(dataSource1);

        Flyway flyway2 = mock(Flyway.class);
        Configuration config2 = mock(Configuration.class);
        when(flyway2.getConfiguration()).thenReturn(config2);
        when(config2.getDataSource()).thenReturn(dataSource2);

        FlywayModularizedSchemaManagementProvider provider =
                new FlywayModularizedSchemaManagementProvider(Arrays.asList(flyway1, flyway2));

        assertThat(provider.getSchemaManagement(dataSource1)).isEqualTo(SchemaManagement.MANAGED);
        assertThat(provider.getSchemaManagement(dataSource2)).isEqualTo(SchemaManagement.MANAGED);
    }

    @Test
    void implementsSchemaManagementProvider() {
        FlywayModularizedSchemaManagementProvider provider =
                new FlywayModularizedSchemaManagementProvider(Collections.emptyList());
        assertThat(provider).isInstanceOf(org.springframework.boot.jdbc.SchemaManagementProvider.class);
    }
}
