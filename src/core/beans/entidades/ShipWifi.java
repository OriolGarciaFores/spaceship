package core.beans.entidades;

import core.utils.Constants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

public class ShipWifi extends Enemy {
    private int timerColor = 0;
    private final float timerColorFrame = (0.2f * Constants.FRAMES);
    private int timerAnimation = 0;
    private int timerAnimationFrame = 3;
    private int cont = 0;

    public ShipWifi(Player player, PVector pos) {
        this.pos = new PVector(pos.x, pos.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 2;
        init_monster(player);
        this.score = 8;
        this.rad = Constants.ENEMY_SHIP_WIFI_RAD;
        this.id = 2;
    }

    public void init_monster(Player player) {
        setPlayer(player);
        c = Color.decode("#3BF200");
    }

    @Override
    public void paint(PGraphics graphics) {
        if (this.inmortal) {
            timerColor();
        } else {
            this.c = Color.decode("#3BF200");
        }

        graphics.noStroke();
        graphics.fill(c.getRGB());
        graphics.pushMatrix();
        graphics.translate(pos.x, pos.y);
        graphics.ellipse(0, 0, this.rad - 20f, this.rad - 20f);
        //debugArea(rad);
        timer(graphics);
        graphics.popMatrix();
    }

    public void timer(PGraphics graphics) {
        graphics.noFill();
        graphics.stroke(c.getRGB());
        graphics.strokeWeight(1);
        timerAnimation++;
        if (timerAnimation >= timerAnimationFrame) {
            cont++;
            if (cont % 2 == 0) {
                graphics.ellipse(0, 0, 30, 30);
                cont = 0;
            } else {
                graphics.ellipse(0, 0, 30, 30);
                graphics.ellipse(0, 0, 40, 40);
            }

            timerAnimation = 0;
        }
    }

    private void timerColor() {
        this.timerColor++;
        if (this.timerColor > this.timerColorFrame) {
            if (this.c.equals(Color.decode("#3BF200"))) {
                this.c = Constants.COLOR_INMORTAL;
            } else {
                this.c = Color.decode("#3BF200");
            }
            this.timerColor = 0;
        }
    }
}
