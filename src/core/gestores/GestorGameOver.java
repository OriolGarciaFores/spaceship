package core.gestores;

import core.beans.comunes.EstadoJuego;
import core.utils.Constants;
import core.utils.Global;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;

public class GestorGameOver implements EstadoJuego {
    private String title;
    private String subTitle;

    private final float bornTimeFrame = 0.2f * Constants.FRAMES;
    private final float animationTimeFrame = 0.5f * Constants.FRAMES;

    private int bornTimer;
    private final int sizeTitle;
    private int positionChar;
    private int animationTimer;
    private int contador = 0;

    private Color cSubtitle;

    public GestorGameOver() {
        this.title = "";
        this.bornTimer = 0;
        this.subTitle = "";
        this.sizeTitle = ("GAME OVER").length();
        this.positionChar = 0;
        this.animationTimer = 0;
        this.cSubtitle = new Color(255, 255, 255);
    }

    @Override
    public void update() {
        //systemSound.beforeStop();
        if (this.title.length() != this.sizeTitle) {
            timer();
        } else {
            if (Constants.KEYBOARD.space) {
                Global.outGameOver = true;
                Global.gestorNiveles.update();
            }
        }
    }

    @Override
    public void paint(PGraphics graphics) {
        graphics.background(0);
        graphics.fill(255);
        graphics.textAlign(PApplet.CENTER);
        graphics.textSize(32f);
        graphics.text(this.title, Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y);

        if (!this.subTitle.equals("")) {
            graphics.text("SCORE: " + Global.finalScore, Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y + 50);
            //TEMPORAL
            if (Global.endGame) {
                graphics.text("END DEMO", Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y + 120);
            }
            animationSubtitle();
            graphics.fill(cSubtitle.getRGB());
            graphics.textSize(18f);
            graphics.text(this.subTitle, Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y + 80);
        }
    }

    private void timer() {
        bornTimer++;
        if (this.bornTimeFrame < this.bornTimer) {
            this.positionChar++;
            switch (this.positionChar) {
                case 1:
                    this.title = "G";
                    break;
                case 2:
                    this.title += "A";
                    break;
                case 3:
                    this.title += "M";
                    break;
                case 4:
                    this.title += "E ";
                    break;
                case 5:
                    this.title += "O";
                    break;
                case 6:
                    this.title += "V";
                    break;
                case 7:
                    this.title += "E";
                    break;
                case 8:
                    this.title += "R";
                    break;
            }
            bornTimer = 0;
        }

        if (this.title.length() == this.sizeTitle) {
            this.subTitle = "Space to restart";
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
