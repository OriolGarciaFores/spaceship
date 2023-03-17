package core.beans.comunes;

import core.utils.*;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;

public class SeccionNivel {
    private String title;
    private String score;
    private int posX;
    private int posY;
    private float sizeText;
    private Color c;
    private Color colorSelected;
    private boolean isSelected;

    public SeccionNivel(String title, String score, int posX, int posY) {
        this.title = title;
        this.score = score;
        this.posX = posX;
        this.posY = posY;
        this.sizeText = 32f;
        this.c = new Color(255, 255, 255);
        this.colorSelected = Constants.COLOR_ORANGE;
        this.isSelected = false;
    }

    public void paint(PGraphics graphics) {
        graphics.pushMatrix();
        graphics.translate(posX, posY);
        graphics.rotate(PApplet.PI / 4);
        graphics.stroke(255);

        if (this.isSelected) {
            graphics.fill(Constants.COLOR_ORANGE.getRGB());
        } else {
            graphics.noFill();
        }

        graphics.rectMode(PApplet.CENTER);
        graphics.rect(0, 0, 128, 128);
        graphics.rectMode(PApplet.CORNER);
        graphics.popMatrix();

        graphics.fill(c.getRGB());
        graphics.textSize(18f);
        graphics.textAlign(PApplet.CENTER);
        graphics.text(this.title, posX, posY - 20);
        graphics.text("SCORE: " + this.score, posX, posY + 20);
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }
}
