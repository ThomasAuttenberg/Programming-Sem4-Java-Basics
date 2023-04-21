import Controllers.WindowController;

import Models.Storage.ImageDataAccessor;
import Models.WindowModel;
import Views.WindowView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {


        WindowModel model = new WindowModel();
        WindowView view = new WindowView(model,1400,700);
        WindowController controller = new WindowController(model,view);
    }
}