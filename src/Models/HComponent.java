package Models;

import java.awt.*;

public class Component {
    private int x;
    private int y;
    private int layer;
    private Image img;

    Component(int x, int y){
        this.x = x; this.y = y;
    }
    public int getX(){return x;}
    public int getY(){return y;}
}
