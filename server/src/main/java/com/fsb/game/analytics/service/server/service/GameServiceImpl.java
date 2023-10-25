
package com.fsb.game.analytics.service.server.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fsb.game.analytics.service.core.constant.GameConstant;
import com.fsb.game.analytics.service.core.exception.dto.InvalidParameterDto;
import com.fsb.game.analytics.service.core.model.Game;
import com.fsb.game.analytics.service.core.model.InvocationContext;
import com.fsb.game.analytics.service.server.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
    private final DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * ConcurrentHashMap for In-Memory Caching
     */
    private Map<String, Game> gameMap = new ConcurrentHashMap<String, Game>();

    private void validateRequestBody(Game game) {
        List<InvalidParameterDto> invalidParameters = new ArrayList<>();

        if (game.getName() == null || game.getName().equals("")) {

            invalidParameters.add(InvalidParameterDto.builder().errorField("name")
                    .errorMessage("Game name is mandate and must not be null or empty").build());
        }

        if (game.getStatus() == null) {
            invalidParameters.add(InvalidParameterDto.builder().errorField("status")
                    .errorMessage("Game Status  is mandate and must not be null or empty").build());
        }

        if (game.getName() != null && !game.getName().isEmpty() && this.gameMap.get(game.getName()) != null) {
            invalidParameters.add(InvalidParameterDto.builder().errorField("status")
                    .errorMessage("Game with name " + game.getName() + " already exist ,it should be unqiue").build());
        }

        if (!invalidParameters.isEmpty()) {
            throw new BusinessException(GameConstant.BAD_REQUEST, GameConstant.GAME_REQUIRED_FIELD_NULL_ERROR,
                    HttpStatus.BAD_REQUEST, invalidParameters);
        }
    }

    /** {@inheritDoc} */
    public Game addGame(Game game) {

        log.info("GameAnalytics:: Call to create new Game Object");

        /* Code to validate Request Body */
        validateRequestBody(game);
        game.setId(UUID.randomUUID().toString());
        game.setCreatedDate(LocalDateTime.now(ZoneId.of(GameConstant.UTC)).format(dateTimeformatter));
        game.setLastModifiedDate(LocalDateTime.now(ZoneId.of(GameConstant.UTC)).format(dateTimeformatter));
        game.setCreatedBy(InvocationContext.getUserId());
        game.setLastModifiedBy(InvocationContext.getUserId());
        this.gameMap.put(game.getName(), game);
        return game;
    }

    /** {@inheritDoc} */
    @CachePut(value = "game", key = "#gameName")
    public Game updateGame(Game game, String gameName) {

        log.info("GameAnalytics:: Call to update existing  Game Object");
        /* Code to check if existing GameObject exits */
        Game gameObj = this.getGamebyName(gameName);
        gameObj.setStatus(game.getStatus());
        gameObj.setDescription(game.getDescription());
        gameObj.setLastModifiedDate(LocalDateTime.now(ZoneId.of(GameConstant.UTC)).format(dateTimeformatter));
        gameObj.setLastModifiedBy(InvocationContext.getUserId());
        return gameObj;
    }

    /** {@inheritDoc} */
    @Cacheable(value = "game", key = "#gameName")
    public Game getGamebyName(String gameName) {
        log.info("First Try to check from Cache , if not then from Hashamp");
        if (this.gameMap.get(gameName) != null) {
            return this.gameMap.get(gameName);
        } else {
            throw new BusinessException(GameConstant.OBJECT_NOT_FOUND,
                    String.format(" %s  Game Object not exit ", gameName), HttpStatus.NOT_FOUND);
        }

    }

    /** {@inheritDoc} */
    @CacheEvict(value = "game", key = "#gameName")
    public void deleteGame(String gameName) {
        log.info("GameAnalytics:: Call to deletew Game Object");
        this.getGamebyName(gameName);
        this.gameMap.remove(gameName);        
    }

}
