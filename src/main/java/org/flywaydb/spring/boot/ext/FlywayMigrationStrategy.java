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
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.spring.boot.ext;

import org.flywaydb.core.Flyway;

/**
 * Strategy interface for customizing Flyway migration behavior.
 *
 * @author Loong Wan (https://github.com/loong10k)
 * @since 1.0.0
 */
@FunctionalInterface
public interface FlywayMigrationStrategy {

	/**
	 * Execute the Flyway migration.
	 * @param flyway the Flyway instance to migrate
	 */
	void migrate(Flyway flyway);

}
