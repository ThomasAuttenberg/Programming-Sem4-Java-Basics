package Models.Entities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public abstract class Entity extends JComponent {

    private BufferedImage img = null;
    protected int width;
    protected int height;
    private int generationTime = 2000;
    private double frequency = 0;


    protected Entity(int width, int height){
        this.width = width;
        this.height = height;
        this.setSize(width,height);
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
        if (img != null) {
            Graphics n = img.createGraphics();
            n.drawImage(img,0,0,null);
            g.drawImage(img.getScaledInstance(width, height, Image.SCALE_DEFAULT), 0, 0, null);
            //System.out.println("DRAWED");
        } else {
            System.out.println("[paintComponent by "+this.getClass()+"]: img variable is null");
        }
        repaint();
    }

    protected void setImageURL(String url) {
        File imgFile = null;
        try {
            imgFile = new File(url);
            img = ImageIO.read(imgFile);
        } catch (Exception exception) {
            System.out.println("Can't load image" + (imgFile == null ? (": incorrect URL") : (" on "+url) ) );
        }
    }

    public abstract Entity clone();
}
