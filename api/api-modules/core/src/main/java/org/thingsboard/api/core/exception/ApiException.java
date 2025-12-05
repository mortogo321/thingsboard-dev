package org.thingsboard.api.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public ApiException(String message) {
        this(message, HttpStatus.BAD_REQUEST, "API_ERROR");
    }

    public ApiException(String message, HttpStatus status) {
        this(message, status, "API_ERROR");
    }

    public ApiException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public static ApiException notFound(String resource) {
        return new ApiException(resource + " not found", HttpStatus.NOT_FOUND, "NOT_FOUND");
    }

    public static ApiException versionNotSupported(String version) {
        return new ApiException("API version " + version + " is not supported",
                HttpStatus.BAD_REQUEST, "VERSION_NOT_SUPPORTED");
    }
}
