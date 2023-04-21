package Views;

import Models.Habitat;
import Models.WindowModel;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;


/*

    WindowView наследуется от JFrame - по сути, этот класс - наше окно. Аналог Window прошлой версии лабораторной работы
    В панели управления используется менеджер расположения Grid (сетка)
    Каждый компонент помещается в контейнер, и уже этот контейнер помещается в сетку.
    В некоторых случаях это позволяет располагать несколько элементов в линию, а в остальных - для соблюдения семантики
    (подобия)

 */
public class WindowView extends JFrame {
    private Habitat habitat; // На самом деле избыточное поле. Можно обойтись хранением модели.
    private JPanel controlPanel; // В целом возможно тоже избыточное




    Color fieldsColor = new Color(134, 134, 134);
    Color bgColor = new Color(54, 54, 54);
    Color textColor = new Color(178, 178, 178);

    // Публичные поля, хранящие компоненты правой панели
    public JButton startButton;
    public JButton stopButton;
    public JRadioButton radioSimTimeShow;
    public JRadioButton radioSimTimeHide;
    public JCheckBox dialogCheckBox;
    public JFormattedTextField guppieGenerationTime;
    public JFormattedTextField goldenFishGenerationTime;
    public JComboBox goldenFishChosenFrequency;
    public JComboBox guppieChosenFrequency;
    private StatisticsLabel label;

    public JMenuBar menuBar;                   // главное меня  с дублированием пользовательского интерфейса
    // вкладка и вложеные вкладки главного
    public JMenu main;                        // главное содержит в себе запуск и остановку
    public JMenuItem startItem;                // кнопка главного меню старт симуляции
    public JMenuItem stopItem ;                // кнопка главного меню остановка симуляции
    // вкладка и вложенные вкладки статитстики
    public JMenu info;
    public JMenuItem ItemInfoActive;
    public JMenuItem ItemInfoDeactive;
    // вкладка и вложенные вкладки управления диологовым окном
    public JMenu control;
    public JMenuItem ItemActiveDialog;

    // Ширина, высота окна
    int width;
    int height;
    //
    // Храним модель
    WindowModel model;
    //
    {
        setBackground(Color.BLUE); // Можно убрать
        // Конфигурация JFrame
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
    }


    public WindowView(WindowModel model, int width, int height){


        // Сохраняем информацию о ширине и высоте окна
        this.width = width;
        this.height = height;

        // Получаем копию объекта WindowModel
        this.model = model;

        // Получаем объекты классов Habitat и JPanel из объекта WindowModel;
        this.habitat = model.getHabitat();
        this.controlPanel = model.getControlPanel();


        // Инициализируем все компоненты объекта JPanel
        this.initPanelElements();

        this.setFocusable(true);


        // Добавляем Habitat и JPanel на экран
        this.add(habitat);
        this.add(controlPanel, BorderLayout.EAST);
        MenuBar();

        // Устанавливаем размер окна приложения и включаем его.
        this.setSize(width,height);
        this.setVisible(true);
    }

    public void MenuBar(){
        // класс для создания окна

            menuBar = new JMenuBar();
            // Создание вкладок
            // вкладка главное
            main = new JMenu("Main");               // главное содержит в себе запуск и остановку
            startItem = new JMenuItem("Start");
            stopItem = new JMenuItem("Stop");
            startItem.setEnabled(true);
            stopItem.setEnabled(false);
            main.add(startItem);
            main.addSeparator();
            main.add(stopItem);
            // вкладка Info
            info = new JMenu("Info");               // статистика
            ItemInfoActive = new JMenuItem("Active");
            ItemInfoDeactive = new JMenuItem("Deactive");
            ItemInfoDeactive.setEnabled(false);
            info.add(ItemInfoActive);
            info.addSeparator();
            info.add(ItemInfoDeactive);
            // вкладка Control
            control = new JMenu("Window Dialog");
            ItemActiveDialog = new JMenuItem("Active/Deactive Dialog");
            ItemActiveDialog.setSelected(false);
            control.add(ItemActiveDialog);
            // Добавление вкладок в меню
            menuBar.add(main);
            menuBar.add(info);
            menuBar.add(control);
            this.setJMenuBar(menuBar);
    }



    public void initStartStopButtons() {

        // Инициализируем кнопки "Старт" и "Стоп" конструкторами по умолчанию
        this.startButton = new JButton("Start");
        this.stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        stopButton.setForeground(Color.WHITE);

        // Создаем, настраиваем и устанавливаем кнопки Start/Stop
        Color colorForStartButton = new Color(0,255,76);
        startButton.setBackground(colorForStartButton);
       // startButton.setContentAreaFilled(false); // TODO возможно меняет цвет кнопки при нажатии
        startButton.setBorder(null);
        startButton.setOpaque(true);
        startButton.setFocusable(false);

        Color colorForStopButton = new Color(125, 0, 125);
        stopButton.setBackground(colorForStopButton);
        //stopButton.setContentAreaFilled(false);
        stopButton.setOpaque(true);
        stopButton.setBorder(null);
        stopButton.setFocusable(false);

        // Добавляем кнопки Старт и стоп в общий контейнер, чтобы разместить их на одном уровне
        JPanel containerForStartStopButtons = new JPanel(new GridLayout(1,2));
        containerForStartStopButtons.setBackground(bgColor);
        containerForStartStopButtons.add(startButton);
        containerForStartStopButtons.add(stopButton);

        // Добавление на панель
        controlPanel.add(containerForStartStopButtons);
    }
    public void initRadioButtons() {
        // Создание радиокнопок для показа и сокрытия времени симуляции
        radioSimTimeShow = new JRadioButton("Show time");
        radioSimTimeShow.setBorderPainted(true);
        radioSimTimeShow.setForeground(textColor);
        radioSimTimeShow.setContentAreaFilled(false);
        radioSimTimeShow.setFocusable(false);

        radioSimTimeHide = new JRadioButton("Hide time");
        radioSimTimeHide.setBorderPainted(true);
        radioSimTimeHide.setForeground(textColor);
        radioSimTimeHide.setContentAreaFilled(false);
        radioSimTimeHide.setFocusable(false);
        radioSimTimeHide.setSelected(true);

        // Объединение радиокнопок в группу, чтобы можно было выбрать только одну
        ButtonGroup group = new ButtonGroup();
        group.add(radioSimTimeHide);
        group.add(radioSimTimeShow);

        // Добавление кнопок в общий контейнер, чтобы они находились рядом
        JPanel radioContainer = new JPanel();
        radioContainer.setBackground(bgColor);
        radioContainer.add(radioSimTimeShow);
        radioContainer.add(radioSimTimeHide);

        // Добавление на панель
        controlPanel.add(radioContainer);
    }
    public void initShowInformation() {

        // Создаем чекбокс, разрешающий или запрещающий показ окна информации после окончания симуляции
        dialogCheckBox = new JCheckBox("Stopping dialog window");
        dialogCheckBox.setForeground(textColor);
        dialogCheckBox.setContentAreaFilled(false);
        dialogCheckBox.setFocusable(false);

        JPanel dialogCheckBoxContainer = new JPanel();
        dialogCheckBoxContainer.add(dialogCheckBox);
        dialogCheckBoxContainer.setBackground(bgColor);
        controlPanel.add(dialogCheckBoxContainer);
    }
    public void initGenerationTime() {

        // Форматтер для полей

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(1000);
        formatter.setMaximum(9999);

        // Подпись и текстовое поле для гуппи
        JLabel guppieGenerationLabel = new JLabel("Guppie generation time");
        guppieGenerationLabel.setForeground(textColor);
        guppieGenerationTime = new JFormattedTextField(formatter);
        guppieGenerationTime.setColumns(4);
        guppieGenerationTime.setValue(2000);
        guppieGenerationTime.setBackground(fieldsColor);
        guppieGenerationTime.setBorder(null);

        // Подпись и текстовое поле для голдфиш
        JLabel goldGenerationLabel = new JLabel("GoldFish generation time");
        goldGenerationLabel.setForeground(textColor);
        goldenFishGenerationTime = new JFormattedTextField(formatter);
        goldenFishGenerationTime.setColumns(4);
        goldenFishGenerationTime.setValue(4000);
        goldenFishGenerationTime.setBackground(fieldsColor);
        goldenFishGenerationTime.setBorder(null);

        // Выставление в ряд подписи и текстового поля гуппи
        JPanel guppieContainer = new JPanel();
        guppieContainer.add(guppieGenerationLabel);
        guppieContainer.add(guppieGenerationTime);

        // Выставление в ряд подписи и текстового поля голдфиш
        JPanel goldContainer = new JPanel();
        goldContainer.add(goldGenerationLabel);
        goldContainer.add(goldenFishGenerationTime);

        // Цветовое оформление
        goldContainer.setBackground(bgColor);
        guppieContainer.setBackground(bgColor);

        controlPanel.add(guppieContainer);
        controlPanel.add(goldContainer);
    }
    public void initChanceOfGeneration() {
        String items[] = {"0%","10%","20%","30%","40%","50%","60%","70%","80%","90%","100%"};

        // Настройка выпадающих списков с выбором процентного шанса появления каждого объекта
        JLabel guppieChanceLabel = new JLabel("Guppie chance of generation");
        guppieChanceLabel.setForeground(textColor);
        guppieChosenFrequency = new JComboBox(items);
        guppieChosenFrequency.setSelectedIndex(5);
        guppieChosenFrequency.setFocusable(false);

        JLabel goldChanceLabel = new JLabel("GoldFish chance of generation");
        goldChanceLabel.setForeground(textColor);
        goldenFishChosenFrequency = new JComboBox(items);
        goldenFishChosenFrequency.setSelectedIndex(3);
        goldenFishChosenFrequency.setFocusable(false);

        JPanel guppieContainer = new JPanel();
        guppieContainer.setBackground(bgColor);
        guppieContainer.add(guppieChanceLabel);
        guppieContainer.add(guppieChosenFrequency);

        JPanel goldContainer = new JPanel();
        goldContainer.setBackground(bgColor);
        goldContainer.add(goldChanceLabel);
        goldContainer.add(goldenFishChosenFrequency);

        controlPanel.add(guppieContainer);
        controlPanel.add(goldContainer);
    }
    public void initPanelElements() {
        controlPanel.setLayout(new GridLayout(9,1));
        controlPanel.setBackground(bgColor);
        initStartStopButtons();
        initRadioButtons();
        initShowInformation();
        initGenerationTime();
        initChanceOfGeneration();
    }
    public void toogleStatisticsLabel() { // Метод, включение/выключение видимости StatisticsLabel (надпись "Simulation time:")
        if (model.getSimulationBeginTime() != -1) {

            if(label == null)
                initLabel();

            label.setVisible(!label.isVisible());
        }
    }

    public void initLabel() { // Метод, создающий/пересоздающий StatisticsLabel

        for(Component comp : habitat.getComponents()){
            if(comp == label) habitat.remove(label); // удаляем объект в habitat
            System.out.println("Removed");
        }
        label = new StatisticsLabel(model);

        habitat.add(label);
        label.setBounds(0,0,300,100);
        label.setVisible( radioSimTimeShow.isSelected() );
    }

    public StatisticsLabel getStatisticsLabel(){
        return label;
    }

}
