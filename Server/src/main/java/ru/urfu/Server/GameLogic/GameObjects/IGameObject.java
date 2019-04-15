package ru.urfu.Server.GameLogic.GameObjects;

public interface IGameObject {
    int getHealth();
    boolean isDestructible();
    boolean canProjectilePass();
    boolean canPlayerPass();
    void hit(int damage);
}
