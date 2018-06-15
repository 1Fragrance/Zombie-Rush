package Code.bullet;
import Code.Game;
import Code.entity.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class Bullet extends GameObject implements Serializable {
    private int bullet_speed = 6;
    protected static int width = 8, height = 8;
    protected double fPosx, fPosy;
    protected double direction;


    public Bullet(double x, double y, double direction) {
        this.pos_x = x;
        this.pos_y = y;
        this.fPosx = x;
        this.fPosy = y;
        this.direction = direction;
    }

    public void move() {
        pos_x += Math.cos(direction * Math.PI / 180) * bullet_speed ;
        pos_y += Math.sin(direction * Math.PI / 180) * bullet_speed ;

        if(pos_x > Game.width || pos_x < 0 ||
           pos_y > Game.height || pos_y < 0)
            setVisible(false);
    }

    public AffineTransform getAffineTransform() { return null; }

    public Rectangle getCol() {
        return new Rectangle((int)pos_x, (int)pos_y, width, height);
    }
    public String getIcon() { return "bullet.png"; }
    public int getSpeed() {
        return bullet_speed;
    }
    public void setSpeed(int bullet_speed) {
        this.bullet_speed = bullet_speed;
    }
    public double getDir() { return direction;}
}
