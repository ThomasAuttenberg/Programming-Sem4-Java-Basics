package Models;

import Models.Fishes.GoldFish;

import javax.swing.*;

public class WindowModel {
    private Habitat habitat;
    private JPanel controlPanel;
    {
        habitat = new Habitat();
        habitat.addGeneratingType(new GoldFish(100,100));
        controlPanel = new JPanel();
    }

    public JPanel getControlPanel(){
        return controlPanel;
    }
    public Habitat getHabitat(){
        return habitat;
    }
}
