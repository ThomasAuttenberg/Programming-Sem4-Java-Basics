package Views;

import Models.HComponent;
import Models.HabitatModel;

import javax.swing.*;
import java.awt.*;

public class HabitatView extends JPanel {
    JComponent[] components;

    {
        setBackground(Color.BLUE);
    }

    public HabitatView(JComponent[] components){
        this.components = components;
    }

}
