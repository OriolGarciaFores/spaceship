package core.beans.entidades;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import core.utils.*;

import java.awt.*;

public class Ball {
    private PVector pos, direction;
    private float rotation, speed;
    private float rad = 20f;
    private Color c = Color.decode("#F79E0C");
    private boolean isDie = false;

    public Ball(PVector pos, PVector direction, float speed) {
        this.pos = new PVector(pos.x, pos.y);
        this.direction = new PVector(direction.x, direction.y);
        rotation = PApplet.atan2(this.direction.y - this.pos.y, this.direction.x - this.pos.x) / PApplet.PI * 180;
        this.speed = speed;
    }

    public void update() {
        calPos();
        calDie();
    }

    private void calPos() {
        this.pos.x = this.pos.x + PApplet.cos(rotation / 180 * PApplet.PI) * speed;
        this.pos.y = this.pos.y + PApplet.sin(rotation / 180 * PApplet.PI) * speed;
    }

    public void paint(PGraphics graphics) {
        graphics.pushMatrix();
        graphics.translate(this.pos.x, this.pos.y);
        graphics.fill(c.getRGB());
        graphics.noStroke();
        graphics.ellipse(0, 0, this.rad, this.rad);
        //debugArea(rad);
        graphics.popMatrix();
    }

    private void calDie() {
        if (this.pos.y <= 0 || this.pos.x <= 0 || this.pos.y >= Constants.HEIGHT || this.pos.x >= Constants.WIDTH) {
            this.isDie = true;
        }
    }

    public void setColor(Color c) {
        this.c = c;
    }

    public void setRad(float rad) {
        this.rad = rad;
    }

    public float getRad() {
        return rad;
    }

    public boolean isDie() {
        return isDie;
    }

    public void setDie(boolean die) {
        isDie = die;
    }

    public PVector getPos() {
        return pos;
    }
}
