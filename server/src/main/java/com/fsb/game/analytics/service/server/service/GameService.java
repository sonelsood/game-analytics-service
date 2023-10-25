
package com.fsb.game.analytics.service.server.service;

import com.fsb.game.analytics.service.core.model.Game;

public interface GameService {

    /**
     * Service method to create Game Managed Object
     * @param gameObj
     * @return
     */
    Game addGame(Game gameObj);

    /**
     * Update existing Game Object
     * @param gameObj
     * @param gameName
     * @return
     */
    Game updateGame(Game gameObj, String gameName);

    /**
     * Get Game Object by gameName 
     * @param gameName
     * @return
     */
    Game getGamebyName(String gameName);

    /**
     * Delete Game Object
     * @param gameName
     * @return
     */
    void deleteGame(String gameName);

}
