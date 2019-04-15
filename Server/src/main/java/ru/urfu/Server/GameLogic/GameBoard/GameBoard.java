package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.IGameObject;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameBoard implements IGameBoard {
    private GameState gameState = GameState.WaitingForPlayers;
    private Map<Point, IGameObject> map;

    public GameBoard(int width, int height)
    {
        map = new HashMap<Point, IGameObject>();
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public Map<Point, IGameObject> getMap() {
        return map;
    }

    @Override
    public void startGame() {
        gameState = GameState.InProgress;
    }

    @Override
    public void processPlayerAction(int playerId, PlayerAction action) {

    }

    @Override
    public void iterate() {

    }
}
