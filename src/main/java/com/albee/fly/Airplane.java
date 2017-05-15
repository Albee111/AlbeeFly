package com.albee.fly;

import java.util.Random;

/**
 * Created by Albee on 2017/5/13.
 */
public class Airplane extends  FlyingObject implements  Enemy {
    private int speed = 3;

    public Airplane() {
        this.image = ShootGame.airplane;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.y = -this.height;
        Random rand = new Random();
        this.x = rand.nextInt(400 - this.width);
    }

    public int getScore() {
        return 5;
    }

    public boolean outOfBounds() {
        return this.y > 654;
    }

    public void step() {
        this.y += this.speed;
    }
}
