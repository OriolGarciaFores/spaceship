package core.gestores;

import core.beans.comunes.EstadoJuego;
import core.utils.*;
import processing.core.PApplet;
import processing.core.PGraphics;

public class GestorEstados {

    private final int ESTADO_DEFAULT = -1;
    private final int MAX_ESTADOS = 5;
    private final EstadoJuego[] ESTADOS;

    private EstadoJuego estadoActual;

    private int estado;

    public GestorEstados(PGraphics pGraphics, PApplet pApplet) {
        this.estado = ESTADO_DEFAULT;
        this.ESTADOS = new EstadoJuego[MAX_ESTADOS];

        initEstado(pGraphics, pApplet);
        initEstadoActual();
    }

    public GestorEstados(PGraphics pGraphics, PApplet pApplet, EstadoJuego estadoActual) {
        this.estado = ESTADO_DEFAULT;
        this.ESTADOS = new EstadoJuego[MAX_ESTADOS];

        initEstado(pGraphics, pApplet);
        this.estadoActual = estadoActual;
    }

    private void initEstado(PGraphics pGraphics, PApplet pApplet) {
        this.ESTADOS[0] = new GestorMenu();
        this.ESTADOS[1] = new SelectorNiveles();
        this.ESTADOS[2] = new GestorJuego(pGraphics, pApplet);
        this.ESTADOS[3] = new GestorGameOver();
        this.ESTADOS[4] = new GestorNivelCompletado(pApplet);
    }

    private void initEstadoActual() {
        this.estadoActual = ESTADOS[0];
    }

    public void cambiarEstado() {
        if (!Global.over) {
            if (Global.isSelection) {
                setEstado(1);
            } else {
                if (Global.isLvlComplete) {
                    setEstado(4);
                } else {
                    if (getIndexEstadoActual() != 0 && !Global.inGame) {
                        setEstado(0);
                    } else if (getIndexEstadoActual() != 2 && Global.inGame) {
                        setEstado(2);
                    }
                }
            }
        } else {
            setEstado(3);
        }
    }

    public void update() {
        this.estadoActual.update();
    }

    public void paint(PGraphics graphics) {
        this.estadoActual.paint(graphics);
    }

    public void setEstado(final int estado) {
        this.estadoActual = ESTADOS[estado];
        this.estado = estado;
    }

    public EstadoJuego getEstadoActual() {
        return this.estadoActual;
    }

    public int getIndexEstadoActual() {
        return this.estado;
    }

    public void resetGestorJuego() {
        //this.estados[1] = new GestorJuego();
    }
}
