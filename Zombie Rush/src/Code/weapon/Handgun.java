package Code.weapon;
import Code.bullet.Bullet;
import Code.Game;

public class Handgun extends Weapon implements IWeapon {

    private static final int firerate = 5;

    public Bullet[] fire(double x, double y, double direction) {
        if(noAmmo())
            return new Bullet[0];
        Bullet[] bullets = new Bullet[1];
        bullets[0] = newBullet(x, y, direction);
        ammo--;
        return bullets;
    }

    @Override
    public String name() {
        return "Handgun";
    }
    @Override
    public int maxAmmo() {
        return 6;
    }
    @Override
    public void playFireSound() {
        Game.playSound("/weapon/handgun/handgun_fire.wav");
    }
    @Override
    public void playReloadSound() {
        Game.playSound("/weapon/handgun/handgun_reload.wav");
    }
}
