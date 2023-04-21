package Controllers;


import Models.Entities.Entity;
import Models.Entities.GoldFish;
import Models.Entities.Guppie;
import Models.WindowModel;
import Views.OnStopDialogWindow;
import Views.WindowView;

import java.awt.event.*;
import java.util.Date;

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



        /*

                    Секция прослушивания событий


         */


       // Обработка клавиш

       view.addKeyListener(
               new KeyAdapter() {
                   @Override
                   public void keyPressed(KeyEvent e) {
                       switch (e.getKeyCode()) {
                           case simStartKey: //если нажата кнопочка simStartKey
                               startActionHandler();
                               break;
                           case simStopKey:
                               stopActionHandler();
                               break;
                           case showStatKey:
                               if(view.radioSimTimeShow.isSelected())
                                   simTimeDisableHandler(); //включаем - выключаем надпись "Simulation Time:"
                               else
                                   simTimeEnableHandler();
                               break;
                       }
                   }

               }
       );



       // Верхнее меню:

       view.startItem.addActionListener(startBtnListener);
       view.stopItem.addActionListener(stopBtnListener);
       view.ItemInfoActive.addActionListener(infoEnableBtnListener);
       view.ItemInfoDeactive.addActionListener(infoDisableBtnListener);
       view.ItemActiveDialog.addActionListener(dialogSwitchListener);


       // Боковое меню:

       view.startButton.addActionListener(startBtnListener);
       view.stopButton.addActionListener(stopBtnListener);

       view.radioSimTimeShow.addActionListener(infoEnableBtnListener);
       view.radioSimTimeHide.addActionListener(infoDisableBtnListener);


       view.goldenFishGenerationTime.addKeyListener(OnEnterReleased);
       view.guppieGenerationTime.addKeyListener(OnEnterReleased);
       view.goldenFishGenerationTime.addFocusListener(goldenTimeFieldFocusListener);
       view.guppieGenerationTime.addFocusListener(guppieTimeFieldFocusListener);

   }

   ActionListener dialogSwitchListener = new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
           if(view.dialogCheckBox.isSelected()){
               view.dialogCheckBox.setSelected(false);
           }else{
               view.dialogCheckBox.setSelected(true);
           }
       }
   };

   ActionListener infoEnableBtnListener = new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) { simTimeEnableHandler(); }
   };

   ActionListener infoDisableBtnListener = new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) { simTimeDisableHandler(); }
   };

    KeyAdapter OnEnterReleased = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                view.requestFocus();
            }
        }
    };
   FocusListener goldenTimeFieldFocusListener = new FocusListener() {
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
   };

    FocusListener guppieTimeFieldFocusListener = new FocusListener() {
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
    };

   ActionListener startBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            startActionHandler();
        }
    };

   ActionListener stopBtnListener = new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
           stopActionHandler();
       }
   };

    private void startActionHandler(){

        if(model.getSimulationBeginTime() == -1) { // проверяем, что симуляция остановлена (флаг - время начала симуляции установлено в -1)
            view.stopItem.setEnabled(true);
            view.startItem.setEnabled(false);
            view.startButton.setEnabled(false);
            view.stopButton.setEnabled(true);
            model.startSimulation(new Date().getTime()); // запускаем симуляцию, передавая на вход текущее время.
            view.initLabel(); // создаем (пересоздаем) надпись "Simulation Time: "
        }
    }

    private void stopActionHandler(){
        if(model.getSimulationBeginTime() != -1){ //если симуляция запущена (время начала симуляции не -1)
            if(view.dialogCheckBox.isSelected()){
                model.pauseSimulation();
                OnStopDialogWindow dialog = new OnStopDialogWindow(model);
                dialog.show();
                if(dialog.getUserResponse() == OnStopDialogWindow.SimulationStatus.continues){
                    model.unpauseSimulation();
                    return;
                }
            }
            view.stopItem.setEnabled(false);
            view.startItem.setEnabled(true);
            view.startButton.setEnabled(true);
            view.stopButton.setEnabled(false);
            //if(view.radioSimTimeShow.isSelected()) view.getStatisticsLabel().toogleFullStatistics(model.getStatisticsManager());
            //Включаем полную статистику, передавая StatisticsManager, он хранит данные о кол-ве созданных объектов
            model.stopSimulation();
            // останавливаем симуляцию
        }
    }

    private void simTimeEnableHandler() {
            view.toogleStatisticsLabel();
            view.radioSimTimeShow.setSelected(true);
            view.radioSimTimeShow.setEnabled(false);
            view.radioSimTimeHide.setEnabled(true);
            view.ItemInfoDeactive.setEnabled(true);
            view.ItemInfoActive.setEnabled(false);
    }

    private void simTimeDisableHandler() {
            view.toogleStatisticsLabel();
            view.radioSimTimeHide.setSelected(true);
            view.radioSimTimeHide.setEnabled(false);
            view.radioSimTimeShow.setEnabled(true);
            view.ItemInfoDeactive.setEnabled(false);
            view.ItemInfoActive.setEnabled(true);
    }




}
