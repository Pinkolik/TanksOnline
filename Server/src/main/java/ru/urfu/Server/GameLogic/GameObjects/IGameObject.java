package ru.urfu.Server.GameLogic.GameObjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Brick.class, name = "brick"),
        @JsonSubTypes.Type(value = Player.class, name = "player"),
        @JsonSubTypes.Type(value = Projectile.class, name = "projectile"),
        @JsonSubTypes.Type(value = Water.class, name = "water"),
        @JsonSubTypes.Type(value = Rock.class, name = "rock"),
        @JsonSubTypes.Type(value = Bush.class, name = "bush")})
public interface IGameObject {
    Direction getDirection();
    void setDirection(Direction direction);
    int getHealth();
    boolean isDestructible();
    boolean canProjectilePass();
    boolean canPlayerPass();
    void hit(int damage);
}
