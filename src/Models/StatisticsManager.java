package Models;

import java.io.Serializable;
import java.util.HashMap;


/*
    Статистик менеджер работает пока что следующим образом. Внутри себя он содержит instanceCounter - объект вложенного класса InstanceCounter.
    Класс InstanceCounter ведет карту [ ключ Class - значение число ]
    incrementInstance(Class class) вызывает увеличение числа в карте по ключу class на единицу.
    decrementInstance работает строго наоборот.
    getNumberOf(Class class) просто возвращает число из карты.

    Простейшая логика, поэтому выглядит слишком сложно для своих функций, но лучше сделать именно подобную архитектуру с заделом на расширение.


 */


public class StatisticsManager implements Serializable {
    public InstancesCounter instancesCounter;

    {
        instancesCounter = new InstancesCounter();
    }

    public class InstancesCounter implements Serializable{
        private HashMap<Class, Integer> instancesCounter;
        {
            instancesCounter = new HashMap<Class, Integer>();
        }
        public void incrementInstance(Class instanceClass){
            Integer instanceCounter = instancesCounter.get(instanceClass);
            if(instanceCounter != null)
                instancesCounter.put(instanceClass, instanceCounter + 1);
            else
                instancesCounter.put(instanceClass, 1);
        }
        public void decrementInstance(Class instanceClass){
            Integer instanceCounter = instancesCounter.get(instanceClass);
            if(instanceCounter != null)
                instancesCounter.put(instanceClass, instanceCounter - 1);
            else
                instancesCounter.put(instanceClass, -1);
        }
        public int getNumberOf(Class instanceClass){
            Integer instanceCounter = instancesCounter.get(instanceClass);
            return instanceCounter == null ? 0 : instanceCounter;
        }

    }

    public void clear(){
        instancesCounter = new InstancesCounter();
    }

}
