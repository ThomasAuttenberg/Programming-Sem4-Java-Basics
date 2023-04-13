package Views;

import Models.Entities.GoldFish;
import Models.Entities.Guppie;
import Models.StatisticsManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Date;

/*

    Весь класс - просто надпись "Simulation Begin: " в углу экрана. Ну, и полная статистика выводится тут же по необходимости


 */


public class StatisticsLabel extends JComponent {

    Font font;
    long simulationBeginTime;
    int yBetweenIndent; // Отступ по вертикали.
    int xIndent = 5; // Отступ по горизонтали
    StatisticsManager stats; // Поле для хранения менеджера статистики
    //boolean fullstat = false;

    {

        // Ниже просто читаем шрифт.

        InputStream is;
        try {
            is = new FileInputStream("res/font.otf");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(30f);
            yBetweenIndent = (int)(font.getSize()*1.5);
        }catch(FontFormatException ex){
            System.out.println("FontFormatException: "+ex.getMessage());
        }
        catch (IOException ex){
            System.out.println("IOException"+ex.getMessage());
        }


    }
    public void toogleFullStatistics(StatisticsManager stats){ //Включает полную статистику. (Востребовано по завершению симуляции)
        this.stats = stats; //Устанавливаем статистик менеджер. Если он установлен, paintComponent сделает остальную работу.
        this.setVisible(true); // Если компонент выключен - включаем
        this.repaint(); // Перерисовка компонента
    }
    public StatisticsLabel(long simulationBeginTime){
        this.simulationBeginTime = simulationBeginTime; // Конструктор. На вход получает время начала симуляции.
    }
    @Override
    protected void paintComponent(Graphics g) {
        // Отрисовываем надпись.
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(Color.ORANGE);
        g2d.setFont(font);
        g2d.drawString("Simulation Time: " + ((new Date()).getTime() - simulationBeginTime)/1000,xIndent,font.getSize());
        // Если установлен статистик менеджер, рисуем полную статистику:
        if(stats != null){
            g2d.drawString("GoldFish generated: " + stats.instancesCounter.getNumberOf(GoldFish.class),xIndent,font.getSize()*2);
            g2d.drawString("Guppie generated: " + stats.instancesCounter.getNumberOf(Guppie.class),xIndent,font.getSize()*3);
        }
    }
}
