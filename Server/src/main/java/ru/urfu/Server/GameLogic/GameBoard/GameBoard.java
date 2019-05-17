package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.Brick;
import ru.urfu.Server.GameLogic.GameObjects.IGameObject;
import ru.urfu.Server.GameLogic.GameObjects.IPlayer;
import ru.urfu.Server.GameLogic.GameObjects.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class GameBoard implements IGameBoard {
    private GameState gameState = GameState.WaitingForPlayers;
    private IGameObject[][] map;
    private IPlayer[] players = new IPlayer[10];
    private HashMap<String, Point> playersPositions = new HashMap<>();

    public GameBoard() {
        map = new IGameObject[10][10];
        for (int i = 0; i < 10; i++) {
            map[i][0] = new Brick();
            map[i][9] = new Brick();
        }
        for (int j = 1; j < 9; j++) {
            map[0][j] = new Brick();
            map[9][j] = new Brick();
        }
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
    public IPlayer[] getPlayers() {
        return players;
    }

    @Override
    public void startGame() {
        gameState = GameState.InProgress;
    }

    @Override
    public void processPlayerAction(PlayerAction playerAction) {
        if (playerAction.getPlayerActionEnum() == PlayerActionEnum.Connected) {
            gameState = GameState.InProgress;
            map[5][5] = new Player(0, playerAction.getPlayerName());
            playersPositions.put(playerAction.getPlayerName(), new Point(5, 5));
        }
        if (playerAction.getPlayerActionEnum() == PlayerActionEnum.MoveUp) {
            Point playerPosition = playersPositions.get(playerAction.getPlayerName());
            Point newPos = new Point(playerPosition.x, playerPosition.y - 1);
            playersPositions.replace(playerAction.getPlayerName(), newPos);
            map[newPos.x][newPos.y] = map[playerPosition.x][playerPosition.y];
            map[playerPosition.x][playerPosition.y] = null;
        }
    }

    @Override
    public void iterate() {

    }
}
