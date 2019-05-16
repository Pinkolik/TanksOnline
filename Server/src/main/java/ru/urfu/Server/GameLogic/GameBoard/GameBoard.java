package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.Brick;
import ru.urfu.Server.GameLogic.GameObjects.IGameObject;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameBoard implements IGameBoard {
    private GameState gameState = GameState.WaitingForPlayers;
    private IGameObject[][] map;

    public GameBoard() {
        map = new IGameObject[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                map[i][j] = new Brick();
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public IGameObject[][] getMap() {
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
