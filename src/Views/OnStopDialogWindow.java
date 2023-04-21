package Views;
import Models.Entities.GoldFish;
import Models.Entities.Guppie;
import Models.WindowModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class OnStopDialogWindow extends JDialog {
    public enum SimulationStatus {
        continues,
        stopped
    }

    public SimulationStatus userResponse;
    private JButton button_ok;
    private JPanel container;
    private JButton button_cancel;
    private JTextArea statistics;
    public OnStopDialogWindow(WindowModel model){
        setTitle("STATISTICS");
        setSize(400,300);
        setLocationRelativeTo(null);
        container = new JPanel();
        container.setVisible(true);
        container.setLayout(null);
        container.setBackground(Color.darkGray);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        userResponse = SimulationStatus.continues;
        //setLayout(null);
        Init_buttons();
        setModal(true);
        long seconds = (new Date().getTime() - model.getSimulationBeginTime())/1000;
        int guppies = model.getStatisticsManager().instancesCounter.getNumberOf(Guppie.class);
        int goldFishes = model.getStatisticsManager().instancesCounter.getNumberOf(GoldFish.class);
        InitStatistics(seconds, guppies, goldFishes);
        add(container);
    }

    private void InitStatistics(long seconds, int guppies, int goldFishes){
        Color textColor = new Color(182, 182, 182);
        statistics = new JTextArea();
        statistics.setEnabled(false);
       // statistics.setFocusable(false);
        statistics.setBackground(Color.DARK_GRAY);
        String time = Long.toString(seconds);
        String fishGuppie = Integer.toString(guppies);
        String fishGoldFish = Integer.toString(goldFishes);
        String result = "\n\n        FULL SIMULATION STATISTICS: \n\n\nSeconds: " + time + " \nGuppies: " + fishGuppie + " \nGold Fish: " + fishGoldFish;
        statistics.setText(result);


        Font font = new Font("Verdana", Font.BOLD, 12);
        statistics .setBounds(60,0,250,200);
        statistics .setFont(font);
        statistics.setDisabledTextColor(textColor);
        container.add (statistics );



    }
    private void Init_buttons(){
        Font font = new Font("Verdana", Font.BOLD, 9);
        Color textColor = new Color(217, 217, 217);
        this.button_ok = new JButton("Stop");
        this.button_cancel = new JButton("Continue");
        button_cancel.setFont(font);
        button_ok.setFont(font);
        button_ok.setEnabled(true);
        button_cancel.setEnabled(true);
        button_ok.setOpaque(true);
        button_cancel.setOpaque(true);
        button_ok.setBounds(80,200,90,30);
        button_ok.setBackground(Color.gray);
        button_ok.setForeground(textColor);
        button_cancel.setBounds(220,200,90,30);
        button_cancel.setBackground(Color.gray);
        button_cancel.setForeground(textColor);

        button_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userResponse = SimulationStatus.stopped;
                setVisible(false);
            }
        });


        button_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userResponse = SimulationStatus.continues;
                setVisible(false);
            }
        });
        container.add(button_cancel);
        container.add(button_ok);
    }

    public SimulationStatus getUserResponse(){
        return userResponse;
    }

}

