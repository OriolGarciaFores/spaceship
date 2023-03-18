package core.beans.entidades;

import processing.core.PGraphics;
import processing.core.PVector;
import core.utils.*;

import java.awt.*;

public class Meteorito extends Enemy {
    private int contador = 0;
    private char direct;

    public Meteorito(Player player, PVector pos, PVector tDirection, char direct) {
        this.pos = new PVector(pos.x, pos.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 4;
        init_monster(player, tDirection);
        this.score = 0;
        this.rad = Constants.METEORITO_RAD;
        this.isFollower = false;
        this.direct = direct;
        this.id = 3;
        this.isDestructible = false;
    }

    public void init_monster(Player player, PVector tDirection) {
        setPlayer(player);
        setTarget(tDirection);
        c = Color.decode("#3399cc");
    }

    @Override
    public void update() {
        super.update();

        switch (this.direct) {
            case 'D':
                if (this.pos.y > Constants.HEIGHT + this.rad) {
                    this.isDie = true;
                }
                break;
            case 'L':
                if (this.pos.x <= -this.rad) {
                    this.isDie = true;
                }
                break;
        }
    }

    @Override
    public void paint(PGraphics graphics) {
        graphics.noFill();
        graphics.strokeWeight(4);
        graphics.stroke(c.getRGB());

        graphics.pushMatrix();
        graphics.translate(pos.x, pos.y);
        contador++;
        graphics.rotate(contador);

        for (int i = 0; i < 4; i++) {
            graphics.rotate(graphics.HALF_PI * i);
            graphics.triangle(0, 0, 1 * 30f, 1 * 30f, 0, 1.41f * 30f);//RAD NO ES TAMAÑO REAL.
        }
        //debugArea(rad);
        graphics.popMatrix();
    }
}
