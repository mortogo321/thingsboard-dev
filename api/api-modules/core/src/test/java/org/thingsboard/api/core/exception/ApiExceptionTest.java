package org.thingsboard.api.core.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionTest {

    @Test
    void defaultConstructorUsesBadRequest() {
        ApiException ex = new ApiException("bad input");

        assertThat(ex.getMessage()).isEqualTo("bad input");
        assertThat(ex.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getErrorCode()).isEqualTo("API_ERROR");
    }

    @Test
    void notFoundFactory() {
        ApiException ex = ApiException.notFound("Device");

        assertThat(ex.getMessage()).isEqualTo("Device not found");
        assertThat(ex.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getErrorCode()).isEqualTo("NOT_FOUND");
    }

    @Test
    void versionNotSupportedFactory() {
        ApiException ex = ApiException.versionNotSupported("v9");

        assertThat(ex.getMessage()).contains("v9");
        assertThat(ex.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getErrorCode()).isEqualTo("VERSION_NOT_SUPPORTED");
    }
}
