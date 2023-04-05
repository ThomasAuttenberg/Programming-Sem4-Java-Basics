import Controllers.WindowController;
import Models.Fishes.GoldFish;
import Models.Habitat;
import Models.WindowModel;
import Views.WindowView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        WindowModel model = new WindowModel();
        WindowView view = new WindowView(model);
        WindowController controller = new WindowController(model, view);
    }
}