package core.beans.entidades;

import processing.core.PGraphics;
import processing.core.PVector;
import core.utils.*;

import java.awt.*;
import java.util.ArrayList;

import static processing.core.PApplet.println;

public abstract class Enemy {
    public int id;
    protected PVector pos, speed, acc;
    protected float maxSpeed;
    protected Color c;
    protected PVector target;
    protected int health;
    protected boolean inmortal = true;
    public boolean animationDestroy = true;

    protected Player player;
    public int score;
    private int inmortalTimer;
    private float inmortalTimerFrame = Constants.FRAMES;

    public float rad = 20f;

    public boolean isDie = false;
    protected boolean isMovil = true;
    protected boolean isFollower = true;
    protected boolean isDestructible = true;
    protected boolean tieneExplosion = false;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLoc(PVector loc) {
        this.pos = new PVector(loc.x, loc.y);
    }

    public void setTarget(PVector ta) {
        this.target = new PVector(ta.x, ta.y);
    }

    public void move() {
        calAcc();
        calSpeed();
        calPos();
        this.acc = new PVector();
    }

    private void calAcc() {
        PVector findTarget = new PVector(this.target.x - this.pos.x, this.target.y - this.pos.y);
        this.acc.add(findTarget);
    }

    private void calSpeed() {
        this.speed.add(this.acc);
        this.speed.limit(this.maxSpeed);
    }

    private void calPos() {
        this.pos.add(this.speed);
    }

    public void update() {
        if (this.isMovil) {
            if (this.isFollower) {
                setTarget(player.pos);
            }
            move();
        }

        timerInmortal();
    }

    public void timerShot() {
    }

    public abstract void paint(PGraphics graphics);

    public void afterDie() {
        if(this.isDie){
            mecanicaAfterDie();
        }
    }

    protected void mecanicaAfterDie(){

    }

    public void decreaseLife() {
        if ((this.health - 1) <= 0) {
            this.health = 0;
        } else {
            this.health -= 1;
        }
        if (this.health == 0) {
            this.isDie = true;
        }
    }

    private void timerInmortal() {
        this.inmortalTimer++;
        if (this.inmortalTimer > this.inmortalTimerFrame) {
            this.inmortal = false;
            this.inmortalTimer = 0;
        }
    }

    public PVector getPos() {
        return pos;
    }

    public Color getC() {
        return c;
    }

    public boolean isInmortal() {
        return inmortal;
    }

    public boolean isDestructible() {
        return isDestructible;
    }
}
