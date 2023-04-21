package Models.Entities;

import Models.Storage.ImageDataAccessor;

public class Guppie extends Entity {

    {
        this.setImg(ImageDataAccessor.instance.get("res/guppie.png"));
    }
    public Guppie(int width, int height){
        super(width,height);
    } // аналогично GoldFish

    @Override
    public Entity clone() { //Аналогично GoldFish
        return new Guppie(this.width, this.height);
    }
}
