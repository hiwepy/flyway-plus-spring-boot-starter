package org.flywaydb.spring.boot.ext;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link FlywayMigrationProvider}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class FlywayMigrationProviderTest {

    @Test
    void migrateRunsFlywayAndPublishesEvent() {
        Flyway flyway = mock(Flyway.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        FlywayMigrationProvider provider = new FlywayMigrationProvider();
        provider.setApplicationEventPublisher(publisher);

        provider.migrate(flyway);

        verify(flyway).migrate();
        verify(publisher).publishEvent(any(FlywayMigratedEvent.class));
    }

    @Test
    void migrateHandlesExceptionGracefully() {
        Flyway flyway = mock(Flyway.class);
        doThrow(new RuntimeException("Migration failed")).when(flyway).migrate();

        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        FlywayMigrationProvider provider = new FlywayMigrationProvider();
        provider.setApplicationEventPublisher(publisher);

        // Should not throw
        provider.migrate(flyway);

        verify(flyway).migrate();
        // Event should not be published on failure
        verify(publisher, never()).publishEvent(any());
    }

    @Test
    void getAndSetApplicationEventPublisher() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        FlywayMigrationProvider provider = new FlywayMigrationProvider();
        assertThat(provider.getApplicationEventPublisher()).isNull();

        provider.setApplicationEventPublisher(publisher);
        assertThat(provider.getApplicationEventPublisher()).isSameAs(publisher);
    }

    @Test
    void implementsFlywayMigrationStrategy() {
        FlywayMigrationProvider provider = new FlywayMigrationProvider();
        assertThat(provider).isInstanceOf(FlywayMigrationStrategy.class);
    }
}
