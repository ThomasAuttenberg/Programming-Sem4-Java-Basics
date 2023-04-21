package Models.Storage;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageDataAccessor extends Accessor{

    static public ImageDataAccessor instance;

    static{
        instance = new ImageDataAccessor();
    }
    private ImageDataAccessor(){}

    @Override
    public BufferedImage get(String name) throws IOException {
        if(storage.containsKey(name))
            return (BufferedImage)storage.get(name);

        storage.put(name, ImageIO.read(new File(name)));
        return (BufferedImage)storage.get(name);
    }
}
