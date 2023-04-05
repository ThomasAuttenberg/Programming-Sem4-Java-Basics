package Models;

import Models.Entities.Entity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Habitat extends JPanel {
    private final ArrayList<Entity> entities;
    private final StatisticsManager stats;
    private final HashSet<Entity> generatingTypes;
    private final HashMap<Entity, Long> lastUpdate;


    private BufferedImage backgroundImage;

    {


        this.setBackground(Color.BLUE);
        this.setLayout(null);
        generatingTypes = new HashSet<>();
        entities = new ArrayList<>();
        lastUpdate = new HashMap<>();



    }


    public Habitat(StatisticsManager stats, String backgroundImageURL) {
        this.stats = stats;
        try {
            backgroundImage = ImageIO.read(new File(backgroundImageURL));
        } catch(IOException ex) {
            System.out.println("Can't read Habitat background image on "+backgroundImageURL);
        }
    }


    public ArrayList<Entity> getEntities(){
        return entities;
    }


    public void update(long simulationTime){


        for(Entity type : generatingTypes){
            if(simulationTime - lastUpdate.get(type) >= type.getGenerationTime()){
                lastUpdate.put(type, simulationTime);
                if(type.getFrequency() >= 1.0){
                    Entity newEntity = type.clone();
                    entities.add(newEntity);
                    this.add(newEntity);
                }else{
                    double rand = Math.random();
                    if(rand<=type.getFrequency()){
                        Entity newEntity = type.clone();
                        newEntity.setLocation((int)(Math.random()*(this.getWidth()+1)),(int)(Math.random()*(this.getHeight()+1)));
                        this.add(newEntity);
                        entities.add(newEntity);
                        System.out.println("REVALIDATION");
                        stats.instancesCounter.incrementInstance(type.getClass());
                    }
                }

            }
        }
       this.repaint();
    }

    public void addGeneratingType(Entity typeToGenerate)
    {
        generatingTypes.add(typeToGenerate);
        lastUpdate.put(typeToGenerate, 0L);
    }
//
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(backgroundImage != null)
            g.drawImage(backgroundImage,0,0,this.getWidth(),this.getHeight(), null);
    }

    public void stopSimulation(){
        for(Entity ent : entities){
            this.remove(ent);
        }
        entities.clear();
        for(Entity type : generatingTypes){
            lastUpdate.put(type,0L);
        }
    }
    public void startSimulation(){
        for(Entity type : generatingTypes){
            lastUpdate.put(type,0L);
        }
    }
}
