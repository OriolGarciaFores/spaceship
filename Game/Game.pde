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
Boolean isShowFps = true;
Boolean inGame = false;
Boolean over = false;
Boolean outGameOver = false;
int finalScore = 0;
int WIDTH = 640;
int HEIGHT = 480;
int CENTRO_VENTANA_X = WIDTH / 2;
int CENTRO_VENTANA_Y = HEIGHT / 2;
boolean endGame = false;

final String version = "Pre-Alfa 0.04.0";

//Objetos
GestorEstados ge;

void settings(){
//VENTANA
 if(isFull){
  fullScreen(P2D);
  changeSizeScreen();
 }else{
  size(WIDTH,HEIGHT,P2D);
 }
}

void setup(){
  println("Game Started");
  frameRate(60);
  loadScreen();
  loadStatus();
  noSmooth(); //no anti-aliasing
}

void draw(){
  background(0);
  changeStatus();
  ge.update();

  if(over && outGameOver){
    loadStatus();
    over = false;
    outGameOver = false;
  }
   //<>//
  if(isShowFps){
    showFPS();
  }
}

void keyReleased(){
    KEYBOARD.keyUp(keyCode); 
}

void keyPressed(){
    KEYBOARD.keyDown(keyCode);
}

private void loadStatus(){
  ge = new GestorEstados();
}

private void changeStatus(){
  if(ge.getIndexEstadoActual() != 0 && !inGame && !over){
    ge.setEstado(0);
  }else if(ge.getIndexEstadoActual() != 1 && inGame && !over){
    ge.setEstado(1);
  } else if(over && ge.getIndexEstadoActual() != 2){
    ge.setEstado(2);
  }
}

private void showFPS(){
   fill(57,255,20);
   textAlign(BASELINE);
   textSize(18);
   text("FPS: " + int(frameRate),20,20);
}

private void loadScreen(){
  surface.setTitle("Game v. " + version);
  surface.setResizable(false);
}

private void changeSizeScreen(){
  WIDTH = displayWidth;
  HEIGHT = displayHeight;
  CENTRO_VENTANA_X = WIDTH / 2;
  CENTRO_VENTANA_Y = HEIGHT / 2;
}
