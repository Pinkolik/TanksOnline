package ru.urfu.Server.GameLogic.GameObjects;

public class Brick implements IGameObject {
    private int health = 10;

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
