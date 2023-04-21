package Models.Storage;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public abstract class Accessor {
    static public Accessor instance;

    private HashMap<String, Object> storage = new HashMap<>();
    protected Accessor(){}
    public abstract void add(String name, File file) throws IOException;
    public abstract Object get(String name);
}
