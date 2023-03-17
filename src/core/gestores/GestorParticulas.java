package core.gestores;

import core.beans.entidades.Particle;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;

public class GestorParticulas {
    ArrayList<Particle> particles;
    PVector origin;

    public GestorParticulas(PVector position) {
        this.origin = position.copy();
        particles = new ArrayList<Particle>();
    }

    public void addParticle(int i, Color c, PApplet pApplet) {
        for (int y = 0; y < i; y++) {
            particles.add(new Particle(origin, c, pApplet));
        }
    }

    public void update() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update();
            if (p.isDead()) {
                particles.remove(i);
            }
        }
    }

    public void paint(PGraphics graphics) {
        for (Particle particle : particles) {
            particle.paint(graphics);
        }
    }

    public boolean isEmpty() {
        if (particles.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
