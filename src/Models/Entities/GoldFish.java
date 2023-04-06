package Models.Entities;

public class GoldFish extends Entity {

    {
        this.setFrequency(0.3);
        this.setGenerationTime(4000);
        this.setImageURL("res/goldfish.png");
    }
    public GoldFish(int width, int height){
        super(width,height);
    }

    @Override
    public Entity clone() {
        return new GoldFish(this.width, this.height);
    }
}
