package Views;

import Models.Habitat;
import Models.WindowModel;

import javax.swing.*;
import java.awt.*;

public class WindowView extends JFrame {
    Habitat habitat;
    JPanel controlPanel;
    StatisticsLabel label;
    int width;
    int height;

    WindowModel model;

    {
        setBackground(Color.BLUE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
    }

   /* public WindowView(JComponent[] components){
        this.components = components;
    }*/
    public WindowView(WindowModel model, int width, int height){

        this.width = width;
        this.height = height;
        this.model = model;

        habitat = model.getHabitat();
        controlPanel = model.getControlPanel();

        this.setFocusable(true);
        this.add(habitat);

        this.add(controlPanel, BorderLayout.EAST);
        controlPanel.add(new Button("MEOW"));


        this.setSize(width,height);
        this.setVisible(true);

    }

    public void toogleStatisticsLabel() {
        if (model.getSimulationBeginTime() != -1) {

            if(label == null)
                initLabel();

            label.setVisible(!label.isVisible());

        }
    }

    public void initLabel(){
        for(Component comp : habitat.getComponents()){
            if(comp == label) habitat.remove(label);
            System.out.println("Removed");
        }
        label = new StatisticsLabel(model.getSimulationBeginTime());

        habitat.add(label);
        label.setBounds(0,0,300,100);
    }

    public StatisticsLabel getStatisticsLabel(){
        return label;
    }




}
