package Models;

import java.util.HashMap;



public class StatisticsModel {
    //HashSet stats;
    public InstancesCounter instancesCounter;

    public class InstancesCounter{
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
}
