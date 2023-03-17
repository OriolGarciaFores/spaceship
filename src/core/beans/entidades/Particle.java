package core.beans.entidades;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

public class Particle {
    private PVector position, speed, acc;
    private float lifespan;
    private Color c;

    public Particle(PVector pos, Color cl, PApplet pApplet) {
        this.acc = new PVector(pApplet.random(-1, 1), pApplet.random(-1, 1));
        this.speed = new PVector(0, 0);
        this.position = pos.copy();
        this.lifespan = 255.0f;
        this.c = cl;
    }

    public void update() {
        move();
        lifespan -= 10.0;
    }

    private void move() {
        this.speed.add(this.acc);
        this.position.add(this.speed);
    }

    public void paint(PGraphics graphics) {
        graphics.noStroke();
        graphics.fill(c.getRGB(), lifespan);
        graphics.pushMatrix();
        graphics.translate(this.position.x, this.position.y);
        graphics.rect(0, 0, 3, 3);
        graphics.popMatrix();
    }

    public boolean isDead() {
        if (this.lifespan < 0.0) {
            return true;
        } else {
            return false;
        }
    }
}
