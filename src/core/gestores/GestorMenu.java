package core.gestores;

import core.beans.comunes.EstadoJuego;
import core.beans.comunes.Seccion;
import core.utils.*;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;

public class GestorMenu extends PApplet implements EstadoJuego {

    private final Seccion[] secciones;
    private final Seccion[] seccionesControls;

    private int position;
    private final int maxPositions = 5;

    private float keyTimer = 0;
    private final float timeFrame = (float) (0.2 * Constants.FRAMES);

    private boolean onChange = false;
    private boolean isMenuControls = false;
    private boolean isMenuAudio = false;

    public GestorMenu() {
        println("GESTOR MENU INIT");
        this.secciones = new Seccion[maxPositions];
        this.position = 0;
        this.seccionesControls = new Seccion[12];
        //if(!inGame)systemSound.play(0);
        initSections();
    }

    private void initSections() {
        this.secciones[0] = new Seccion("PLAY", Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y - 120);

        Seccion survival = new Seccion("SURVIVAL", Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y - 50);
        survival.setColor(new Color(100, 100, 100));

        this.secciones[1] = survival;
        this.secciones[2] = new Seccion("AUDIO", Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y + 20);
        this.secciones[3] = new Seccion("CONTROLS", Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y + 90);
        this.secciones[4] = new Seccion("EXIT", Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y + 160);

        //MENU DE CONTROLES
        float size = 18f;

        this.seccionesControls[0] = new Seccion("-------- MOVIMIENTOS --------", Constants.CENTRO_VENTANA_X, 80, size);
        this.seccionesControls[1] = new Seccion("ARRIBA        W", Constants.CENTRO_VENTANA_X, 110, size);
        this.seccionesControls[2] = new Seccion("IZQUIERDA    A", Constants.CENTRO_VENTANA_X, 140, size);
        this.seccionesControls[3] = new Seccion("ABAJO          S", Constants.CENTRO_VENTANA_X, 170, size);
        this.seccionesControls[4] = new Seccion("DERECHA      D", Constants.CENTRO_VENTANA_X, 200, size);//start
        this.seccionesControls[5] = new Seccion("-------- DISPARAR ----------", Constants.CENTRO_VENTANA_X, 250, size);
        this.seccionesControls[6] = new Seccion("ARRIBA        ↑", Constants.CENTRO_VENTANA_X, 280, size);
        this.seccionesControls[7] = new Seccion("IZQUIERDA    →", Constants.CENTRO_VENTANA_X, 310, size);
        this.seccionesControls[8] = new Seccion("ABAJO         ↓", Constants.CENTRO_VENTANA_X, 340, size);
        this.seccionesControls[9] = new Seccion("DERECHA      ←", Constants.CENTRO_VENTANA_X, 370, size);
        this.seccionesControls[10] = new Seccion("----- HABILIDADES --------", Constants.CENTRO_VENTANA_X, 420, size);
        this.seccionesControls[11] = new Seccion("ESCUDO     Q", Constants.CENTRO_VENTANA_X, 450, size);
    }

    @Override
    public void update() {
        updateMenus();
    }

    private void updateMenus() {
        if (this.isMenuControls) {
            if (Constants.KEYBOARD.enter) {
                this.isMenuControls = false;
                delay(300);
            }
        } else if (this.isMenuAudio) {
            //systemSound.update();
            if (Constants.KEYBOARD.enter) {
                this.isMenuAudio = false;
                delay(300);
            }
        } else {
            allSectionsUpdate();
            timer();
            if (onChange) {
                movePosition();
            }
        }
    }

    private void allSectionsUpdate() {
        for (int i = 0; i < this.secciones.length; i++) {
            if (i == position) {
                actionMenu();
            }
        }
    }

    @Override
    public void paint(PGraphics graphics) {
        this.g = graphics;
        showMenus();
    }

    public int getSeccionActual() {
        return this.position;
    }

    private void paintSections() {
        for (int i = 0; i < this.secciones.length; i++) {
            secciones[i].paint(g);
            if (i == position) {
                secciones[i].selected(this.g);
            }
        }
    }

    private void timer() {
        keyTimer++;
        if (keyTimer > timeFrame && (Constants.KEYBOARD.up || Constants.KEYBOARD.down)) {
            onChange = true;
            keyTimer = 0;
        } else {
            onChange = false;
        }
    }

    private void movePosition() {
        if (Constants.KEYBOARD.down && position < (maxPositions - 1)) {
            position++;
        } else if (Constants.KEYBOARD.up && position > 0) {
            position--;
        }
    }

    private void actionMenu() {
        if (Constants.KEYBOARD.enter) {
            switch (this.position) {
                case 0:
                    Global.isSelection = true;
                    delay(300);
                    break;
                case 2:
                    this.isMenuAudio = true;
                    delay(300);
                    break;
                case 3:
                    isMenuControls = true;
                    delay(300);
                    break;
                case 4:
                    Global.isExit = true;
                    break;
            }
        }
    }

    private void showMenus() {
        if (this.isMenuControls) {
            showControls();
            textSize(24f);
            textAlign(CENTER);
            text("ENTER to exit", Constants.CENTRO_VENTANA_X, Constants.HEIGHT - 10);
        } else if (this.isMenuAudio) {
            textSize(24f);
            textAlign(CENTER);
            text("ENTER to exit", Constants.CENTRO_VENTANA_X, Constants.HEIGHT - 10);
        } else {
            paintSections();
        }
    }

    private void showControls() {
        for (int i = 0; i < this.seccionesControls.length; i++) {
            seccionesControls[i].paint(this.g);
        }
    }
}
