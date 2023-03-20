package core.beans.entidades.bosses;

import core.beans.entidades.Bala;
import core.beans.entidades.Player;
import core.utils.Constants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Destroyer extends Boss {

    /*
     * BOSS. Inicialmente se posicionara moviendose
     * Contendra disparos e invocara naves
     */

    private PVector hitBox = new PVector(Constants.WIDTH,Constants.HEIGHT/2);

    boolean loadShooters = false;
    private float timer;
    private final float timerFrame = (0.5f*Constants.FRAMES);

    public Destroyer(Player player, PVector pos){
        this.id = 1;
        this.pos = new PVector(pos.x, pos.y);
        this.speed = new PVector();
        this.acc = new PVector();
        this.maxSpeed = 1;
        init_monster(player);
        this.score = 50;
        this.isStarted = false;
        this.rad = 560f;
        this.radPuntoDebil = this.rad - 220f;
        this.animationDead = true;
        this.fase = 1;
        this.soloPuntoDebil = true;
        this.shieldActive = true;
        this.shield = 40;
        this.health = 40;
    }

    public void init_monster(Player player){
        setPlayer(player);
        c = new Color(255, 255, 255);
    }

    @Override
    public void updateBoss() {
        if(pos.x >= Constants.WIDTH-(Constants.WIDTH/4) && !this.isStarted){
            pos = new PVector(pos.x, 0);
        }else{
            this.isStarted = true;
            pos = new PVector(Constants.WIDTH-(Constants.WIDTH/4),0);
            updateFase();
        }
    }

    private void updateFase(){
        switch (this.fase) {
            case 1:
                this.inmortal = true;
                this.modoInvocacion = true;
                this.tiposShipsInvocacion.put(0, 50);//Determinar numeros por variables. ID, cantidad
                this.invocacionTimer = true;
                break;
            case 2:
                this.modoInvocacion = false;
                this.invocacionTimer = false;
                this.tiposShipsInvocacion = new HashMap<>();
                this.inmortal = false;
                if(this.shield <= 0) siguienteFase();
                break;
            case 3:
                this.isProyectilColisionable = false;
                break;
            case 4:
                this.isProyectilColisionable = true;
                break;
        }

        /*

         Destroyer destroyer = (Destroyer) boss;
                //FASES
                switch (destroyer.getFase()) {
                    case 1:
                        if (bornShipsInBoss < 50) {
                            shipBasicBornTimer++;
                            if (shipBasicBornTimer >= SHIP_BASIC_BORN_DIST) {
                                bornShipsInBoss++;
                                addShipBasicBoss(5);
                                shipBasicBornTimer = 0;
                            }
                        } else if (this.monstersAlive[0] == 0) {
                            destroyer.setFase(2);
                        }
                        if (!destroyer.isLoadShooters()) {
                            destroyer.setLoadShooters(true);
                            addShooterBoss();
                        }
                        break;
                    case 2:
                        if (destroyer.getShield() <= 0) {
                            destroyer.setFase(3);
                        }
                        break;
                    case 3:
                        if (this.monstersAlive[1] == 0) {
                            destroyer.setFase(4);
                            destroyer.setShield(40);
                        }
                        break;
                    case 4:
                        //DAÑO A LA NAVE
                        if (destroyer.getShield() <= 0) {
                            this.player.setScore(destroyer.score);
                            destroyer.isDie = true;
                        }
                        break;
                }
                break;



         */
    }

    @Override
    public void paint(PGraphics graphics){
        timerColor();
        graphics.noStroke();
        int w = 60;
        int h = Constants.HEIGHT-(Constants.HEIGHT/4 + Constants.HEIGHT/4);

        graphics.fill(c.getRGB());
        graphics.pushMatrix();
        graphics.translate(pos.x, pos.y);//POSICIONAMIENTO

        graphics.fill(this.puntoDebilColor.getRGB());
        graphics.ellipse(Constants.WIDTH/4,Constants.HEIGHT/2,this.rad-300f,this.rad-300f);//PUNTO DEBIL CENTRAL

        graphics.fill(255);
        graphics.triangle( Constants.WIDTH/4, 0, Constants.WIDTH/4, Constants.HEIGHT/4, 0, Constants.HEIGHT/4);
        graphics.rect((Constants.WIDTH/4)-w,Constants.HEIGHT/4,w,h);
        graphics.triangle(0, Constants.HEIGHT-(Constants.HEIGHT/4), Constants.WIDTH/4, Constants.HEIGHT-(Constants.HEIGHT/4), Constants.WIDTH/4, Constants.HEIGHT);
        graphics.fill(shieldColor.getRGB(), 50);
        graphics.stroke(c.getRGB());
        graphics.strokeWeight(4);
        if(shield > 0 && this.fase <= 3){
            graphics.stroke(shieldColor.getRGB());
            graphics.ellipse(Constants.WIDTH/4,Constants.HEIGHT/2,this.rad,this.rad);
        }

        //debugAreaPoint(this.rad-80f,WIDTH/4,HEIGHT/2);
        //debugAreaPoint(this.rad,WIDTH/4,HEIGHT/2);
        graphics.popMatrix();


    }

    public void timerColor(){
        if(this.puntoDebilColor == Constants.COLOR_DMG || this.shieldColor == Constants.COLOR_DMG) timer++;
        if(this.puntoDebilColor == Constants.COLOR_DMG && timer >= timerFrame){
            this.puntoDebilColor = Constants.COLOR_PUNTO_DEBIL;
            timer = 0;
        }

        if(this.shieldColor == Constants.COLOR_DMG && timer >= timerFrame){
            this.shieldColor = Color.decode("#9B9B9B");
            timer = 0;
        }
    }

    public int getFase(){
        return this.fase;
    }

    public void setFase(int fase){
        this.fase = fase;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield){
        this.shield = shield;
    }

    public boolean isLoadShooters() {
        return loadShooters;
    }

    public void setLoadShooters(boolean loadShooters) {
        this.loadShooters = loadShooters;
    }

    @Override
    public float getRad() {
        return !this.shieldActive ? this.rad - 80f : super.getRad();
    }

    @Override
    public PVector getPos() {
        return this.hitBox;
    }
}
