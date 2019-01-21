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
Boolean isShowPositions = true;
Boolean inGame = false;
Boolean over = false;
Boolean outGameOver = false;
int finalScore = 0;
int WIDTH = 720;
int HEIGHT = 560;
int CENTRO_VENTANA_X = WIDTH / 2;
int CENTRO_VENTANA_Y = HEIGHT / 2;
boolean endGame = false;
boolean isSelection = false;

private final String version = "Pre-Alfa 0.05.0";

//Objetos
private GestorEstados ge;
GestorNiveles gestorNiveles;

void settings(){
//VENTANA
 if(isFull){
  fullScreen(P2D);
  changeSizeScreen();
 }else{
  size(WIDTH,HEIGHT,P2D);
 }
 PJOGL.setIcon("icono_spaceship.png");
 noSmooth(); //no anti-aliasing
}

void setup(){
  println("Game Started");
  frameRate(60);
  loadScreen();
  this.gestorNiveles = new GestorNiveles();
  loadStatus();
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
 if(!over){
   if(isSelection){
     ge.setEstado(1);
   }else{
     if(ge.getIndexEstadoActual() != 0 && !inGame){
       ge.setEstado(0);
     }else if(ge.getIndexEstadoActual() != 2 && inGame){
       ge.setEstado(2);
     }
   }
 }else{
    ge.setEstado(3);
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
