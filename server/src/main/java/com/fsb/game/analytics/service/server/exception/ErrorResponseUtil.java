package com.fsb.game.analytics.service.server.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import com.fsb.game.analytics.service.core.exception.dto.ErrorResponseDto;
import com.fsb.game.analytics.service.core.exception.dto.InvalidParameterDto;



public final class ErrorResponseUtil {

	private ErrorResponseUtil() {
	}

	/**
	 * Builds an error response based on given parameters.
	 *
	 * @param code    the error code
	 * @param message the error message
	 * @param status  the http status attached to the error
	 * @return the error response
	 */
	public static ErrorResponseDto build(final String code, final String message, final HttpStatus status) {
		return buildDetails(code, message, status);
	}

	/**
	 * Builds an error response based on given parameters.
	 *
	 * @param code              the error code
	 * @param message           the error message
	 * @param status            the http status attached to the error
	 * @param invalidParameters the list of invalid parameters
	 * @return the error response
	 */
	public static ErrorResponseDto build(final String code, final String message, final HttpStatus status,
			final List<InvalidParameterDto> invalidParameters) {
		return buildDetails(code, message, status, invalidParameters);
	}

	private static ErrorResponseDto buildDetails(final String code, final String message, final HttpStatus status) {
		return buildDetails(code, message, status, null);
	}

	private static ErrorResponseDto buildDetails(final String code, final String message, final HttpStatus status,
			final List<InvalidParameterDto> invalidParameters) {
		final ErrorResponseDto errorResponseDetails = new ErrorResponseDto();
		errorResponseDetails.setErrorCode(code);
		errorResponseDetails.setErrorMessage(message);
		if (!Objects.isNull(status)) {
			errorResponseDetails.setStatus(status.value());
			
			if(status == HttpStatus.NOT_FOUND) {
				errorResponseDetails.setErrorCode("OBJECT_NOT_FOUND");
			}
		}
		errorResponseDetails.setTimestamp(LocalDateTime.now());
		if (!CollectionUtils.isEmpty(invalidParameters)) {
			errorResponseDetails.setErrors(invalidParameters);
		}
		return errorResponseDetails;
	}

}