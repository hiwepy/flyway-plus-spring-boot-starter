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

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.StringUtils;

/**
 * Resolves the {@code {vendor}} placeholder in migration locations. <p>Each occurrence of
 * {@code {vendor}} is replaced with the database vendor identifier detected from the data
 * source.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class LocationVendorResolver {

	private static final String VENDOR_PLACEHOLDER = "{vendor}";

	private final DataSource dataSource;

	/**
	 * Constructs a resolver that detects the vendor from the given data source.
	 * @param dataSource the data source used to detect the vendor
	 */
	public LocationVendorResolver(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Resolves the {@code {vendor}} placeholder in the given locations.
	 * @param locations the locations to resolve
	 * @return the resolved locations
	 */
	public String[] resolveLocations(Collection<String> locations) {
		return resolveLocations(StringUtils.toStringArray(locations));
	}

	/**
	 * Resolves the {@code {vendor}} placeholder in the given locations.
	 * @param locations the locations to resolve
	 * @return the resolved locations
	 */
	public String[] resolveLocations(String[] locations) {
		if (usesVendorLocation(locations)) {
			DatabaseDriver databaseDriver = getDatabaseDriver();
			return replaceVendorLocations(locations, databaseDriver);
		}
		return locations;
	}

	/**
	 * Replaces the {@code {vendor}} placeholder in each location with the given driver's id.
	 * @param locations the locations to resolve
	 * @param databaseDriver the detected database driver
	 * @return the resolved locations, or the originals if the driver is unknown
	 */
	private String[] replaceVendorLocations(String[] locations,
			DatabaseDriver databaseDriver) {
		if (databaseDriver == DatabaseDriver.UNKNOWN) {
			return locations;
		}
		String vendor = databaseDriver.getId();
		return Arrays.stream(locations)
				.map((location) -> location.replace(VENDOR_PLACEHOLDER, vendor))
				.toArray(String[]::new);
	}

	/**
	 * Detects the {@link DatabaseDriver} from the data source's JDBC URL.
	 * @return the detected database driver
	 */
	private DatabaseDriver getDatabaseDriver() {
		try {
			String url = JdbcUtils.extractDatabaseMetaData(this.dataSource, "getURL");
			return DatabaseDriver.fromJdbcUrl(url);
		}
		catch (MetaDataAccessException ex) {
			throw new IllegalStateException(ex);
		}

	}

	/**
	 * Returns whether any of the given locations contains the {@code {vendor}} placeholder.
	 * @param locations the locations to inspect
	 * @return {@code true} if the placeholder is present
	 */
	private boolean usesVendorLocation(String... locations) {
		for (String location : locations) {
			if (location.contains(VENDOR_PLACEHOLDER)) {
				return true;
			}
		}
		return false;
	}
	
}
