package Models.Storage;


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
    public void add(String name, File file) throws IOException {

    }

    @Override
    public BufferedImage get(String name) {
        return null;
    }



}
