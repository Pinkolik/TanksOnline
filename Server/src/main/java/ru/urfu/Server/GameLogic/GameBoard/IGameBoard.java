package ru.urfu.Server.GameLogic.GameBoard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.urfu.Server.GameLogic.GameObjects.IGameObject;
import ru.urfu.Server.GameLogic.GameObjects.IPlayer;
import ru.urfu.Server.GameLogic.GameObjects.IProjectile;

import java.awt.*;
import java.util.HashMap;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameBoard.class, name = "gameBoard")})
public interface IGameBoard {
    GameState getGameState();
    IGameObject[][] getMap();
    @JsonDeserialize(keyUsing = PlayerMapKeyDeserializer.class)
    HashMap<IPlayer, Point> getPlayersPositions();
    @JsonDeserialize(keyUsing = ProjectileMapKeyDeserializer.class)
    HashMap<IProjectile, Point> getProjectilesPositions();
    void processPlayerAction(PlayerAction playerAction);
}
