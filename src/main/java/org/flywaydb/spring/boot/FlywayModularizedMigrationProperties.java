package org.flywaydb.spring.boot;

import java.util.ArrayList;
import java.util.List;

import org.flywaydb.spring.boot.ext.FlywayModularizedProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for modularized Flyway migrations. <p>Binds the
 * {@code spring.flyway} prefix and exposes the list of migration modules and the master
 * switch that enables modularized migrations.</p>
 *
 * @author Loong Wan (https://github.com/loong10k)
 * @since 1.0.0
 */
@ConfigurationProperties(FlywayModularizedMigrationProperties.PREFIX)
public class FlywayModularizedMigrationProperties {

	public static final String PREFIX = "spring.flyway";

	private List<FlywayModularizedProperties> modules = new ArrayList<>();

	private boolean moduleable = false;

	/**
	 * Returns the list of configured migration modules.
	 * @return the migration modules
	 */
	public List<FlywayModularizedProperties> getModules() {
		return modules;
	}

	/**
	 * Sets the list of configured migration modules.
	 * @param modules the migration modules
	 */
	public void setModules(List<FlywayModularizedProperties> modules) {
		this.modules = modules;
	}

	/**
	 * Returns whether modularized migrations are enabled.
	 * @return {@code true} if modularized migrations are enabled
	 */
	public boolean isModuleable() {
		return moduleable;
	}

	/**
	 * Enables or disables modularized migrations.
	 * @param moduleable whether modularized migrations are enabled
	 */
	public void setModuleable(boolean moduleable) {
		this.moduleable = moduleable;
	}

}