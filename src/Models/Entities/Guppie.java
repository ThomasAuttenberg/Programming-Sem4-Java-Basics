package Models.Entities;

public class Guppie extends Entity {
    {
        this.setFrequency(0.4);
        this.setGenerationTime(2000);
        this.setImageURL("res/guppie.png");
    }
    public Guppie(int width, int height){
        super(width,height);
    }

    @Override
    public Entity clone() {
        Guppie copy = new Guppie(this.width, this.height);
        return copy;
    }
}
