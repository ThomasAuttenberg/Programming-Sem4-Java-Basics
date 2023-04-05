package Views;

import Models.Habitat;
import Models.WindowModel;

import javax.swing.*;
import java.awt.*;

public class WindowView extends JFrame {
    Habitat habitat;
    JPanel controlPanel;

    {
        setBackground(Color.BLUE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1000,1000);
        this.setContentPane(new JPanel());
        this.getContentPane().setLayout(new BorderLayout());
        this.setVisible(true);
    }

   /* public WindowView(JComponent[] components){
        this.components = components;
    }*/
    public WindowView(WindowModel model){

        habitat = model.getHabitat();
        controlPanel = model.getControlPanel();
        this.add(habitat);
        this.add(controlPanel, BorderLayout.EAST);

    }


}
