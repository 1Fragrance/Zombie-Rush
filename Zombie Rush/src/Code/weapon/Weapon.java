package Code.weapon;
import Code.bullet.Bullet;

import java.io.Serializable;

abstract public class Weapon implements IWeapon, Serializable {
    public int ammo = 0;
    protected int firerate;

    public Weapon() {
        this.reload();
    }
    public Bullet[] fire(double x, double y, double direction) {
        Bullet bullet = new Bullet(x, y, direction);
        bullet.setSpeed(getFirerate());
        Bullet[] bs = { bullet };
        ammo--;
        return bs;
    }
    public String name() {
        return this.getClass().toString();
    }
    public int ammoLeft() {
        return ammo;
    }
    public boolean noAmmo() {
        if(ammo == 0)
            return true;
        else {
            return false;
        }
    }
    public boolean reload() {
        this.ammo = maxAmmo();
        return true;
    }
    public int getFirerate() {
        return firerate;
    }
    public Bullet newBullet(double x, double y, double direction) {
        return new Bullet(x, y, direction);
    }
    public abstract void playFireSound();
    public abstract void playReloadSound();
}
