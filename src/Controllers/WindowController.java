package Controllers;

import Models.Habitat;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WindowController {

   final Timer timer;
   private final int timerDelay = 0;
   private final int timerPeriod = 1000;
   public final Date simulationBeginDate;
    Habitat habitat = new Habitat();

    {

        timer = new Timer();
        simulationBeginDate = new Date();
        timer.schedule(new TimerTask() {
            public void run(){
                habitat.update(new Date().getTime()-simulationBeginDate.getTime());
            }

            }, timerDelay, timerPeriod);

    }



}
