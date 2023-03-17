package core.beans.habilidades;

import core.beans.entidades.Ball;
import core.beans.entidades.Player;
import core.utils.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

public class MultiDisparo extends Habilidades {
    public ArrayList<Ball> balls;
    private Player player;

    public MultiDisparo(Player player) {
        this.pos = new PVector(0, 0);
        this.isReady = true;
        this.coldown = (8 * Constants.FRAMES);//AMPLIAR CD MUCHO
        this.lvlRequired = 3;
        this.player = player;
        this.balls = new ArrayList<Ball>();
    }

    public void update() {
        Actionhability();
        updateBalls();
        timer();
        move();
    }

    public void timer() {
        if (!this.isReady) {
            this.timer++;
            if (this.timer >= this.coldown) {
                this.isReady = true;
                this.timer = 0;
            }
        }
    }

    public void paint(PGraphics graphics) {
        if (this.isReady) {
            //DIBUJAR HABILIDAD EN LA INTERFAZ
            graphics.noFill();
            graphics.stroke(Color.decode("#F79E0C").getRGB());
            graphics.strokeWeight(2);
            graphics.ellipse(Constants.CENTRO_VENTANA_X + 30, 40, 20, 20);
        }
        paintBalls(graphics);
    }

    private void paintBalls(PGraphics graphics) {
        for (Ball ball : balls) {
            ball.paint(graphics);
        }
    }

    private void Actionhability() {
        if (this.isReady) {
            if (Constants.KEYBOARD.activeMultiShot) {
                addShoot();
                this.isReady = false;
            }
        }
    }

    private void updateBalls() {
        if (!balls.isEmpty()) {
            for (int i = balls.size() - 1; i >= 0; i--) {
                Ball ball = balls.get(i);
                ball.update();
                if (ball.isDie()) {
                    balls.remove(i);
                }
            }
        }
    }

    private void addShoot() {
        float points = 16;
        float pointAngle = 360 / points;

        for (float angle = 0; angle < 360; angle = angle + pointAngle) {
            float x = PApplet.cos(PApplet.radians(angle)) * Constants.CENTRO_VENTANA_X;
            float y = PApplet.sin(PApplet.radians(angle)) * Constants.CENTRO_VENTANA_Y;
            Ball ball = new Ball(this.pos, new PVector(x + this.pos.x, y + this.pos.y), 6);
            ball.setColor(new Color(255, 255, 255));
            this.balls.add(ball);
        }
    }

    public void move() {
        this.pos = this.player.pos;
    }

    public float getRad() {
        return 0;
    }

    public void setIsEquiped(boolean isEquiped) {
        this.isEquiped = isEquiped;
    }

    public ArrayList<Object> getObjects() {
        return new ArrayList<Object>(this.balls);
    }
}
