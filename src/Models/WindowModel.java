package Models;

import Models.Entities.GoldFish;
import Models.Entities.Guppie;


import javax.swing.*;
import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;

public class WindowModel {
    private Habitat habitat; // Храним Habitat
    StatisticsManager stats; // Храним stats
    private JPanel controlPanel; // Храним controlPanel - правую панель окна
    private long simulationBeginTime = -1; // Время начала симуляции

    private int timerDelay = 0; // Задаем начальные условия - задержка таймера и период обновления
    private int timerPeriod = 100;
    private Timer timer;
    private long lastTimerTick;

    {

        // Создаем менеджер статистики, Habitat и controlPanel
        stats = new StatisticsManager();
        habitat = new Habitat(stats, "res/HabitatBackground.png");
        habitat.addGeneratingType(new GoldFish(100,100)); //добавляем типы для генерации
        habitat.addGeneratingType(new Guppie(50,50));
        controlPanel = new JPanel();

    }


    // Простейшие геттеры
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

    public void startSimulation(long simulationBeginTime) { //Старт симуляции:
        habitat.revalidate(); //ревалидейт - служебный метод Swing. Необходимо вызывать каждый раз, когда удаляются объекты.
        // скорее всего, мы запустим симуляцию после предыдущей симуляции - когда мы уже удалили кучу рыб
        this.simulationBeginTime = simulationBeginTime; // выставляем время начала симуляции
        habitat.startSimulation(); // запускаем симуляцию у Habitat
        stats.clear(); // очищаем менеджер статистики - внутренний метод
        createTimer(); // создаем таймер. см. ниже
    }

    public void stopSimulation(){ //Остановка симуляции: (не пауза)
        timer.cancel(); // прекращаем таймер
        timer.purge();
        this.simulationBeginTime = -1; // время начала симуляции в минус один.
        habitat.stopSimulation(); // останавливаем симуляцию Habitat
        //this.getStatisticsManager().clear();
    }

    public void pauseSimulation(){
        lastTimerTick = new Date().getTime();
        timer.cancel();
    }
    public void unpauseSimulation(){
        simulationBeginTime += new Date().getTime() - lastTimerTick;
        createTimer();
    }

    private void createTimer(){ // Запуск таймера
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run(){
                habitat.update(new Date().getTime()-simulationBeginTime);
            }
        }, timerDelay, timerPeriod);
    }


}
