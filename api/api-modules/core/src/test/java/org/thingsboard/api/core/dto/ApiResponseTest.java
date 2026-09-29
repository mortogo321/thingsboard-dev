package org.thingsboard.api.core.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void successPopulatesAllFields() {
        List<String> data = List.of("a", "b");

        ApiResponse<List<String>> response = ApiResponse.success(data, "v1");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getVersion()).isEqualTo("v1");
        assertThat(response.getTimestamp()).isNotNull().isPositive();
        assertThat(response.getMessage()).isNull();
    }

    @Test
    void errorPopulatesMessageAndVersion() {
        ApiResponse<Object> response = ApiResponse.error("boom", "v2");

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("boom");
        assertThat(response.getVersion()).isEqualTo("v2");
        assertThat(response.getTimestamp()).isNotNull().isPositive();
        assertThat(response.getData()).isNull();
    }

    @Test
    void builderRoundTrip() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("ok")
                .data("payload")
                .version("v1")
                .timestamp(123L)
                .build();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("ok");
        assertThat(response.getData()).isEqualTo("payload");
        assertThat(response.getVersion()).isEqualTo("v1");
        assertThat(response.getTimestamp()).isEqualTo(123L);
    }
}
