package core.gestores;

import core.beans.comunes.EstadoJuego;
import core.beans.comunes.Map;
import core.beans.entidades.Player;
import core.utils.*;
import processing.core.PApplet;
import processing.core.PGraphics;

import static processing.core.PApplet.println;

public class GestorJuego implements EstadoJuego {
    //VARIABLES
    /*OPTIMIZABLE LA CARGA SE HACE AL INICIAR EL PROGRAMA
     *
     */

    private Map map;
    private Player player;
    private GestorEnemies gestorEnemies;
    private GestorDisparos gestorDisparos;
    private GestorColisiones gestorColisiones;
    private final PGraphics graphics;
    private final PApplet parent;

    private boolean isLoaded;

    public GestorJuego(PGraphics graphics, PApplet pApplet) {
        println("Gestor Juego inicializado");
        this.isLoaded = false;
        this.graphics = graphics;
        this.parent = pApplet;
    }

    @Override
    public void update() {
        if (!Global.over && !Global.isLvlComplete && this.isLoaded) {
            this.player.update();
            this.gestorDisparos.update();
            this.gestorEnemies.update();
            //Se podria limitar a no validar a cada actualización. cada 5 - 10?
            this.gestorColisiones.validarColisionesEnemies(this.gestorDisparos.getBalasPlayer(), this.gestorEnemies.getEnemies());
            this.gestorColisiones.validarColisionesBalasEnemies(this.gestorDisparos.getBalasEnemy());
            this.gestorColisiones.validarColisionesBallsEnemies(this.gestorDisparos.getBallsEnemies());
            this.gestorColisiones.validarColisionesBosses(this.gestorEnemies.getBosses(), this.gestorDisparos.getBalasPlayer());
            Global.gestorNiveles.updateProgress(this.player.getScore());
        }

        if (!Global.over && !Global.isLvlComplete && !this.isLoaded) init();
    }

    public void init() {
        println("Gestor Juego cargado");
        this.map = new Map(Global.gestorNiveles, graphics, parent);
        this.player = new Player(Constants.CENTRO_VENTANA_X, Constants.CENTRO_VENTANA_Y);
        this.gestorDisparos = new GestorDisparos(this.player);
        this.gestorEnemies = new GestorEnemies(this.player, this.parent, this.gestorDisparos);
        this.gestorColisiones = new GestorColisiones(this.player);
        this.isLoaded = true;
    }

    @Override
    public void paint(PGraphics graphics) {
        if (!Global.over && !Global.isLvlComplete && this.isLoaded) {
            this.map.paint();
            this.player.paint(graphics);
            this.gestorDisparos.paint(graphics);
            this.gestorEnemies.paint(graphics);
            this.player.showScore(graphics);
        }
    }

}
