
package com.fsb.game.analytics.service.server.exception;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;

import com.fsb.game.analytics.service.core.exception.dto.ErrorResponseDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Custom exception class to use in case of an application exception.
 */
@Getter
@Setter
public class ApplicationException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -3123885280697604388L;

    private final String code;

    private final String message;

    private final HttpStatus httpStatus;

    /**
     * Constructor accepting an exception reason.
     *
     * @param reason the reason of the exception
     */
    public ApplicationException(final ApplicationExceptionReason reason) {
        this.code = reason.getCode();
        this.message = reason.getMessage();
        this.httpStatus = reason.getHttpStatus();
    }

    /**
     * Constructor accepting an excepting reason and optional parameters which are replaced in the message.
     *
     * @param reason the reason of the exception
     * @param parameters the optional parameters
     */
    public ApplicationException(final ApplicationExceptionReason reason, final Object... parameters) {
        if (parameters != null) {
            this.message = format(reason.getMessage(), parameters);
        } else {
            this.message = reason.getMessage();
        }

        this.code = reason.getCode();
        this.httpStatus = reason.getHttpStatus();
    }

    /**
     * Constructor accepting an exception reason and an http status that will override the default one from the reason.
     *
     * @param message the message of the exception
     * @param overridingHttpStatus the http status which overrides the one from the reason
     */
    public ApplicationException(final String message, final HttpStatus overridingHttpStatus) {
        this.code = BusinessExceptionReason.class.getSimpleName();
        this.message = message;
        this.httpStatus = overridingHttpStatus;
    }

    /**
     * Constructor accepting an errorResponseDto object
     * 
     * @param errorResponseDto The error response DTO.
     */
    public ApplicationException(final ErrorResponseDto errorResponseDto) {
        this.code = errorResponseDto.getErrorCode();
        this.message = errorResponseDto.getErrorMessage();
        this.httpStatus = HttpStatus.valueOf(errorResponseDto.getStatus());
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    public String toString() {
        return format("ApplicationException(code=%s, message=%s)", this.getCode(), this.getMessage());
    }
}
