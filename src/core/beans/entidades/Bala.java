package core.beans.entidades;

import core.utils.Constants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

public class Bala {
    private PVector pos;
    private String direct;
    private int speed;
    private Color c;
    private boolean isDie;

    private float rad = 8f;

    public Bala(PVector loc, String direc) {
        this.pos = new PVector(loc.x, loc.y);
        this.speed = 8;
        this.c = new Color(190, 190, 190);
        this.direct = direc;
        this.isDie = false;
    }

    public Bala(PVector loc, String direc, Color c) {
        this.pos = new PVector(loc.x, loc.y);
        this.speed = 8;
        this.c = c;
        this.direct = direc;
        this.isDie = false;
    }

    public void update() {
        move();
        //paint();
        calDie();
    }

    public void paint(PGraphics graphics) {
        graphics.pushMatrix();
        graphics.translate(pos.x, pos.y);
        graphics.strokeWeight(4);
        graphics.stroke(c.getRGB());
        graphics.fill(c.getRGB());
        graphics.rect(-2, -2, 5, 5);
        //debugArea(rad);
        graphics.popMatrix();
    }

    private void move() {
        switch (this.direct) {
            case "U":
                pos.y -= speed;
                break;
            case "L":
                pos.x -= speed;
                break;
            case "D":
                pos.y += speed;
                break;
            case "R":
                pos.x += speed;
                break;
            case "UL":
                pos.x -= speed;
                pos.y -= speed;
                break;
            case "UR":
                pos.x += speed;
                pos.y -= speed;
                break;
            case "DL":
                pos.x -= speed;
                pos.y += speed;
                break;
            case "DR":
                pos.x += speed;
                pos.y += speed;
                break;
        }
    }

    private void calDie() {
        if (this.pos.y <= 0 || this.pos.x <= 0 || this.pos.y >= Constants.HEIGHT || this.pos.x >= Constants.WIDTH) {
            isDie = true;
        }
    }

    public void setDie(boolean die) {
        isDie = die;
    }

    public boolean isDie() {
        return this.isDie;
    }

    public PVector getPos() {
        return pos;
    }

    public float getRad() {
        return rad;
    }
}
