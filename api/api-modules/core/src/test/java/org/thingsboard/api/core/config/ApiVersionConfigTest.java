package org.thingsboard.api.core.config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApiVersionConfigTest {

    @Test
    void defaultsEnableV1AndV2() {
        ApiVersionConfig config = new ApiVersionConfig();

        assertThat(config.getDefaultVersion()).isEqualTo("v1");
        assertThat(config.isVersionEnabled("v1")).isTrue();
        assertThat(config.isVersionEnabled("v2")).isTrue();
        assertThat(config.isVersionEnabled("v3")).isFalse();
    }

    @Test
    void nothingDeprecatedByDefault() {
        ApiVersionConfig config = new ApiVersionConfig();

        assertThat(config.isVersionDeprecated("v1")).isFalse();
        assertThat(config.isVersionDeprecated("v2")).isFalse();
    }

    @Test
    void deprecatedVersionsAreReported() {
        ApiVersionConfig config = new ApiVersionConfig();
        config.setEnabledVersions(List.of("v1", "v2"));
        config.setDeprecatedVersions("v1");

        assertThat(config.isVersionDeprecated("v1")).isTrue();
        assertThat(config.isVersionDeprecated("v2")).isFalse();
    }
}
