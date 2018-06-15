package Code.entity;

import Code.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class GameObject  {
    protected double pos_x, pos_y;
    protected static int width = 100, height = 66;
    public boolean visible = true;
    protected int direction_x, direction_y;
    protected Image image;

    public void move() {
        if(pos_x + direction_x - width/4  < 0 || pos_x + direction_x + width  > Game.width || pos_y + direction_y -height/4 < 0 || pos_y + direction_y + height + 20  > Game.height)
            return;
        pos_x += direction_x;
        pos_y += direction_y;
    }
    public void moveTo(double x, double y) {
        this.pos_x = x;
        this.pos_y = y;
    }
    public double getPos_x() {
        return pos_x ;
    }
    public double getPos_y() {
        return pos_y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean v) {
        visible = v;
    }
    public Image getImage() {
        if(null == image) {
            String image_location = "/Resources/" + this.getIcon();
            ImageIcon ii = new ImageIcon(getClass().getResource(image_location));
            image = ii.getImage();
        }
        return image;
    }
    abstract public Rectangle getCol();
    abstract public AffineTransform getAffineTransform();
    abstract public String getIcon();
}
