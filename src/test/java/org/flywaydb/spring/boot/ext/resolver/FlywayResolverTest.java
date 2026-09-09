package org.flywaydb.spring.boot.ext.resolver;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class FlywayResolverTest {

    @Test
    void resolvesModuleInArraysCollectionsAndTables() {
        LocationModuleResolver locations = new LocationModuleResolver("orders");
        assertThat(locations.resolveLocations(new String[] {"classpath:db/{module}", "classpath:shared"}))
                .containsExactly("classpath:db/orders", "classpath:shared");
        assertThat(locations.resolveLocations(Arrays.asList("db/{module}"))).containsExactly("db/orders");
        assertThat(locations.resolveLocations(new String[] {"db/shared"})).containsExactly("db/shared");

        TableModuleResolver tables = new TableModuleResolver("orders");
        assertThat(tables.resolveTable("flyway_{module}_history")).isEqualTo("flyway_orders_history");
        assertThat(tables.resolveTable("flyway_history")).isEqualTo("flyway_history");
    }

    @Test
    void resolvesVendorFromRealJdbcMetadata() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:resolver;DB_CLOSE_DELAY=-1");
        LocationVendorResolver resolver = new LocationVendorResolver(dataSource);

        assertThat(resolver.resolveLocations(new String[] {"classpath:db/{vendor}", "classpath:shared"}))
                .containsExactly("classpath:db/h2", "classpath:shared");
        assertThat(resolver.resolveLocations(Arrays.asList("classpath:db/{vendor}")))
                .containsExactly("classpath:db/h2");
        assertThat(resolver.resolveLocations(new String[] {"classpath:db/common"}))
                .containsExactly("classpath:db/common");
    }
}
