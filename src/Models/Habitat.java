package Models;

import javax.swing.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;


public class Habitat extends JPanel {
    private ArrayList<Entity> enteties;
    private HashSet<Entity> generatingTypes;
    private HashMap<StatisticKeys, Integer> statistics;
    private long lastUpdateTime = -1;
    public enum StatisticKeys{
        SIMULATION_TIME,
        GUPPIE_NUM,
        GOLDGFISH_NUM;
    }

    {
        generatingTypes = new HashSet<Entity>();
        enteties = new ArrayList<Entity>();
    }

    public int getStatistics(StatisticKeys key) {
        return statistics.get(key);
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
                    enteties.add(new type.getClass());
                }else{

                }
                double rand = Math.random();

            }
        }

    }


}
