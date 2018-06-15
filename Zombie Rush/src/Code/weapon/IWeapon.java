package Code.weapon;

import Code.bullet.Bullet;

public interface IWeapon
{
    // стрельба
    Bullet[] fire(double x, double y, double direction);
    // вернуть макс. кол-во патронов.
    int maxAmmo();
    // вернуть оставш. кол-во патронов.
    int ammoLeft();
    // -//-
    String name();
    // Перезарядка
    boolean reload();
}