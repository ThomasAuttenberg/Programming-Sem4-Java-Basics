package Models.Entities;

public class GoldFish extends Entity {

    {
        this.setFrequency(0.3); //устанавливаем частоту, обращаясь к сеттеру Entity
        this.setGenerationTime(4000); // аналогично
        this.setImageURL("res/goldfish.png"); // аналогично
    }
    public GoldFish(int width, int height){
        super(width,height);
    } //Вызываем конструктор entity и передаем ему полученные параметры

    @Override
    public Entity clone() { //переопределяем clone метод Entity
        return new GoldFish(this.width, this.height);
    }
}
