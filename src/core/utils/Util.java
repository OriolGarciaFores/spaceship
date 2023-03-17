package core.utils;

import core.beans.entidades.Ball;
import core.beans.entidades.Player;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.util.ArrayList;

public class Util {

    /*
     * Metodos globales
     */

    /*
     *******************************************************
     *
     * Representa el area de los obstaculos colisionables
     *
     *******************************************************
     */
    public static void debugArea(float rad, PGraphics graphics) {
        graphics.stroke(255);
        graphics.strokeWeight(4);
        graphics.noFill();
        graphics.ellipse(0, 0, rad, rad);
    }

    public static void debugAreaPoint(float rad, float w, float h, PGraphics graphics) {
        graphics.stroke(255);
        graphics.strokeWeight(4);
        graphics.noFill();
        graphics.ellipse(w, h, rad, rad);
    }

    /*
     *******************************************************
     *
     * Calculo de margenes del mapa con los objetos o
     * ser vivos colisionables
     *
     *******************************************************
     */

    public static float range(float p, float min, float max) {
        if (p < min) {
            p = min;
        }
        if (p > max) {
            p = max;
        }
        return p;
    }

    /*
     *******************************************************
     *
     * Muestra x valores en la esquina de la ventana
     *
     *******************************************************
     */

    public static void debugValue(String typeValue, int value, int posX, int posY, PGraphics graphics) {
        graphics.fill(255);
        graphics.textSize(18);
        graphics.text(typeValue + " " + value, posX, posY);
    }

    /*
     *******************************************************
     *
     * DEBUG DATOS GENERICOS FPS, POSICIONES, ETC.
     *
     *******************************************************
     */

    public static void showFPS(boolean isShow, PGraphics graphics, PApplet pApplet) {
        if (isShow) {
            graphics.fill(57, 255, 20);
            graphics.textAlign(PConstants.BASELINE);
            graphics.textSize(18);
            graphics.text("FPS: " + (int) pApplet.frameRate, 20, 20);
        }
    }

    public static void showPositions(float posX, float posY, PGraphics graphics) {
        graphics.fill(155);
        graphics.textSize(18);
        graphics.text("POS X: " + (int) posX, 20, 40);
        graphics.text("POS Y: " + (int) posY, 20, 60);
    }

    /*
     *******************************************************
     *
     * CASTEO DE OBJETOS
     *
     *******************************************************
     */

    public static ArrayList<Ball> ObjectsToBalls(Player player) {
        ArrayList<Ball> balls = new ArrayList<Ball>();

        for (int i = 0; i < player.getHability(1).getObjects().size(); i++) {
            balls.add((Ball) player.getHability(1).getObjects().get(i));
        }

        return balls;
    }

    /*
     *******************************************************
     *
     * CALCULAR COLISION CIRCLE VS RECT
     *
     *******************************************************
     */

    public static boolean circleRectColission(float cx, float cy, float radius, float rx, float ry, float rw, float rh) {

        // temporary variables to set edges for testing
        float testX = cx;
        float testY = cy;

        // which edge is closest?
        if (cx < rx) testX = rx;      // left edge
        else if (cx > rx + rw) testX = rx + rw;   // right edge
        if (cy < ry) testY = ry;      // top edge
        else if (cy > ry + rh) testY = ry + rh;   // bottom edge

        // get distance from closest edges
        float distX = cx - testX;
        float distY = cy - testY;
        float distance = PApplet.sqrt((distX * distX) + (distY * distY));

        // if the distance is less than the radius, collision!
        if (distance <= radius) {
            return true;
        }
        return false;
    }


}
