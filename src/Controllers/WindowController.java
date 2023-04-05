package Controllers;

import Models.Habitat;
import Models.WindowModel;
import Views.WindowView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WindowController {

   final Timer timer;
   private WindowModel model;
   private WindowView view;
   private final int timerDelay = 0;
   private final int timerPeriod = 1000;
   public final Date simulationBeginDate;
   public WindowController(WindowModel model, WindowView view){

       this.model = model;
       this.view = view;

       timer = new Timer();
       simulationBeginDate = new Date();
       timer.schedule(new TimerTask() {
           public void run(){
               model.getHabitat().update(new Date().getTime()-simulationBeginDate.getTime());
           }

       }, timerDelay, timerPeriod);

   }



}
