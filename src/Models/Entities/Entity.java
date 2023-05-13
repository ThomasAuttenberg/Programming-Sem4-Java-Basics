package Models.Entities;

import Models.Interfaces.IBehaivor;
import Models.Storage.ImageDataAccessor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public abstract class Entity extends JComponent implements IBehaivor, Serializable {

    transient private Image img = null;
    protected int width;
    protected int height;
    private int generationTime = 2000;
    private double frequency = 0;
    private long lifeTime;

    private String imageURL;
    private int id;


    protected Entity(int width, int height){
        this.width = width; //Устанавливаем размеры рыбки
        this.height = height;
        this.setSize(width,height); //Подгоняем под них размер компонента.
    }

    public void setFrequency(double frequency) {
        // Установка частоты появления рыбки
        if(frequency < 0) throw new IllegalArgumentException(); // на вход подали ненормальное число - бросаем исключение
        this.frequency = frequency; //устанавливаем частоту
    }

    // Дальше тоже простые геттеры сеттеры
    public void setGenerationTime(int generationTime) {
        this.generationTime = generationTime;
    }

    public double getFrequency() {
        return frequency;
    }

    public int getGenerationTime() {
        return generationTime;
    }

    public long getLifeTime(){
        return lifeTime;
    }

    public void setLifeTime(long lifeTime){
        this.lifeTime = lifeTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Переопределяем paintComponent функцию JComponent. Отвечает за отрисовку компонента.
    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        //super.paintComponent(g);
        if (img != null) {
            //Graphics n = img.createGraphics(); //вот это я тут эксперементировал, хз почему раньше не убрал
            //n.drawImage(img,0,0,null);
            g.drawImage(img, 0, 0, null); // рисуем картинку просто с кайфом
            //System.out.println("DRAWED");
        } else {
            System.out.println("[paintComponent by "+this.getClass()+"]: img variable is null");
        }
        //repaint();
    }

    // сеттер на картинку. На вход строка с путем к картинке. Берем картинку с этого пути или обрабатываем исключения
    protected void setImageURL(String url) {
        imageURL = url;
        try {
            this.img = ImageDataAccessor.instance.get(url).getScaledInstance(width,height,Image.SCALE_DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void setImg(BufferedImage img){
        this.img = img.getScaledInstance(width,height,Image.SCALE_DEFAULT);
    }

    // Абстрактный метод клонирования объекта. Необходимо переопределять в каждом классе. Нужен для корректной работы Habitat-а.
    public abstract Entity clone();

    @Override
    public void shift(int x, int y) {
            this.setLocation(this.getX() + x, this.getY() + y);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        setImageURL(imageURL);
    }

}
