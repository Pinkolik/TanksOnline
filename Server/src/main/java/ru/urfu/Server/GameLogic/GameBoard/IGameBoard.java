package ru.urfu.Server.GameLogic.GameBoard;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.urfu.Server.GameLogic.GameObjects.IGameObject;
import ru.urfu.Server.GameLogic.GameObjects.IPlayer;

import java.awt.*;
import java.util.Map;

public interface IGameBoard {
    GameState getGameState();
    IGameObject[][] getMap();
    void startGame();
    void processPlayerAction(int playerId, PlayerAction action);
    void iterate();
}
