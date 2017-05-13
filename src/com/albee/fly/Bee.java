package com.albee.fly;

import java.util.Random;

/**
 * Created by Albee on 2017/5/13.
 */
public class Bee extends FlyingObject implements  Award{

    private int xSpeed = 1;
    private int ySpeed = 2;
    private int awardType;

    public Bee() {
        this.image = ShootGame.bee;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.y = -this.height;
        Random rand = new Random();
        this.x = rand.nextInt(400 - this.width);
        this.awardType = rand.nextInt(2);
    }

    public int getType() {
        return this.awardType;
    }

    public boolean outOfBounds() {
        return this.y > 654;
    }

    public void step() {
        this.x += this.xSpeed;
        this.y += this.ySpeed;
        if(this.x > 400 - this.width) {
            this.xSpeed = -1;
        }

        if(this.x < 0) {
            this.xSpeed = 1;
        }

    }

}
