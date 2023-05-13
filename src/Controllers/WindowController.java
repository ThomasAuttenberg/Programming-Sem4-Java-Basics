package Controllers;


import Models.BaseAI;
import Models.ConsoleModel;
import Models.Entities.Entity;
import Models.Entities.GoldFish;
import Models.Entities.Guppie;
import Models.WindowModel;
import Views.Console;
import Views.ListObjectsWindow;
import Views.OnStopDialogWindow;
import Views.WindowView;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static java.awt.event.KeyEvent.*;

public class WindowController {
    String DEFAULT_HABITAT_SAVING_PATH = "res/habitat.txt";
    String CONFIGURATION_PATH = "res/configuration.txt";
    private enum settingsKeys{
        guppiesGenerationTime,
        goldfishesGenerationTime,
        guppiesLifeTime,
        goldfishesLifeTime,
        guppiesFrequency,
        goldfishesFrequency,
        doesStatLabelShows,
        guppiesThreadPriorityIndex,
        goldfishesThreadPriorityIndex,
        doesStoppingDialogWindowShows
    }


    // Объявляем константы клавиш:
   final int simStopKey = VK_E;
   final int simStartKey = VK_B;
   final int showStatKey = VK_T;

   private boolean loadedState = false;
   private  WindowModel model = null;
   private  WindowView view = null;
   private WindowController controller = this;


   public WindowController(WindowModel model, WindowView view){

       this.model = model;
       this.view = view;

       loadConfiguration(CONFIGURATION_PATH);
       revalidateHabitatState(); //Using UI fields to configure habitat generatingTypes settings



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


       // Обработка взаимодействия с GUI


       // Верхнее меню:
            //Main:
       view.startItem.addActionListener(startBtnListener);
       view.stopItem.addActionListener(stopBtnListener);
       view.consoleItem.addActionListener(consoleBtnListener);
       view.saveSettingsItem.addActionListener(saveConfigureBtnListener);
       view.saveHabitatItem.addActionListener(saveHabitatBtnListener);
       view.loadHabitatItem.addActionListener(loadHabitatBtnListner);

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

       view.goldenFishGenerationTime.addPropertyChangeListener(goldenTimeFieldFocusListener);
       view.guppieGenerationTime.addPropertyChangeListener(guppieTimeFieldFocusListener);

       view.goldenFishChosenFrequency.addItemListener(freqComboBoxIListener);
       view.guppieChosenFrequency.addItemListener(freqComboBoxIListener);

       view.goldenFishLifeTime.addKeyListener(OnEnterReleased);
       view.guppieLifeTime.addKeyListener(OnEnterReleased);

       view.goldenFishLifeTime.addPropertyChangeListener(goldenLifeTimePropertyListener);
       view.guppieLifeTime.addPropertyChangeListener(guppieLifeTimeFieldPropertyListener);

       view.guppieAIPriority.addItemListener(priorityComboBoxIListener);
       view.goldenAIPriority.addItemListener(priorityComboBoxIListener);

       view.listObjects.addActionListener(aliveObjectsBtnListener);


   }

    public void startActionHandler(){

        if(model.getSimulationBeginTime() == -1) { // проверяем, что симуляция остановлена (флаг - время начала симуляции установлено в -1)
            view.stopItem.setEnabled(true);
            view.startItem.setEnabled(false);
            view.startButton.setEnabled(false);
            view.stopButton.setEnabled(true);

            if(loadedState)
                model.startSimulation(new Date().getTime() - model.getHabitat().getHabitatTime());
            else
                model.startSimulation(new Date().getTime()); // запускаем симуляцию, передавая на вход текущее время.

            view.initLabel();
        }
    }

    public boolean stopActionHandler(){
        if(model.getSimulationBeginTime() != -1){ //если симуляция запущена (время начала симуляции не -1)
            if(view.dialogCheckBox.isSelected()){
                model.pauseSimulation();
                OnStopDialogWindow dialog = new OnStopDialogWindow(model);
                dialog.show();
                if(dialog.getUserResponse() == OnStopDialogWindow.SimulationStatus.continues){
                    model.unpauseSimulation();
                    return false;
                }
            }
            view.stopItem.setEnabled(false);
            view.startItem.setEnabled(true);
            view.startButton.setEnabled(true);
            view.stopButton.setEnabled(false);
            //if(view.radioSimTimeShow.isSelected()) view.getStatisticsLabel().toogleFullStatistics(model.getStatisticsManager());
            //Включаем полную статистику, передавая StatisticsManager, он хранит данные о кол-ве созданных объектов
            model.stopSimulation();
            loadedState = false;
            view.getStatisticsLabel().setVisible(false);
            // останавливаем симуляцию
        }
        return true;
    }


    public void loadConfiguration(String path){

        HashMap<settingsKeys, Integer> settingsMap;
        ObjectInputStream settingsMapStream;

        try {

            FileInputStream fileInputStream = new FileInputStream(path);
            settingsMapStream = new ObjectInputStream(fileInputStream);
            settingsMap = (HashMap<settingsKeys, Integer>)settingsMapStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(settingsMap == null) return;

        //Gets frequencies

        int loadedFrequency;
        int frequencyInListIndex;

        loadedFrequency = settingsMap.get(settingsKeys.goldfishesFrequency);

        //Checks if loaded frequency is already exists in comboBox. If it's not, creates the new comboBox Item
        frequencyInListIndex = -1;
        for(int i = 0; i<view.goldenFishChosenFrequency.getItemCount(); i++){
            String chosenFrequencyAtIndex = (String) view.goldenFishChosenFrequency.getItemAt(i);
            if(chosenFrequencyAtIndex.substring(0,chosenFrequencyAtIndex.length()-1).equals(String.valueOf(loadedFrequency))){
                 frequencyInListIndex = i;
                break;
            }
        }
        if(frequencyInListIndex != -1){
            view.goldenFishChosenFrequency.setSelectedIndex(frequencyInListIndex);
        }else{
            view.goldenFishChosenFrequency.addItem(loadedFrequency+"%");
            view.goldenFishChosenFrequency.setSelectedIndex(view.GENERATION_CHANCES_COMBOBOX_NUMBER);
        }

        loadedFrequency = settingsMap.get(settingsKeys.guppiesFrequency);

        //Checks if loaded frequency is already exists in comboBox. If it's not, creates the new comboBox Item
        frequencyInListIndex = -1;
        for(int i = 0; i<view.guppieChosenFrequency.getItemCount(); i++){
            String chosenFrequencyAtIndex = (String) view.guppieChosenFrequency.getItemAt(i);
            if(chosenFrequencyAtIndex.substring(0,chosenFrequencyAtIndex.length()-1).equals(String.valueOf(loadedFrequency))){
                frequencyInListIndex = i;
                break;
            }
        }
        if(frequencyInListIndex != -1){
            view.guppieChosenFrequency.setSelectedIndex(frequencyInListIndex);
        }else{
            view.guppieChosenFrequency.addItem(loadedFrequency+"%");
            view.guppieChosenFrequency.setSelectedIndex(view.GENERATION_CHANCES_COMBOBOX_NUMBER);
        }

        //Gets generationTime;
        view.goldenFishGenerationTime.setValue(settingsMap.get(settingsKeys.goldfishesGenerationTime));
        view.guppieGenerationTime.setValue(settingsMap.get(settingsKeys.guppiesGenerationTime));

        //Gets lifeTime:
        view.goldenFishLifeTime.setValue(settingsMap.get(settingsKeys.goldfishesLifeTime));
        view.guppieLifeTime.setValue(settingsMap.get(settingsKeys.guppiesLifeTime));

        //Gets AI Thread Priorities
        view.goldenAIPriority.setSelectedIndex(settingsMap.get(settingsKeys.goldfishesThreadPriorityIndex));
        view.guppieAIPriority.setSelectedIndex(settingsMap.get(settingsKeys.guppiesThreadPriorityIndex));

        //Gets show time label state
        int state = settingsMap.get(settingsKeys.doesStatLabelShows);
        if(state == 0) view.radioSimTimeHide.setSelected(true); else view.radioSimTimeShow.setSelected(true);

        //Gets stopping dialog window state
        view.dialogCheckBox.setSelected(settingsMap.get(settingsKeys.doesStoppingDialogWindowShows) == 0 ? false : true);

    }

    public void unloadConfiguration(String path){


        HashMap<settingsKeys, Integer> settingsMap = new HashMap<>();
        settingsMap.put(settingsKeys.doesStoppingDialogWindowShows, view.dialogCheckBox.isSelected() ? 1 : 0);
        settingsMap.put(settingsKeys.doesStatLabelShows, view.radioSimTimeShow.isSelected() ? 1 : 0);
        settingsMap.put(settingsKeys.guppiesLifeTime, (Integer) view.guppieLifeTime.getValue());
        settingsMap.put(settingsKeys.goldfishesLifeTime, (Integer) view.goldenFishLifeTime.getValue());
        settingsMap.put(settingsKeys.goldfishesGenerationTime, (Integer) view.goldenFishGenerationTime.getValue());
        settingsMap.put(settingsKeys.guppiesGenerationTime, (Integer) view.guppieGenerationTime.getValue());
        settingsMap.put(settingsKeys.goldfishesFrequency, Integer.parseInt(((String) view.goldenFishChosenFrequency.getSelectedItem()).substring(0,((String) view.goldenFishChosenFrequency.getSelectedItem()).length()-1)));
        settingsMap.put(settingsKeys.guppiesFrequency, Integer.parseInt(((String) view.guppieChosenFrequency.getSelectedItem()).substring(0,((String) view.guppieChosenFrequency.getSelectedItem()).length()-1)));
        settingsMap.put(settingsKeys.goldfishesThreadPriorityIndex, view.goldenAIPriority.getSelectedIndex());
        settingsMap.put(settingsKeys.guppiesThreadPriorityIndex, view.guppieAIPriority.getSelectedIndex());

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream settingsMapOutputStream = new ObjectOutputStream(fileOutputStream);
            settingsMapOutputStream.writeObject(settingsMap);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadHabitat(){
        boolean isStoppingAllowed = stopActionHandler(); //simulation stopping attempt

        if(isStoppingAllowed) { // If simulation is already stopped or user gives his permit

            ObjectInputStream objectInputStream;
            JFileChooser fileChooser = new JFileChooser("res/States");
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Habitat files", "habitat");
            fileChooser.setFileFilter(filter);
            fileChooser.showOpenDialog(view);
            File file = fileChooser.getSelectedFile();
            if(file == null) return;

            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                model.loadState(objectInputStream); //loading state to model
                objectInputStream.close();
                fileInputStream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            revalidateHabitatState(); //Using UI fields to configure habitat generatingTypes settings
            loadedState = true;
        }
    }

    private void saveHabitat(){
        model.pauseSimulation();
        ObjectOutputStream objectOutputStream;

        JFileChooser fileChooser = new JFileChooser("res/States");
        fileChooser.showSaveDialog(view);
        fileChooser.setDialogTitle("Choose directory to save habitat");
        File file = fileChooser.getSelectedFile();

        if(file == null){
            model.unpauseSimulation();
            return;
        }

        //Checking file extension and edit if necessary:

        String[] fileName = ((file.getName()).split("[.]"));
        if(fileName.length == 0 || !(fileName[fileName.length-1].equals("habitat"))){
            file = new File(file.getPath()+".habitat");
        }
        model.unpauseSimulation();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            model.getHabitat().saveState(objectOutputStream);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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

    private void revalidateHabitatState(){
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

        // Первоначальная настройка времени жизни:
        for(Entity ent : model.getHabitat().getGeneratingTypes()) {
            if (ent.getClass() == GoldFish.class) {
                int lifeTimeValue = (int)view.goldenFishLifeTime.getValue();
                ent.setLifeTime(lifeTimeValue);
            }
            if (ent.getClass() == Guppie.class) {
                int lifeTimeValue = (int)view.guppieLifeTime.getValue();
                ent.setLifeTime(lifeTimeValue);
            }
        }

        // Первоначальная настройка шансов генерации:

        String chosenFrequency = (String) view.guppieChosenFrequency.getSelectedItem();
        for (Entity ent : model.getHabitat().getGeneratingTypes()) {
            if (ent.getClass() == Guppie.class) {
                double frequency = Integer.parseInt(chosenFrequency.substring(0, chosenFrequency.length() - 1));
                ent.setFrequency(frequency / 100);
                //System.out.println(frequency / 100);
            }
        }
        chosenFrequency = (String) view.goldenFishChosenFrequency.getSelectedItem();
        for (Entity ent : model.getHabitat().getGeneratingTypes()) {
            if (ent.getClass() == GoldFish.class) {
                double frequency = Integer.parseInt(chosenFrequency.substring(0, chosenFrequency.length() - 1));
                ent.setFrequency(frequency / 100);
                //System.out.println(frequency / 100);
            }
        }

        // Первоначальная настройка приоритетов потоков:

        String chosenItem;
        int chosenPriority;
        BaseAI AI;

        chosenItem = (String) view.goldenAIPriority.getSelectedItem();
        AI = model.getHabitat().getGoldFishAI();
        chosenPriority = Integer.parseInt(chosenItem);
        model.getHabitat().setGoldFishPriority(chosenPriority);


        chosenItem = (String) view.guppieAIPriority.getSelectedItem();
        AI = model.getHabitat().getGuppieAI();
        chosenPriority = Integer.parseInt(chosenItem);
        model.getHabitat().setGuppiePriority(chosenPriority);
    }




    /* Реализуем слушатели */

    PropertyChangeListener goldenTimeFieldFocusListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            for(Entity ent : model.getHabitat().getGeneratingTypes()){
                if(ent.getClass() == GoldFish.class){
                    ent.setGenerationTime((int)view.goldenFishGenerationTime.getValue());
                }
            }
        }
    };
    PropertyChangeListener guppieTimeFieldFocusListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            for(Entity ent : model.getHabitat().getGeneratingTypes()){
                if(ent.getClass() == Guppie.class){
                    ent.setGenerationTime((int)view.guppieGenerationTime.getValue());
                }
            }
        }
    };
    PropertyChangeListener guppieLifeTimeFieldPropertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            for(Entity ent : model.getHabitat().getGeneratingTypes()){
                if(ent.getClass() == Guppie.class){
                    ent.setLifeTime((int)view.guppieLifeTime.getValue());
                }
            }
        }
    };
    PropertyChangeListener goldenLifeTimePropertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            for(Entity ent : model.getHabitat().getGeneratingTypes()){
                if(ent.getClass() == GoldFish.class){
                    ent.setLifeTime((int)view.goldenFishLifeTime.getValue());
                }
            }
        }
    };
    ActionListener saveConfigureBtnListener = (e) -> { unloadConfiguration(CONFIGURATION_PATH); };
    ActionListener consoleBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Console console = new Console();
            ConsoleModel consoleModel = new ConsoleModel(model, controller, view);
            ConsoleController consoleController = new ConsoleController(console, consoleModel);
        }
    };
    ItemListener priorityComboBoxIListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox source = (JComboBox) e.getSource();
                String chosenItem;
                int chosenPriority;
                BaseAI AI;
                if (source == view.goldenAIPriority) {

                    chosenItem = (String) view.goldenAIPriority.getSelectedItem();
                    AI = model.getHabitat().getGoldFishAI();
                    chosenPriority = Integer.parseInt(chosenItem);
                    model.getHabitat().setGoldFishPriority(chosenPriority);

                }
                if (source == view.guppieAIPriority) {

                    chosenItem = (String) view.guppieAIPriority.getSelectedItem();
                    AI = model.getHabitat().getGuppieAI();
                    chosenPriority = Integer.parseInt(chosenItem);
                    model.getHabitat().setGuppiePriority(chosenPriority);
                }
            }
        }
    };
    ItemListener freqComboBoxIListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox source = (JComboBox) e.getSource();
            if (source == view.guppieChosenFrequency) {
                String chosenFrequency = (String) source.getSelectedItem();
                for (Entity ent : model.getHabitat().getGeneratingTypes()) {
                    if (ent.getClass() == Guppie.class) {
                        double frequency = Integer.parseInt(chosenFrequency.substring(0, chosenFrequency.length() - 1));
                        ent.setFrequency(frequency / 100);
                        //System.out.println(frequency / 100);
                    }
                }
            }
            if (source == view.goldenFishChosenFrequency) {
                String chosenFrequency = (String) source.getSelectedItem();
                for (Entity ent : model.getHabitat().getGeneratingTypes()) {
                    if (ent.getClass() == GoldFish.class) {
                        double frequency = Integer.parseInt(chosenFrequency.substring(0, chosenFrequency.length() - 1));
                        ent.setFrequency(frequency / 100);
                        //System.out.println(frequency / 100);
                    }
                }
            }
        }
    };
    ActionListener aliveObjectsBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ListObjectsWindow listObjectsWindow = new ListObjectsWindow(model);
            //listObjectsWindow.LoadInfo(); // ПЕРЕДАТЬ ПАРАМЕТРЫ
            listObjectsWindow.Show();
        }
    };
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
    ActionListener loadHabitatBtnListner = (e) -> {
        loadHabitat();
    };
    ActionListener saveHabitatBtnListener = (e) -> {
        saveHabitat();
    };


}
