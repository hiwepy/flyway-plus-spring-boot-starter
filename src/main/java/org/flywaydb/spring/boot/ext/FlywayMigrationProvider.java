package org.flywaydb.spring.boot.ext;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// FlywayMigrationStrategy is now a local interface in this package
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Flyway migration strategy that runs the migration and publishes a
 * {@link FlywayMigratedEvent} on completion.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class FlywayMigrationProvider implements FlywayMigrationStrategy, ApplicationEventPublisherAware {

	protected static Logger LOG = LoggerFactory.getLogger(FlywayMigrationProvider.class);

	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * Runs the Flyway migration and publishes a {@link FlywayMigratedEvent} afterwards.
	 * @param flyway the Flyway instance to migrate
	 */
	@Override
	public void migrate(Flyway flyway) {

        try {
        	LOG.info("[Start] Flyway Migration run .. ");
        	// Execute the migration
			flyway.migrate();
			// Publish the migration-completed notification
			getApplicationEventPublisher().publishEvent(new FlywayMigratedEvent(this));

			LOG.info("[End] Flyway Migration run .. ");

		} catch (Exception e) {
			LOG.error("Flyway Migrated Error . ", e);
		}

	}

	/**
	 * Sets the application event publisher used to broadcast migration events.
	 * @param applicationEventPublisher the event publisher
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * Returns the application event publisher.
	 * @return the event publisher
	 */
	public ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

}
