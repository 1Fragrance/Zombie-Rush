package Code.entity;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class Human extends GameObject implements IRotate {
    protected int width = 50, height = 50;
    public double direction = 0;
    protected int hp;

    public Human() {
        this.hp = startHP();
    }
    public int startHP() {
        return 10;
    }
    public int getHp() {
        return hp;
    }
    public boolean isDead() {
        if(hp > 0)
            return false;
        else
            return true;
    }
    public void changeHP(int amount) {
        this.hp += amount;
    }
    public void setHP(int amount) {this.hp = amount;}
    public void watch(double x, double y) {
        double delta_x = this.pos_x - x;
        double delta_y = this.pos_y - y;
        direction = 180 + Math.atan2(delta_y,delta_x) * 180 / Math.PI;
    }

    public AffineTransform getAffineTransform() {
        AffineTransform trans = new AffineTransform();
        trans.translate(pos_x, pos_y);
        trans.rotate(Math.toRadians(direction), width / 2, height / 2);

        return trans;
    }
    public Rectangle getCol() {
        return new Rectangle((int)pos_x,(int) pos_y, width, height);
    }
    public String getIcon() {
        return null;
    }

}
