package Views;

import Models.Habitat;
import Models.WindowModel;

import javax.swing.*;
import java.awt.*;

public class WindowView extends JFrame {
    Habitat habitat;
    JPanel controlPanel;
    static final Color bgColor = Color.darkGray;
    static final Color textColor = new Color(169, 169, 169);
    public final JButton startButton;
    public final JButton stopButton;
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
        controlPanel.setLayout(new GridLayout(9,1));
        GridBagConstraints constraints = new GridBagConstraints();
        controlPanel.setBackground(Color.DARK_GRAY);

        this.setFocusable(true);
        this.add(habitat);
        this.add(controlPanel, BorderLayout.EAST);


        //Buttons setup
        startButton = new JButton("Start");
        startButton.setBackground(new Color(0,255,76));
        startButton.setContentAreaFilled(false);
        startButton.setOpaque(true);
        startButton.setFocusable(false);

        stopButton = new JButton("Stop");
        stopButton.setContentAreaFilled(false);
        stopButton.setOpaque(true);
        stopButton.setForeground(Color.WHITE);
        stopButton.setBackground(new Color(124, 2, 2));
        stopButton.setFocusable(false);

        JPanel buttonContainer = new JPanel(new GridLayout(1,2));
        buttonContainer.add(startButton);
        buttonContainer.add(stopButton);
        controlPanel.add(buttonContainer, constraints);

        //Simulation time show/hide switchers setup
        JLabel simTimeVisibilityLabel = new JLabel("  Simulation time label visibility:  ");
        simTimeVisibilityLabel.setForeground(textColor);
        simTimeVisibilityLabel.setFont(simTimeVisibilityLabel.getFont().deriveFont(Font.BOLD,14f));

        JRadioButton radioSimTimeShow = new JRadioButton("Show");
        radioSimTimeShow.setContentAreaFilled(false);
        radioSimTimeShow.setFocusable(false);
        radioSimTimeShow.setForeground(textColor);

        JRadioButton radioSimTimeHide = new JRadioButton("Hide");
        radioSimTimeHide.setContentAreaFilled(false);
        radioSimTimeHide.setFocusable(false);
        radioSimTimeHide.setForeground(textColor);

        JPanel radioContainer = new JPanel();
        radioContainer.add(radioSimTimeShow);
        radioContainer.setBackground(bgColor);
        radioContainer.add(radioSimTimeHide);

        controlPanel.add(simTimeVisibilityLabel);
        controlPanel.add(radioContainer);

        //Dialog modal window checkbox setup
        JCheckBox dialogCheckBox = new JCheckBox("Stopping dialog window");
        dialogCheckBox.setContentAreaFilled(false);
        dialogCheckBox.setForeground(textColor);
        dialogCheckBox.setFocusable(false);

        JPanel dialogCheckBoxContainer = new JPanel();
        dialogCheckBoxContainer.setSize(dialogCheckBox.getSize());
        dialogCheckBoxContainer.add(dialogCheckBox, BorderLayout.CENTER);
        dialogCheckBoxContainer.setBackground(bgColor);

        controlPanel.add(dialogCheckBoxContainer);

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
