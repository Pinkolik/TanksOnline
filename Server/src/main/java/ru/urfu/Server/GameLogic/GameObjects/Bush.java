package ru.urfu.Server.GameLogic.GameObjects;

public class Bush implements IGameObject {
    private String type = "bush";

    @Override
    public Direction getDirection() {
        return Direction.Up;
    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public int getHealth() {
        return 1;
    }

    @Override
    public boolean isDestructible() {
        return false;
    }

    @Override
    public boolean canProjectilePass() {
        return true;
    }

    @Override
    public boolean canPlayerPass() {
        return true;
    }

    @Override
    public void hit(int damage) {

    }
}
