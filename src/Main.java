import Models.Habitat;
import Views.WindowView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        WindowView view = new WindowView(new Habitat().getComponents());

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