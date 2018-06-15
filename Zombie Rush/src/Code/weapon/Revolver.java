package Code.weapon;

import Code.Game;
import Code.bullet.Bullet;


public class Revolver extends Handgun {

    private static final int firerate = 6;

    public Bullet newBullet(double x, double y, double direction) {
        Bullet bullet = new Bullet(x, y, direction);
        bullet.setSpeed(firerate);
        return bullet;
    }

    @Override
    public String name() {
        return "Revolver";
    }
    @Override
    public void playFireSound() {
        Game.playSound("weapon/revolver/revolver_fire.wav");
    }
    @Override
    public void playReloadSound() {
        Game.playSound("weapon/revolver/revolver_reload.wav");
    }
}
