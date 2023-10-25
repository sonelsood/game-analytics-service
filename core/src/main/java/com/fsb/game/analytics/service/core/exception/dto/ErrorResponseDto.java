package com.fsb.game.analytics.service.core.exception.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "errorCode", "errorMessage", "status", "errors", "timestamp" })
public class ErrorResponseDto {

	@Schema(name = "errorCode", description = "errorCode of error", implementation = String.class, example = "OBJECT_NOT_FOUND")
	private String errorCode;
	
	@Schema(name = "errorMessage", description = "errorMessage of error", implementation = String.class, example = "object not found")
	private String errorMessage;
	
	@Schema(name = "status", description = "http status of error", implementation = String.class, example = "403/404/409/500")
	private Integer status;
	
	@Schema(name = "errors", description = "validation errors", anyOf = InvalidParameterDto.class)
	private List<InvalidParameterDto> errors;
	
	@Schema(name = "timestamp", description = "time at which error occured", implementation = LocalDateTime.class)
	private LocalDateTime timestamp;
	

}