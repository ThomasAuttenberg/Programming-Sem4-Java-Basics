package Models;

import Controllers.WindowController;
import Models.Entities.Entity;
import Models.Entities.GoldFish;
import Models.Entities.Guppie;
import Views.WindowView;

import java.io.*;
import java.lang.module.FindException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
public class ConsoleModel {

    private WindowView windowView;
    private WindowModel windowModel;
    private WindowController windowController;
    public ConsoleModel(WindowModel windowModel, WindowController windowController, WindowView windowView){
        this.windowModel = windowModel;
        this.windowController = windowController;
        this.windowView = windowView;
    }

    public InputStream call(InputStream is){
        byte[] n;
        try {
            n = is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] buffer;
        buffer = call(new String(n, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream requestResult = new ByteArrayInputStream(buffer);
        return requestResult;
    }
    public String call(String userRequest){

        userRequest = userRequest.toLowerCase(Locale.ROOT);
        String request[] = userRequest.split(" ");

        switch(request[0]){
            case "stop":
                if(windowModel.getSimulationBeginTime() != -1) {
                    long simTime = new Date().getTime() - windowModel.getSimulationBeginTime();
                    windowController.stopActionHandler();
                    return "\nSimulation has been stopped\n\n" +
                            "\tStatistics:\n\tGuppies: " + windowModel.getStatisticsManager().instancesCounter.getNumberOf(Guppie.class) +
                            "\n\tGold Fishes: " + windowModel.getStatisticsManager().instancesCounter.getNumberOf(GoldFish.class) +
                            "\n\tSimulation time: " +  simTime + "ms\n";

                }else{
                    return "\n[ ERROR ] Simulation is already stopped!\n";
                }
            case "start":
                if(windowModel.getSimulationBeginTime() == -1) {
                    windowController.startActionHandler();
                    return "\nSimulation has been started\n";
                }else{
                    return "\n[ ERROR ] Simulation is already working!\n";
                }

            case "set":
                if(request.length > 1) {

                    switch (request[1]) {

                        case "goldfishes":

                            if(request.length > 2){

                                switch (request[2]){
                                    case "generationtime":

                                        if(request.length > 3) {
                                            int generationTime = -1;
                                            try {
                                                generationTime = Integer.parseInt(request[3]);
                                            }catch (NumberFormatException ex){
                                                return "\nUse: set goldfishes generationTime [1000-9999]\n";
                                            }
                                            if (generationTime >= 1000 && generationTime <= 9999) {
                                                windowView.goldenFishGenerationTime.setValue(generationTime);
                                                return "\nGoldfishes generation time has been set as "+generationTime+"\n";
                                            }
                                        }
                                        return "\nUse: set goldfishes generationTime [1000-9999]\n";

                                    case "lifetime":

                                        if(request.length > 3){
                                            int lifeTime = -1;
                                            try {
                                                lifeTime = Integer.parseInt(request[3]);
                                            }catch (NumberFormatException ex){
                                                return "\nUse: set goldfishes lifetime [1000-99999]\n";
                                            }
                                            if(lifeTime >= 1000 && lifeTime <=99999){
                                                windowView.goldenFishLifeTime.setValue(lifeTime);
                                            }
                                            return "\nGoldfishes life time has been set as "+lifeTime+"\n";
                                        }
                                        return "\nUse: set goldfishes lifetime [1000-99999]\n";

                                    case "frequency":

                                        if(request.length > 3){
                                            double frequency = -1;
                                            try {
                                                frequency = Double.parseDouble(request[3]);
                                            }catch (NumberFormatException ex){
                                                return "\nUse: set guppies frequency [0-1]\n";
                                            }
                                            if(frequency >= 0 && frequency <= 1){

                                                if(windowView.goldenFishChosenFrequency.getItemCount() > windowView.GENERATION_CHANCES_COMBOBOX_NUMBER)
                                                    windowView.goldenFishChosenFrequency.removeItemAt(windowView.GENERATION_CHANCES_COMBOBOX_NUMBER);

                                                int chosenFrequencyInList = -1;
                                                for(int i = 0; i<windowView.goldenFishChosenFrequency.getItemCount(); i++){
                                                    if(windowView.goldenFishChosenFrequency.getItemAt(i).equals((int)(frequency * 100) + "%")){
                                                        chosenFrequencyInList = i;
                                                    }
                                                }

                                                if(chosenFrequencyInList == -1) windowView.goldenFishChosenFrequency.addItem((int) (frequency * 100) + "%");
                                                windowView.goldenFishChosenFrequency.setSelectedIndex(chosenFrequencyInList != -1 ? chosenFrequencyInList : windowView.GENERATION_CHANCES_COMBOBOX_NUMBER);
                                                return "\nGoldfishes frequency has been set as "+frequency+"\n";
                                            }
                                        }
                                        return "\nUse: set goldfishes frequency [0-1]\n";


                                    default: return "\nUse set goldfishes [ lifeTime | generationTime | frequency ]\n";
                                }

                            }
                            return "\nUse set goldfishes [ lifeTime | generationTime | frequency ]\n";

                        case "guppies":

                            if(request.length > 2){

                                switch (request[2]){
                                    case "generationtime":

                                        if(request.length > 3) {
                                            int generationTime = -1;
                                            try {
                                                generationTime = Integer.parseInt(request[3]);
                                            }catch (NumberFormatException ex){
                                                return "\nUse: set guppies generationTime [1000-9999]\n";
                                            }
                                            if (generationTime >= 1000 && generationTime <= 9999) {
                                                windowView.guppieGenerationTime.setValue(generationTime);
                                                return "\nGuppies generation time has been set as "+generationTime+"\n";
                                            }
                                        }
                                        return "\nUse: set guppies generationTime [1000-9999]\n";

                                    case "lifetime":

                                        if(request.length > 3){
                                            int lifeTime = -1;
                                            try {
                                                lifeTime = Integer.parseInt(request[3]);
                                            }catch (NumberFormatException ex){
                                                return "\nUse: set guppies lifeTime [1000-99999]\n";
                                            }
                                            if(lifeTime >= 1000 && lifeTime <=99999){
                                                windowView.guppieLifeTime.setValue(lifeTime);
                                                return "\nGuppies life time has been set as "+lifeTime+"\n";
                                            }
                                        }
                                        return "\nUse: set guppies lifeTime [1000-99999]\n";

                                    case "frequency":

                                        if(request.length > 3){
                                            double frequency = -1;
                                            try {
                                                frequency = Double.parseDouble(request[3]);
                                            }catch (NumberFormatException ex){
                                                return "\nUse: set guppies frequency [0-1]\n";
                                            }
                                            if(frequency >= 0 && frequency <= 1){
                                                if(windowView.guppieChosenFrequency.getItemCount() > windowView.GENERATION_CHANCES_COMBOBOX_NUMBER)
                                                    windowView.guppieChosenFrequency.removeItemAt(windowView.GENERATION_CHANCES_COMBOBOX_NUMBER);

                                                int chosenFrequencyInList = -1;
                                                for(int i = 0; i<windowView.guppieChosenFrequency.getItemCount(); i++){
                                                    if(windowView.guppieChosenFrequency.getItemAt(i).equals((int)(frequency * 100) + "%")){
                                                        chosenFrequencyInList = i;
                                                    }
                                                }

                                                if(chosenFrequencyInList == -1) windowView.guppieChosenFrequency.addItem((int) (frequency * 100) + "%");
                                                windowView.guppieChosenFrequency.setSelectedIndex(chosenFrequencyInList != -1 ? chosenFrequencyInList : windowView.GENERATION_CHANCES_COMBOBOX_NUMBER);
                                                return "\nGuppies frequency has been set as "+frequency+"\n";
                                            }
                                        }
                                        return "\nUse: set guppies frequency [0-1]\n";


                                    default: return "\nUse set guppies [ lifeTime | generationTime | frequency ]\n";

                                }

                            }
                            return "\nUse set guppies [ lifeTime | generationTime | frequency ]\n";
                    }
                }
                return "\nUse: set [ goldfishes | guppies ]\n";

            case "get":

                if(request.length > 1){

                    switch(request[1]){
                        case "goldfishes":
                            if(request.length > 2){
                                HashSet<Entity> generatingTypes = windowModel.getHabitat().getGeneratingTypes();
                                switch (request[2]){
                                    case "generationtime":
                                                return "\n[ RESULT ]: Goldfishes generation time is " + String.valueOf(windowView.goldenFishGenerationTime.getValue()) + " ms\n";
                                    case "lifetime":
                                                return "\n[ RESULT ]: Goldfishes lifetime is " + String.valueOf(windowView.goldenFishLifeTime.getValue()) + " ms\n";
                                    case "frequency":
                                                return "\n[ RESULT ]: Goldfishes frequency is " + windowView.goldenFishChosenFrequency.getSelectedItem() +"\n";
                                    default: return "\nUse: get goldfishes [ generationsTime | lifeTime | frequency ]\n";
                                }
                            }
                            return "\nUse: get goldfishes [ generationsTime | lifeTime | frequency ]\n";
                        case "guppies":
                            if(request.length > 2){
                                HashSet<Entity> generatingTypes = windowModel.getHabitat().getGeneratingTypes();
                                switch (request[2]){
                                    case "generationtime":
                                        return "\n[ RESULT ]: Guppies generation time is " + String.valueOf(windowView.guppieGenerationTime.getValue()) + " ms\n";
                                    case "lifetime":
                                        return "\n[ RESULT ]: Guppies lifetime is " + String.valueOf(windowView.guppieLifeTime.getValue()) + " ms\n";
                                    case "frequency":
                                        return "\n[ RESULT ]: Guppies frequency is " + windowView.guppieChosenFrequency.getSelectedItem() +"\n";
                                    default: return "\nUse: get guppies [ generationsTime | lifeTime | frequency ]\n";
                                }
                            }
                            return "\nUse: get guppies [ generationsTime | lifeTime | frequency ]\n";
                        default: return "\nUse: get [ goldfishes | guppies ]\n";
                    }

                }
                return "\nUse: get [ goldfishes | guppies ]\n";
            default: return "\nUse: [ stop | start | set | get ]\n";
        }

    }

}
