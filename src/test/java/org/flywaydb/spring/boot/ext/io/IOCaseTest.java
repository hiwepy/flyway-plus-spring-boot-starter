package org.flywaydb.spring.boot.ext.io;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link IOCase}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class IOCaseTest {

    @Test
    void sensitiveCheckEquals() {
        assertThat(IOCase.SENSITIVE.checkEquals("abc", "abc")).isTrue();
        assertThat(IOCase.SENSITIVE.checkEquals("abc", "ABC")).isFalse();
    }

    @Test
    void insensitiveCheckEquals() {
        assertThat(IOCase.INSENSITIVE.checkEquals("abc", "abc")).isTrue();
        assertThat(IOCase.INSENSITIVE.checkEquals("abc", "ABC")).isTrue();
    }

    @Test
    void sensitiveCheckEqualsThrowsOnNull() {
        assertThatThrownBy(() -> IOCase.SENSITIVE.checkEquals(null, "abc"))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> IOCase.SENSITIVE.checkEquals("abc", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void sensitiveCheckStartsWith() {
        assertThat(IOCase.SENSITIVE.checkStartsWith("abcdef", "abc")).isTrue();
        assertThat(IOCase.SENSITIVE.checkStartsWith("abcdef", "ABC")).isFalse();
    }

    @Test
    void insensitiveCheckStartsWith() {
        assertThat(IOCase.INSENSITIVE.checkStartsWith("abcdef", "abc")).isTrue();
        assertThat(IOCase.INSENSITIVE.checkStartsWith("abcdef", "ABC")).isTrue();
    }

    @Test
    void sensitiveCheckEndsWith() {
        assertThat(IOCase.SENSITIVE.checkEndsWith("abcdef", "def")).isTrue();
        assertThat(IOCase.SENSITIVE.checkEndsWith("abcdef", "DEF")).isFalse();
    }

    @Test
    void insensitiveCheckEndsWith() {
        assertThat(IOCase.INSENSITIVE.checkEndsWith("abcdef", "def")).isTrue();
        assertThat(IOCase.INSENSITIVE.checkEndsWith("abcdef", "DEF")).isTrue();
    }

    @Test
    void sensitiveCheckIndexOf() {
        assertThat(IOCase.SENSITIVE.checkIndexOf("abcdef", 0, "cde")).isEqualTo(2);
        assertThat(IOCase.SENSITIVE.checkIndexOf("abcdef", 0, "CDE")).isEqualTo(-1);
    }

    @Test
    void insensitiveCheckIndexOf() {
        assertThat(IOCase.INSENSITIVE.checkIndexOf("abcdef", 0, "cde")).isEqualTo(2);
        assertThat(IOCase.INSENSITIVE.checkIndexOf("abcdef", 0, "CDE")).isEqualTo(2);
    }

    @Test
    void sensitiveCheckRegionMatches() {
        assertThat(IOCase.SENSITIVE.checkRegionMatches("abcdef", 2, "cde")).isTrue();
        assertThat(IOCase.SENSITIVE.checkRegionMatches("abcdef", 2, "CDE")).isFalse();
    }

    @Test
    void insensitiveCheckRegionMatches() {
        assertThat(IOCase.INSENSITIVE.checkRegionMatches("abcdef", 2, "cde")).isTrue();
        assertThat(IOCase.INSENSITIVE.checkRegionMatches("abcdef", 2, "CDE")).isTrue();
    }

    @Test
    void sensitiveCheckCompareTo() {
        assertThat(IOCase.SENSITIVE.checkCompareTo("abc", "abc")).isEqualTo(0);
        assertThat(IOCase.SENSITIVE.checkCompareTo("abc", "ABC")).isNotEqualTo(0);
    }

    @Test
    void insensitiveCheckCompareTo() {
        assertThat(IOCase.INSENSITIVE.checkCompareTo("abc", "abc")).isEqualTo(0);
        assertThat(IOCase.INSENSITIVE.checkCompareTo("abc", "ABC")).isEqualTo(0);
    }

    @Test
    void sensitiveCheckCompareToThrowsOnNull() {
        assertThatThrownBy(() -> IOCase.SENSITIVE.checkCompareTo(null, "abc"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void toStringReturnsName() {
        assertThat(IOCase.SENSITIVE.toString()).isEqualTo("Sensitive");
        assertThat(IOCase.INSENSITIVE.toString()).isEqualTo("Insensitive");
    }

    @Test
    void isCaseSensitive() {
        assertThat(IOCase.SENSITIVE.isCaseSensitive()).isTrue();
        assertThat(IOCase.INSENSITIVE.isCaseSensitive()).isFalse();
    }

    @Test
    void getName() {
        assertThat(IOCase.SENSITIVE.getName()).isEqualTo("Sensitive");
        assertThat(IOCase.INSENSITIVE.getName()).isEqualTo("Insensitive");
        assertThat(IOCase.SYSTEM.getName()).isEqualTo("System");
    }

    @Test
    void forName() {
        assertThat(IOCase.forName("Sensitive")).isEqualTo(IOCase.SENSITIVE);
        assertThat(IOCase.forName("Insensitive")).isEqualTo(IOCase.INSENSITIVE);
        assertThat(IOCase.forName("System")).isEqualTo(IOCase.SYSTEM);
    }

    @Test
    void forNameThrowsOnInvalid() {
        assertThatThrownBy(() -> IOCase.forName("Invalid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void valueOf() {
        assertThat(IOCase.valueOf("SENSITIVE")).isEqualTo(IOCase.SENSITIVE);
        assertThat(IOCase.valueOf("INSENSITIVE")).isEqualTo(IOCase.INSENSITIVE);
        assertThat(IOCase.valueOf("SYSTEM")).isEqualTo(IOCase.SYSTEM);
    }

    @Test
    void systemCaseDependsOnOS() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWindows) {
            assertThat(IOCase.SYSTEM.isCaseSensitive()).isFalse();
        } else {
            assertThat(IOCase.SYSTEM.isCaseSensitive()).isTrue();
        }
    }
}
