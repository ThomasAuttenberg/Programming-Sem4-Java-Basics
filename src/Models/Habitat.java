package Models;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;


public class Habitat extends JPanel {
    private ArrayList<Entity> enteties;
    private StatisticsModel stats;
    private HashSet<Entity> generatingTypes;
    //private HashMap<StatisticKeys, Integer> statistics;
    private long lastUpdateTime = -1;

    {
        stats = new StatisticsModel();
        generatingTypes = new HashSet<Entity>();
        enteties = new ArrayList<Entity>();
    }

    public ArrayList<Entity> getEnteties(){
        return enteties;
    }

    public void update(long simulationTime){

        if(lastUpdateTime == -1) lastUpdateTime = simulationTime;
        long lastUpdateDifference = lastUpdateTime - simulationTime;

        for(Entity type : generatingTypes){
            type.getFrequency();
            if(type.getGenerationTime() <= lastUpdateDifference){
                if(type.getFrequency() >= 1.0){
                    Entity newEntity = type.clone();
                }else{
                    double rand = Math.random();
                    if(type.getFrequency() <= rand){
                        Entity newEntity = type.clone();
                        this.add(newEntity);
                        stats.instancesCounter.incrementInstance(type.getClass());
                    }
                }

            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
