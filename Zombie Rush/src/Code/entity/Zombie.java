package Code.entity;

import Code.Game;

import java.io.Serializable;

public class Zombie extends Human implements Serializable {
    private Player player;
    private static final int width = 64;
    private static final int height = 59;
    public Zombie(Player _player) {
        player = _player;
    }
    public void move() {
        double diffX =  player.getPos_x()- pos_x;
        double diffY = player.getPos_y() - pos_y;
        double angle = (float)Math.atan2(diffY, diffX);
        pos_x += 1.5 * Math.cos(angle);
        pos_y += 1.5 * Math.sin(angle);
        watch(player.getPos_x(),player.getPos_y());
    }
    public String getIcon() {
        return "zombie.png";
    }
    public static void playDeathSound() {
        Game.playSound("enemyDeath.wav");
    }
}
