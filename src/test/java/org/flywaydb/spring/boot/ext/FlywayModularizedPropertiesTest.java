package org.flywaydb.spring.boot.ext;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FlywayModularizedProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class FlywayModularizedPropertiesTest {

    @Test
    void defaultValues() {
        FlywayModularizedProperties props = new FlywayModularizedProperties();
        assertThat(props.getModule()).isEqualTo("module");
        assertThat(props.isEnabled()).isTrue();
        assertThat(props.isCheckLocation()).isTrue();
        assertThat(props.isFailOnMissingLocations()).isFalse();
        assertThat(props.getLocations()).containsExactly("classpath:db/migration/{module}/{vendor}");
        assertThat(props.getEncoding()).isEqualTo(StandardCharsets.UTF_8);
        assertThat(props.getConnectRetries()).isEqualTo(0);
        assertThat(props.getConnectRetriesInterval()).isEqualTo(Duration.ofSeconds(120));
        assertThat(props.getLockRetryCount()).isEqualTo(50);
        assertThat(props.getSchemas()).isEmpty();
        assertThat(props.isCreateSchemas()).isTrue();
        assertThat(props.getTable()).isEqualTo("flyway_{module}_schema_history");
        assertThat(props.getTablespace()).isNull();
        assertThat(props.getBaselineDescription()).isEqualTo("<< Flyway Modularized Baseline >>");
        assertThat(props.getBaselineVersion()).isEqualTo("1");
        assertThat(props.getInstalledBy()).isNull();
        assertThat(props.getPlaceholders()).isEmpty();
        assertThat(props.getPlaceholderPrefix()).isEqualTo("${");
        assertThat(props.getPlaceholderSuffix()).isEqualTo("}");
        assertThat(props.getPlaceholderSeparator()).isEqualTo(":");
        assertThat(props.isPlaceholderReplacement()).isTrue();
        assertThat(props.getSqlMigrationPrefix()).isEqualTo("V");
        assertThat(props.getSqlMigrationSuffixes()).containsExactly(".sql");
        assertThat(props.getSqlMigrationSeparator()).isEqualTo("__");
        assertThat(props.getRepeatableSqlMigrationPrefix()).isEqualTo("R");
        assertThat(props.getTarget()).isEqualTo("latest");
        assertThat(props.getUser()).isNull();
        assertThat(props.getPassword()).isNull();
        assertThat(props.getDriverClassName()).isNull();
        assertThat(props.getUrl()).isNull();
        assertThat(props.getInitSqls()).isEmpty();
        assertThat(props.isBaselineOnMigrate()).isFalse();
        assertThat(props.isCleanDisabled()).isTrue();
        assertThat(props.isCleanOnValidationError()).isFalse();
        assertThat(props.isGroup()).isFalse();
        assertThat(props.isMixed()).isFalse();
        assertThat(props.isOutOfOrder()).isFalse();
        assertThat(props.isSkipDefaultCallbacks()).isFalse();
        assertThat(props.isSkipDefaultResolvers()).isFalse();
        assertThat(props.isValidateMigrationNaming()).isFalse();
        assertThat(props.isValidateOnMigrate()).isTrue();
        assertThat(props.getScriptPlaceholderPrefix()).isEqualTo("FP__");
        assertThat(props.getScriptPlaceholderSuffix()).isEqualTo("__");
        assertThat(props.isExecuteInTransaction()).isTrue();
        assertThat(props.getLoggers()).containsExactly("slf4j");
        assertThat(props.getBatch()).isNull();
        assertThat(props.getDryRunOutput()).isNull();
        assertThat(props.getErrorOverrides()).isNull();
        assertThat(props.getStream()).isNull();
        assertThat(props.getJdbcProperties()).isEmpty();
        assertThat(props.getKerberosConfigFile()).isNull();
        assertThat(props.getOutputQueryResults()).isNull();
        assertThat(props.getSkipExecutingMigrations()).isNull();
        assertThat(props.getIgnoreMigrationPatterns()).isNull();
        assertThat(props.getDetectEncoding()).isNull();
        assertThat(props.getOracle()).isNotNull();
        assertThat(props.getPostgresql()).isNotNull();
        assertThat(props.getSqlserver()).isNotNull();
    }

    @Test
    void isCreateDataSourceWhenUrlSet() {
        FlywayModularizedProperties props = new FlywayModularizedProperties();
        assertThat(props.isCreateDataSource()).isFalse();
        props.setUrl("jdbc:h2:mem:test");
        assertThat(props.isCreateDataSource()).isTrue();
    }

    @Test
    void isCreateDataSourceWhenUserSet() {
        FlywayModularizedProperties props = new FlywayModularizedProperties();
        props.setUser("sa");
        assertThat(props.isCreateDataSource()).isTrue();
    }

    @Test
    void settersAndGetters() {
        FlywayModularizedProperties props = new FlywayModularizedProperties();
        props.setModule("custom");
        assertThat(props.getModule()).isEqualTo("custom");

        props.setEnabled(false);
        assertThat(props.isEnabled()).isFalse();

        props.setCheckLocation(false);
        assertThat(props.isCheckLocation()).isFalse();

        props.setLocations(Arrays.asList("classpath:custom"));
        assertThat(props.getLocations()).containsExactly("classpath:custom");

        props.setTable("custom_history");
        assertThat(props.getTable()).isEqualTo("custom_history");

        props.setUrl("jdbc:h2:mem:custom");
        assertThat(props.getUrl()).isEqualTo("jdbc:h2:mem:custom");

        props.setUser("user");
        assertThat(props.getUser()).isEqualTo("user");

        props.setPassword("pass");
        assertThat(props.getPassword()).isEqualTo("pass");

        props.setDriverClassName("org.h2.Driver");
        assertThat(props.getDriverClassName()).isEqualTo("org.h2.Driver");

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("key", "value");
        props.setPlaceholders(placeholders);
        assertThat(props.getPlaceholders()).containsEntry("key", "value");

        props.setInitSqls(Arrays.asList("SELECT 1"));
        assertThat(props.getInitSqls()).containsExactly("SELECT 1");

        props.setBaselineOnMigrate(true);
        assertThat(props.isBaselineOnMigrate()).isTrue();

        props.setCleanDisabled(false);
        assertThat(props.isCleanDisabled()).isFalse();

        props.setGroup(true);
        assertThat(props.isGroup()).isTrue();

        props.setMixed(true);
        assertThat(props.isMixed()).isTrue();

        props.setOutOfOrder(true);
        assertThat(props.isOutOfOrder()).isTrue();

        props.setTarget("1.0");
        assertThat(props.getTarget()).isEqualTo("1.0");

        props.setBatch(true);
        assertThat(props.getBatch()).isTrue();

        props.setStream(true);
        assertThat(props.getStream()).isTrue();
    }

    @Test
    void toStringContainsModule() {
        FlywayModularizedProperties props = new FlywayModularizedProperties();
        props.setModule("test-module");
        String str = props.toString();
        assertThat(str).contains("test-module");
    }
}
