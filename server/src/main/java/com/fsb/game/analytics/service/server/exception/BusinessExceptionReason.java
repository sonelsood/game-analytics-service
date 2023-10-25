package com.fsb.game.analytics.service.server.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines the business exception reasons.
 */
@Getter
@AllArgsConstructor
public enum BusinessExceptionReason {

	REQUIRED_FIELD_NULL_ERROR("Request Body required field must not be null or empty ", HttpStatus.BAD_REQUEST);
	

	private final String code = BusinessExceptionReason.class.getSimpleName();
	private final String message;
	private final HttpStatus httpStatus;


}