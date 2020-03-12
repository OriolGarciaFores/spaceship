/* Control principal del Game.
 * Carga de settings
 * Carga de ventana
 * Carga de menu
 * carga de estados de juego
 */
import java.awt.event.KeyEvent;

// SON GLOBALES TODAS

// VARIABLES
Boolean isFull = false;
Boolean inGame = false;
Boolean over = false;
Boolean outGameOver = false;
boolean endGame = false;
boolean isSelection = false;
boolean isLvlComplete = false;

int WIDTH = 720;
int HEIGHT = 560;
int finalScore = 0;
int CENTRO_VENTANA_X = WIDTH / 2;
int CENTRO_VENTANA_Y = HEIGHT / 2;
int finalLvl = 0;

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
private final String version = "Pre-Alfa 0.2.0";

//Objetos
private GestorEstados ge;
GestorNiveles gestorNiveles;
SystemSaveData ssd;
SystemSound systemSound;

void settings() {
  //VENTANA
  if (isFull) {
    fullScreen(P2D);
    changeSizeScreen();
  } else {
    size(WIDTH, HEIGHT, P2D);
  }
  PJOGL.setIcon("icono_spaceship.png");
  noSmooth(); //no anti-aliasing
}

void setup() {
  println("Game Started");
  
  frameRate(60);
  loadScreen();
  this.ssd = new SystemSaveData();
  this.systemSound = new SystemSound(this);
  this.gestorNiveles = new GestorNiveles();
  loadStatus();
}

void draw() {
  background(0);
  changeStatus();
  ge.update();

  if (over && outGameOver) {
    loadStatus();
    over = false;
    outGameOver = false;
  }
  //showFPS(true);
}

void keyReleased() {
  KEYBOARD.keyUp(keyCode);
}

void keyPressed() {
  KEYBOARD.keyDown(keyCode);
}

private void loadStatus() {
  ge = new GestorEstados();
}

private void changeStatus() {
  if (!over) {
    if (isSelection) {
      ge.setEstado(1);
    } else {
      if (isLvlComplete) {
        ge.setEstado(4);
      } else {
        if (ge.getIndexEstadoActual() != 0 && !inGame) {
          ge.setEstado(0);
        } else if (ge.getIndexEstadoActual() != 2 && inGame) {
          ge.setEstado(2);
        }
      }
    }
  } else {
    ge.setEstado(3);
  }
}

private void loadScreen() {
  surface.setTitle("Game v. " + version);
  surface.setResizable(false);
}

private void changeSizeScreen() {
  WIDTH = displayWidth;
  HEIGHT = displayHeight;
  CENTRO_VENTANA_X = WIDTH / 2;
  CENTRO_VENTANA_Y = HEIGHT / 2;
}
