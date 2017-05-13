package com.albee.fly;

/**
 * Created by Albee on 2017/5/13.
 */
import java.awt.image.BufferedImage;

public class Hero extends FlyingObject {
    private int speed = 1;
    private BufferedImage[] images = new BufferedImage[0];
    private int index = 0;
    private int doubleFire = 0;
    private int life = 3;

    public Hero() {
        this.images = new BufferedImage[]{ShootGame.hero0, ShootGame.hero1};
        this.image = ShootGame.hero0;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.x = 150;
        this.y = 400;
    }

    public int isDoubleFire() {
        return this.doubleFire;
    }

    public void setDoubleFire(int doubleFire) {
        this.doubleFire = doubleFire;
    }

    public void addDoubleFire() {
        this.doubleFire = 40;
    }

    public void addLife() {
        ++this.life;
    }

    public void subtractLife() {
        --this.life;
    }

    public int getLife() {
        return this.life;
    }

    public void moveTo(int x, int y) {
        this.x = x - this.width / 2;
        this.y = y - this.height / 2;
    }

    public boolean outOfBounds() {
        return false;
    }

    public Bullet[] shoot() {
        int xStep = this.width / 4;
        int yStep = 20;
        Bullet[] bullets;
        if(this.doubleFire > 0) {
            bullets = new Bullet[]{new Bullet(this.x + 1 * xStep, this.y - yStep), new Bullet(this.x + 3 * xStep, this.y - yStep)};
            return bullets;
        } else {
            bullets = new Bullet[]{new Bullet(this.x + 2 * xStep, this.y - yStep)};
            return bullets;
        }
    }

    public void step() {
        if(this.images.length > 0) {
            this.image = this.images[this.index++ / 10 % this.images.length];
        }

    }

    public boolean hit(FlyingObject other) {
        int x1 = other.x - this.width / 2;
        int x2 = other.x + this.width / 2 + other.width;
        int y1 = other.y - this.height / 2;
        int y2 = other.y + this.height / 2 + other.height;
        int herox = this.x + this.width / 2;
        int heroy = this.y + this.height / 2;
        return herox > x1 && herox < x2 && heroy > y1 && heroy < y2;
    }
}

