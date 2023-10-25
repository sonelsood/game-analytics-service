package com.fsb.game.analytics.service.server.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Handling exception for every request made
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorDetails {

	/** to show date time for the exception */
	private Date timestamp;

	/** to show error message for the exception */
	private String message;

	/** to show details error message for the exception */
	private String details;

	/** to show status code of error for the exception */
	private String status;

	public ErrorDetails(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
}
