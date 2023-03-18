package core.beans.entidades;

import core.gestores.GestorDisparos;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import core.utils.*;

import java.awt.*;

public class Bomba extends Enemy {
    private float timer;
    private final float timerFrame = (0.5f * Constants.FRAMES);

    private int timerColor = 0;
    private int timerFases = 0;
    private int fase = 1;

    private final float timerColorFrame = (0.2f * Constants.FRAMES);
    private final Color COLOR_BOMB = Color.decode("#00FFD2");

    private boolean isDestroy;

    private GestorDisparos gestorDisparos;

    public Bomba(Player player, PVector pos, GestorDisparos gestorDisparos) {
        this.pos = new PVector(pos.x, pos.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 0;
        init_monster(player);
        this.score = 2;
        this.health = 5;
        this.isMovil = false;
        this.id = 4;
        this.rad = Constants.BOMBA_RAD;
        this.isDestroy = false;
        this.animationDestroy = false;
        this.gestorDisparos = gestorDisparos;
    }

    public void init_monster(Player player) {
        setPlayer(player);
        c = COLOR_BOMB;
    }

    @Override
    public void update() {
        super.update();
        timer();
    }

    private void timer() {
        if (this.c == Constants.COLOR_DMG) {
            timer++;
            if (timer >= timerFrame) {
                this.c = COLOR_BOMB;
                timer = 0;
            }
        }

        if (this.inmortal) {
            this.timerColor++;
            if (this.timerColor > this.timerColorFrame) {
                if (this.c == COLOR_BOMB) {
                    this.c = Constants.COLOR_INMORTAL;
                } else {
                    this.c = COLOR_BOMB;
                }
                this.timerColor = 0;
            }
        } else if (this.c == Constants.COLOR_INMORTAL) {
            this.c = COLOR_BOMB;
        } else {
            //TIMER CAMBIAR COLORES PARA DESTURIR
            timerFases++;
            switch (fase) {
                case 1:
                    changeFase(COLOR_BOMB, 2);
                    break;
                case 2:
                    changeFase(Color.decode("#00ED1D"), 3);
                    break;
                case 3:
                    changeFase(Color.decode("#CDEA11"), 4);
                    break;
                case 4:
                    changeFase(Color.decode("#FA9005"), 5);
                    break;
                case 5:
                    changeFase(Color.decode("#FA2A05"), 6);
                    break;
                case 6:
                    this.isDie = true;
                    break;

            }
        }
    }

    public void paint(PGraphics graphics) {
        //timerColor();
        graphics.fill(c.getRGB());
        graphics.strokeWeight(2);
        graphics.stroke(0);

        graphics.pushMatrix();
        graphics.translate(this.pos.x, this.pos.y);

        for (int i = 0; i < 8; i++) {
            graphics.rotate(graphics.QUARTER_PI * i);
            graphics.triangle(0, 0, this.rad / 2, this.rad / 2, 0, this.rad / 2);
        }
        //debugArea(rad);
        graphics.popMatrix();
    }

    @Override
    protected void mecanicaAfterDie() {
        explosion();
    }

    private void addBalls() {
        float points = 16;
        float pointAngle = 360 / points;

        for (float angle = 0; angle < 360; angle = angle + pointAngle) {
            float x = PApplet.cos(PApplet.radians(angle)) * Constants.CENTRO_VENTANA_X;
            float y = PApplet.sin(PApplet.radians(angle)) * Constants.CENTRO_VENTANA_Y;
            Ball ball = new Ball(this.pos, new PVector(x + this.pos.x, y + this.pos.y), 4);
            ball.setColor(Color.decode("#FAD91C"));
            this.gestorDisparos.addBallEnemy(ball);
        }
    }

    private void changeFase(Color c, int fase) {
        if (timerFases >= Constants.FRAMES) {
            this.c = c;
            this.fase = fase;
            timerFases = 0;
        }
    }

    private void explosion() {
        if (this.fase == 6) this.score = 0;
        addBalls();
    }

}
