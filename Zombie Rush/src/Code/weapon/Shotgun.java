package Code.weapon;
import Code.Game;
import Code.bullet.*;

public class Shotgun extends Weapon implements IWeapon {
    private static int firePower = 7;
    private static final int firerate = 4;

    public Bullet[] fire(double x, double y, double direction) {
        if(noAmmo())
            return new Bullet[0];
        Bullet[] bullets = new Bullet[firePower];

        for(int i = 0; i < bullets.length; i++) {
            double bullet_x, bullet_y;
            bullet_x = x +  Math.round((Math.random() * 40));
            bullet_y = y +  Math.round(Math.random() * 10);
            Bullet b = new ShotgunShot(bullet_x, bullet_y, direction);
            bullets[i] = b;
        }
        ammo--;
        return bullets;
    }

    @Override
    public String name() {
        return "Shotgun";
    }
    @Override
    public int maxAmmo() {
        return 2;
    }
    @Override
    public void playFireSound() {
        Game.playSound("weapon/shotgun/shotgun_fire.wav");
    }
    @Override
    public void playReloadSound() {
        Game.playSound("weapon/shotgun/shotgun_reload.wav");
    }
}
