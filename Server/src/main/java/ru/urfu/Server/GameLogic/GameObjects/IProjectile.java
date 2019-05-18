package ru.urfu.Server.GameLogic.GameObjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.urfu.Server.GameLogic.GameBoard.ProjectileMapKeyDeserializer;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Projectile.class, name = "projectile")})
public interface IProjectile extends IGameObject {
    int getDamage();
    String getOwner();
}
