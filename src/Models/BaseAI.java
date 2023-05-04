package Models;

import Models.Entities.Entity;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseAI extends Thread {

    final List<Entity> entities;
    final long updatePeriod;

    private boolean waiting = false;
    Object actionData = null;

    public BaseAI(List<Entity> entities, long updatePeriod){
        this.entities = entities;
        this.updatePeriod = updatePeriod;
    }

    public BaseAI(List<Entity> entities, long updatePeriod, Object actionData){
        this.entities = entities;
        this.actionData = actionData;
        this.updatePeriod = updatePeriod;
    }

    public synchronized void toogleWaitingMode(){
        waiting = !waiting;
        if(waiting == false){
            notify();
        }
    }

    @Override
    public void run() {
            while(true) {

                synchronized(entities){

                    for (Entity entity : entities) {
                        action(entity, actionData);
                    }

                }

                try {
                    if(waiting == true){
                        synchronized (this) {
                            wait();
                        }
                    }
                    Thread.sleep(updatePeriod);
                } catch (InterruptedException e) {
                    break;
                    //throw new RuntimeException(e);
                }



            }
    }

    public abstract void action(Entity ent, Object additionalObject);
}
