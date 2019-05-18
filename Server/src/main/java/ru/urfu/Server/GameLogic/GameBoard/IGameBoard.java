package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.IGameObject;
import ru.urfu.Server.GameLogic.GameObjects.IPlayer;
import ru.urfu.Server.GameLogic.GameObjects.Player;

import java.util.ArrayList;

public interface IGameBoard {
    GameState getGameState();
    IGameObject[][] getMap();
    ArrayList<IPlayer> getPlayers();
    void processPlayerAction(PlayerAction playerAction);
    void iterate();
}
