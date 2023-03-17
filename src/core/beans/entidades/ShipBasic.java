package core.beans.entidades;

import core.utils.*;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

public class ShipBasic extends Enemy {
    private int timerColor = 0;
    private final float timerColorFrame = (0.2f * Constants.FRAMES);

    public ShipBasic(Player player, PVector pos) {
        this.pos = new PVector(pos.x, pos.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 1;
        init_monster(player);
        this.score = 3;
        this.id = 0;
        this.rad = Constants.ENEMY_SHIP;
    }

    public void init_monster(Player player) {
        setPlayer(player);
        c = Color.decode("#3399cc");
    }

    public void paint(PGraphics graphics) {
        if (this.inmortal) {
            timerColor();
        } else {
            this.c = Color.decode("#3399cc");
        }

        graphics.noFill();
        graphics.strokeWeight(4);
        graphics.stroke(c.getRGB());

        graphics.pushMatrix();
        graphics.translate(pos.x, pos.y);
        graphics.rect(-7, -7, 15, 15);
        //debugArea(rad);
        graphics.popMatrix();
    }

    private void timerColor() {
        this.timerColor++;
        if (this.timerColor > this.timerColorFrame) {
            if (this.c.equals(Color.decode("#3399cc"))) {
                this.c = Constants.COLOR_INMORTAL;
            } else {
                this.c = Color.decode("#3399cc");
            }
            this.timerColor = 0;
        }
    }
}
