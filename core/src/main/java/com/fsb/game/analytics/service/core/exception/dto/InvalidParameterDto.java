package com.fsb.game.analytics.service.core.exception.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvalidParameterDto {

	
	@Schema(name = "errorField", description = "errorField of invalid error", implementation = String.class, example = "id")
    private String errorField;
    @Builder.Default
    @Schema(name = "errorCode", description = "errorCode of invalid error", implementation = String.class, example = "OBJECT_NOT_DEFINED")
    private String errorCode="OBJECT_NOT_DEFINED";
    
    @Schema(name = "errorMessage", description = "errorMessage of invalid error", implementation = String.class, example = "id is mandatory")
    private String errorMessage;

}