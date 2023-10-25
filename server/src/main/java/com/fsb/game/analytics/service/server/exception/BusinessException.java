
package com.fsb.game.analytics.service.server.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.fsb.game.analytics.service.core.exception.dto.ErrorResponseDto;
import com.fsb.game.analytics.service.core.exception.dto.InvalidParameterDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Custom exception class to use in case of a business exception.
 */
@Setter
@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7941439567423940154L;

    protected final String code;

    protected final String message;

    protected final HttpStatus httpStatus;

    protected List<InvalidParameterDto> invalidParameters;

    /**
     * Constructor accepting an exception reason.
     *
     * @param reason the reason of the exception
     */
    public BusinessException(final BusinessExceptionReason reason) {
        this.code = reason.getCode();
        this.message = reason.getMessage();
        this.httpStatus = reason.getHttpStatus();
    }

    /**
     * Constructor accepting an exception reason.
     *
     * @param reason the reason of the exception
     */
    public BusinessException(final BusinessExceptionReason reason, List<InvalidParameterDto> invalidParameters) {
        this.code = reason.getCode();
        this.message = reason.getMessage();
        this.httpStatus = reason.getHttpStatus();
        this.invalidParameters = invalidParameters;
    }

    /**
     * Constructor accepting an exception reason and an http status that will override the default one from the reason.
     *
     * @param reason the reason of the exception
     * @param overridingHttpStatus the http status which overrides the one from the reason
     */
    public BusinessException(final BusinessExceptionReason reason, final HttpStatus overridingHttpStatus) {
        this.code = reason.getCode();
        this.message = reason.getMessage();
        this.httpStatus = overridingHttpStatus;
    }

    /**
     * Constructor accepting an exception reason and an http status that will override the default one from the reason.
     *
     * @param message the mesage of the exception
     * @param overridingHttpStatus the http status which overrides the one from the reason
     */
    public BusinessException(final String message, final HttpStatus overridingHttpStatus) {
        this.code = BusinessExceptionReason.class.getSimpleName();
        this.message = message;
        this.httpStatus = overridingHttpStatus;
    }

    /**
     * Constructor accepting an errorResponseDto object
     * 
     * @param errorResponseDto The error response DTO.
     */
    public BusinessException(final ErrorResponseDto errorResponseDto) {
        this.code = errorResponseDto.getErrorCode();
        this.message = errorResponseDto.getErrorMessage();
        this.httpStatus = HttpStatus.valueOf(errorResponseDto.getStatus());
        this.invalidParameters = errorResponseDto.getErrors();
    }

    public BusinessException(final String code, final String message, final HttpStatus overridingHttpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = overridingHttpStatus;
    }

}
