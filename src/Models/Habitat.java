package Models;

import Models.Entities.Entity;
import Models.Entities.GoldFish;
import Models.Entities.Guppie;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.KeyPair;
import java.util.*;
import java.util.List;


// Habitat - класс, представляющий собой все поле с рыбками (левая панель). Наследуется, по логике, от JPanel.
// Хранит в себе рыбок в качестве компонентов

public class Habitat extends JPanel {

    private final List<Entity> entities; // Список хранящихся существ (рыбок)
    private final HashSet<Integer> entitiesID;
    private final TreeMap<Integer, Long> bornTime;
    private final StatisticsManager stats; // Статистик-менеджер.
    private final HashSet<Entity> generatingTypes;
    //    !!!!!!! Список уникальных (на самом деле надо переопределить еще hashCode и equals в наших объектах, чтобы HashSet действительно смотрел на уникальность)
    // объектов. Habitat будет брать отсюда объекты и копировать их, создавая новый, такой же объект, если частота и время генерации подходят
    // для генерации нового объекта. Одним словом, generatingTypes - список генерируемых типов
    private final HashMap<Entity, Long> lastUpdate;
    // Когда вызовется update по таймеру, мы получим время симуляции. Отлично, вот только время симуляции ничего
    // с математической точки зрения нам не даст. В прошлой версии лабораторной работы это все держалось на лютых костылях,
    // и если немного где-то подредактировать время - все посыпется.
    // Здесь мы используем карту (передаем объект из generatingTypes в качестве ключа - получаем время), который хранит последнее время, когда
    // simulationTime >= entity.generationTime. Изначально там хранится 0.
    // Ситуация: самый первый вызов update(). На вход подано значение 3154.
    // У нас есть золотая рыбка с временем генерации 2000.
    // Проверяем: 3154 >= 0 (время последнего обновления) + 2000(время генерации) => да! Ставим время последнего обновления раным 3154 - 3154%2000 = 2000
    // В следующей итерации на вход подается 5680
    // 5680 >= 2000 + 2000 => Да!!! Ставим время последнего обновления равным 5680 - 5680%2000 = 4000.
    // Т.е. эта система нужна для того, чтобы все работало вне зависимости от времени обновления.
    // Сейчас пока я это пишу, я понимаю, что это работает очень плохо и сложно, и если за время обновления должно
    // было появиться несколько рыбок, появится только одна.
    // в идеале систему надо немного поменять. Идеи приветствуются
    //
    private BaseAI goldFishAI;
    private BaseAI guppieAI;


    private BufferedImage backgroundImage;

    {

        // Habitat - это все еще JPanel, потому что наследуется от нее,
        // поэтому надо произвести первоначальную настройку.

        this.setBackground(Color.BLUE); // можно убрать вроде

        this.setLayout(null); // изначально ставится FlowLayout, он не позволяет использовать
        // Абсолютное позиционирование. Обнуляем лэйаут менеджер.

        generatingTypes = new HashSet<>(); // просто выделяем память под наши поля
        entities = Collections.synchronizedList( new LinkedList<Entity>());
        lastUpdate = new HashMap<>();
        entitiesID = new HashSet<>();
        bornTime = new TreeMap<>();




    }

    public Habitat(StatisticsManager stats, String backgroundImageURL) {
        this.stats = stats;
        try {
            backgroundImage = ImageIO.read(new File(backgroundImageURL));
        } catch(IOException ex) {
            System.out.println("Can't read Habitat background image on "+backgroundImageURL);
        }
    }


    public List<Entity> getEntities(){
        return entities;
    }


    public void update(long simulationTime) {
        //synchronized (entities) {
            for (Entity type : generatingTypes) { // для каждого type из generatingTypes.
                if (simulationTime - lastUpdate.get(type) >= type.getGenerationTime()) { //если время симуляции >= время генерации типа + время последнего обновления
                    lastUpdate.put(type, simulationTime - simulationTime % type.getGenerationTime()); // устанавливаем время последнего обновления

                    double rand = Math.random();  // Получаем ранд число в полуинтервале [0;1)
                    if (rand <= type.getFrequency()) { // если оно меньше, чем частота появления
                        Entity newEntity = type.clone(); // Клонируем объект
                        newEntity.setLifeTime(type.getLifeTime()); //Копируем время жизни в новый объект
                        int newEntID = setNewID(newEntity); // Устанавливаем новый идентификатор на объект
                        bornTime.put(newEntID, simulationTime); //Устанавливаем время рождения
                        newEntity.setLocation((int) (Math.random() * (this.getWidth() * 0.8 + 1)), (int) (Math.random() * (this.getHeight() * 0.8 + 1))); // Устанавливаем его локацию случайным образом
                        this.add(newEntity); //добавляем как компонент JPanel
                        entities.add(newEntity); // добавляем в список объектов Habitat
                        stats.instancesCounter.incrementInstance(type.getClass()); // Вызываем счетчик объектов менеджера статистики, передаем класс объекта
                        // Счетчик объектов этого типа увеличится на 1.
                        // (Возможный баг: если мы будем передавать модифицированные типы с разными значениями полей, он также будет считать их как объект одного класса)
                    }

                }
            }
            // Удаление старых
            ArrayList<Entity> entitiesToRemove = new ArrayList<>();
            for (Entity ent : entities) {

                int entityID = ent.getId();
                if (simulationTime - bornTime.get(entityID) > ent.getLifeTime()) {
                    entitiesToRemove.add(ent);
                }
            }
            for (Entity ent : entitiesToRemove) {
                removeEntityFromHabitat(ent);
            }

            this.revalidate();
            this.repaint(); // На каждом обновлении перерисовываем JPanel.
        //}
   }

    public void addGeneratingType(Entity typeToGenerate) // метод добавляет хабитату тип для генерации
    {
        generatingTypes.add(typeToGenerate);
        lastUpdate.put(typeToGenerate, 0L);
    }

    public HashSet<Entity> getGeneratingTypes(){
        return generatingTypes;
    }

    // Переопределяем метод paintComponent JPanel, отрисовывая фоновую картинку.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(backgroundImage != null)
            g.drawImage(backgroundImage,0,0,this.getWidth(),this.getHeight(), null);
    }

    // Метод остановки симуляции. Удаление всех компонентов.
    public void stopSimulation(){
        for(Entity ent : entities){
            this.remove(ent);
        }
        // Можно было бы просто поменять весь цикл на this.removeAll(), но мы не можем гарантировать, что необходимо удалить ВСЕ элементы Habitat.
        // Поэтому удаляем только сущности (Рыбок)
        goldFishAI.interrupt();
        guppieAI.interrupt();
        entities.clear(); // очищаем список объектов
    }

    public void pauseSimulation(){
        goldFishAI.toogleWaitingMode();
        guppieAI.toogleWaitingMode();
    }

    public void unpauseSimulation(){
        goldFishAI.toogleWaitingMode();
        guppieAI.toogleWaitingMode();
    }

    public void startSimulation(){
        for(Entity type : generatingTypes){ //подготовка симуляции - расставляем нули в карте последних обновлений
            lastUpdate.put(type,0L);
        }
        initAI();
    }

    public HashSet<Integer> getEntitiesID() {
        return entitiesID;
    }

    public TreeMap<Integer, Long> getBornTime() {
        return bornTime;
    }

    public BaseAI getGoldFishAI(){
        return goldFishAI;
    }

    public BaseAI getGuppieAI(){
        return guppieAI;
    }

    public synchronized void setGoldFishPriority(int chosenPriority){
        if(chosenPriority == 0){

            if(!(goldFishAI.getState() == Thread.State.WAITING)) {
                goldFishAI.toogleWaitingMode();
            }

        }else{

            if(goldFishAI.getState() == Thread.State.WAITING){
                goldFishAI.toogleWaitingMode();
            }

            goldFishAI.setPriority(chosenPriority);
            System.out.println(goldFishAI.getPriority());

        }
    }

    public synchronized void setGuppiePriority(int chosenPriority){
        if(chosenPriority == 0){

            if(!(guppieAI.getState() == Thread.State.WAITING)) {
                guppieAI.toogleWaitingMode();
            }

        }else{

            if(guppieAI.getState() == Thread.State.WAITING){
                guppieAI.toogleWaitingMode();
            }

            guppieAI.setPriority(chosenPriority);
            System.out.println(guppieAI.getPriority());

        }
    }

    private Entity getEntityByID(int id){
        for (Entity ent : entities){
            if(ent.getId() == id){
                return ent;
            }
        }
       return null;
    }

    private int setNewID(Entity ent){

        while(true){

            int newId = (int)(Math.random() * Integer.MAX_VALUE);

            if( entitiesID.contains(newId) ) continue;

            ent.setId(newId);
            entitiesID.add(newId);
            return newId;

        }

    }

    private void removeEntityFromHabitat(int ID){
        for(Entity ent : entities){
            if(ent.getId() == ID){
                this.remove(ent);
                bornTime.remove(ID);
                entitiesID.remove(ID);
                entities.remove(ent);
            }
        }
    }

    private void removeEntityFromHabitat(Entity ent){
                int entId = ent.getId();
                this.remove(ent);
                bornTime.remove(entId);
                entitiesID.remove(entId);
                entities.remove(ent);
    }

    private void initAI(){

        int speedPerUpdatePeriod = 1;
        long hashMapValidationTime = 50000;

        ArrayList<Object> data1 = new ArrayList<>();

        data1.add(this.getWidth());
        data1.add(new HashMap<Integer, Boolean>());
        data1.add(new Date().getTime());
        data1.add(entitiesID);

        goldFishAI = new BaseAI(entities, 25, data1) {
            @Override
            public void action(Entity ent, Object actionData) {

                //Convering actionData back to ArrayList:
                ArrayList<Object> actData = ((ArrayList<Object>)actionData);

                HashSet<Integer> hashSetForValidation = (HashSet)actData.get(3);


                int habitatWidth = (int) actData.get(0);
                HashMap<Integer, Boolean> movingDirection = (HashMap)actData.get(1);

                if(ent.getClass() == GoldFish.class ) {

                    //Reading the direction flag and set the correct value if it's null:
                    Boolean flagMovingPositive = movingDirection.get(ent.getId());
                    if(flagMovingPositive == null){
                        movingDirection.put(ent.getId(), true);
                        flagMovingPositive = true;
                    }

                    //Checking the direction correctness:
                    if(ent.getX() > habitatWidth * 0.9 && flagMovingPositive == true){
                        movingDirection.put(ent.getId(), false);
                        flagMovingPositive = false;
                    }
                    if(ent.getX() < habitatWidth * 0.1 && flagMovingPositive == false){
                        movingDirection.put(ent.getId(), true);
                        flagMovingPositive = true;
                    }
                    //move:
                    if(flagMovingPositive) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ent.shift(speedPerUpdatePeriod, 0);
                            }
                        });
                    }else{
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ent.shift(-speedPerUpdatePeriod, 0);
                            }
                        });
                    }
                }

                // Directions Map validation: (some entities probably do not exist but their direction is still in the map)
                long time = new Date().getTime();
                if(time - (long)actData.get(2) > hashMapValidationTime){
                    LinkedList<Integer> idsToRemove = new LinkedList<>();
                    for (int i : movingDirection.keySet()) {
                        if (!(hashSetForValidation.contains(i))) {
                               idsToRemove.add(i);
                        }
                    }

                    for(int i : idsToRemove){
                        movingDirection.remove(i);
                    }

                    actData.set(2, time);

                }


            }

        };

        ArrayList<Object> data2 = new ArrayList<>();

        data2.add(this.getHeight());
        data2.add(new HashMap<Integer, Boolean>());
        data2.add(new Date().getTime());
        data2.add(entitiesID);
        guppieAI = new BaseAI(entities, 20, data2) {
            @Override
            public void action(Entity ent, Object actionData) {

                //Convering actionData back to ArrayList:
                ArrayList<Object> actData = ((ArrayList<Object>)actionData);

                HashSet<Integer> hashSetForValidation = (HashSet)actData.get(3);


                int habitatHeight = (int) actData.get(0);
                HashMap<Integer, Boolean> movingDirection = (HashMap)actData.get(1);

                if(ent.getClass() == Guppie.class ) {

                    //Reading the direction flag and set the correct value if it's null:
                    Boolean flagMovingPositive = movingDirection.get(ent.getId());
                    if(flagMovingPositive == null){
                        movingDirection.put(ent.getId(), true);
                        flagMovingPositive = true;
                    }

                    //Checking the direction correctness:
                    if(ent.getY() > habitatHeight * 0.9 && flagMovingPositive == true){
                        movingDirection.put(ent.getId(), false);
                        flagMovingPositive = false;
                    }
                    if(ent.getY() < habitatHeight * 0.1 && flagMovingPositive == false){
                        movingDirection.put(ent.getId(), true);
                        flagMovingPositive = true;
                    }
                    //move:
                    if(flagMovingPositive) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ent.shift(0, speedPerUpdatePeriod);
                            }
                        });
                    }else{
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                ent.shift(0, -speedPerUpdatePeriod);
                            }
                        });
                    }
                }

                // Directions Map validation: (some entities probably do not exist but their direction is still in the map)
                long time = new Date().getTime();
                if(time - (long)actData.get(2) > hashMapValidationTime){
                    LinkedList<Integer> idsToRemove = new LinkedList<>();
                    for (int i : movingDirection.keySet()) {
                        if (!(hashSetForValidation.contains(i))) {
                            idsToRemove.add(i);
                        }
                    }

                    for(int i : idsToRemove){
                        movingDirection.remove(i);
                    }

                    actData.set(2, time);

                }


            }

        };

        guppieAI.start();
        goldFishAI.start();


    }

}
