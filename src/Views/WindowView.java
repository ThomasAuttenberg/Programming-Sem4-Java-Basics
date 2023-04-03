package Views;

import javax.swing.*;
import java.awt.*;

public class WindowView extends JFrame {
    JComponent[] components;

    {
        setBackground(Color.BLUE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1000,1000);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
    }

   /* public WindowView(JComponent[] components){
        this.components = components;
    }*/

}
