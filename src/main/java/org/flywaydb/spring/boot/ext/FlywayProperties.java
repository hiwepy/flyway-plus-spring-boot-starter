/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.spring.boot.ext;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

/**
 * Local replacement for the Flyway properties removed from Spring Boot 4.1 autoconfigure.
 * Contains the global Flyway configuration properties bound to {@code spring.flyway.*}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.flyway")
/**
 * <p>Auto-configuration for FlywayProperties.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class FlywayProperties {

	/**
	 * Whether to enable flyway.
	 */
	private boolean enabled = true;

	/**
	 * Whether to check that migration scripts location exists.
	 */
	private boolean checkLocation = true;

	/**
	 * Locations of migrations scripts. Can contain the special "{vendor}" placeholder to
	 * use vendor-specific locations.
	 */
	private List<String> locations = new ArrayList<>(Collections.singletonList("classpath:db/migration"));

	/**
	 * Encoding of SQL migrations.
	 */
	private Charset encoding = StandardCharsets.UTF_8;

	/**
	 * Maximum number of retries when attempting to connect to the database.
	 */
	private int connectRetries;

	/**
	 * Maximum time between retries when attempting to connect to the database. If a
	 * duration suffix is not specified, seconds will be used.
	 */
	@DurationUnit(ChronoUnit.SECONDS)
	private Duration connectRetriesInterval = Duration.ofSeconds(120);

	/**
	 * Maximum number of retries when trying to obtain a lock.
	 */
	private int lockRetryCount = 50;

	/**
	 * Scheme names managed by Flyway (case-sensitive).
	 */
	private List<String> schemas = new ArrayList<>();

	/**
	 * Whether Flyway should attempt to create the schemas specified in the schemas
	 * property.
	 */
	private boolean createSchemas = true;

	/**
	 * Name of the schema history table that will be used by Flyway.
	 */
	private String table = "flyway_schema_history";

	/**
	 * Tablespace in which the schema history table is created. Ignored when using a
	 * database that does not support tablespaces. Defaults to the default tablespace of
	 * the connection used by Flyway.
	 */
	private String tablespace;

	/**
	 * Description to tag an existing schema with when applying a baseline.
	 */
	private String baselineDescription = "<< Flyway Baseline >>";

	/**
	 * Version to tag an existing schema with when executing baseline.
	 */
	private String baselineVersion = "1";

	/**
	 * Username recorded in the schema history table as having applied the migration.
	 */
	private String installedBy;

	/**
	 * Placeholders and their replacements to apply to sql migration scripts.
	 */
	private Map<String, String> placeholders = new HashMap<>();

	/**
	 * Prefix of placeholders in migration scripts.
	 */
	private String placeholderPrefix = "${";

	/**
	 * Suffix of placeholders in migration scripts.
	 */
	private String placeholderSuffix = "}";

	/**
	 * Perform placeholder replacement in migration scripts.
	 */
	private boolean placeholderReplacement = true;

	/**
	 * File name prefix for SQL migrations.
	 */
	private String sqlMigrationPrefix = "V";

	/**
	 * File name suffix for SQL migrations.
	 */
	private List<String> sqlMigrationSuffixes = new ArrayList<>(Collections.singleton(".sql"));

	/**
	 * File name separator for SQL migrations.
	 */
	private String sqlMigrationSeparator = "__";

	/**
	 * File name prefix for repeatable SQL migrations.
	 */
	private String repeatableSqlMigrationPrefix = "R";

	/**
	 * Target version up to which migrations should be considered.
	 */
	private String target = "latest";

	/**
	 * Whether to automatically call baseline when migrating a non-empty schema.
	 */
	private boolean baselineOnMigrate;

	/**
	 * Whether to disable cleaning of the database.
	 */
	private boolean cleanDisabled;

	/**
	 * Whether to automatically call clean when a validation error occurs.
	 */
	private boolean cleanOnValidationError;

	/**
	 * Whether to group all pending migrations together in the same transaction when
	 * applying them.
	 */
	private boolean group;

	/**
	 * Whether to allow mixing transactional and non-transactional statements within the
	 * same migration.
	 */
	private boolean mixed;

	/**
	 * Whether to allow migrations to be run out of order.
	 */
	private boolean outOfOrder;

	/**
	 * Whether to skip default callbacks. If true, only custom callbacks are used.
	 */
	private boolean skipDefaultCallbacks;

	/**
	 * Whether to skip default resolvers. If true, only custom resolvers are used.
	 */
	private boolean skipDefaultResolvers;

	/**
	 * Whether to automatically call validate when performing a migration.
	 */
	private boolean validateOnMigrate = true;

	/**
	 * Whether to batch SQL statements when executing them. Requires Flyway Teams.
	 */
	private Boolean batch;

	/**
	 * File to which the SQL statements of a migration dry run should be output. Requires
	 * Flyway Teams.
	 */
	private File dryRunOutput;

	/**
	 * Rules for the built-in error handling to override specific SQL states and error
	 * codes. Requires Flyway Teams.
	 */
	private String[] errorOverrides;

	/**
	 * Whether to stream SQL migrations when executing them. Requires Flyway Teams.
	 */
	private Boolean stream;

	/**
	 * The license key to use to unlock Flyway Teams or Enterprise features.
	 */
	private String licenseKey;

	/**
	 * Whether to enable Oracle SQL*Plus commands.
	 */
	private Boolean oracleSqlplus;

	/**
	 * Whether to issue a warning rather than an error when a SQL*Plus command is
	 * encountered.
	 */
	private Boolean oracleSqlplusWarn;

	/**
	 * File name prefix for undo SQL migrations. Requires Flyway Teams.
	 */
	private String undoSqlMigrationPrefix;

	/**
	 * Ignore migrations that match this comma-separated list of patterns when validating
	 * migrations.
	 */
	private String[] ignoreMigrationPatterns;

	private final Oracle oracle = new Oracle();

	private final Postgresql postgresql = new Postgresql();

	private final Sqlserver sqlserver = new Sqlserver();

	@Getter
	@Setter
	/**
	 * <p>Auto-configuration for Oracle.</p>
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @since 1.0.0
	 */
	public static class Oracle {

		/**
		 * Whether to enable Flyway's Oracle SQL*Plus support.
		 */
		private Boolean sqlplus;

		/**
		 * Whether Flyway should issue a warning rather than an error when SQL*Plus
		 * commands are encountered.
		 */
		private Boolean sqlplusWarn;

	}

	@Getter
	@Setter
	/**
	 * <p>Auto-configuration for Postgresql.</p>
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @since 1.0.0
	 */
	public static class Postgresql {

		/**
		 * Whether to enable transactional locks for Flyway PostgreSQL support.
		 */
		private Boolean transactionalLock;

	}

	@Getter
	@Setter
	/**
	 * <p>Auto-configuration for Sqlserver.</p>
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @since 1.0.0
	 */
	public static class Sqlserver {

		/**
		 * Whether to enable Flyway's SQL Server transactional locks support.
		 */
		private Boolean transactionalLock;

	}

}
