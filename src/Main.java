import Models.Fishes.GoldFish;
import Models.Habitat;
import Views.WindowView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        WindowView view = new WindowView();
        Habitat habitat = new Habitat();
        habitat.setSize(500,500);
        habitat.addGeneratingType(new GoldFish(200,200));
        view.add(habitat, BorderLayout.WEST);
       // frame.repaint();
        //frame.setBackground(Color.blue);
    }
}