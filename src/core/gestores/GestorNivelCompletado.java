package core.gestores;

import core.beans.comunes.DataNivel;
import core.beans.comunes.EstadoJuego;
import core.utils.Constants;
import core.utils.Global;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;

public class GestorNivelCompletado implements EstadoJuego {
    private String title;
    private String subTitle;
    private String description;

    private final float bornTimeFrame = 0.2f * Constants.FRAMES;
    private final float animationTimeFrame = 0.5f * Constants.FRAMES;

    private int bornTimer;
    private final int sizeTitle;
    private int positionChar;
    private int animationTimer;
    private int contador = 0;

    private Color cSubtitle;
    private PApplet pApplet;

    public GestorNivelCompletado(PApplet pApplet) {
        this.title = "";
        this.bornTimer = 0;
        this.subTitle = "";
        this.sizeTitle = ("Complete").length();
        this.positionChar = 0;
        this.animationTimer = 0;
        this.cSubtitle = new Color(255, 255, 255);
        this.pApplet = pApplet;
    }

    @Override
    public void update() {
        if (!Global.gestorSaveData.isSaved) {
            Global.gestorSonido.beforeStop();
            //SAVE DATA JSON
            Global.gestorSaveData.update(new DataNivel(Global.finalLvl, Global.finalScore));
            Global.gestorSaveData.isSaved = true;
        }

        if (this.title.length() != this.sizeTitle) {
            timer();
        } else {
            if (Constants.KEYBOARD.enter) {
                //NEXT LVL
                Global.gestorSaveData.isSaved = false;
                Global.isLvlComplete = false;
                if (Global.finalLvl >= Constants.MAX_LVLS) {
                    //RELOAD JSON ?
                    Global.gestorSonido.play(0);
                    Global.isSelection = true;
                    pApplet.delay(300);
                } else {
                    Global.gestorNiveles.update();
                }
            }
        }
    }

    @Override
    public void paint(PGraphics graphics) {
        //background(0);
        graphics.fill(255);
        graphics.textAlign(pApplet.CENTER);
        graphics.textSize(32f);
        graphics.text(this.title, Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y - 200);

        if (!this.subTitle.equals("")) {
            if (Global.finalLvl >= Constants.MAX_LVLS) {
                graphics.text("END DEMO", Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y - 30);
            }
            newHability(graphics);
            graphics.fill(255);
            graphics.textSize(32f);
            graphics.text("SCORE: " + Global.finalScore, Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y + 50);
            animationSubtitle();
            graphics.fill(cSubtitle.getRGB());
            graphics.textSize(18f);
            graphics.text(this.subTitle, Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y + 80);
        }
    }

    private void newHability(PGraphics graphics) {
        switch (Global.finalLvl) {
            case 1:
                graphics.strokeWeight(2);
                graphics.stroke(255);
                graphics.noFill();
                graphics.ellipse(Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y - 140, 30, 30);
                graphics.textSize(18f);
                description = "Nueva habilidad desbloqueada!\n Ahora al pulsar Q podrás activar un escudo que evitaras\n ser golpeado por proyectiles durante unos segundos.";
                graphics.text(this.description, Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y - 100);
                break;
            case 2:
                graphics.strokeWeight(2);
                graphics.stroke(Color.decode("#FF5500").getRGB());
                graphics.noFill();
                graphics.ellipse(Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y - 140, 30, 30);
                graphics.textSize(18f);
                description = "Nueva habilidad desbloqueada!\n Ahora al pulsar E podrás disparar una onda expansiva de balas.";
                graphics.text(this.description, Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y - 100);
                break;
            case 4:
                //NUEVA HABILIDAD LASER????
                break;
        }
    }

    private void timer() {
        bornTimer++;
        if (this.bornTimeFrame < this.bornTimer) {
            this.positionChar++;
            switch (this.positionChar) {
                case 1:
                    this.title = "C";
                    break;
                case 2:
                    this.title += "O";
                    break;
                case 3:
                    this.title += "M";
                    break;
                case 4:
                    this.title += "P";
                    break;
                case 5:
                    this.title += "L";
                    break;
                case 6:
                    this.title += "E";
                    break;
                case 7:
                    this.title += "T";
                    break;
                case 8:
                    this.title += "E";
                    break;
            }
            bornTimer = 0;
        }

        if (this.title.length() == this.sizeTitle) {
            this.subTitle = "ENTER to next level " + (Global.finalLvl + 1);
        }
    }

    private void animationSubtitle() {
        this.animationTimer++;
        if (this.animationTimer > this.animationTimeFrame) {
            this.contador++;

            if (this.contador % 2 == 0) {
                this.contador = 0;
                cSubtitle = new Color(0, 0, 0);
            } else {
                cSubtitle = new Color(255, 255, 255);
            }
            this.animationTimer = 0;
        }
    }
}
