/*
 * Copyright (c) 2017, hiwepy (https://github.com/hiwepy).
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


import org.springframework.context.ApplicationEvent;

/**
 * Event published once a Flyway migration has completed. <p>The source of the event is the
 * object that triggered the migration (e.g. the {@code FlywayMigrationProvider}).</p>
 *
 * @author Loong Wan (https://github.com/loong10k)
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class FlywayMigratedEvent extends ApplicationEvent {

	/**
	 * Constructs a migration-completed event.
	 * @param source the object on which the event initially occurred
	 */
	public FlywayMigratedEvent(Object source) {
		super(source);
	}
	
	

}
