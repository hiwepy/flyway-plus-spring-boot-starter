/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.flywaydb.spring.boot.ext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.spring.boot.ext.resolver.LocationModuleResolver;

/**
 * Extended {@link FluentConfiguration} that adds a module-name parameter. <p>The module name
 * is substituted into the configured migration locations and schema-history table via the
 * {@code {module}} placeholder.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class FlywayFluentConfiguration extends FluentConfiguration {
	
	private static final String DEFAULT_FLYWAY_MODULE_PATH = "classpath:db/migration/{module}/{vendor}";
	private static final String DEFAULT_FLYWAY_MODULE_TABLE = "flyway_{module}_schema_history";
    
	/**
     * The module of Sql migrations. (default: module)
     */
	private String module = "module";
	/**
	 * The locations of migrations scripts.
	 */
	private List<String> locationAsStrings = new ArrayList<>();

    /**
     * Creates a new default configuration.
     * @param module The module of Sql migrations.
     * @param baselineDescription The description to tag an existing schema with when executing baseline. (default: &lt;&lt; Flyway Baseline &gt;&gt;)
     * @param baselineVersion The version to tag an existing schema with when executing baseline. (default: 1)
     */
    public FlywayFluentConfiguration(String module, String baselineDescription, String baselineVersion) {
    	super();
    	this.module = module;
    	this.init(baselineDescription, baselineVersion);
    }

    /**
     * Creates a new default configuration with this classloader.
     *
     * @param classLoader The ClassLoader to use for loading migrations, resolvers, etc from the classpath. (default: Thread.currentThread().getContextClassLoader() )
     * @param module The module of Sql migrations.
     * @param baselineDescription The description to tag an existing schema with when executing baseline. (default: &lt;&lt; Flyway Baseline &gt;&gt;)
     * @param baselineVersion The version to tag an existing schema with when executing baseline. (default: 1)
     */
    public FlywayFluentConfiguration(ClassLoader classLoader, String module, String baselineDescription, String baselineVersion) {
    	super(classLoader);
    	this.module = module;
    	this.init(baselineDescription, baselineVersion);
    }

	/**
	 * Sets the migration locations, resolving the {@code {module}} placeholder against the
	 * configured module name.
	 * @param locations the migration locations
	 * @return this configuration instance
	 */
    @Override
    public FluentConfiguration locations(String... locations) {
		String[] moduleLocations = new LocationModuleResolver(this.getModule())
				.resolveLocations(locations);
		this.locationAsStrings = Arrays.asList(moduleLocations);
    	return super.locations(moduleLocations);
    }

	/**
	 * Initializes the configuration with the default module locations, table name and
	 * baseline settings.
	 * @param baselineDescription the baseline description
	 * @param baselineVersion the baseline version
	 */
    protected void init(String baselineDescription, String baselineVersion) {
    	this.locations(DEFAULT_FLYWAY_MODULE_PATH)
    	 	.table(DEFAULT_FLYWAY_MODULE_TABLE)
    	 	.baselineDescription(baselineDescription)
    	 	.baselineOnMigrate(true)
    	 	.baselineVersion(baselineVersion);
	}

	/**
	 * Returns the name of the schema-history table.
	 * @return the schema-history table name
	 */
    @Override
    public String getTable() {
    	return super.getTable();
    }

	/**
	 * Returns the module name used for location and table resolution.
	 * @return the module name
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Returns the resolved migration locations as strings.
	 * @return the resolved migration locations
	 */
	public List<String> getLocationAsStrings() {
		return locationAsStrings;
	}
	
}
