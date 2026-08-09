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

/**
 * Resolves the {@code {module}} placeholder in the schema-history table name. <p>Each
 * occurrence of {@code {module}} is replaced with the configured module name.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class TableModuleResolver {

	private static final String MODULE_PLACEHOLDER = "{module}";
	private final String module;

	/**
	 * Constructs a resolver for the given module name.
	 * @param module the module name to substitute
	 */
	public TableModuleResolver(String module) {
		this.module = module;
	}

	/**
	 * Resolves the {@code {module}} placeholder in the given table name.
	 * @param table the table name to resolve
	 * @return the resolved table name
	 */
	public String resolveTable(String table) {
		if (usesModuleTable(table)) {
			return table.replace(MODULE_PLACEHOLDER, module);
		}
		return table;
	}

	/**
	 * Returns whether the given table name contains the {@code {module}} placeholder.
	 * @param table the table name to inspect
	 * @return {@code true} if the placeholder is present
	 */
	private boolean usesModuleTable(String table) {
		if (table.contains(MODULE_PLACEHOLDER)) {
			return true;
		}
		return false;
	}

}
