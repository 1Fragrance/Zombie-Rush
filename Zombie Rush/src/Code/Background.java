package Code;

import javax.swing.*;
import java.awt.*;

public class Background extends JPanel {
    private Image image;
    public Background(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
