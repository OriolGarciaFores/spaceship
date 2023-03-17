package core.gestores;

import core.beans.comunes.DataNivel;
import core.beans.comunes.EstadoJuego;
import core.beans.comunes.SeccionNivel;
import core.utils.*;
import processing.core.PGraphics;

import java.util.ArrayList;

public class SelectorNiveles implements EstadoJuego {
    // LOAD JSON DADAS DE LAS PARTIDAS QUE ESTEN COMPLETADAS PARA MOSTRAR EN LA SELECCION LA INFO
    //private Seccion[] secciones;
    private SeccionNivel[] secciones;

    private int position;
    private int maxPositions;

    private float keyTimer = 0;
    private final float timeFrame = (float) (0.2 * Constants.FRAMES);

    private boolean onChange = false;

    private GestorConfiguracion config;

    private ArrayList<DataNivel> datas;

    public SelectorNiveles() {
        this.config = new GestorConfiguracion();
        this.maxPositions = Integer.parseInt(this.config.getInfo("maxLevel"));
        this.secciones = new SeccionNivel[maxPositions];
        this.datas = Global.gestorSaveData.loadData();
        position = 0;
        initSections();
    }

    private void initSections() {
        if (this.datas.size() < maxPositions) {
            for (int i = 0; i < maxPositions; i++) {
                try {
                    this.datas.get(i);
                } catch (Exception ex) {
                    this.datas.add(new DataNivel(i + 1, 0));
                }
            }
        }

        int dist = 64;
        int separation = 110;
        for (int i = 0; i < maxPositions; i++) {
            this.secciones[i] = new SeccionNivel("LV " + this.datas.get(i).getLvl(), "" + this.datas.get(i).getScore(), separation, Constants.CENTRO_VENTANA_Y);
            separation += (Constants.WIDTH / 4) + dist;
        }
    }

    @Override
    public void update() {
        /*if (this.maxPositions == 1) {
            gestorNiveles.update();
            Global.isSelection = false;
            Global.inGame = true;
        } else {
            allSectionsUpdate();
            timer();
            if (onChange) {
                movePosition();
            }
        }*/
        allSectionsUpdate();
        timer();
        if (onChange) {
            movePosition();
        }
    }

    private void allSectionsUpdate() {
        for (int i = 0; i < this.secciones.length; i++) {
            if (i == position) {
                secciones[i].setSelected(true);
                actionMenu();
            } else {
                secciones[i].setSelected(false);
            }
        }
    }

    private void timer() {
        keyTimer++;
        if (keyTimer > timeFrame && (Constants.KEYBOARD.left || Constants.KEYBOARD.right)) {
            onChange = true;
            keyTimer = 0;
        } else {
            onChange = false;
        }
    }

    private void movePosition() {
        if (Constants.KEYBOARD.right && position < (maxPositions - 1)) {
            if (position >= 2) {
                for (int i = 0; i < this.secciones.length; i++) {
                    this.secciones[i].setPosX(this.secciones[i].getPosX() - (Constants.WIDTH / 4) - 64);
                }
            }
            position++;
        } else if (Constants.KEYBOARD.left && position > 0) {
            if (position >= 3) {
                for (int i = 0; i < this.secciones.length; i++) {
                    this.secciones[i].setPosX(this.secciones[i].getPosX() + (Constants.WIDTH / 4) + 64);
                }
            }
            position--;
        }
    }

    public int getSeccionActual() {
        return this.position;
    }

    @Override
    public void paint(PGraphics graphics) {
        paintSections(graphics);
    }

    private void paintSections(PGraphics graphics) {
        for (int i = 0; i < this.secciones.length; i++) {
            secciones[i].paint(graphics);
        }

        if (this.maxPositions >= 4 && position < 3 && this.maxPositions != position) {
            graphics.pushMatrix();
            graphics.translate(Constants.WIDTH - 85, Constants.CENTRO_VENTANA_Y - 100);
            graphics.triangle(70, 18.75f, 43, 0, 43, 37.5f);
            graphics.popMatrix();
        }

        if (position > 2) {
            graphics.pushMatrix();
            graphics.translate(0, Constants.CENTRO_VENTANA_Y - 100);
            graphics.triangle(15, 18.75f, 43, 0, 43, 37.5f);
            graphics.popMatrix();
        }
    }

    private void actionMenu() {
        if (Constants.KEYBOARD.enter) {
            switch (this.position) {
                case 0:
                    Global.gestorNiveles.setLevel(1);
                    Global.gestorNiveles.update();
                    Global.isSelection = false;
                    Global.inGame = true;
                    break;
                case 1:
                    Global.gestorNiveles.setLevel(2);
                    Global.gestorNiveles.update();
                    Global.isSelection = false;
                    Global.inGame = true;
                    break;
                case 2:
                    Global.gestorNiveles.setLevel(3);
                    Global.gestorNiveles.update();
                    Global.isSelection = false;
                    Global.inGame = true;
                    break;
                case 3:
                    Global.gestorNiveles.setLevel(4);
                    Global.gestorNiveles.update();
                    Global.isSelection = false;
                    Global.inGame = true;
                    break;
            }
        }
    }
}
