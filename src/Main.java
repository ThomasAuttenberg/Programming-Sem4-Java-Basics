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

        try {

            ImageDataAccessor.instance.add("res/goldfish.png", ImageIO.read(new File("res/goldfish.png")));
            ImageDataAccessor.instance.add("res/guppie.png", ImageIO.read(new File("res/guppie.png")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WindowModel model = new WindowModel();
        WindowView view = new WindowView(model,1400,700);
        WindowController controller = new WindowController(model,view);
    }
}