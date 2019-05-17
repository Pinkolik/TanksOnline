package ru.urfu.Server.GameLogic.GameObjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Brick.class, name = "brick"),
        @JsonSubTypes.Type(value = Player.class, name = "player")})
public interface IGameObject {
    int getHealth();
    boolean isDestructible();
    boolean canProjectilePass();
    boolean canPlayerPass();
    void hit(int damage);
}
