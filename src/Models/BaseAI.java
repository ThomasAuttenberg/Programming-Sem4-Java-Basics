package Models;

import Models.Entities.Entity;

import java.util.LinkedList;

public abstract class BaseAI extends Thread {

    Class type;
    LinkedList<Entity> entities;

    Object actionData = null;

    public BaseAI(LinkedList<Entity> entities, Class type){
        this.entities = entities;
        this.type = type;
    }

    public BaseAI(LinkedList<Entity> entities, Class type, Object actionData){
        this.entities = entities;
        this.type = type;
        this.actionData = actionData;
    }

    @Override
    public void run() {
        synchronized(entities){
            while(true) {
                for (Entity entity : entities) {
                    if (entity.getClass() == type) {
                        //action(entity, actionData);
                    }
                }
            }
        }
    }

    public abstract void action(Entity ent, Object additionalObject);
}
