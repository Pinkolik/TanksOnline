package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.IGameObject;
import ru.urfu.Server.GameLogic.GameObjects.IPlayer;

import java.awt.*;
import java.util.Map;

public interface IGameBoard {
    GameState getGameState();
    Map<Point, IGameObject> getMap();
    void startGame();
    void processPlayerAction(int playerId, PlayerAction action);
    void iterate();
}
