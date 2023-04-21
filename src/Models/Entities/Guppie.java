package Models.Entities;

import Models.Storage.ImageDataAccessor;

import java.io.IOException;

public class Guppie extends Entity {

    {
        try {
            this.setImg(ImageDataAccessor.instance.get("res/guppie.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Guppie(int width, int height){
        super(width,height);
    } // аналогично GoldFish

    @Override
    public Entity clone() { //Аналогично GoldFish
        return new Guppie(this.width, this.height);
    }
}
