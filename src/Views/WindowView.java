package Views;

import javax.swing.*;
import java.awt.*;

public class WindowView extends JFrame {
    JComponent[] components;

    {
        setBackground(Color.BLUE);
    }

    public WindowView(JComponent[] components){
        this.components = components;
    }

}
