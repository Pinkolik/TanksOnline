package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.IGameObject;
import ru.urfu.Server.GameLogic.GameObjects.IPlayer;

public interface IGameBoard {
    GameState getGameState();
    IGameObject[][] getMap();
    IPlayer[] getPlayers();
    void startGame();
    void processPlayerAction(PlayerAction playerAction);
    void iterate();
}
