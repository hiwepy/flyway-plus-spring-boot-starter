package org.flywaydb.spring.boot.ext.io;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link FilenameUtils}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class FilenameUtilsTest {

    @Test
    void getExtension() {
        assertThat(FilenameUtils.getExtension("file.txt")).isEqualTo("txt");
        assertThat(FilenameUtils.getExtension("file.tar.gz")).isEqualTo("gz");
        assertThat(FilenameUtils.getExtension("file")).isEqualTo("");
        assertThat(FilenameUtils.getExtension("file.")).isEqualTo("");
        assertThat(FilenameUtils.getExtension(null)).isNull();
    }

    @Test
    void removeExtension() {
        assertThat(FilenameUtils.removeExtension("file.txt")).isEqualTo("file");
        assertThat(FilenameUtils.removeExtension("file.tar.gz")).isEqualTo("file.tar");
        assertThat(FilenameUtils.removeExtension("file")).isEqualTo("file");
        assertThat(FilenameUtils.removeExtension(null)).isNull();
    }

    @Test
    void getBaseName() {
        assertThat(FilenameUtils.getBaseName("file.txt")).isEqualTo("file");
        assertThat(FilenameUtils.getBaseName("path/file.txt")).isEqualTo("file");
        assertThat(FilenameUtils.getBaseName(null)).isNull();
    }

    @Test
    void getName() {
        assertThat(FilenameUtils.getName("path/file.txt")).isEqualTo("file.txt");
        assertThat(FilenameUtils.getName("file.txt")).isEqualTo("file.txt");
        assertThat(FilenameUtils.getName(null)).isNull();
    }

    @Test
    void getPath() {
        assertThat(FilenameUtils.getPath("path/file.txt")).isEqualTo("path/");
        assertThat(FilenameUtils.getPath("file.txt")).isEqualTo("");
        assertThat(FilenameUtils.getPath(null)).isNull();
    }

    @Test
    void getFullPath() {
        assertThat(FilenameUtils.getFullPath("path/file.txt")).isEqualTo("path/");
        assertThat(FilenameUtils.getFullPath("file.txt")).isEqualTo("");
        assertThat(FilenameUtils.getFullPath(null)).isNull();
    }

    @Test
    void separatorsToUnix() {
        assertThat(FilenameUtils.separatorsToUnix("path\\file.txt")).isEqualTo("path/file.txt");
        assertThat(FilenameUtils.separatorsToUnix("path/file.txt")).isEqualTo("path/file.txt");
        assertThat(FilenameUtils.separatorsToUnix(null)).isNull();
    }

    @Test
    void separatorsToWindows() {
        assertThat(FilenameUtils.separatorsToWindows("path/file.txt")).isEqualTo("path\\file.txt");
        assertThat(FilenameUtils.separatorsToWindows("path\\file.txt")).isEqualTo("path\\file.txt");
        assertThat(FilenameUtils.separatorsToWindows(null)).isNull();
    }

    @Test
    void normalize() {
        assertThat(FilenameUtils.normalize("path/./file.txt")).isEqualTo("path/file.txt");
        assertThat(FilenameUtils.normalize(null)).isNull();
    }

    @Test
    void concat() {
        assertThat(FilenameUtils.concat("path", "file.txt")).isEqualTo("path/file.txt");
        assertThat(FilenameUtils.concat("path/", "file.txt")).isEqualTo("path/file.txt");
        assertThat(FilenameUtils.concat(null, "file.txt")).isNull();
    }

    @Test
    void equalsNormalized() {
        assertThat(FilenameUtils.equalsNormalized("path/file.txt", "path/file.txt")).isTrue();
        assertThat(FilenameUtils.equalsNormalized("path/FILE.txt", "path/file.txt")).isFalse();
    }

    @Test
    void equalsNormalizedOnSystem() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWindows) {
            assertThat(FilenameUtils.equalsNormalizedOnSystem("path/FILE.txt", "path/file.txt")).isTrue();
        } else {
            assertThat(FilenameUtils.equalsNormalizedOnSystem("path/FILE.txt", "path/file.txt")).isFalse();
        }
    }

    @Test
    void wildcardMatch() {
        assertThat(FilenameUtils.wildcardMatch("file.txt", "*.txt")).isTrue();
        assertThat(FilenameUtils.wildcardMatch("file.txt", "file.*")).isTrue();
        assertThat(FilenameUtils.wildcardMatch("file.txt", "*.pdf")).isFalse();
        assertThat(FilenameUtils.wildcardMatch(null, "*.txt")).isFalse();
        assertThat(FilenameUtils.wildcardMatch("file.txt", null)).isFalse();
    }

    @Test
    void isExtension() {
        assertThat(FilenameUtils.isExtension("file.txt", "txt")).isTrue();
        assertThat(FilenameUtils.isExtension("file.txt", "pdf")).isFalse();
        assertThat(FilenameUtils.isExtension(null, "txt")).isFalse();
    }

    @Test
    void isExtensionWithArray() {
        assertThat(FilenameUtils.isExtension("file.txt", new String[]{"txt", "pdf"})).isTrue();
        assertThat(FilenameUtils.isExtension("file.txt", new String[]{"pdf", "doc"})).isFalse();
    }

    @Test
    void getPrefixLength() {
        assertThat(FilenameUtils.getPrefixLength("file.txt")).isEqualTo(0);
        assertThat(FilenameUtils.getPrefixLength("/file.txt")).isEqualTo(1);
    }

    @Test
    void indexOfLastSeparator() {
        assertThat(FilenameUtils.indexOfLastSeparator("path/file.txt")).isEqualTo(4);
        assertThat(FilenameUtils.indexOfLastSeparator("file.txt")).isEqualTo(-1);
    }

    @Test
    void indexOfExtension() {
        assertThat(FilenameUtils.indexOfExtension("file.txt")).isEqualTo(4);
        assertThat(FilenameUtils.indexOfExtension("file")).isEqualTo(-1);
    }

    @Test
    void equals() {
        assertThat(FilenameUtils.equals("file.txt", "file.txt")).isTrue();
        assertThat(FilenameUtils.equals("file.txt", "FILE.txt")).isFalse();
    }

    @Test
    void equalsOnSystem() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWindows) {
            assertThat(FilenameUtils.equalsOnSystem("file.txt", "FILE.txt")).isTrue();
        } else {
            assertThat(FilenameUtils.equalsOnSystem("file.txt", "FILE.txt")).isFalse();
        }
    }

    @Test
    void separatorsToSystem() {
        String result = FilenameUtils.separatorsToSystem("path/file.txt");
        assertThat(result).isNotNull();
    }

    @Test
    void getPrefix() {
        assertThat(FilenameUtils.getPrefix("/file.txt")).isEqualTo("/");
        assertThat(FilenameUtils.getPrefix("file.txt")).isEqualTo("");
    }

    @Test
    void getPathNoEndSeparator() {
        assertThat(FilenameUtils.getPathNoEndSeparator("path/file.txt")).isEqualTo("path");
    }

    @Test
    void getFullPathNoEndSeparator() {
        assertThat(FilenameUtils.getFullPathNoEndSeparator("path/file.txt")).isEqualTo("path");
    }

    @Test
    void directoryContains() throws Exception {
        assertThat(FilenameUtils.directoryContains("/parent", "/parent/child")).isTrue();
    }

    @Test
    void normalizeNoEndSeparator() {
        assertThat(FilenameUtils.normalizeNoEndSeparator("path/file.txt")).isEqualTo("path/file.txt");
        assertThat(FilenameUtils.normalizeNoEndSeparator(null)).isNull();
    }

    @Test
    void normalize_unixSeparator() {
        assertThat(FilenameUtils.normalize("path\\file.txt", true)).isEqualTo("path/file.txt");
        assertThat(FilenameUtils.normalize("path/file.txt", false)).isEqualTo("path\\file.txt");
        assertThat(FilenameUtils.normalize(null, true)).isNull();
    }

    @Test
    void normalizeNoEndSeparator_unixSeparator() {
        assertThat(FilenameUtils.normalizeNoEndSeparator("path\\file.txt", true)).isEqualTo("path/file.txt");
        assertThat(FilenameUtils.normalizeNoEndSeparator("path/file.txt", false)).isEqualTo("path\\file.txt");
        assertThat(FilenameUtils.normalizeNoEndSeparator(null, true)).isNull();
    }

    @Test
    void normalize_edgeCases() {
        assertThat(FilenameUtils.normalize("")).isEqualTo("");
        assertThat(FilenameUtils.normalize("/foo//")).isEqualTo("/foo/");
        assertThat(FilenameUtils.normalize("/foo/./")).isEqualTo("/foo/");
        assertThat(FilenameUtils.normalize("/foo/../bar")).isEqualTo("/bar");
        assertThat(FilenameUtils.normalize("/foo/../bar/")).isEqualTo("/bar/");
        assertThat(FilenameUtils.normalize("foo/bar/..")).isEqualTo("foo/");
        assertThat(FilenameUtils.normalize("foo/../bar")).isEqualTo("bar");
    }

    @Test
    void normalize_invalidPaths() {
        assertThat(FilenameUtils.normalize("/../")).isNull();
        assertThat(FilenameUtils.normalize("foo/../../bar")).isNull();
    }

    @Test
    void isExtension_nullExtension() {
        assertThat(FilenameUtils.isExtension("file.txt", (String) null)).isFalse();
        assertThat(FilenameUtils.isExtension("file.txt", "")).isFalse();
        assertThat(FilenameUtils.isExtension("file", (String) null)).isTrue();
    }

    @Test
    void isExtension_collection() {
        java.util.List<String> extensions = java.util.Arrays.asList("txt", "pdf");
        assertThat(FilenameUtils.isExtension("file.txt", extensions)).isTrue();
        assertThat(FilenameUtils.isExtension("file.pdf", extensions)).isTrue();
        assertThat(FilenameUtils.isExtension("file.doc", extensions)).isFalse();
        assertThat(FilenameUtils.isExtension(null, extensions)).isFalse();
        assertThat(FilenameUtils.isExtension("file.txt", (java.util.Collection<String>) null)).isFalse();
        assertThat(FilenameUtils.isExtension("file.txt", java.util.Collections.emptyList())).isFalse();
    }

    @Test
    void isExtension_array_nullAndEmpty() {
        assertThat(FilenameUtils.isExtension("file.txt", (String[]) null)).isFalse();
        assertThat(FilenameUtils.isExtension("file.txt", new String[0])).isFalse();
        assertThat(FilenameUtils.isExtension(null, new String[]{"txt"})).isFalse();
    }

    @Test
    void wildcardMatch_edgeCases() {
        assertThat(FilenameUtils.wildcardMatch(null, null)).isTrue();
        assertThat(FilenameUtils.wildcardMatch("file.txt", null)).isFalse();
        assertThat(FilenameUtils.wildcardMatch("c.txt", "*.???")).isTrue();
        assertThat(FilenameUtils.wildcardMatch("c.txt", "*.????")).isFalse();
        assertThat(FilenameUtils.wildcardMatch("a/b/c.txt", "a/b/*")).isTrue();
    }

    @Test
    void wildcardMatchOnSystem() {
        assertThat(FilenameUtils.wildcardMatchOnSystem("file.txt", "*.txt")).isTrue();
        assertThat(FilenameUtils.wildcardMatchOnSystem("file.txt", "*.pdf")).isFalse();
    }

    @Test
    void splitOnTokens() {
        assertThat(FilenameUtils.splitOnTokens("abc")).isEqualTo(new String[]{"abc"});
        assertThat(FilenameUtils.splitOnTokens("a?b")).isEqualTo(new String[]{"a", "?", "b"});
        assertThat(FilenameUtils.splitOnTokens("a*b")).isEqualTo(new String[]{"a", "*", "b"});
        assertThat(FilenameUtils.splitOnTokens("a**b")).isEqualTo(new String[]{"a", "*", "b"});
    }

    @Test
    void getPrefixLength_edgeCases() {
        assertThat(FilenameUtils.getPrefixLength(null)).isEqualTo(-1);
        assertThat(FilenameUtils.getPrefixLength("")).isEqualTo(0);
        assertThat(FilenameUtils.getPrefixLength("~")).isEqualTo(2);
        assertThat(FilenameUtils.getPrefixLength("~/file")).isEqualTo(2);
        assertThat(FilenameUtils.getPrefixLength("~user/file")).isEqualTo(6);
        assertThat(FilenameUtils.getPrefixLength(":file")).isEqualTo(-1);
    }

    @Test
    void getPrefix_edgeCases() {
        assertThat(FilenameUtils.getPrefix(null)).isNull();
        assertThat(FilenameUtils.getPrefix("~")).isEqualTo("~/");
        assertThat(FilenameUtils.getPrefix("~/file")).isEqualTo("~/");
    }

    @Test
    void getPath_edgeCases() {
        assertThat(FilenameUtils.getPath("a/b/c/")).isEqualTo("a/b/c/");
        assertThat(FilenameUtils.getPath("~/a/b/c.txt")).isEqualTo("a/b/");
    }

    @Test
    void getFullPath_edgeCases() {
        assertThat(FilenameUtils.getFullPath("~/a/b/c.txt")).isEqualTo("~/a/b/");
        assertThat(FilenameUtils.getFullPath("~")).isEqualTo("~/");
        assertThat(FilenameUtils.getFullPath("~/")).isEqualTo("~/");
    }

    @Test
    void getFullPathNoEndSeparator_edgeCases() {
        assertThat(FilenameUtils.getFullPathNoEndSeparator("~/a/b/c.txt")).isEqualTo("~/a/b");
        assertThat(FilenameUtils.getFullPathNoEndSeparator("~")).isEqualTo("~");
    }

    @Test
    void getName_edgeCases() {
        assertThat(FilenameUtils.getName("a/b/c/")).isEqualTo("");
    }

    @Test
    void concat_edgeCases() {
        assertThat(FilenameUtils.concat("/foo", "/bar")).isEqualTo("/bar");
        assertThat(FilenameUtils.concat("", "file.txt")).isEqualTo("file.txt");
        assertThat(FilenameUtils.concat(null, null)).isNull();
    }

    @Test
    void equals_edgeCases() {
        assertThat(FilenameUtils.equals(null, null)).isTrue();
        assertThat(FilenameUtils.equals(null, "file.txt")).isFalse();
        assertThat(FilenameUtils.equals("file.txt", null)).isFalse();
    }

    @Test
    void equalsNormalized_edgeCases() {
        assertThat(FilenameUtils.equalsNormalized(null, null)).isTrue();
    }

    @Test
    void directoryContains_edgeCases() throws Exception {
        assertThat(FilenameUtils.directoryContains("/parent", "/parent")).isFalse();
        assertThat(FilenameUtils.directoryContains("/parent", null)).isFalse();
    }

    @Test
    void constructor() {
        FilenameUtils utils = new FilenameUtils();
        assertThat(utils).isNotNull();
    }

    @Test
    void constants() {
        assertThat(FilenameUtils.EXTENSION_SEPARATOR).isEqualTo('.');
        assertThat(FilenameUtils.EXTENSION_SEPARATOR_STR).isEqualTo(".");
    }
}
