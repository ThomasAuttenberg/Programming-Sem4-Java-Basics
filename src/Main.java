import Models.HabitatModel;
import Views.HabitatView;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        HabitatView view = new HabitatView(new HabitatModel());

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLUE);
        panel.setSize(200,200);
        panel.setVisible(true);


        JFrame frame = new JFrame("Meow");
        frame.setVisible(true);
        frame.setSize(500,500);
        frame.add(view);
       // frame.repaint();
        //frame.setBackground(Color.blue);
    }
}