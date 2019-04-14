package ru.urfu.Server.GameLogic.GameBoard;

import ru.urfu.Server.GameLogic.GameObjects.IGameObject;

import java.awt.*;
import java.util.Map;

public interface IGameBoard {
    Map<Point, IGameObject> Map = null;
    GameState GAME_STATE = GameState.WaitingForPlayers;

    //Implement other stuff....
}
