package core.beans.habilidades;

import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

public abstract class Habilidades {
    protected PVector pos;
    protected boolean isActive;
    protected boolean isReady;
    public boolean isEquiped; //PARA EL FUTURO SISTEMA DE INVENTARIO
    public int lvlRequired;
    protected int timer;
    protected float timerFrame;
    protected float coldown;
    protected Color c;

    public abstract void update();

    abstract void timer();

    public abstract void paint(PGraphics graphics);

    abstract float getRad();

    public abstract void setIsEquiped(boolean isEquiped);

    public abstract ArrayList<Object> getObjects();
}
