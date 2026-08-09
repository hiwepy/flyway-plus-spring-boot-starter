package org.flywaydb.spring.boot.ext.resolver;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LocationModuleResolver}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class LocationModuleResolverTest {

    @Test
    void replacesModulePlaceholder() {
        LocationModuleResolver resolver = new LocationModuleResolver("mymodule");
        String[] locations = resolver.resolveLocations(new String[]{"classpath:db/migration/{module}/{vendor}"});
        assertThat(locations).containsExactly("classpath:db/migration/mymodule/{vendor}");
    }

    @Test
    void replacesMultipleModulePlaceholders() {
        LocationModuleResolver resolver = new LocationModuleResolver("auth");
        String[] locations = resolver.resolveLocations(new String[]{
                "classpath:db/migration/{module}/{vendor}",
                "classpath:db/data/{module}"
        });
        assertThat(locations).containsExactly(
                "classpath:db/migration/auth/{vendor}",
                "classpath:db/data/auth"
        );
    }

    @Test
    void noPlaceholderReturnsOriginal() {
        LocationModuleResolver resolver = new LocationModuleResolver("mymodule");
        String[] locations = resolver.resolveLocations(new String[]{"classpath:db/migration"});
        assertThat(locations).containsExactly("classpath:db/migration");
    }

    @Test
    void emptyModuleReplacesWithEmpty() {
        LocationModuleResolver resolver = new LocationModuleResolver("");
        String[] locations = resolver.resolveLocations(new String[]{"classpath:db/migration/{module}"});
        assertThat(locations).containsExactly("classpath:db/migration/");
    }

    @Test
    void emptyArrayReturnsEmpty() {
        LocationModuleResolver resolver = new LocationModuleResolver("mymodule");
        String[] locations = resolver.resolveLocations(new String[]{});
        assertThat(locations).isEmpty();
    }

    @Test
    void resolvesFromCollection() {
        LocationModuleResolver resolver = new LocationModuleResolver("mymodule");
        String[] locations = resolver.resolveLocations(Collections.singletonList("classpath:db/migration/{module}"));
        assertThat(locations).containsExactly("classpath:db/migration/mymodule");
    }

    @Test
    void resolvesFromCollectionWithMultiple() {
        LocationModuleResolver resolver = new LocationModuleResolver("auth");
        String[] locations = resolver.resolveLocations(Arrays.asList(
                "classpath:db/migration/{module}",
                "classpath:db/data/{module}"
        ));
        assertThat(locations).containsExactly(
                "classpath:db/migration/auth",
                "classpath:db/data/auth"
        );
    }
}
