package core.beans.entidades.bosses;

import core.beans.entidades.Ball;
import core.beans.entidades.Player;
import core.gestores.GestorDisparos;
import core.utils.Constants;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DeathStar extends Boss {
    /*
     * BOSS centro disparando.
     *
     */

    private final int MAX_HEALTH = 100;
    private final float TIMER_FRAME = Constants.FRAMES / 6;
    private final float TIMER_RAGE_FRAMES = (5 * Constants.FRAMES);
    private final float TIMER_COLOR_FRAME = (0.2f * Constants.FRAMES);
    private final float TIMER_PROB_RAGE_FRAMES = (10 * Constants.FRAMES);

    private float timer;

    private int timerBurst = 0;
    private int timerBurstFrame = (5 * Constants.FRAMES);
    private int timerRage = 0;
    private int timerColor = 0;
    private int probabilityRage = 3;// 1/X
    private int timerProbRage = 0;

    private int[] numBalls = {8, 8, 12, 12, 12, 12, 16, 16, 20, 20};

    protected boolean isRage;

    private PApplet pApplet;
    private GestorDisparos gestorDisparos;
    private List<Integer> fasesVida = new ArrayList<>(Arrays.asList(60, 40, 20));

    public DeathStar(Player player, PVector pos, PApplet pApplet, GestorDisparos gestorDisparos) {
        this.pos = new PVector(pos.x, pos.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 3;
        init_monster(player);
        this.score = 50;
        this.isStarted = false;
        this.rad = 100f;
        this.isRage = false;
        this.health = MAX_HEALTH;
        this.animationDead = true;
        this.pApplet = pApplet;
        this.gestorDisparos = gestorDisparos;
        this.inmortal = true;
        this.tiposShipsInvocacion.put(2, 5);
    }

    public void init_monster(Player player) {
        setPlayer(player);
        setTarget(new PVector(Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y));
        c = new Color(169, 169, 169);
    }

    @Override
    public void updateBoss() {
        if (pos.y < Constants.CENTRO_VENTANA_Y - 2 && !this.isStarted) {
            pos = new PVector(Constants.CENTRO_VENTANA_X, pos.y);
        } else {
            this.isStarted = true;
            pos = new PVector(Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y);
            //DISPAROS EN EXPANSIVO SOLO CENTRO DE PANTALLA
            timerShot();
        }

        //MODO RAGE ES INMORTAL
        //SI HAY NAVES ES INMORTAL

        //SI NO ESTA EN MODO RAGE CONTADOR PARA VOLVER A INTENTAR A ENTRAR MODO RAGE.
        //si tiene poca vida, siempre el burs será más elevado.
        if (!this.isRage) {
            timerProbRage++;
            if (this.health <= 20) {
                this.timerBurstFrame = (2 * Constants.FRAMES);
            }
        }

        //A PARTIR DE 1/4 DE VIDA y no este en rage cada 10s hay una probabilidad de 1/3 de entrar en modo IMPORTAL y RAGE.
        //En este modo el burst es cada 0.5s
        if (MAX_HEALTH - (MAX_HEALTH / 4) >= this.health && !this.isRage && timerProbRage >= TIMER_PROB_RAGE_FRAMES && !this.inmortal) {
            boolean enter = new Random().nextInt(this.probabilityRage) == 0;
            if (enter) {
                this.inmortal = true;
                this.isRage = true;
                this.c = Constants.COLOR_RAGE;
                this.timerBurstFrame = Constants.FRAMES / 2;
            }
            this.timerProbRage = 0;
        }

        //SI ESTA EN RAGE CONTADOR PARA QUITAR EL MODO RAGE.
        //EL BURS VOLVERA A SER NORMAL.
        if (this.isRage) {
            timerRage++;
            if (timerRage >= TIMER_RAGE_FRAMES) {
                this.inmortal = false;
                this.isRage = false;
                this.c = new Color(169, 169, 169);
                this.timerRage = 0;
                this.timerBurstFrame = (5 * Constants.FRAMES);
            }
        }

        updateFase();
    }

    private void updateFase() {
        switch (this.fase) {
            case 1:
                this.inmortal = true;
                this.modoInvocacion = true;
                break;
            case 2:
                this.modoInvocacion = false;
                if(!this.isRage) this.inmortal = false;

                if (fasesVida.contains(this.health)) {
                    fasesVida.remove(0);
                    this.fase = 1;
                }
                break;
        }
    }

    @Override
    public void paint(PGraphics graphics) {
        timerColor();

        graphics.stroke(0);
        graphics.strokeWeight(2);
        graphics.fill(c.getRGB());
        graphics.pushMatrix();
        graphics.translate(pos.x, pos.y);//POSICIONAMIENTO
        graphics.ellipse(0, 0, this.rad, this.rad);

        graphics.fill(128, 128, 128);
        graphics.ellipse(0, 0, 43, 43);
        graphics.fill(169, 169, 169);

        for (int i = 0; i < 4; i++) {
            graphics.rotate(PApplet.HALF_PI * i);
            graphics.triangle(0, 0, 1 * 15f, 1 * 15f, 0, 1.41f * 15f);
        }

        graphics.strokeWeight(0);
        graphics.fill(128, 128, 128);
        graphics.square(-33, 18, 10);
        graphics.square(-40, 3, 10);
        graphics.square(-3, 24, 8);
        graphics.square(-3, -33, 8);
        graphics.square(-3, -46, 8);
        graphics.square(-15, -46, 8);
        graphics.square(10, -46, 8);
        graphics.square(-3, 37, 8);
        graphics.square(-15, 37, 8);
        graphics.square(10, 37, 8);
        graphics.square(25, 18, 10);
        graphics.square(30, 3, 10);
        graphics.square(23, -23, 18);
        graphics.square(-42, -23, 18);
        graphics.strokeWeight(2);
        graphics.fill(c.getRGB());

        graphics.line(-35, 35, 35, 35);
        graphics.line(-50, 0, 50, 0);
        graphics.line(-35, -35, 35, -35);

        if (this.inmortal) {
            graphics.fill(Constants.COLOR_INMORTAL.getRGB(), 50);
            graphics.stroke(Constants.COLOR_INMORTAL.getRGB());
            graphics.strokeWeight(4);
            graphics.ellipse(0, 0, this.rad + 20f, this.rad + 20f);
        }
        graphics.popMatrix();
        //debugValue("VIDA BOSS", this.health, 20, 90);
    }

    public void timerShot() {
        timer++;
        timerBurst++;
        if (timer >= TIMER_FRAME && timerBurst < timerBurstFrame) {
            this.gestorDisparos.addBallEnemy(new Ball(this.pos, this.player.pos, 6));
            timer = 0;
        }

        if (timerBurst >= timerBurstFrame) {
            float points = randomBalls();
            float pointAngle = 360 / points;

            for (float angle = 0; angle < 360; angle = angle + pointAngle) {
                float x = PApplet.cos(PApplet.radians(angle)) * Constants.CENTRO_VENTANA_X;
                float y = PApplet.sin(PApplet.radians(angle)) * Constants.CENTRO_VENTANA_Y;
                this.gestorDisparos.addBallEnemy(new Ball(this.pos, new PVector(x + Constants.CENTRO_VENTANA_X, y + Constants.CENTRO_VENTANA_Y), 4));
            }
            timer = -80;
            timerBurst = 0;
        }
    }

    public void timerColor() {
        if (this.c == Constants.COLOR_DMG) {
            timerColor++;
            if (timerColor >= TIMER_COLOR_FRAME) {
                if (this.isRage) {
                    this.c = Constants.COLOR_RAGE;
                } else {
                    this.c = new Color(169, 169, 169);
                }
                timerColor = 0;
            }
        }
    }

    private int randomBalls() {
        int index = (int) (pApplet.random(numBalls.length));
        return numBalls[index];
    }
}
