package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class GameBoard implements IGameBoard {
    private GameState gameState = GameState.WaitingForPlayers;
    private IGameObject[][] map;
    private ArrayList<IPlayer> players = new ArrayList<>();
    private HashMap<String, Point> playersPositions = new HashMap<>();
    private int width = 10;
    private int height = 10;
    private int playersCount = 0;

    public GameBoard() {
        map = new IGameObject[width][height];
        for (int i = 0; i < width; i++) {
            map[i][0] = new Brick();
            map[i][height - 1] = new Brick();
        }
        for (int j = 1; j < height - 1; j++) {
            map[0][j] = new Brick();
            map[width - 1][j] = new Brick();
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
    public ArrayList<IPlayer> getPlayers() {
        return players;
    }

    @Override
    public void processPlayerAction(PlayerAction playerAction) {
        PlayerActionEnum action = playerAction.getPlayerActionEnum();
        switch (action) {

            case Connected:
                putPlayerOnBoard(playerAction);
                break;
            case Disconnected:
                removePlayer(playerAction);
                break;
            case MoveLeft:
            case MoveRight:
            case MoveUp:
            case MoveDown:
                movePlayer(playerAction);
                break;
            case Shoot:
                break;
            case Update:
                break;
        }
    }

    private void removePlayer(PlayerAction playerAction) {
        Point playerPosition = playersPositions.get(playerAction.getPlayerName());
        playersPositions.remove(playerAction.getPlayerName());
        map[playerPosition.x][playerPosition.y] = null;
        players.removeIf(p -> p.getName().equals(playerAction.getPlayerName()));
        if (players.size() == 0)
            gameState = GameState.WaitingForPlayers;
    }

    private void putPlayerOnBoard(PlayerAction playerAction) {
        gameState = GameState.InProgress;
        Random random = new Random();
        int x = 0;
        int y = 0;
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
        } while (map[x][y] != null && !map[x][y].canPlayerPass());
        Player player = new Player(playersCount++, playerAction.getPlayerName());
        players.add(player);
        playersPositions.put(playerAction.getPlayerName(), new Point(x, y));
        map[x][y] = player;
    }

    private void movePlayer(PlayerAction playerAction) {
        Point playerPosition = playersPositions.get(playerAction.getPlayerName());
        Point newPos = null;
        IGameObject player = map[playerPosition.x][playerPosition.y];
        switch (playerAction.getPlayerActionEnum()) {
            case MoveUp:
                newPos = new Point(playerPosition.x, playerPosition.y - 1);
                player.setDirection(Direction.Up);
                break;
            case MoveDown:
                newPos = new Point(playerPosition.x, playerPosition.y + 1);
                player.setDirection(Direction.Down);
                break;
            case MoveLeft:
                newPos = new Point(playerPosition.x - 1, playerPosition.y);
                player.setDirection(Direction.Left);
                break;
            case MoveRight:
                newPos = new Point(playerPosition.x + 1, playerPosition.y);
                player.setDirection(Direction.Right);
                break;
        }
        if (map[newPos.x][newPos.y] == null || map[newPos.x][newPos.y].canPlayerPass()) {
            playersPositions.replace(playerAction.getPlayerName(), newPos);
            map[newPos.x][newPos.y] = map[playerPosition.x][playerPosition.y];
            map[playerPosition.x][playerPosition.y] = null;
        }
    }

    @Override
    public void iterate() {

    }
}
