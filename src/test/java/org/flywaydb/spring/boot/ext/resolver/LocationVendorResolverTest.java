package org.flywaydb.spring.boot.ext.resolver;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link LocationVendorResolver}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class LocationVendorResolverTest {

    @Test
    void returnsOriginalWhenNoVendorPlaceholder() {
        DataSource dataSource = mock(DataSource.class);
        LocationVendorResolver resolver = new LocationVendorResolver(dataSource);

        String[] result = resolver.resolveLocations(new String[]{"classpath:db/migration"});
        assertThat(result).containsExactly("classpath:db/migration");
    }

    @Test
    void returnsOriginalWhenEmptyArray() {
        DataSource dataSource = mock(DataSource.class);
        LocationVendorResolver resolver = new LocationVendorResolver(dataSource);

        String[] result = resolver.resolveLocations(new String[]{});
        assertThat(result).isEmpty();
    }

    @Test
    void resolvesLocationsFromCollection() {
        DataSource dataSource = mock(DataSource.class);
        LocationVendorResolver resolver = new LocationVendorResolver(dataSource);

        Collection<String> locations = Arrays.asList("classpath:db/migration");
        String[] result = resolver.resolveLocations(locations);
        assertThat(result).containsExactly("classpath:db/migration");
    }

    @Test
    void resolvesLocationsFromCollectionWithVendorPlaceholder() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        DatabaseMetaData metaData = mock(DatabaseMetaData.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getURL()).thenReturn("jdbc:h2:mem:test");

        LocationVendorResolver resolver = new LocationVendorResolver(dataSource);
        Collection<String> locations = Arrays.asList("classpath:db/migration/{vendor}");
        String[] result = resolver.resolveLocations(locations);
        assertThat(result).hasSize(1);
        assertThat(result[0]).contains("h2");
    }

    @Test
    void resolvesMultipleLocationsWithVendorPlaceholder() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        DatabaseMetaData metaData = mock(DatabaseMetaData.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getURL()).thenReturn("jdbc:mysql://localhost/test");

        LocationVendorResolver resolver = new LocationVendorResolver(dataSource);
        String[] result = resolver.resolveLocations(new String[]{
                "classpath:db/migration/{vendor}",
                "classpath:db/data"
        });
        assertThat(result).hasSize(2);
        assertThat(result[0]).contains("mysql");
        assertThat(result[1]).isEqualTo("classpath:db/data");
    }

    @Test
    void returnsOriginalWhenUnknownVendor() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        DatabaseMetaData metaData = mock(DatabaseMetaData.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getURL()).thenReturn("jdbc:unknown:mem:test");

        LocationVendorResolver resolver = new LocationVendorResolver(dataSource);
        String[] result = resolver.resolveLocations(new String[]{"classpath:db/migration/{vendor}"});
        assertThat(result).containsExactly("classpath:db/migration/{vendor}");
    }

    @Test
    void throwsWhenDataSourceAccessFails() throws SQLException {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection failed"));

        LocationVendorResolver resolver = new LocationVendorResolver(dataSource);
        assertThatThrownBy(() -> resolver.resolveLocations(new String[]{"classpath:db/migration/{vendor}"}))
                .isInstanceOf(IllegalStateException.class);
    }
}
