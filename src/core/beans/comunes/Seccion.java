package core.beans.comunes;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;

public class Seccion {
    private String title;
    private int posX;
    private int posY;
    private float sizeText;
    private Color c;

    public Seccion(String title, int posX, int posY) {
        this.title = title;
        this.posX = posX;
        this.posY = posY;
        this.sizeText = 32f;
        this.c = new Color(255, 255, 255);
    }

    public Seccion(String title, int posX, int posY, float sizeText) {
        this.title = title;
        this.posX = posX;
        this.posY = posY;
        this.sizeText = sizeText;
        this.c = new Color(255, 255, 255);
    }

    public void update() {

    }

    public void paint(PGraphics graphics) {
        graphics.noStroke();
        graphics.fill(c.getRGB());
        graphics.textAlign(PApplet.CENTER);
        graphics.textSize(this.sizeText);
        graphics.text(this.title, posX, posY);
    }


    public void selected(PGraphics graphics) {
        graphics.fill(255);
        graphics.rect(this.posX - 150, this.posY - 15, 25, 10);
    }

    public void setColor(Color c) {
        this.c = c;
    }
}
