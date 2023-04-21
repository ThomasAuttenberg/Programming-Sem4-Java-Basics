package Models.Storage;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public abstract class Accessor {
    protected HashMap<String, Object> storage = new HashMap<>();
    protected abstract void add(String name, Object obj);
    public abstract Object get(String name);
}
