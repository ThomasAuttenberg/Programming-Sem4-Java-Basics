package Controllers;


import Models.WindowModel;
import Views.WindowView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

import static java.awt.event.KeyEvent.*;

public class WindowController {

   final int simStopKey = VK_E;
   final int simStartKey = VK_B;
   final int showStatKey = VK_T;
   private final WindowModel model;
   private final WindowView view;


   public WindowController(WindowModel model, WindowView view){

       this.model = model;
       this.view = view;

       view.addKeyListener(
               new KeyAdapter() {
                   @Override
                   public void keyPressed(KeyEvent e) {
                       switch (e.getKeyCode()) {
                           case simStartKey:
                               if(model.getSimulationBeginTime() == -1) {
                                   model.startSimulation(new Date().getTime());
                               }
                               view.initLabel();
                               if(view.getStatisticsLabel().isVisible()) view.toogleStatisticsLabel();
                               break;
                           case simStopKey:
                               if(model.getSimulationBeginTime() != -1){
                                   view.getStatisticsLabel().toogleFullStatistics(model.getStatisticsManager());
                                   model.stopSimulation();
                               }
                               break;
                           case showStatKey:
                                   view.toogleStatisticsLabel();
                               break;
                       }
                   }

               }
       );

   }


}
