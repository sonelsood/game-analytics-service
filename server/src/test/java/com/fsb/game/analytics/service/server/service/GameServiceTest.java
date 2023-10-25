
package com.fsb.game.analytics.service.server.service;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fsb.game.analytics.service.core.constant.GameConstant;
import com.fsb.game.analytics.service.core.model.Game;
import com.fsb.game.analytics.service.core.model.GameStatus;
import com.fsb.game.analytics.service.server.exception.BusinessException;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GameServiceTest {
    @InjectMocks
    private GameServiceImpl gameService;

    @Before
    public void setup() {
        Map<String, Game> gameMap = new ConcurrentHashMap<String, Game>();
        Game obj = new Game();
        obj.setName("Hockey1");
        obj.setDescription("Hockey Game");
        obj.setStatus(GameStatus.ACTIVE);

        gameMap.put("Hockey1", obj);

        ReflectionTestUtils.setField(gameService, "gameMap", gameMap);

    }

    @Test
    public void createGameTest_Success() {

        Game obj = new Game();
        obj.setName("Hockey2");
        obj.setDescription("Hockey Game");
        obj.setStatus(GameStatus.ACTIVE);
        Game response = gameService.addGame(obj);
        assertEquals(response.getName(), "Hockey2");

    }

    @Test
    public void createGameTest_DuplicateGame() {
        Game obj = new Game();
        obj.setName("Hockey1");
        obj.setDescription("Hockey Game");
        obj.setStatus(GameStatus.ACTIVE);

        try {
            gameService.addGame(obj);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getCode(), GameConstant.BAD_REQUEST);
            Assert.assertEquals(e.getMessage(), GameConstant.GAME_REQUIRED_FIELD_NULL_ERROR);
        }
    }

    @Test
    public void createGameTest_EmptyName() {
        Game obj = new Game();
        obj.setDescription("Hockey Game");
        obj.setStatus(GameStatus.ACTIVE);

        try {
            gameService.addGame(obj);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getCode(), GameConstant.BAD_REQUEST);
            Assert.assertEquals(e.getMessage(), GameConstant.GAME_REQUIRED_FIELD_NULL_ERROR);
        }
    }

    @Test
    public void GetGameTest_Success() {

        Game response = gameService.getGamebyName("Hockey1");
        assertEquals(response.getName(), "Hockey1");
        assertEquals(response.getStatus(), GameStatus.ACTIVE);
    }

    @Test
    public void GetGameTest_NotFound() {
        try {
            gameService.getGamebyName("Hockey1");
        } catch (BusinessException e) {
            Assert.assertEquals(e.getCode(), GameConstant.OBJECT_NOT_FOUND);

        }
    }

    @Test
    public void DeleteGameTest_Success() {
        gameService.deleteGame("Hockey1");
        try {
            gameService.getGamebyName("Hockey1");
        } catch (BusinessException e) {
            Assert.assertEquals(e.getCode(), GameConstant.OBJECT_NOT_FOUND);

        }

    }

    @Test
    public void UpdateGameTest_Success() {
        Game game = new Game();
        game.setStatus(GameStatus.INACTIVE);
        game.setDescription("New Hockey Game");
        Game response = gameService.updateGame(game, "Hockey1");

        assertEquals(response.getStatus(), GameStatus.INACTIVE);

    }
}
