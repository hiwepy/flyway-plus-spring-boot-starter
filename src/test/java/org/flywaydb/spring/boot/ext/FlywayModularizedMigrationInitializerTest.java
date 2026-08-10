package org.flywaydb.spring.boot.ext;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link FlywayModularizedMigrationInitializer}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class FlywayModularizedMigrationInitializerTest {

    @Test
    void defaultOrderIsZero() {
        FlywayModularizedMigrationInitializer initializer =
                new FlywayModularizedMigrationInitializer(Collections.emptyList());
        assertThat(initializer.getOrder()).isEqualTo(0);
    }

    @Test
    void setAndGetOrder() {
        FlywayModularizedMigrationInitializer initializer =
                new FlywayModularizedMigrationInitializer(Collections.emptyList());
        initializer.setOrder(5);
        assertThat(initializer.getOrder()).isEqualTo(5);
    }

    @Test
    void afterPropertiesSetMigratesAllFlyways() throws Exception {
        Flyway flyway1 = mock(Flyway.class);
        Flyway flyway2 = mock(Flyway.class);

        FlywayModularizedMigrationInitializer initializer =
                new FlywayModularizedMigrationInitializer(Arrays.asList(flyway1, flyway2));

        initializer.afterPropertiesSet();

        verify(flyway1).migrate();
        verify(flyway2).migrate();
    }

    @Test
    void afterPropertiesSetUsesMigrationStrategy() throws Exception {
        Flyway flyway1 = mock(Flyway.class);
        Flyway flyway2 = mock(Flyway.class);
        FlywayMigrationStrategy strategy = mock(FlywayMigrationStrategy.class);

        FlywayModularizedMigrationInitializer initializer =
                new FlywayModularizedMigrationInitializer(Arrays.asList(flyway1, flyway2), strategy);

        initializer.afterPropertiesSet();

        verify(strategy).migrate(flyway1);
        verify(strategy).migrate(flyway2);
    }

    @Test
    void constructorWithNullFlywaysThrows() {
        assertThatThrownBy(() -> new FlywayModularizedMigrationInitializer(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Flyways must not be null");
    }

    @Test
    void constructorWithSingleArgDelegatesToTwoArg() throws Exception {
        Flyway flyway = mock(Flyway.class);
        FlywayModularizedMigrationInitializer initializer =
                new FlywayModularizedMigrationInitializer(Collections.singletonList(flyway));

        initializer.afterPropertiesSet();
        verify(flyway).migrate();
    }

    @Test
    void implementsOrdered() {
        FlywayModularizedMigrationInitializer initializer =
                new FlywayModularizedMigrationInitializer(Collections.emptyList());
        assertThat(initializer).isInstanceOf(org.springframework.core.Ordered.class);
    }

    @Test
    void implementsInitializingBean() {
        FlywayModularizedMigrationInitializer initializer =
                new FlywayModularizedMigrationInitializer(Collections.emptyList());
        assertThat(initializer).isInstanceOf(org.springframework.beans.factory.InitializingBean.class);
    }
}
