package ru.urfu.Server.GameLogic.GameObjects;

import org.springframework.stereotype.Component;

public class Brick implements IGameObject {
    private int health = 10;
    private String type = "brick";

    @Override
    public Direction getDirection() {
        return Direction.Up;
    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public boolean isDestructible() {
        return true;
    }

    @Override
    public boolean canProjectilePass() {
        return false;
    }

    @Override
    public boolean canPlayerPass() {
        return false;
    }

    @Override
    public void hit(int damage) {
        health -= damage;
    }
}
