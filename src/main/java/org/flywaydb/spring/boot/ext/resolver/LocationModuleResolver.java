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
package org.flywaydb.spring.boot.ext.resolver;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.util.StringUtils;

/**
 * Resolves the {@code {module}} placeholder in migration locations. <p>Each occurrence of
 * {@code {module}} is replaced with the configured module name.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class LocationModuleResolver {

	private static final String MODULE_PLACEHOLDER = "{module}";
	private final String module;

	/**
	 * Constructs a resolver for the given module name.
	 * @param module the module name to substitute
	 */
	public LocationModuleResolver(String module) {
		this.module = module;
	}

	/**
	 * Resolves the {@code {module}} placeholder in the given locations.
	 * @param locations the locations to resolve
	 * @return the resolved locations
	 */
	public String[] resolveLocations(Collection<String> locations) {
		return resolveLocations(StringUtils.toStringArray(locations));
	}

	/**
	 * Resolves the {@code {module}} placeholder in the given locations.
	 * @param locations the locations to resolve
	 * @return the resolved locations
	 */
	public String[] resolveLocations(String[] locations) {
		if (usesModuleLocation(locations)) {
			return Arrays.stream(locations)
					.map((location) -> location.replace(MODULE_PLACEHOLDER, module))
					.toArray(String[]::new);
		}
		return locations;
	}

	/**
	 * Returns whether any of the given locations contains the {@code {module}} placeholder.
	 * @param locations the locations to inspect
	 * @return {@code true} if the placeholder is present
	 */
	private boolean usesModuleLocation(String... locations) {
		for (String location : locations) {
			if (location.contains(MODULE_PLACEHOLDER)) {
				return true;
			}
		}
		return false;
	}
	
}
