package core.beans.comunes;

import core.gestores.GestorNiveles;
import core.utils.*;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PShader;

public class Map {
    PShader nebula, galaxy;
    private final GestorNiveles gestorNiveles;
    private final PGraphics pGraphics;
    private final PApplet parent;

    public Map(GestorNiveles gestorNiveles, PGraphics pGraphics, PApplet parent) {
        this.gestorNiveles = gestorNiveles;
        this.pGraphics = pGraphics;
        this.parent = parent;

        init();
    }

    void init() {
        pGraphics.noStroke();
        nebula = pGraphics.loadShader("resources/nebula.glsl");
        nebula.set("resolution", Constants.WIDTH, Constants.HEIGHT);

        galaxy = pGraphics.loadShader("resources/galaxy.glsl");
        galaxy.set("resolution", Constants.WIDTH, Constants.HEIGHT);
    }

    public void paint() {
        pGraphics.noStroke();
        switch (gestorNiveles.getLevel()) {
            case 1:
                pGraphics.fill(255);
                nebula.set("time", (float) (parent.millis() / 500.0));
                pGraphics.shader(nebula);
                pGraphics.rect(0, 0, Constants.WIDTH, Constants.HEIGHT);
                pGraphics.resetShader();
                break;
            case 2:
            case 3:
            case 4:
                pGraphics.fill(255);
                galaxy.set("time", (float) (parent.millis() / 5000.0));
                pGraphics.shader(galaxy);
                pGraphics.rect(0, 0, Constants.WIDTH, Constants.HEIGHT);
                pGraphics.resetShader();
                break;
            default:
                pGraphics.background(0);
                break;
        }
    }
}
