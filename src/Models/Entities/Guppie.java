package Models.Entities;

public class Guppie extends Entity {
    {
        this.setFrequency(0.4); // устанавливаем частоту, промежутки возможной генерации, картинку
        this.setGenerationTime(2000);
        this.setImageURL("res/guppie.png");
    }
    public Guppie(int width, int height){
        super(width,height);
    } // аналогично GoldFish

    @Override
    public Entity clone() { //Аналогично GoldFish
        Guppie copy = new Guppie(this.width, this.height);
        return copy;
    }
}
