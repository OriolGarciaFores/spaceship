package core.beans.entidades;

import core.gestores.GestorDisparos;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import core.utils.*;

import java.awt.*;

public class Shooter extends Enemy {
    /*
     * MONSTRUO NO MOVIL, QUE DISPARARA EN UNA UNICA DIRECCION QUE SE DETERMINARA POR UN RANDOM DE 8.
     */

    private GestorDisparos gestorDisparos;
    private float timer;
    private final float timerFrame = (0.5f * Constants.FRAMES);

    private int timerColor = 0;
    private final float timerColorFrame = (0.2f * Constants.FRAMES);
    private int timerShootEnemy = 0;
    private final float shootDistEnemy = Constants.FRAMES / 2;

    private int timerChangeTarget = 0;
    private final float changeTargetFrame = Constants.FRAMES;

    private final String[] targetDirect = {"L", "R", "U", "D", "UL", "UR", "DL", "DR"};
    private String direct;

    private PApplet pApplet;

    public Shooter(Player player, PVector pos, GestorDisparos gestorDisparos, PApplet pApplet) {
        this.pos = new PVector(pos.x, pos.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 0;
        init_monster(player);
        this.score = 5;
        this.health = 1;
        this.gestorDisparos = gestorDisparos;
        this.isMovil = false;
        this.id = 1;
        this.rad = Constants.ENEMY_SHOOTER_RAD;
        this.pApplet = pApplet;
        this.direct = randomTarget();
    }

    public Shooter(Player player, PVector pos, int health, GestorDisparos gestorDisparos, PApplet pApplet) {
        this.pos = new PVector(pos.x, pos.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 0;
        init_monster(player);
        this.score = 0;
        this.health = health;
        this.gestorDisparos = gestorDisparos;
        this.rad = 35f;
        this.isMovil = false;
        this.isFollower = false;
        this.id = 1;
        this.pApplet = pApplet;
        this.direct = randomTarget();
    }

    public void init_monster(Player player) {
        setPlayer(player);
        c = Color.decode("#FAD91C");
    }

    public void paint(PGraphics graphics) {
        timerColor();
        graphics.fill(c.getRGB());
        graphics.strokeWeight(4);
        graphics.stroke(c.getRGB());

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

    @Override
    public void timerShot() {
        if (this.inmortal) return;

        timerChangeTarget++;
        if (timerChangeTarget >= changeTargetFrame) {
            this.direct = randomTarget();
        }

        timerShootEnemy++;
        if (timerShootEnemy >= shootDistEnemy) {
            this.gestorDisparos.addBalaEnemy(this.pos, this.direct);
            timerShootEnemy = 0;
        }

    }

    private String randomTarget() {
        int index = (int) (this.pApplet.random(targetDirect.length));

        return targetDirect[index];
    }
}
