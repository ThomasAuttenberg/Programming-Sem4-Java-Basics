package Controllers;


import Models.Entities.Entity;
import Models.Entities.GoldFish;
import Models.Entities.Guppie;
import Models.WindowModel;
import Views.WindowView;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Date;
import java.util.EventListener;

import static java.awt.event.KeyEvent.*;

public class WindowController {

    // Объявляем константы клавиш:
   final int simStopKey = VK_E;
   final int simStartKey = VK_B;
   final int showStatKey = VK_T;

   private final WindowModel model;
   private final WindowView view;


   public WindowController(WindowModel model, WindowView view){

       this.model = model;
       this.view = view;

       // Первоначальная установка шансов генерации.
       for(Entity ent : model.getHabitat().getGeneratingTypes()){
           if(ent.getClass() == GoldFish.class){
               String chosenFrequency = (String)view.goldenFishChosenFrequency.getSelectedItem();
               double frequency = Integer.parseInt(chosenFrequency.substring(0,chosenFrequency.length()-1));
               ent.setFrequency(frequency/100);
               System.out.println(frequency/100);
           }
           if(ent.getClass() == Guppie.class){
               String chosenFrequency = (String)view.guppieChosenFrequency.getSelectedItem();
               double frequency = Integer.parseInt(chosenFrequency.substring(0,chosenFrequency.length()-1));
               ent.setFrequency(frequency/100);
               System.out.println(frequency/100);
           }
       }

       // Первоначальная настройка промежутков генерации:
       for(Entity ent : model.getHabitat().getGeneratingTypes()) {
           if (ent.getClass() == GoldFish.class) {
               int generationTime_ = (int)view.goldenFishGenerationTime.getValue();
               ent.setGenerationTime(generationTime_);
           }
           if (ent.getClass() == Guppie.class) {
               int generationTime_ = (int)view.guppieGenerationTime.getValue();
               ent.setGenerationTime(generationTime_);
           }
       }


       // Добавляем к окну программы (view) обработчик нажатия клавиш:
       view.addKeyListener(
               new KeyAdapter() {
                   @Override
                   public void keyPressed(KeyEvent e) {
                       switch (e.getKeyCode()) {
                           case simStartKey: //если нажата кнопочка simStartKey
                               if(model.getSimulationBeginTime() == -1) { // проверяем, что симуляция остановлена (флаг - время начала симуляции установлено в -1)
                                   model.startSimulation(new Date().getTime()); // запускаем симуляцию, передавая на вход текущее время.
                                   // Это время - время начала симуляции. Будет использоваться для подсчета времени симуляции там, где необходимо
                                   view.initLabel(); // создаем (пересоздаем) надпись "Simulation Time: "
                                   if (view.getStatisticsLabel().isVisible()) view.toogleStatisticsLabel(); //Если надпись на экране уже есть, выключаем ее.
                               }
                               break;
                           case simStopKey:
                               if(model.getSimulationBeginTime() != -1){ //если симуляция запущена (время начала симуляции не -1)
                                   view.getStatisticsLabel().toogleFullStatistics(model.getStatisticsManager());
                                   //Включаем полную статистику, передавая StatisticsManager, он хранит данные о кол-ве созданных объектов
                                   model.stopSimulation();
                                   // останавливаем симуляцию
                               }
                               break;
                           case showStatKey:
                                   view.toogleStatisticsLabel(); //включаем - выключаем надпись "Simulation Time:"
                               break;
                       }
                   }

               }
       );


       // Слушаем события на ComboBox выбора вероятностей
       view.goldenFishChosenFrequency.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String chosenFrequency = (String)view.goldenFishChosenFrequency.getSelectedItem();
               double frequency = Integer.parseInt(chosenFrequency.substring(0,chosenFrequency.length()-1));
               for(Entity ent : model.getHabitat().getGeneratingTypes()){
                   if(ent.getClass() == GoldFish.class){
                       ent.setFrequency(frequency/100);
                   }
               }
           }
       });

       view.guppieChosenFrequency.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String chosenFrequency = (String)view.guppieChosenFrequency.getSelectedItem();
               double frequency = Integer.parseInt(chosenFrequency.substring(0,chosenFrequency.length()-1));
               for(Entity ent : model.getHabitat().getGeneratingTypes()){
                   if(ent.getClass() == Guppie.class){
                       ent.setFrequency(frequency/100);
                   }
               }
           }
       });

       // Слушаем события на TextField времени генерации:

       KeyAdapter OnEnterReleased = new KeyAdapter() {
           @Override
           public void keyReleased(KeyEvent e) {
               if(e.getKeyCode() == KeyEvent.VK_ENTER){
                   view.requestFocus();
               }
           }
       };

       view.goldenFishGenerationTime.addKeyListener(OnEnterReleased);
       view.guppieGenerationTime.addKeyListener(OnEnterReleased);

       view.goldenFishGenerationTime.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                for(Entity ent : model.getHabitat().getGeneratingTypes()){
                    if(ent.getClass() == GoldFish.class){
                        ent.setGenerationTime((int)view.goldenFishGenerationTime.getValue());
                    }
                }
            }
        });

       view.guppieGenerationTime.addFocusListener(new FocusListener() {
           @Override
           public void focusGained(FocusEvent e) {

           }

           @Override
           public void focusLost(FocusEvent e) {
               for(Entity ent : model.getHabitat().getGeneratingTypes()){
                   if(ent.getClass() == Guppie.class){
                       ent.setGenerationTime((int)view.guppieGenerationTime.getValue());
                   }
               }
           }
       });

   }



}
