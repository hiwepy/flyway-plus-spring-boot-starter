package org.flywaydb.spring.boot.ext;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.SchemaManagement;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class FlywayConfigurationContractTest {

    @Test
    void fluentConfigurationAppliesModuleDefaults() {
        FlywayFluentConfiguration configuration = new FlywayFluentConfiguration(
                "billing", "Billing baseline", "2");
        assertThat(configuration.getModule()).isEqualTo("billing");
        assertThat(configuration.getLocationAsStrings())
                .containsExactly("classpath:db/migration/billing/{vendor}");
        assertThat(configuration.getTable()).isEqualTo("flyway_{module}_schema_history");
        assertThat(configuration.getBaselineDescription()).isEqualTo("Billing baseline");
        assertThat(configuration.getBaselineVersion().getVersion()).isEqualTo("2");
        assertThat(configuration.isBaselineOnMigrate()).isTrue();

        FlywayFluentConfiguration withLoader = new FlywayFluentConfiguration(
                getClass().getClassLoader(), "inventory", "Inventory baseline", "3");
        withLoader.locations("classpath:custom/{module}");
        assertThat(withLoader.getLocationAsStrings()).containsExactly("classpath:custom/inventory");
    }

    @Test
    void initializerUsesStrategyForEveryFlywayAndExposesOrder() throws Exception {
        Flyway first = flyway("initializer_one");
        Flyway second = flyway("initializer_two");
        AtomicInteger migrations = new AtomicInteger();
        FlywayModularizedMigrationInitializer initializer = new FlywayModularizedMigrationInitializer(
                Arrays.asList(first, second), ignored -> migrations.incrementAndGet());
        initializer.setOrder(42);
        initializer.afterPropertiesSet();
        assertThat(initializer.getOrder()).isEqualTo(42);
        assertThat(migrations).hasValue(2);
    }

    @Test
    void initializerRejectsNullAndMigratesWithoutStrategy() throws Exception {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new FlywayModularizedMigrationInitializer(null));
        Flyway flyway = flyway("initializer_default");
        new FlywayModularizedMigrationInitializer(Collections.singletonList(flyway)).afterPropertiesSet();
        assertThat(flyway.info().current()).isNotNull();
    }

    @Test
    void migrationProviderPublishesCompletionAndContainsFailure() {
        AtomicReference<ApplicationEvent> event = new AtomicReference<>();
        FlywayMigrationProvider provider = new FlywayMigrationProvider();
        provider.setApplicationEventPublisher(new ApplicationEventPublisher() {
            @Override
            public void publishEvent(ApplicationEvent applicationEvent) {
                event.set(applicationEvent);
            }

            @Override
            public void publishEvent(Object publishedEvent) {
                event.set((ApplicationEvent) publishedEvent);
            }
        });
        assertThat(provider.getApplicationEventPublisher()).isNotNull();
        provider.migrate(flyway("provider_success"));
        assertThat(event.get()).isInstanceOf(FlywayMigratedEvent.class);

        Flyway failing = org.mockito.Mockito.mock(Flyway.class);
        org.mockito.Mockito.when(failing.migrate()).thenThrow(new IllegalStateException("migration failed"));
        provider.migrate(failing);
    }

    @Test
    void schemaProviderRecognizesOnlyManagedDataSources() {
        Flyway managed = flyway("managed_schema");
        FlywayModularizedSchemaManagementProvider provider =
                new FlywayModularizedSchemaManagementProvider(Collections.singletonList(managed));
        DataSource managedDataSource = managed.getConfiguration().getDataSource();
        assertThat(provider.getSchemaManagement(managedDataSource)).isEqualTo(SchemaManagement.MANAGED);
        assertThat(provider.getSchemaManagement(flyway("other_schema").getConfiguration().getDataSource()))
                .isEqualTo(SchemaManagement.UNMANAGED);
    }

    private Flyway flyway(String database) {
        return Flyway.configure()
                .dataSource("jdbc:h2:mem:" + database + ";DB_CLOSE_DELAY=-1", "sa", "")
                .locations("classpath:db/migration/test")
                .load();
    }
}
