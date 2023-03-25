package core;

import core.gestores.*;
import processing.core.PApplet;
import processing.opengl.PJOGL;
import core.utils.*;

import java.awt.*;

public class Game extends PApplet {

    /*
     **************************************************
     *
     *  VERSION X.X.XX
     *  X. ->     VERSION FINAL.
     *  0.X.X0 -> VERSION POR NIVEL COMPLETADO DEPENDIENDO DE LA CANTIDAD DE CAMBIOS.
     *  0.0.XX -> VERSION POR MEJORAS O BUGS.
     *
     **************************************************
     */
    private final String version = "Pre-Alfa 0.3.0-SNAPSHOT";
    private GestorEstados gestorEstados;
    private final float GAME_UPDATE_FREQUENCY = 60f;
    private float lastGameUpdateTime = 0f;
    private boolean isFrequenciaSincFPS;
    private boolean isVSync = true;


    public void settings() {
        size(Constants.WIDTH, Constants.HEIGHT, P2D);
        PJOGL.setIcon("resources/icono_spaceship.png");
        noSmooth(); //no anti-aliasing
    }

    public void setup() {
        println("core.Game Started");
        int monitorRefreshRate = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();

        if (monitorRefreshRate == Constants.FRAMES) isFrequenciaSincFPS = true;
        else isFrequenciaSincFPS = false;

        if (isFrequenciaSincFPS) frameRate(Constants.FRAMES);
        else if (isVSync) frameRate(monitorRefreshRate);
        else frameRate(1000);//TODO OPCION MODIFICLABE EN MENÚ - ESTE CASO SERIA SIN LIMITE

        loadScreen();

        Global.logger = new Logger(this.g);
        Global.gestorSaveData = new GestorSaveData(this);
        Global.gestorSonido = new GestorSonido(this);
        Global.gestorNiveles = new GestorNiveles();
        gestorEstados = new GestorEstados(this.g, this);
    }

    public void draw() {
        Global.logger.inicializarLogRendimiento("Loop inicial");

        if (isFrequenciaSincFPS) {
            update();
        } else {
            float timeSinceLastUpdate = (float) (millis() / 1000.0 - lastGameUpdateTime);

            if (timeSinceLastUpdate >= 1.0 / GAME_UPDATE_FREQUENCY) {
                update();
                lastGameUpdateTime = (float) (millis() / 1000.0);
            }
        }

        background(0);
        gestorEstados.paint(this.g);
        Util.showFPS(true, this.g, this);
        Global.logger.finalizarLogRendimiento("Loop inicial");
        Global.logger.pintarLogsRendimiento();
    }

    private void loadScreen() {
        surface.setTitle("core.Game v. " + version);
        surface.setResizable(false);
        //TODO TEMPORAL - PANTALLA SECUNDARIA
        //surface.setLocation(displayWidth, 0);
    }

    private void update() {
        gestorEstados.cambiarEstado();
        gestorEstados.update();

        if (Global.over && Global.outGameOver) {
            gestorEstados = new GestorEstados(this.g, this, gestorEstados.getEstadoActual());
            Global.over = false;
            Global.outGameOver = false;
        }

        if (Global.isExit) exit();
    }

    public void keyReleased() {
        Constants.KEYBOARD.keyUp(keyCode);
    }

    public void keyPressed() {
        Constants.KEYBOARD.keyDown(keyCode);
    }
}
