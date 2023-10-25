
package com.fsb.game.analytics.service.core.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({ "id", "name", "status", "lastModifiedDate", "createdDate" })
public class Game {

    private String id;

    @Schema(name = "name", description = "Name of the Game", required = true, implementation = String.class, example = "#game1")
    /* Unique Name */
    private String name;

    @Schema(name = "description", description = "description of the Game", required = true, implementation = String.class, example = "#description")
    private String description;

    @Schema(name = "status", description = "Operational Status of game", implementation = UUID.class, example = "ACTIVE")
    private GameStatus status;

    /*
     * record modified date
     */
    @Schema(name = "lastModifiedDate", description = "lastModifiedDate", implementation = String.class, required = false, example = "lastModifiedDate", hidden = true)
    private String lastModifiedDate;

    /**
     * record insertion date
     */
    @Schema(name = "createdDate", description = "createdDate", implementation = String.class, required = false, example = "createdDate", hidden = true)
    private String createdDate;

    /** User who have created the record */
    @Schema(name = "createdBy", description = "createdBy", implementation = String.class, required = false, example = "createdBy", hidden = true)
    private String createdBy;;

    /** most recent user to modify the record */
    @Schema(name = "lastModifiedBy", description = "lastModifiedBy", implementation = String.class, required = false, example = "lastModifiedBy", hidden = true)
    private String lastModifiedBy;

}