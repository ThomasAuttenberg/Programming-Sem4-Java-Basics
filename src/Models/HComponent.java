package Models;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

public class HComponent extends JComponent {
    private int x;
    private int y;
    private int layer;
    private URL imageURL;
    private BufferedImage img;

    {
        try {
            img = ImageIO.read(new URL("src/res/HabitatBackground.png"));
        }catch(I)
    }

    HComponent(int x, int y){
        this.setLocation(x,y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D)g).drawImage;
    }
}
