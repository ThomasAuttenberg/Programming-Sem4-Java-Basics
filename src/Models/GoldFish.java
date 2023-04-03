package Models;

import java.net.URL;

public class GoldFish extends Entity{

    {
        this.setFrequency(0.3);
        this.setGenerationTime(2000);
        this.setImageURL("src/res/goldfish.png");
    }
    GoldFish(int x, int y, int width, int height){
        super(x,y,width,height);

    }
}
