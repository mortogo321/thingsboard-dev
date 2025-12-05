package org.thingsboard.api.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private String version;
    private Long timestamp;

    public static <T> ApiResponse<T> success(T data, String version) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .version(version)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, String version) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .version(version)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
