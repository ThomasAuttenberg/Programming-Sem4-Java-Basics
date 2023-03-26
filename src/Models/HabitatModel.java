package Models;

import javax.swing.*;
import java.util.Dictionary;
import java.util.HashMap;

import static Models.HabitatModel.StatisticKeys.*;


public class HabitatModel {
    private JComponent[] components;
    private HashMap<StatisticKeys, Integer> statistics;

    public enum StatisticKeys{
        SIMULATION_TIME,
        GUPPIE_NUM,
        GOLDGFISH_NUM;
    }

    public double getStatistics(StatisticKeys key) {
        return statistics.get(key);
    }

    public JComponent[] getComponents(){
        return components;
    }



}
