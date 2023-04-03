package Models.Fishes;

import Models.Entity;

public class GoldFish extends Entity {

    {
        this.setFrequency(0.3);
        this.setGenerationTime(2000);
        this.setImageURL("src/res/goldfish.png");
    }
    GoldFish(int x, int y, int width, int height){
        super(x,y,width,height);
    }

    @Override
    protected Entity clone() {
        GoldFish copy = new GoldFish(this.getX(), this.getY(), this.width, this.height);
        return copy;
    }
}
