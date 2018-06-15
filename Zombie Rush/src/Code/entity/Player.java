package Code.entity;
import Code.Game;
import Code.bullet.Bullet;
import Code.weapon.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Player extends Human implements Serializable {

    // Singleton
    private static volatile Player instance;

    public ArrayList<Bullet> bullets;
    public ArrayList<Weapon> weapons;
    public int weaponEq;
    private static final int speed = 2;
    private static final int width = 45;
    private static final int height = 45;

    private Player() {
        super();
        bullets = new ArrayList<>();
        weapons = new ArrayList<>();
        weapons.add(new Handgun());
        weapons.add(new Revolver());
        weapons.add(new Shotgun());
        weapons.add(new Rifle());
        weaponEq = 0;

    }
    public /*synchronized*/ static Player getInstance() {
        Player localInstance = instance;
        if(localInstance == null) {
            synchronized (Player.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Player();
                }
            }
        }
        /*if(instance == null) {
            instance = new Player();
        }*/
        return localInstance;
    }

    public Weapon getWeapon() {
        return weapons.get(weaponEq);
    }
    public void nextWeapon() {
        if(weaponEq == weapons.size() - 1) {
            weaponEq = 0;
        } else {
            weaponEq += 1;
        }
        image = null;
    }
    public void prevWeapon() {
        if(weaponEq == 0) {
            weaponEq = weapons.size() - 1;
        } else {
            weaponEq -= 1;
        }
        image = null;
    }
    // стрельба
    public void fire() {
        //double point_x = getPos_x() * Math.cos((Math.toRadians(direction)));
        //double point_y = getPos_y() * Math.sin((Math.toRadians(direction)));
        //double point_x = getPos_x() * Math.cos(direction) - getPos_y() * Math.sin(direction);
        //double point_y = getPos_x() * Math.sin(direction) + getPos_y() * Math.cos(direction);
        Collections.addAll(bullets, getWeapon().fire(getPos_x(), getPos_y(), direction));
    }
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W)
            direction_y = -speed; // Up
        if(key == KeyEvent.VK_S)
            direction_y = speed;  // Down
        if(key == KeyEvent.VK_A)
            direction_x = -speed; // Left
        if(key == KeyEvent.VK_D)
            direction_x = speed;  // Right
        if(key == KeyEvent.VK_Q)
            prevWeapon();
        if(key == KeyEvent.VK_E)
            nextWeapon();
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W)
            direction_y = 0; // stop Up
        if(key == KeyEvent.VK_S)
            direction_y = 0; // stop Down
        if(key == KeyEvent.VK_A)
            direction_x = 0; // stop Left
        if(key == KeyEvent.VK_D)
            direction_x = 0; // stop Right
    }
    public String getIcon() {
        switch(weaponEq) {
            case 0:
                return "player_hg.png";
            case 1:
                return "player_hg.png";
            case 2:
                return "player_sg.png";
            case 3:
                return "player_rf.png";
            default:
                return null;
        }
    }
    public void playDeathSound() {
        Game.playSound("deathSound.wav");
    }
    public void playGetDMGSound() {
        Game.playSound("getDMGSound.wav");
    }

}

