package Code.weapon;

import Code.Game;
import Code.bullet.Bullet;

public class Rifle extends Weapon implements IWeapon {
    private static final int firerate = 6;

    public Bullet[] fire(double x, double y, double direction) {
        if(noAmmo())
            return new Bullet[0];
        Bullet[] bullets = new Bullet[1];
        bullets[0] = newBullet(x, y, direction);
        ammo--;
        return bullets;
    }
    @Override
    public int maxAmmo() {
        return 50;
    }
    @Override
    public String name() {
        return "Rifle";
    }
    @Override
    public void playFireSound() {
        Game.playSound("weapon/rifle/rifle_fire.wav");
    }
    @Override
    public void playReloadSound() {
        Game.playSound("weapon/rifle/rifle_reload.wav");
    }

}
