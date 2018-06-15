package Code.bullet;

public class ShotgunShot extends Bullet {
    private final int travel_distance = 140;

    public ShotgunShot(double x, double y, double d) {
        super(x, y, d);
    }
    @Override
    public void move() {
        super.move();
        if(Math.abs(pos_x - fPosx) > travel_distance) {
            setVisible(false);
        }
    }
}
