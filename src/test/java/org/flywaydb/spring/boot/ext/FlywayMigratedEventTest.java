package org.flywaydb.spring.boot.ext;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FlywayMigratedEvent}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class FlywayMigratedEventTest {

    @Test
    void constructorSetsSource() {
        Object source = new Object();
        FlywayMigratedEvent event = new FlywayMigratedEvent(source);
        assertThat(event.getSource()).isSameAs(source);
    }

    @Test
    void sourceIsPreserved() {
        String source = "test-source";
        FlywayMigratedEvent event = new FlywayMigratedEvent(source);
        assertThat(event.getSource()).isEqualTo("test-source");
    }
}
