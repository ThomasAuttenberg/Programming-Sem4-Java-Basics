package Models.Entities;

import Models.Interfaces.IBehaivor;
import Models.Storage.ImageDataAccessor;

import java.io.IOException;

public class GoldFish extends Entity {

    {

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
