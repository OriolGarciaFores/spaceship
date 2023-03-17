package core.beans.habilidades;

import core.beans.entidades.Player;
import core.utils.*;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

public class Escudo extends Habilidades {
    final float radShield;
    private Player player;

    private int timerColor;

    public Escudo(Player player) {
        this.pos = new PVector(0, 0);
        this.timer = 0;
        this.timerFrame = (4 * Constants.FRAMES);
        this.coldown = (12 * Constants.FRAMES);
        this.isActive = false;
        this.isReady = true;
        this.c = Constants.COLOR_INMORTAL;
        this.radShield = player.r + 20f;
        this.player = player;
        this.isEquiped = false;
        this.lvlRequired = 2;
    }

    public void update() {
        Actionhability();
        move();
        timer();
    }

    public void timer() {
        if (this.isActive) {
            this.timer++;
            if (this.timer >= this.timerFrame) {
                this.isActive = false;
                this.timer = 0;
            }
        } else if (!this.isReady) {
            this.timer++;
            if (this.timer >= this.coldown) {
                this.isReady = true;
                this.timer = 0;
            }
        }
    }

    public void paint(PGraphics graphics) {
        if (this.isActive) {
            graphics.pushMatrix();
            graphics.translate(pos.x, pos.y);
            graphics.fill(this.c.getRGB(), 50);
            if (this.timer >= (this.timerFrame / 2)) {
                animation();
            }
            graphics.stroke(this.c.getRGB());
            graphics.strokeWeight(2);
            graphics.ellipse(0, 0, this.radShield, this.radShield);
            graphics.popMatrix();
        }
        if (this.isReady) {
            showHability(graphics);
        }
    }

    private void animation() {
        timerColor++;
        if (this.c == Constants.COLOR_INMORTAL && timerColor >= 10) {
            this.c = new Color(0);
            timerColor = 0;
        } else if (timerColor >= 10) {
            this.c = Constants.COLOR_INMORTAL;
            timerColor = 0;
        }
    }

    public void showHability(PGraphics graphics) {
        graphics.noFill();
        graphics.stroke(255);
        graphics.strokeWeight(2);
        graphics.ellipse(Constants.CENTRO_VENTANA_X, 40, 20, 20);
    }

    private void Actionhability() {
        if (this.isReady) {
            if (Constants.KEYBOARD.activeShield) {
                this.isActive = true;
                this.isReady = false;
            }
        }
    }

    public void move() {
        this.pos = this.player.pos;
    }

    public float getRad() {
        return this.radShield;
    }

    public void setIsEquiped(boolean isEquiped) {
        this.isEquiped = isEquiped;
    }

    public ArrayList<Object> getObjects() {
        return null;
    }
}
