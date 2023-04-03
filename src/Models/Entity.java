package Models;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class Entity extends JComponent {
    private int layer;
    //private URL imageURL;
    private BufferedImage img = null;
    int width;
    private int generationTime = 2000;
    private double frequency = 0;
    int height;

    Entity(int x, int y, int width, int height){
        this.setLocation(x,y);
        this.width = width;
        this.height = height;
    }

    public void setFrequency(double frequency) {
        if(frequency < 0) throw new IllegalArgumentException();
        this.frequency = frequency;
    }

    public void setGenerationTime(int generationTime) {
        this.generationTime = generationTime;
    }

    public double getFrequency() {
        return frequency;
    }

    public int getGenerationTime() {
        return generationTime;
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        if(img != null)
            g.drawImage(img.getScaledInstance(width, height, Image.SCALE_DEFAULT),this.getX(),this.getY(),null);
        else
            g.drawRect(this.getX(), this.getY(), width, height);
    }

    protected void setImageURL(String url) {
        URL imageURL = null;
        try {
            imageURL = new URL(url);
            img = ImageIO.read(imageURL);
        } catch (Exception exception) {
            System.out.println("Can't load image" + imageURL == null ? ": incorrect URL" : " on "+imageURL);
        }
    }
}
