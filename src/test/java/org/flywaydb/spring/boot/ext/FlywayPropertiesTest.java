package org.flywaydb.spring.boot.ext;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FlywayProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class FlywayPropertiesTest {

    @Test
    void defaultValues() {
        FlywayProperties props = new FlywayProperties();
        assertThat(props.isEnabled()).isTrue();
        assertThat(props.isCheckLocation()).isTrue();
        assertThat(props.getLocations()).containsExactly("classpath:db/migration");
        assertThat(props.getEncoding()).isEqualTo(StandardCharsets.UTF_8);
        assertThat(props.getConnectRetries()).isEqualTo(0);
        assertThat(props.getConnectRetriesInterval()).isEqualTo(Duration.ofSeconds(120));
        assertThat(props.getLockRetryCount()).isEqualTo(50);
        assertThat(props.getSchemas()).isEmpty();
        assertThat(props.isCreateSchemas()).isTrue();
        assertThat(props.getTable()).isEqualTo("flyway_schema_history");
        assertThat(props.getTablespace()).isNull();
        assertThat(props.getBaselineDescription()).isEqualTo("<< Flyway Baseline >>");
        assertThat(props.getBaselineVersion()).isEqualTo("1");
        assertThat(props.getInstalledBy()).isNull();
        assertThat(props.getPlaceholders()).isEmpty();
        assertThat(props.getPlaceholderPrefix()).isEqualTo("${");
        assertThat(props.getPlaceholderSuffix()).isEqualTo("}");
        assertThat(props.isPlaceholderReplacement()).isTrue();
        assertThat(props.getSqlMigrationPrefix()).isEqualTo("V");
        assertThat(props.getSqlMigrationSuffixes()).containsExactly(".sql");
        assertThat(props.getSqlMigrationSeparator()).isEqualTo("__");
        assertThat(props.getRepeatableSqlMigrationPrefix()).isEqualTo("R");
        assertThat(props.getTarget()).isEqualTo("latest");
        assertThat(props.isBaselineOnMigrate()).isFalse();
        assertThat(props.isCleanDisabled()).isFalse();
        assertThat(props.isCleanOnValidationError()).isFalse();
        assertThat(props.isGroup()).isFalse();
        assertThat(props.isMixed()).isFalse();
        assertThat(props.isOutOfOrder()).isFalse();
        assertThat(props.isSkipDefaultCallbacks()).isFalse();
        assertThat(props.isSkipDefaultResolvers()).isFalse();
        assertThat(props.isValidateOnMigrate()).isTrue();
        assertThat(props.getBatch()).isNull();
        assertThat(props.getDryRunOutput()).isNull();
        assertThat(props.getErrorOverrides()).isNull();
        assertThat(props.getStream()).isNull();
        assertThat(props.getLicenseKey()).isNull();
        assertThat(props.getOracleSqlplus()).isNull();
        assertThat(props.getOracleSqlplusWarn()).isNull();
        assertThat(props.getUndoSqlMigrationPrefix()).isNull();
        assertThat(props.getIgnoreMigrationPatterns()).isNull();
        assertThat(props.getOracle()).isNotNull();
        assertThat(props.getPostgresql()).isNotNull();
        assertThat(props.getSqlserver()).isNotNull();
    }

    @Test
    void settersAndGetters() {
        FlywayProperties props = new FlywayProperties();
        props.setEnabled(false);
        assertThat(props.isEnabled()).isFalse();

        props.setTable("custom_table");
        assertThat(props.getTable()).isEqualTo("custom_table");

        props.setLicenseKey("key");
        assertThat(props.getLicenseKey()).isEqualTo("key");

        props.setOracleSqlplus(true);
        assertThat(props.getOracleSqlplus()).isTrue();

        props.setOracleSqlplusWarn(true);
        assertThat(props.getOracleSqlplusWarn()).isTrue();

        props.setStream(true);
        assertThat(props.getStream()).isTrue();

        props.setUndoSqlMigrationPrefix("U");
        assertThat(props.getUndoSqlMigrationPrefix()).isEqualTo("U");

        props.setBatch(true);
        assertThat(props.getBatch()).isTrue();

        props.setDryRunOutput(new java.io.File("dry.sql"));
        assertThat(props.getDryRunOutput()).isEqualTo(new java.io.File("dry.sql"));

        String[] overrides = {"42000:E"};
        props.setErrorOverrides(overrides);
        assertThat(props.getErrorOverrides()).isEqualTo(overrides);

        String[] patterns = {"*:V0__*"};
        props.setIgnoreMigrationPatterns(patterns);
        assertThat(props.getIgnoreMigrationPatterns()).isEqualTo(patterns);
    }

    @Test
    void innerClasses() {
        FlywayProperties.Oracle oracle = new FlywayProperties.Oracle();
        oracle.setSqlplus(true);
        assertThat(oracle.getSqlplus()).isTrue();
        oracle.setSqlplusWarn(true);
        assertThat(oracle.getSqlplusWarn()).isTrue();

        FlywayProperties.Postgresql postgresql = new FlywayProperties.Postgresql();
        postgresql.setTransactionalLock(true);
        assertThat(postgresql.getTransactionalLock()).isTrue();

        FlywayProperties.Sqlserver sqlserver = new FlywayProperties.Sqlserver();
        sqlserver.setTransactionalLock(true);
        assertThat(sqlserver.getTransactionalLock()).isTrue();
    }
}
