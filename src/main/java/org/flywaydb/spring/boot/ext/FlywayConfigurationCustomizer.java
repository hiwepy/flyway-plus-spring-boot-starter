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

import org.flywaydb.core.api.configuration.FluentConfiguration;

/**
 * Callback interface that can be implemented by beans wishing to customize the Flyway
 * {@link FluentConfiguration} while keeping its default auto-configuration.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@FunctionalInterface
public interface FlywayConfigurationCustomizer {

	/**
	 * Customize the given {@link FluentConfiguration}.
	 * @param configuration the configuration to customize
	 */
	void customize(FluentConfiguration configuration);

}
