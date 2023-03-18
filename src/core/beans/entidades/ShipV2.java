package core.beans.entidades;

import core.gestores.GestorDisparos;
import processing.core.PGraphics;
import processing.core.PVector;
import core.utils.*;

import java.awt.*;

public class ShipV2 extends Enemy {
    private float timer;
    private final float timerFrame = (0.5f * Constants.FRAMES);
    private float timerShot;
    private final float timerShotFrames = (2f * Constants.FRAMES);

    private int timerColor = 0;
    private final float timerColorFrame = (0.2f * Constants.FRAMES);
    private GestorDisparos gestorDisparos;

    public ShipV2(Player player, PVector pos, GestorDisparos gestorDisparos) {
        this.pos = new PVector(pos.x, pos.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 2;
        init_monster(player);
        this.score = 12;
        this.health = 3;
        this.isMovil = true;
        this.id = 5;
        this.rad = Constants.SHIP_V2_RAD;
        this.gestorDisparos = gestorDisparos;
    }

    public void init_monster(Player player) {
        setPlayer(player);
        c = Color.decode("#FAD91C");
    }

    @Override
    public void timerShot() {
        if (this.inmortal) return;

        timerShot++;
        if (timerShot >= timerShotFrames) {
            addBalls();
            timerShot = 0;
        }

    }

    @Override
    public void paint(PGraphics graphics) {
        timerColor();
        graphics.fill(c.getRGB());
        graphics.strokeWeight(3);
        graphics.stroke(Constants.COLOR_INMORTAL.getRGB());

        graphics.pushMatrix();
        graphics.translate(pos.x, pos.y);
        graphics.ellipse(0, 0, rad, rad);
        //debugArea(rad);
        graphics.popMatrix();
    }

    public void timerColor() {
        timer++;
        if (this.c == Constants.COLOR_DMG && timer >= timerFrame) {
            this.c = Color.decode("#FAD91C");
            timer = 0;
        }

        if (this.inmortal) {
            this.timerColor++;
            if (this.timerColor > this.timerColorFrame) {
                if (this.c == Color.decode("#FAD91C")) {
                    this.c = Constants.COLOR_INMORTAL;
                } else {
                    this.c = Color.decode("#FAD91C");
                }
                this.timerColor = 0;
            }
        } else if (this.c == Constants.COLOR_INMORTAL) {
            this.c = Color.decode("#FAD91C");
        }
    }

    private void addBalls() {
        Ball ball = new Ball(this.pos, this.player.pos, 8);
        ball.setColor(Constants.COLOR_ORANGE);
        ball.setRad(20f);
        this.gestorDisparos.addBallEnemy(ball);
    }
}
