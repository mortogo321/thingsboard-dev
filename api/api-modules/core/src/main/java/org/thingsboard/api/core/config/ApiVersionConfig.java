package org.thingsboard.api.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiVersionConfig {

    private List<String> enabledVersions = List.of("v1", "v2");
    private String defaultVersion = "v1";
    private String deprecatedVersions = "";

    public boolean isVersionEnabled(String version) {
        return enabledVersions.contains(version);
    }

    public boolean isVersionDeprecated(String version) {
        return deprecatedVersions.contains(version);
    }
}
