package Models.Entities;

import Models.Storage.ImageDataAccessor;

import java.io.IOException;

public class GoldFish extends Entity {

    {

        try {

            this.setImg(ImageDataAccessor.instance.get("res/goldfish.png")); // аналогично


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public GoldFish(int width, int height){
        super(width,height);
    } //Вызываем конструктор entity и передаем ему полученные параметры

    @Override
    public Entity clone() { //переопределяем clone метод Entity
        return new GoldFish(this.width, this.height);
    }
}
