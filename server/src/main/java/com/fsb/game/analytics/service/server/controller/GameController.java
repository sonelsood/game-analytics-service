
package com.fsb.game.analytics.service.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsb.game.analytics.service.core.exception.dto.ErrorResponseDto;
import com.fsb.game.analytics.service.core.model.Game;
import com.fsb.game.analytics.service.server.service.GameService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/game-analytics/v1/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    GameService gameService;

    @PostMapping()
    @Operation(tags = "Game CRUD APIs", description = "Create new Game Object", operationId = "schedulerConfigTime", summary = "Create new Game Object", responses = {
            @ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = Game.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    public ResponseEntity<Game> addGame(@RequestBody Game gameObj) {
        return new ResponseEntity<Game>(gameService.addGame(gameObj), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{gameName}")
    @Operation(tags = "Game CRUD APIs", description = "Get Game Object by gameId", operationId = "schedulerConfigTime", summary = "Get Game Object by gameId", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Game.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Not Found Request", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    public ResponseEntity<Game> getGameById(@PathVariable String gameName) {
        return new ResponseEntity<Game>(gameService.getGamebyName(gameName), HttpStatus.OK);
    }

    @PutMapping(value = "/{gameId}")
    @Operation(tags = "Game CRUD APIs", description = "Update existing game Object", operationId = "schedulerConfigTime", summary = "Update existing game Object", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Game.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    public ResponseEntity<Game> updateGame(@RequestBody Game gameObj, @PathVariable String gameId) {
        return new ResponseEntity<Game>(gameService.updateGame(gameObj, gameId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{gameId}")
    @Operation(tags = "Game CRUD APIs", description = "Delete Exiting Game Object", operationId = "schedulerConfigTime", summary = "Delete Exiting Game Object", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Game.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    public ResponseEntity<Void> deleteGame(@PathVariable String gameId) {
        gameService.deleteGame(gameId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
