package com.fsb.game.analytics.service.server.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines the application exception reasons.
 */
@Getter
@AllArgsConstructor
public enum ApplicationExceptionReason {

	BEAN_PROPERTY_NOT_EXISTS("Property '%s' for object '%s' doesn't exists", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String code = BusinessExceptionReason.class.getSimpleName();
	private final String message;
	private final HttpStatus httpStatus;
}
