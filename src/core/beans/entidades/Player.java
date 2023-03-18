package core.beans.entidades;

import core.beans.habilidades.Escudo;
import core.beans.habilidades.Habilidades;
import core.beans.habilidades.MultiDisparo;
import core.utils.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Player {
    public PVector pos;
    private PVector speed;
    private PVector acc;

    private float maxSpeed;
    public final float r = 30f;
    //final float radShield = r+20f;

    private boolean isAuto;

    private int score;
    private int targetAutoX;
    private int targetAutoY;
    private int health;
    private final int MAX_HABILITIES = 2;
    private int lvl;

    private Habilidades[] habilidades;

    public Player(int x, int y) {
        pos = new PVector(x, y);
        this.speed = new PVector(0, 0);
        this.acc = new PVector(0, 0);
        this.maxSpeed = 5;
        this.score = 0;
        this.health = 1;
        this.isAuto = false;
        this.targetAutoX = Constants.CENTRO_VENTANA_X;
        this.targetAutoY = Constants.CENTRO_VENTANA_Y;
        this.lvl = Global.gestorNiveles.getLevel();
        this.habilidades = new Habilidades[MAX_HABILITIES];
        addHabilities();
    }

    public void update() {
        if (!this.isAuto) {
            direction();
            move();
        } else {
            autoMove();
        }
        validateHabilities();
        updateHabilities();
    }

    public void paint(PGraphics graphics) {
        graphics.pushMatrix();
        graphics.translate(pos.x, pos.y);
        graphics.rotate(PApplet.PI / 4);
        body(graphics);
        graphics.popMatrix();
        //Util.showPositions(this.pos.x, this.pos.y, graphics);
        paintHabilidades(graphics);
    }

    private void body(PGraphics graphics) {
        graphics.noStroke();
        graphics.fill(Constants.COLOR_ORANGE.getRGB());
        graphics.rect(-10, -10, 20, 20);
    }

    private void paintHabilidades(PGraphics graphics) {
        if (Global.gestorNiveles.getLevel() >= 2) {
            for (int i = 0; i < habilidades.length; i++) {
                if (habilidades[i].isEquiped) habilidades[i].paint(graphics);
            }
        }
    }

    private void move() {
        if (this.acc.x == 0 && this.acc.y == 0) {
            speed.mult(0.9f);
        } else {
            speed.add(this.acc);
            speed.limit(maxSpeed);
        }
        pos.add(speed);
        collision();
    }

    private void direction() {
        PVector direction = new PVector(0, 0);

        if (Constants.KEYBOARD.up) {
            direction.add(new PVector(0, -1));
        }
        if (Constants.KEYBOARD.left) {
            direction.add(new PVector(-1, 0));
        }
        if (Constants.KEYBOARD.down) {
            direction.add(new PVector(0, 1));
        }
        if (Constants.KEYBOARD.right) {
            direction.add(new PVector(1, 0));
        }

        this.acc = direction;
    }

    public void autoMove() {
        PVector findTarget = new PVector(this.targetAutoX - this.pos.x + 2, this.targetAutoY - this.pos.y + 2);

        if (PApplet.abs((int) findTarget.x) >= 0 && PApplet.abs((int) findTarget.x) <= 4 && PApplet.abs((int) findTarget.y) <= 4 && PApplet.abs((int) findTarget.y) >= 0) {
            return;
        }

        this.acc.add(findTarget);

        this.speed.add(this.acc);
        this.speed.limit(this.maxSpeed);

        this.pos.add(this.speed);

        this.acc = new PVector();
    }

    private void collision() {
        float x = Util.range(this.pos.x, r / 2, Constants.WIDTH - r / 2);
        float y = Util.range(this.pos.y, r / 2, Constants.HEIGHT - r / 2);

        this.pos = new PVector(x, y);
    }

    public void setScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return this.score;
    }

    public void showScore(PGraphics graphics) {
        graphics.fill(255);
        graphics.textSize(18f);
        graphics.text("SCORE: " + this.score, Constants.CENTRO_VENTANA_X - 10, 20);
    }

    //OPTIMIZABLE
    public void updateHabilities() {
        if (Global.gestorNiveles.getLevel() >= 2) {
            for (int i = 0; i < habilidades.length; i++) {
                if (!habilidades[i].isEquiped) {
                    if (habilidades[i].lvlRequired <= Global.gestorNiveles.getLevel()) {
                        habilidades[i].setIsEquiped(true);
                    } else {
                        continue;
                    }
                }
                habilidades[i].update();
            }
        }
    }

    void addHabilities() {
        habilidades[0] = new Escudo(this);
        habilidades[1] = new MultiDisparo(this);
    }

    void validateHabilities() {
        if (Global.gestorNiveles.getLevel() != this.lvl) {
            this.lvl = Global.gestorNiveles.getLevel();
            addHabilities();
        }
    }

    public Habilidades getHability(int id) {
        return habilidades[id];
    }

    public void setAutoMove(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public boolean getIsAutoMove() {
        return this.isAuto;
    }

    public void setTargetAutoX(int x) {
        this.targetAutoX = x;
    }

    public void setTargetAutoY(int y) {
        this.targetAutoY = y;
    }

    public void reset() {
        pos = new PVector(Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y);
        this.speed = new PVector(0, 0);
        this.acc = new PVector(0, 0);
        this.score = 0;
        this.isAuto = false;
        this.targetAutoX = Constants.CENTRO_VENTANA_X;
        this.targetAutoY = Constants.CENTRO_VENTANA_Y;
        this.habilidades = new Habilidades[MAX_HABILITIES];
        addHabilities();
    }

    public void decreaseLife() {
        if(getHability(0).isActive()) return;

        if ((this.health - 1) <= 0) {
            this.health = 0;
        } else {
            this.health -= 1;
        }
        if (this.health == 0) {
            Global.finalScore = this.score;
            Global.over = true;
        }
    }


}
