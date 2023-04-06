package Models;

import Models.Entities.GoldFish;
import Models.Entities.Guppie;

import javax.swing.*;
import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;

public class WindowModel {
    private Habitat habitat;
    StatisticsManager stats;
    private JPanel controlPanel;
    private long simulationBeginTime = -1;

    private int timerDelay = 0;
    private int timerPeriod = 1000;
    private Timer timer;

    {

        stats = new StatisticsManager();
        habitat = new Habitat(stats, "res/HabitatBackground.png");
        habitat.addGeneratingType(new GoldFish(100,100));
        habitat.addGeneratingType(new Guppie(50,50));
        controlPanel = new JPanel();

    }


    public JPanel getControlPanel(){
        return controlPanel;
    }
    public Habitat getHabitat(){
        return habitat;
    }
    public StatisticsManager getStatisticsManager(){
        return stats;
    }

    public long getSimulationBeginTime() {
        return simulationBeginTime;
    }

    public void startSimulation(long simulationBeginTime) {
        habitat.revalidate();
        this.simulationBeginTime = simulationBeginTime;
        habitat.startSimulation();
        stats.clear();
        createTimer();
    }

    public void stopSimulation(){
        timer.cancel();
        this.simulationBeginTime = -1;
        habitat.stopSimulation();
        //this.getStatisticsManager().clear();
    }

    public void createTimer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run(){
                habitat.update(new Date().getTime()-simulationBeginTime);
            }
        }, timerDelay, timerPeriod);
        this.timer = timer;
    }

}
