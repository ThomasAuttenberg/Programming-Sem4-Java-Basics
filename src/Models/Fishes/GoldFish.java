package Models.Fishes;

import Models.Entity;

public class GoldFish extends Entity {

    {
        this.setFrequency(0.3);
        this.setGenerationTime(2000);
        this.setImageURL("src/res/goldfish.png");
    }
    public GoldFish(int width, int height){
        super(width,height);
    }

    @Override
    protected Entity clone() {
        GoldFish copy = new GoldFish(this.width, this.height);
        return copy;
    }
}
