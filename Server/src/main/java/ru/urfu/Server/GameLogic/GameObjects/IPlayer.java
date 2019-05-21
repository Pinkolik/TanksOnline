package ru.urfu.Server.GameLogic.GameObjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Player.class, name = "player")})
public interface IPlayer extends IGameObject {
    String getName();
    int getMovesCount();
    void increaseMovesCount();
    int getFiredShotsCount();
    void increaseFiredShotsCount();
    int getKillsCount();
    void increaseKillsCount();
}
