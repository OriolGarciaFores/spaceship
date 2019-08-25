import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.event.KeyEvent; 
import java.util.Properties; 
import java.io.IOException; 
import java.nio.file.Path; 
import java.nio.file.Paths; 
import java.nio.file.Files; 
import java.io.OutputStream; 
import java.io.FileOutputStream; 
import java.io.FileInputStream; 
import java.io.InputStream; 
import java.util.Random; 
import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Game extends PApplet {

/* Control principal del Game.
 * Carga de settings
 * Carga de ventana
 * Carga de menu
 * carga de estados de juego
 */


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
private final String version = "Pre-Alfa 0.1.4";

//Objetos
private GestorEstados ge;
GestorNiveles gestorNiveles;
SystemSaveData ssd;
SystemSound systemSound;

public void settings() {
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

public void setup() {
  println("Game Started");
  frameRate(60);
  loadScreen();
  this.ssd = new SystemSaveData();
  this.systemSound = new SystemSound(this);
  this.gestorNiveles = new GestorNiveles();
  loadStatus();
}

public void draw() {
  background(0);
  changeStatus();
  ge.update();

  if (over && outGameOver) {
    loadStatus();
    over = false;
    outGameOver = false;
  }
  showFPS(true);
}

public void keyReleased() {
  KEYBOARD.keyUp(keyCode);
}

public void keyPressed() {
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
class Bala {
  private PVector pos;
  private String direct;
  private int speed;
  private int c;
  private boolean isDie;

  float rad = 8f;

  public Bala(PVector loc, String direc) {
    this.pos = new PVector(loc.x, loc.y);
    this.speed = 8;
    this.c = color(190);
    this.direct = direc;
    this.isDie = false;
  }

  public Bala(PVector loc, String direc, int c) {
    this.pos = new PVector(loc.x, loc.y);
    this.speed = 8;
    this.c = c;
    this.direct = direc;
    this.isDie = false;
  }

  public void update() {
    move();
    paint();
    calDie();
  }

  public void paint() {
    pushMatrix();
    translate(pos.x, pos.y);
    strokeWeight(4);
    stroke(c);
    fill(c);
    rect(-2, -2, 5, 5);
    //debugArea(rad);
    popMatrix();
  }

  private void move() {      
    switch(this.direct) {
    case "U":
      pos.y -= speed;
      break;
    case "L":
      pos.x -= speed;
      break;
    case "D":
      pos.y += speed;
      break;
    case "R":
      pos.x += speed;
      break;
    case "UL":
      pos.x -= speed;
      pos.y -= speed;
      break;
    case "UR":
      pos.x += speed;
      pos.y -= speed;
      break;
    case "DL":
      pos.x -= speed;
      pos.y += speed;
      break;
    case "DR":
      pos.x += speed;
      pos.y += speed;
      break;
    }
  }

  private void calDie() {
    if (this.pos.y <=0 || this.pos.x <= 0 || this.pos.y >= HEIGHT || this.pos.x >= WIDTH) {
      isDie = true;
    }
  }

  public boolean isDie() {
    return this.isDie;
  }
}
class Ball {
  PVector pos, direction;
  float rotation, speed;
  private float rad = 20f;
  private int c = color(0xffF79E0C);
  private boolean isDie = false;

  Ball(PVector pos, PVector direction, float speed) {
    this.pos = new PVector(pos.x, pos.y);
    this.direction = new PVector(direction.x, direction.y);
    rotation = atan2(this.direction.y - this.pos.y, this.direction.x - this.pos.x) / PI * 180;
    this.speed = speed;
  }

  public void update() {
    calPos();
    calDie();
    paint();
  }

  private void calPos() {
    this.pos.x = this.pos.x + cos(rotation/180*PI)*speed;
    this.pos.y = this.pos.y + sin(rotation/180*PI)*speed;
  }

  public void paint() {
    pushMatrix();
    translate(this.pos.x, this.pos.y);
    fill(c);
    noStroke();
    ellipse(0, 0, this.rad, this.rad);
    //debugArea(rad);
    popMatrix();
  }

  private void calDie() {
    if (this.pos.y <=0 || this.pos.x <= 0 || this.pos.y >= HEIGHT || this.pos.x >= WIDTH) {
      this.isDie = true;
    }
  }
  
  public void setColor(int c){
    this.c = c;
  }
  
  public void setRad(float rad){
    this.rad = rad;
  }
}
class Bomb extends Monster {

  private float timer;
  private final float timerFrame = (0.5f*FRAMES);

  private int timerColor = 0;
  private int timerFases = 0;
  private int fase = 1;
  private int killScore = 8;
  private int destroyScore = 3;
  
  private final float timerColorFrame = (0.2f*FRAMES);
  private final int COLOR_BOMB = color(0xff00FFD2);

  private boolean isDestroy;

  private ArrayList<Ball> balls;

  public Bomb(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 0;
    init_monster(player);
    this.score = 0;// TIENE 2 SCORES ESTE ENEMIGO.
    this.health = 5;
    this.isMovil = false;
    this.id = 4;
    this.rad = MONSTER_BOMB_RAD;
    this.isDestroy = false;
    this.balls = new ArrayList<Ball>();
    this.animationDestroy = false; //Animacion de particulas al morir.
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = COLOR_BOMB;
  }

  @Override
    public void updatePaint(ArrayList<Bala> balas) {
    //pos = new PVector(pos.x, pos.y);
    if (!this.inmortal) {
      colision(balas);
    }
    dieAfterDestroy();
    if (!this.isDestroy) {
      paint();
    }
  }

  public void paint() {
    timerColor();
    fill(c);
    strokeWeight(2);
    stroke(0);

    pushMatrix();
    translate(this.pos.x, this.pos.y);

    for (int i = 0; i<8; i++) {
      rotate(QUARTER_PI*i);
      triangle(0, 0, this.rad/2, this.rad/2, 0, this.rad/2);
    }
    //debugArea(rad);
    popMatrix();
  }

  public void timerColor() {
    if (this.c == COLOR_DMG) {
      timer++;
      if (timer >= timerFrame) {
        this.c = COLOR_BOMB;
        timer = 0;
      }
    }

    if (this.inmortal) {
      this.timerColor++;
      if (this.timerColor > this.timerColorFrame) {
        if (this.c == COLOR_BOMB) {
          this.c = COLOR_INMORTAL;
        } else {
          this.c = COLOR_BOMB;
        }
        this.timerColor = 0;
      }
    } else if (this.c == COLOR_INMORTAL) {
      this.c = COLOR_BOMB;
    } else {
      //TIMER CAMBIAR COLORES PARA DESTURIR
      timerFases++;
      switch(fase) {
      case 1:
        changeFase(COLOR_BOMB, 2);
        break;
      case 2:
        changeFase(0xff00ED1D, 3);
        break;
      case 3:
        changeFase(0xffCDEA11, 4);
        break;
      case 4:
        changeFase(0xffFA9005, 5);
        break;
      case 5:
        changeFase(0xffFA2A05, 6);
        break;
      case 6:
        explosion();
      break;
        
      }
    }
  }

  public void colision(ArrayList<Bala> balas) {
    //INTERACCIONA CON EL PLAYER
    if (!this.isDie && this.balls.isEmpty() && !this.isDestroy) {

      if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+rad/2) {
        this.player.decreaseLife();
      }

      int i = 0;
      while (!this.isDestroy && i < balas.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+rad/2) {
          balas.get(i).isDie = true;
          this.health--;
          if (this.health <= 0) {
            //NO MUERE DEL TODO.
            this.score = this.killScore;
            this.isDestroy = true;
            addBalls();
          }
          this.c = COLOR_DMG;
        }
        i++;
      }

      if (this.player.getHability(1).isEquiped) {
        ArrayList<Ball> balls = ObjectsToBalls(this.player);
        int ind = 0;
        while (!this.isDestroy && ind < balls.size()) {
          //INTERSECCION ENTRE BALA Y BICHO
          if (PVector.dist(this.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+rad/2) {
            balls.get(ind).isDie = true;
            this.health--;
            if (this.health <= 0) {
              //NO MUERE DEL TODO.
              this.score = this.killScore;
              this.isDestroy = true;
              addBalls();
            }
            this.c = COLOR_DMG;
          }
          ind++;
        }
      }
    } else if (!this.balls.isEmpty()) {
      //VALIDAR BALLS DEL ENEMIGO AUN ESTANDO MUERTO.
      if (this.player.getHability(0).isActive) {//OPTIMIZAR -> PELIGRO DE ERROR
        int ind = 0;
        while (!over && ind < balls.size()) {
          //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER CON ESCUDO
          if (PVector.dist(this.player.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+this.player.getHability(0).getRad()/2) {
            balls.get(ind).isDie = true;
          }
          ind++;
        }
      } else {
        int ind = 0;
        while (!over && ind < this.balls.size()) {
          //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
          if (PVector.dist(this.player.pos, this.balls.get(ind).pos)<=this.balls.get(ind).rad/2+this.player.r/2) {
            this.balls.get(ind).isDie = true;
            this.player.decreaseLife();
          }
          ind++;
        }
      }
    }
  }

  private void addBalls() {
    float points = 16;
    float pointAngle = 360/points;

    for (float angle = 0; angle < 360; angle = angle+pointAngle) {
      float x = cos(radians(angle)) * CENTRO_VENTANA_X;
      float y = sin(radians(angle)) * CENTRO_VENTANA_Y;
      Ball ball = new Ball(this.pos, new PVector(x+this.pos.x, y+this.pos.y), 4);
      ball.setColor(color(0xffFAD91C));
      this.balls.add(ball);
    }
  }

  private void dieAfterDestroy() {
    if (!this.isDie && this.isDestroy && this.balls.isEmpty()) {
      this.isDie = true;
    } else {
      updateBalls();
    }
  }

  private void updateBalls() {
    for (int i = 0; i < balls.size(); i++) {
      Ball ball = balls.get(i);
      ball.update();
      if (ball.isDie) {
        balls.remove(i);
      }
    }
  }

  private void changeFase(int c,int fase) {
    if (timerFases >= FRAMES) {
      this.c = c;
      this.fase = fase;
      timerFases = 0;
    }
  }
  
  private void explosion(){
    if (timerFases >= FRAMES) {
      this.score = this.destroyScore;
      timerFases = 0;
      this.isDestroy = true;
      addBalls();
    }
  }
}










class Configuration extends Properties {

  public Configuration() {
    createSave();
  }

  //SI YA EXISTE EL FICHERO NO SE SOBRESCRIBE.
  private void createSave() {
    File directory = new File(ROUTE_DIRECTORY_CONFIG);
    directory.mkdir();
    Path path = Paths.get(ROUTE_CONFIG);
    try {
      Files.createFile(path);
      update("maxLevel", "1");
    } 
    catch(IOException ex) {
      println("Error file is exist.");
    }
  }

  public void update(String keyProperty, String valueProperty) {
    try {
      OutputStream output = new FileOutputStream(ROUTE_CONFIG);

      setProperty(keyProperty, valueProperty);
      store(output, null);
    } 
    catch(IOException io) {
      println("ERROR WRITE FILE");
    }
  }

  public String getInfo(String property) {
    InputStream input = null;
    String value = "";

    try {
      input = new FileInputStream(ROUTE_CONFIG);

      this.load(input);

      value = getProperty(property);
    } 
    catch(IOException ex) {
      println("ERROR GET PROPERTY");
    } 
    finally {
      if (input != null) {
        try {
          input.close();
        } 
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return value;
  }
}
final int FRAMES = 60;
final int MAX_LVLS = 3;

final GestorControles KEYBOARD = new GestorControles();
final String USER = System.getProperty ("user.home");

//COLORS CODE
final int WHITE = 255;
final int COLOR_DMG = 0xffD64C16;
final int COLOR_RAGE = 0xffCB1010;
final int COLOR_INMORTAL = 0xffECAAFF;
final int COLOR_ORANGE = color(230, 100, 50);


//RADIO ENEMIGOS
final float MONSTER_EASY_RAD = 20f;
final float MONSTER_SHOT_RAD = 20f;
final float METEORITO_RAD = 80f;
final float MONSTER_WIFI_RAD = 40f;
final float MONSTER_BOMB_RAD = 80f;
final float SHOOTER_V2_RAD = 30f;
final float BOSS_V3_RAD = 50F;

//FILES
//final String ROUTE_SAVE = USER + "/Documents/Spaceship/Save/data_dev.json";
final String ROUTE_SAVE = USER + "/Documents/Spaceship/Save/data.json";
final String ROUTE_DIRECTORY_CONFIG = USER + "/Documents/Spaceship/configuration";
//final String ROUTE_CONFIG = USER + "/Documents/Spaceship/configuration/config_dev.properties";
final String ROUTE_CONFIG = USER + "/Documents/Spaceship/configuration/config.properties";
class DataLvl{
  private int lvl;
  private int score;
  
  DataLvl(int lvl, int score){
    this.lvl = lvl;
    this.score = score;
  }
  
  public void setLvl(int lvl){
    this.lvl = lvl;
  }
  
  public void setScore(int score){
    this.score = score;
  }
  
  public int getLvl(){
    return this.lvl;
  }
  
  public int getScore(){
    return this.score;
  }
}
public interface EstadoJuego{
  public void update();
}
class GestorControles{
  
 Boolean left = false;
 Boolean right = false;
 Boolean up = false;
 Boolean down = false;
 Boolean enter = false;
 Boolean space = false;
 
 Boolean shotUp = false;
 Boolean shotDown = false;
 Boolean shotLeft = false;
 Boolean shotRight = false;
 Boolean activeShield = false;
 Boolean activeMultiShot = false;
 
 Boolean isRun = false;
 
 public void keyDown(int p_key){
   
   switch(p_key){
     case KeyEvent.VK_A:
       left = true;
     break;
     case KeyEvent.VK_D:
       right = true;
     break;
     case KeyEvent.VK_W:
       up = true;
     break;
     case KeyEvent.VK_S:
       down = true;
     break;
     case KeyEvent.VK_ENTER:
       enter = true;
     break;
     case UP:
       shotUp = true;
     break;
     case DOWN:
       shotDown = true;
     break;
     case LEFT:
       shotLeft = true;
     break;
     case RIGHT:
       shotRight = true;
     break;
     case KeyEvent.VK_SPACE:
       space = true;
     break;
     case KeyEvent.VK_Q:
       activeShield = true;
     break;
     case KeyEvent.VK_E:
       activeMultiShot = true;
     break;
   }
 }
 
 public void keyUp(int p_key){
   switch(p_key){
     case KeyEvent.VK_A:
       left = false;
     break;
     case KeyEvent.VK_D:
       right = false;
     break;
     case KeyEvent.VK_W:
       up = false;
     break;
     case KeyEvent.VK_S:
       down = false;
     break;
     case KeyEvent.VK_ENTER:
       enter = false;
     break;
     case UP:
       shotUp = false;
     break;
     case DOWN:
       shotDown = false;
     break;
     case LEFT:
       shotLeft = false;
     break;
     case RIGHT:
       shotRight = false;
     break;
     case KeyEvent.VK_SPACE:
       space = false;
     break;
     case KeyEvent.VK_Q:
       activeShield = false;
     break;
     case KeyEvent.VK_E:
       activeMultiShot = false;
     break;
   }
 }
}
class GestorDisparos{
  private ArrayList<Bala> balas;
  private Player player;
  
  private int timerShoot = 0;
  private final float shootDist = FRAMES/6;
  
  public GestorDisparos(Player player){
    this.balas = new ArrayList<Bala>();
    this.player = player;
  }
  
  public void update(){
    timer();
    move();
  }
  
  private void move(){
    if(!balas.isEmpty()){
      for (int i = 0; i < balas.size();i++){
        Bala b = balas.get(i);
        b.update();
        b.paint();
        if(b.isDie()){
          balas.remove(i);
        }
      }
    }    
  }
  
  private void timer(){
    timerShoot++;
    if(timerShoot >= shootDist){
      setShot();
      timerShoot = 0;
    }
  }
  
  public void addBala(PVector pos, String direct){
    balas.add(new Bala(new PVector(pos.x, pos.y), direct));
  }
  
  public void setShot(){
    String direct = "";
    
    if(!KEYBOARD.shotUp && !KEYBOARD.shotDown && !KEYBOARD.shotLeft && !KEYBOARD.shotRight ||
  KEYBOARD.shotUp && KEYBOARD.shotDown || KEYBOARD.shotLeft && KEYBOARD.shotRight){return;}
    
    if(KEYBOARD.shotUp){
      direct += "U";
    } 
    if(KEYBOARD.shotDown){
      direct += "D";
    } 
    if(KEYBOARD.shotLeft){
      direct += "L";
    } 
    if (KEYBOARD.shotRight){
      direct += "R";
    }
    
    addBala(this.player.pos, direct);
  }
  
  public ArrayList<Bala> getBalas(){
    return balas;
  }
  
}
class GestorDisparosEnemigos{ 
  //BALAS ENEMIGOS
  private ArrayList<Bala> enemyBalas;
  private PVector pos;
  
  private int timerShootEnemy = 0;
  private final float shootDistEnemy = FRAMES/2;
  
  private int timerChangeTarget = 0;
  private final float changeTargetFrame = FRAMES;
  
  private final String[] targetDirect = {"L", "R", "U", "D", "UL", "UR", "DL", "DR"};
  private String direct;
  private boolean autoShot;
  
  public GestorDisparosEnemigos(PVector pos){
    //ENEMIES
    this.enemyBalas = new ArrayList<Bala>();
    this.pos = pos;
    this.direct = randomTarget();
    this.autoShot = true;
  }
  
  
  public void update(){
    if(autoShot){
      timer();
    }else{
      shoot();
    }
    move();
  }
  
  private void shoot(){
    
  }
  
  private void move(){
    if(!enemyBalas.isEmpty()){
      for (int i = 0; i < enemyBalas.size();i++){
        Bala eb = enemyBalas.get(i);
        eb.update();
        eb.paint();
        if(eb.isDie()){
          enemyBalas.remove(i);
        }
      }
    }
    
  }
  
  private void timer(){
    timerChangeTarget++;
    if(timerChangeTarget >= changeTargetFrame){
      this.direct = randomTarget();
    }
    
    timerShootEnemy++;
    if(timerShootEnemy >= shootDistEnemy){
      //addBalaEnemy(this.shooter.pos, randomTarget());//RANDOM LA DIRECCION
      //GENERAR ALGUN ENEMIGO CON CADA DISPARO SEA ALEATORIO?¿?? MODO HARDCORE
      addBalaEnemy(this.pos, this.direct);
      timerShootEnemy = 0;
    }
    
  }
  
  public void addBalaEnemy(PVector pos, String direct){
    enemyBalas.add(new Bala(new PVector(pos.x, pos.y), direct, color(0xffFAD91C)));
  }
  
  public ArrayList<Bala> getBalas(){
    return enemyBalas;
  }
  
  private String randomTarget(){
    int index = PApplet.parseInt(random(targetDirect.length));
    
    return targetDirect[index];
  }
  
  public void setTarget(String target){
    this.direct = target;
  }
  
  public void setPos(PVector pos){
    this.pos = pos;
  }
  
  public void setAutoShot(boolean autoShot){
    this.autoShot = autoShot;
  }
  
}
public class GestorEstados {
  //VARIABLES
  private final EstadoJuego[] estados;
  private EstadoJuego estadoActual;

  private int estado;


  public GestorEstados() {
    this.estado = -1;
    this.estados = new EstadoJuego[5];

    startEstado();
    startEstadoActual();
  }

  private void startEstado() {
    this.estados[0] = new GestorMenu();
    this.estados[1] = new SelectLvl();
    this.estados[2] = new GestorJuego();
    this.estados[3] = new GestorGameOver();
    this.estados[4] = new GestorLvlComplete();
  }

  private void startEstadoActual() {
    this.estadoActual = estados[0];
  }

  public void update() {
    this.estadoActual.update();
  }

  public void setEstado(final int estado) {
    this.estadoActual = estados[estado];
    this.estado = estado;
  }

  public EstadoJuego getEstadoActual() {
    return this.estadoActual;
  }

  public int getIndexEstadoActual() {
    return this.estado;
  }

  public void resetGestorJuego() {
    this.estados[1] = new GestorJuego();
  }
}
class GestorGameOver implements EstadoJuego {

  private String title;
  private String subTitle;
  
  private final float bornTimeFrame = 0.2f*FRAMES;
  private final float animationTimeFrame = 0.5f*FRAMES;
  
  private int bornTimer;
  private final int sizeTitle;
  private int positionChar;
  private int animationTimer;
  private int contador = 0;
  
  private int cSubtitle;

  public GestorGameOver() {
    this.title = "";
    this.bornTimer = 0;
    this.subTitle = "";
    this.sizeTitle = ("GAME OVER").length();
    this.positionChar = 0;
    this.animationTimer = 0;
    this.cSubtitle = color(255);
  }

  public void update() {
    systemSound.beforeStop();
    if (this.title.length() != this.sizeTitle) {
      timer();
    } else {
      if (KEYBOARD.space) {
        gestorNiveles.update();
        outGameOver = true;
      }
    }
    paint();
  }

  public void paint() {
    background(0);
    fill(255);
    textAlign(CENTER);
    textSize(32f);
    text(this.title, CENTRO_VENTANA_X, CENTRO_VENTANA_Y);

    if (!this.subTitle.equals("")) {
      text("SCORE: " + finalScore, CENTRO_VENTANA_X, CENTRO_VENTANA_Y+50);
      //TEMPORAL
      if (endGame) {
        text("END DEMO", CENTRO_VENTANA_X, CENTRO_VENTANA_Y+120);
      }
      animationSubtitle();
      fill(cSubtitle);
      textSize(18f);
      text(this.subTitle, CENTRO_VENTANA_X, CENTRO_VENTANA_Y+80);
    }
  }

  private void timer() {
    bornTimer++;
    if (this.bornTimeFrame < this.bornTimer) {
      this.positionChar++;
      switch(this.positionChar) {
      case 1:
        this.title = "G";
        break;
      case 2:
        this.title += "A";
        break;
      case 3:
        this.title += "M";
        break;
      case 4:
        this.title += "E ";
        break;
      case 5:
        this.title +=  "O"; 
        break;
      case 6:
        this.title += "V";
        break;
      case 7:
        this.title += "E";
        break;
      case 8:
        this.title += "R";
        break;
      }
      bornTimer = 0;
    }

    if (this.title.length() == this.sizeTitle) {
      this.subTitle = "Space to restart";
    }
  }

  private void animationSubtitle() {
    this.animationTimer++;
    if (this.animationTimer > this.animationTimeFrame) {
      this.contador++;

      if (this.contador%2==0) {
        this.contador = 0;
        cSubtitle = color(0);
      } else {
        cSubtitle = color(255);
      }
      this.animationTimer = 0;
    }
  }
}
class GestorJuego implements EstadoJuego{
  
  //VARIABLES
  /*OPTIMIZABLE LA CARGA SE HACE AL INICIAR EL PROGRAMA
  *
  */
  
  private final Map map;
  private final Player player;
  private final GestorMonsters gm;
  private final GestorDisparos gd;
  
  public GestorJuego(){
    println("Gestor Juego inicializado");
    this.map = new Map(gestorNiveles);
    this.player = new Player(CENTRO_VENTANA_X,CENTRO_VENTANA_Y);
    this.gm = new GestorMonsters(this.player);
    this.gd = new GestorDisparos(this.player);
  }

 public void update(){
   if(!over && !isLvlComplete){
     this.map.update();
     this.player.update();
     this.gd.update();
     this.gm.update(gd.getBalas());
     this.player.showScore();
   }
 }
}
class GestorLvlComplete implements EstadoJuego {

  private String title;
  private String subTitle;
  private String description;

  private final float bornTimeFrame = 0.2f*FRAMES;
  private final float animationTimeFrame = 0.5f*FRAMES;

  private int bornTimer;
  private final int sizeTitle;
  private int positionChar;
  private int animationTimer;
  private int contador = 0;

  private int cSubtitle;

  public GestorLvlComplete() {
    this.title = "";
    this.bornTimer = 0;
    this.subTitle = "";
    this.sizeTitle = ("Complete").length();
    this.positionChar = 0;
    this.animationTimer = 0;
    this.cSubtitle = color(255);
  }

  public void update() {
    if (!ssd.isSaved) {
      systemSound.beforeStop();
      //SAVE DATA JSON
      ssd.update(new DataLvl(finalLvl, finalScore));
      ssd.isSaved = true;
    }

    if (this.title.length() != this.sizeTitle) {
      timer();
    } else {
      if (KEYBOARD.enter ) {
        //NEXT LVL
        ssd.isSaved = false;
        isLvlComplete = false;
        if (finalLvl >= MAX_LVLS) {
          //RELOAD JSON ?
          systemSound.play(0);
          isSelection = true;
          delay(300);
        } else {
          gestorNiveles.update();
        }
      }
    }
    paint();
  }

  public void paint() {
    background(0);
    fill(255);
    textAlign(CENTER);
    textSize(32f);
    text(this.title, CENTRO_VENTANA_X, CENTRO_VENTANA_Y-200);

    if (!this.subTitle.equals("")) {
      newHability();
      fill(255);
      textSize(32f);
      text("SCORE: " + finalScore, CENTRO_VENTANA_X, CENTRO_VENTANA_Y+50);
      animationSubtitle();
      fill(cSubtitle);
      textSize(18f);
      text(this.subTitle, CENTRO_VENTANA_X, CENTRO_VENTANA_Y+80);
    }
  }

  private void newHability() {
    switch(finalLvl) {
    case 1:
      strokeWeight(2);
      stroke(255);
      noFill();
      ellipse(CENTRO_VENTANA_X, CENTRO_VENTANA_Y-140, 30, 30);
      textSize(18f);
      description = "Nueva habilidad desbloqueada!\n Ahora al pulsar Q podrás activar un escudo que evitaras\n ser golpeado por proyectiles durante unos segundos.";
      text(this.description, CENTRO_VENTANA_X, CENTRO_VENTANA_Y-100);
      break;
    case 2:
      strokeWeight(2);
      stroke(0xffFF5500);
      noFill();
      ellipse(CENTRO_VENTANA_X, CENTRO_VENTANA_Y-140, 30, 30);
      textSize(18f);
      description = "Nueva habilidad desbloqueada!\n Ahora al pulsar E podrás disparar una onda expansiva de balas.";
      text(this.description, CENTRO_VENTANA_X, CENTRO_VENTANA_Y-100);
      break;
      case 4:
      //NUEVA HABILIDAD LASER????
      break;
    }
  }

  private void timer() {
    bornTimer++;
    if (this.bornTimeFrame < this.bornTimer) {
      this.positionChar++;
      switch(this.positionChar) {
      case 1:
        this.title = "C";
        break;
      case 2:
        this.title += "O";
        break;
      case 3:
        this.title += "M";
        break;
      case 4:
        this.title += "P";
        break;
      case 5:
        this.title +=  "L"; 
        break;
      case 6:
        this.title += "E";
        break;
      case 7:
        this.title += "T";
        break;
      case 8:
        this.title += "E";
        break;
      }
      bornTimer = 0;
    }

    if (this.title.length() == this.sizeTitle) {
      this.subTitle = "ENTER to next level " + (finalLvl+1);
    }
  }

  private void animationSubtitle() {
    this.animationTimer++;
    if (this.animationTimer > this.animationTimeFrame) {
      this.contador++;

      if (this.contador%2==0) {
        this.contador = 0;
        cSubtitle = color(0);
      } else {
        cSubtitle = color(255);
      }
      this.animationTimer = 0;
    }
  }
}
class GestorMenu implements EstadoJuego {

  private final Seccion[] secciones;
  private final Seccion[] seccionesControls;

  private int position;
  private final int maxPositions = 4;

  private float keyTimer = 0;
  private final float timeFrame = 0.2f*FRAMES;

  private boolean onChange = false;
  private boolean isMenuControls = false;
  private boolean isMenuAudio = false;

  public GestorMenu() {
    println("GESTOR MENU INIT");
    this.secciones = new Seccion[maxPositions];
    this.position = 0;
    this.seccionesControls = new Seccion[12];
    if(!inGame)systemSound.play(0);
    initSections();
  }

  public void update() {
    showMenus();
  }

  private void initSections() {
    this.secciones[0] = new Seccion("PLAY", CENTRO_VENTANA_X, CENTRO_VENTANA_Y-50);
    this.secciones[1] = new Seccion("AUDIO", CENTRO_VENTANA_X, CENTRO_VENTANA_Y+20);
    this.secciones[2] = new Seccion("CONTROLS", CENTRO_VENTANA_X, CENTRO_VENTANA_Y+90);
    this.secciones[3] = new Seccion("EXIT", CENTRO_VENTANA_X, CENTRO_VENTANA_Y+160);

    //MENU DE CONTROLES
    float size = 18f;
    this.seccionesControls[0] = new Seccion("-------- MOVIMIENTOS --------", CENTRO_VENTANA_X, 80, size);
    this.seccionesControls[1] = new Seccion("ARRIBA        W", CENTRO_VENTANA_X, 110, size);
    this.seccionesControls[2] = new Seccion("IZQUIERDA    A", CENTRO_VENTANA_X, 140, size);
    this.seccionesControls[3] = new Seccion("ABAJO          S", CENTRO_VENTANA_X, 170, size);
    this.seccionesControls[4] = new Seccion("DERECHA      D", CENTRO_VENTANA_X, 200, size);//start
    this.seccionesControls[5] = new Seccion("-------- DISPARAR ----------", CENTRO_VENTANA_X, 250, size);
    this.seccionesControls[6] = new Seccion("ARRIBA        ↑", CENTRO_VENTANA_X, 280, size);
    this.seccionesControls[7] = new Seccion("IZQUIERDA    →", CENTRO_VENTANA_X, 310, size);
    this.seccionesControls[8] = new Seccion("ABAJO         ↓", CENTRO_VENTANA_X, 340, size);
    this.seccionesControls[9] = new Seccion("DERECHA      ←", CENTRO_VENTANA_X, 370, size);
    this.seccionesControls[10] = new Seccion("----- HABILIDADES --------", CENTRO_VENTANA_X, 420, size);
    this.seccionesControls[11] = new Seccion("ESCUDO     Q", CENTRO_VENTANA_X, 450, size);
  }

  public int getSeccionActual() {
    return this.position;
  }

  private void allSections() {
    for (int i = 0; i < this.secciones.length; i++) {
      secciones[i].update();
      if (i == position) {
        secciones[i].selected();
        actionMenu();
      }
    }
  }

  private void timer() {
    keyTimer++;
    if (keyTimer > timeFrame && (KEYBOARD.up || KEYBOARD.down)) {
      onChange = true;
      keyTimer = 0;
    } else {
      onChange = false;
    }
  }

  private void movePosition() {
    if (KEYBOARD.down && position < (maxPositions-1)) {
      position++;
    } else if (KEYBOARD.up && position > 0) {
      position--;
    }
  }

  private void actionMenu() {   
    if (KEYBOARD.enter) {
      switch(this.position) {
      case 0:
        isSelection = true;
        delay(300);
        break;
      case 1:
        this.isMenuAudio = true;
        delay(300);
        break;
      case 2:
        isMenuControls = true;
        delay(300);
        break;
      case 3:
        exit();
        break;
      }
    }
  }

  private void showMenus() {
    if (this.isMenuControls) {
      if (KEYBOARD.enter) {
        isMenuControls = false;
        delay(300);
      }
      showControls();
      textSize(24f);
      textAlign(CENTER);
      text("ENTER to exit", CENTRO_VENTANA_X, HEIGHT-10);
    } else if (this.isMenuAudio) {
      systemSound.update();
      if (KEYBOARD.enter) {
        this.isMenuAudio = false;
        delay(300);
      }
      textSize(24f);
      textAlign(CENTER);
      text("ENTER to exit", CENTRO_VENTANA_X, HEIGHT-10);
    } else {
      allSections();
      timer();
      if (onChange) {
        movePosition();
      }
    }
  }

  private void showControls() {
    for (int i = 0; i < this.seccionesControls.length; i++) {
      seccionesControls[i].update();
    }
  }
}
class GestorMonsters {
  //ARRAY INT[] -> IDENTIFICADOR DEL BICHO ES EL INDICE -> VALOR ACUMULADOR DE BICHOS VIVOS DE EL TIPO EN CONCRETO
  private final int[] monstersAlive = new int[6];

  int monsterEasyBornTimer;
  final int monsterEasyBornDist = PApplet.parseInt(0.5f*FRAMES);//Ratio de aparicion por frame
  int monsterShotBornTimer;
  final int monsterShotBornDist = PApplet.parseInt(3*FRAMES);
  int meteoBornTimer;
  int meteoBornDist = (3*FRAMES);//45
  int monsterWifiBornTimer;
  final int monsterWifiBornDist = PApplet.parseInt(0.5f*FRAMES);
  final int bombBornDist = (3*FRAMES);
  int bombBornTimer;
  final int shooterV2BornDist = (5*FRAMES);//5*FRAMES
  int shooterV2Timer;

  ArrayList<Monster> monsters = new ArrayList<Monster>();

  private int bornShipsInBoss = 0;

  private MonsterBoss mb;
  private MonsterBossV2 mb2;
  private MonsterBossV3 mb3;
  private Player player;
  private ArrayList<ParticleSystem> particlesSystems;

  public GestorMonsters(Player player) {
    this.monstersAlive[0] = 0;
    this.monstersAlive[1] = 0;
    this.monstersAlive[2] = 0;
    this.monstersAlive[3] = 0;
    this.monstersAlive[4] = 0;
    this.monstersAlive[5] = 0;

    this.player = player;
    this.monsterEasyBornTimer = 0;
    this.mb = new MonsterBoss(this.player, new PVector(WIDTH+20, 0));
    this.mb2 = new MonsterBossV2(this.player, new PVector(CENTRO_VENTANA_X, -100));
    this.mb3 = new MonsterBossV3(this.player, new PVector(WIDTH-BOSS_V3_RAD, CENTRO_VENTANA_Y));
    this.particlesSystems = new ArrayList<ParticleSystem>();
  }

  public void update(ArrayList<Bala> balas) {

    if (gestorNiveles.getLevel() >= 3) {
      gestorNiveles.updateProgress();
    }

    mechanicalBoss(balas);
    updateMonsters(balas);

    if (this.player.score < gestorNiveles.getMaxScore()) {
      timer();
    }

    updateParticles();
    //PANTALLA DE RESULTADOS Y RESETEAR EL SCORE
    if (this.player.score >= gestorNiveles.getMaxScore() && this.monsters.isEmpty()) updateResults(balas);
  }

  //OPTIMIZAR VALIDACION DE MONSTER ALIVE -> La idea es que no haya monstruos al empezar el boss.
  private void mechanicalBoss(ArrayList<Bala> balas) {
    switch(gestorNiveles.getLevel()) {
    case 1:
      if (this.player.score >= gestorNiveles.getMaxScore() && (this.monstersAlive[0] == 0 && this.monstersAlive[1] == 0 || mb.getIsStarted()) && !mb.isDie) {
        mb.updateBoss(balas);
        if (!mb.getIsStarted()) {
          this.player.setAutoMove(true);
          mb.update();
        } else {
          this.player.setAutoMove(false);
          timerBoss(1);
        }
        mb.paint();
      }
      break;
    case 2:
      //BOSS 2n
      if (this.player.score >= gestorNiveles.getMaxScore() && (this.monstersAlive[0] == 0 && this.monstersAlive[2] == 0 || mb2.getIsStarted()) && !mb2.isDie) {
        mb2.updateBoss(balas);
        if (!mb2.getIsStarted()) {
          this.player.setTargetAutoX(CENTRO_VENTANA_X-200);
          this.player.setAutoMove(true);
          mb2.update();
        } else {
          this.player.setAutoMove(false);
          timerBoss(2);
        }
        mb2.paint();
      }
      break;
    case 4:
      if (!mb3.isDie) {
        mb3.updateBoss(balas);
        mb3.update();
        timerBoss(3);
        mb3.paint();
      }
      break;
    }
  }

  private void timer() {
    if (gestorNiveles.getMaxMonsterEasy() != 0) {
      monsterEasyBornTimer++;
      if (monsterEasyBornTimer >= monsterEasyBornDist) {
        addMonsterEasy(1);
        monsterEasyBornTimer = 0;
      }
    }
    if (gestorNiveles.getMaxMonsterShooter() != 0) {
      monsterShotBornTimer++;
      if (monsterShotBornTimer >= monsterShotBornDist) {
        addMonsterShooter(1);
        monsterShotBornTimer = 0;
      }
    }
    if (gestorNiveles.getMaxMeteoritos() != 0) {
      meteoBornTimer++;
      if (meteoBornTimer >= meteoBornDist) {
        addMeteo(1);
        meteoBornTimer = 0;
      }
    }
    if (gestorNiveles.getMaxMonsterWifi() != 0) {
      monsterWifiBornTimer++;
      if (monsterWifiBornTimer >= monsterWifiBornDist) {
        addMonsterWifi(1);
        monsterWifiBornTimer = 0;
      }
    }
    if (gestorNiveles.getMaxMonsterBomb() != 0) {
      bombBornTimer++;
      if (bombBornTimer >= bombBornDist) {
        addBomb(1);
        bombBornTimer = 0;
      }
    }
    if (gestorNiveles.getMaxShooterV2() != 0) {
      shooterV2Timer++;
      if (shooterV2Timer >= shooterV2BornDist) {
        addShooterV2(1);
        shooterV2Timer = 0;
      }
    }
  }

  private void timerBoss(int idBoss) {
    switch(idBoss) {
      //1r BOSS
    case 1:
      //FASES
      switch(mb.getFase()) {
      case 1:
        if (bornShipsInBoss < 50) {
          monsterEasyBornTimer++;
          if (monsterEasyBornTimer >= monsterEasyBornDist) {
            bornShipsInBoss++;
            addMonsterEasyBoss(5);
            monsterEasyBornTimer = 0;
          }
        } else if (this.monstersAlive[0] == 0) {
          mb.setFase(2);
        }
        if (!mb.loadShooters) {
          mb.loadShooters = true;
          addMonsterShooterBoss();
        }
        break;
      case 2:
        if (mb.shield <= 0) {
          mb.setFase(3);
        }
        break;
      case 3:
        if (this.monstersAlive[1] == 0) {
          mb.setFase(4);
          mb.setShield(40);
        }
        break;
      case 4:
        //DAÑO A LA NAVE
        if (mb.shield <= 0) {
          this.player.setScore(mb.score);
          mb.isDie = true;
        }
        break;
      }
      break;
      //2n BOSS
    case 2:
      if (mb2.needShips && mb2.getShieldActive()) {
        addMonsterWifi(5);
        mb2.needShips = false;
      } else if (this.monstersAlive[2] == 0) {
        mb2.setShieldActive(false);
        mb2.timerShield();
      }  
      break;
    case 3:
        //INICIAR 2 NAVES FOLLOWER
        // TIMER REINVOCANDOLOS
        // TIMER INVOCANDO METEORITOS
      break;
    }
  }

  private void addMonsterEasy(int i) {
    if (this.monstersAlive[0] < gestorNiveles.getMaxMonsterEasy()) {
      for (int c = 0; c < i; c++) {
        this.monstersAlive[0]++;
        monsters.add(new Monster_easy(this.player, respawn(MONSTER_EASY_RAD, false)));
      }
    }
  }

  private void addMonsterEasyBoss(int i) {
    if (this.monstersAlive[0] < 100) {
      for (int c = 0; c < i; c++) {
        this.monstersAlive[0]++;
        monsters.add(new Monster_easy(this.player, respawnInBoss(MONSTER_EASY_RAD)));
      }
    }
  }

  private void addMonsterShooter(int i) {
    if (this.monstersAlive[1] < gestorNiveles.getMaxMonsterShooter()) {
      for (int c = 0; c < i; c++) {
        this.monstersAlive[1]++;
        monsters.add(new Monster_shooter(this.player, respawn(MONSTER_SHOT_RAD, true)));
      }
    }
  }

  private void addMonsterWifi(int i) {
    if (this.monstersAlive[2] < gestorNiveles.getMaxMonsterWifi()) {
      for (int c = 0; c < i; c++) {
        this.monstersAlive[2]++;
        monsters.add(new MonsterWifi(this.player, respawn(MONSTER_WIFI_RAD, false)));
      }
    }
  }

  private void addBomb(int i) {
    if (this.monstersAlive[4] < gestorNiveles.getMaxMonsterBomb()) {
      for (int c = 0; c < i; c++) {
        this.monstersAlive[4]++;
        monsters.add(new Bomb(this.player, respawn(MONSTER_BOMB_RAD, false)));
      }
    }
  }

  private void addMonsterShooterBoss() {
    this.monstersAlive[1] = 5;
    monsters.add(new Monster_shooter(this.player, new PVector(WIDTH-50, 50), 20));
    monsters.add(new Monster_shooter(this.player, new PVector(WIDTH-50, HEIGHT/2), 20));
    monsters.add(new Monster_shooter(this.player, new PVector(WIDTH-50, HEIGHT/3), 20));
    monsters.add(new Monster_shooter(this.player, new PVector(WIDTH-50, (HEIGHT/2)+100), 20));
    monsters.add(new Monster_shooter(this.player, new PVector(WIDTH-50, HEIGHT-50), 20));
  }

  private void addMeteo(int i) {
    if (this.monstersAlive[3] < gestorNiveles.getMaxMeteoritos()) {
      if (meteoBornDist > 50) {
        meteoBornDist -= 15;
      }
      for (int c = 0; c < i; c++) {
        this.monstersAlive[3]++;
        if (this.player.score >= (gestorNiveles.getMaxScore()/2)) {
          meteoBornDist = 35;
          float y = random(20, HEIGHT-20);
          monsters.add(new Meteorito(this.player, new PVector(WIDTH+200, y), new PVector(-200, y), 'L'));
        } else {
          float x = random(20, WIDTH-20);
          monsters.add(new Meteorito(this.player, new PVector(x, -100), new PVector(x, HEIGHT+200), 'D'));
        }
      }
    }
  }

  private void addShooterV2(int i) {
    if (this.monstersAlive[5] < gestorNiveles.getMaxShooterV2()) {
      for (int c = 0; c < i; c++) {
        this.monstersAlive[5]++;
        monsters.add(new ShooterV2(this.player, respawn(SHOOTER_V2_RAD, false)));
      }
    }
  }

  //SPAWN EN LOS MARGENES
  private PVector respawn(float rad, boolean isMargin) {    
    float rx = 0f;
    float ry = 0f;
    float angle = 0f;
    float disting = 0f;
    int h = 0;
    int w = 0;

    if (isMargin) {
      h = 200;
      w = 200;
    }

    do {

      angle = random(-PI, PI); //ANGULO RANDOM
      disting = random(HEIGHT-h, WIDTH-w);
      rx = disting*cos(angle)+this.player.pos.x;
      ry = disting*sin(angle)+this.player.pos.y;
      rx = range(rx, rad, WIDTH-rad);
      ry = range(ry, rad, HEIGHT-rad);
    } while (PVector.dist(new PVector(rx, ry), this.player.pos)<=this.player.r*5/2+rad/2);

    return new PVector(rx, ry);
  }

  private PVector respawnInBoss(float rad) {
    //CENTRO DE LA NAVE NODRIZA
    float rx = random(WIDTH, WIDTH-rad-50);
    float ry = random(HEIGHT/4, HEIGHT-(HEIGHT/4));

    return new PVector(rx, ry);
  }

  private void updateParticles() {    
    for (int i = 0; i < particlesSystems.size(); i++) {
      ParticleSystem ps = particlesSystems.get(i);
      ps.update();
      if (ps.isEmpty()) {
        particlesSystems.remove(i);
      }
    }
  }

  private void updateMonsters(ArrayList<Bala> balas) {
    for (int i = 0; i< monsters.size(); i++) {
      monsters.get(i).update();
      monsters.get(i).updatePaint(balas);
      if (monsters.get(i).isDie) {
        if (monsters.get(i).animationDestroy) {
          ParticleSystem ps = new ParticleSystem(monsters.get(i).pos);
          ps.addParticle(20, monsters.get(i).c);
          this.particlesSystems.add(ps);
        }
        this.player.setScore(monsters.get(i).score);
        this.monstersAlive[monsters.get(i).id]--;
        monsters.remove(i);
      }
    }
  }

  private void updateResults(ArrayList<Bala> balas) {
    switch(gestorNiveles.getLevel()) {
    case 1:
      if (mb.isDie) {
        if (mb.animationDead) {
          animationDestroyBossRandom(30, (WIDTH-(WIDTH/4)), (WIDTH-20), 50, (HEIGHT-30));
          mb.animationDead = false;
        }
      }
      if (mb.isDie && this.particlesSystems.isEmpty()) {
        //procesing results -> finallvl, score, array balas, nextLvl, idBoss
        procesingResults(1, this.player.score, balas, 2, 1);
      }
      break;
    case 2:
      if (mb2.isDie && mb2.animationDead) {
        animationDestroyBoss(10, mb2.pos);
        mb2.animationDead = false;
      }
      if (mb2.isDie && this.particlesSystems.isEmpty()) {
        //procesing results -> finallvl, score, array balas, nextLvl, idBoss
        this.player.setScore(mb2.score);// ?¿?¿?¿?¿??¿
        procesingResults(2, this.player.score, balas, 3, 2);
      }
      break;
    case 3:
      procesingResults(3, this.player.score, balas, 4, 0);
      break;
    case 4:
      //RESULTS LVL 4.
      break;
    }
  }

  private void animationDestroyBoss(int quantity, PVector pos) {
    for (int i = 0; i < quantity; i++) {
      ParticleSystem ps = new ParticleSystem(pos);
      ps.addParticle(300, color(255, 152, 15));
      this.particlesSystems.add(ps);
    }
  }

  private void animationDestroyBossRandom(int quantity, int rxInit, int rxFinal, int ryInit, int ryFinal) {
    for (int i = 0; i < quantity; i++) {
      ParticleSystem ps = new ParticleSystem(new PVector(random(rxInit, rxFinal), random(ryInit, ryFinal)));
      ps.addParticle(300, color(255, 152, 15));
      this.particlesSystems.add(ps);
    }
  }

  private void procesingResults(final int lvl, final int score, ArrayList<Bala> balas, final int nextLvl, final int idBoss) {
    finalLvl = lvl;
    finalScore = score;
    this.player.reset();
    balas.clear();
    gestorNiveles.setLevel(nextLvl);
    resetBoss(idBoss);
    isLvlComplete = true;
  }

  private void resetBoss(final int idBoss) {
    switch(idBoss) {
    case 1:
      this.mb = new MonsterBoss(this.player, new PVector(WIDTH+20, 0));
      break;
    case 2:
      this.mb2 = new MonsterBossV2(this.player, new PVector(CENTRO_VENTANA_X, -100));
      break;
    }
  }
}
/*
* CONTROL DE NIVELES DE PANTALLA/DIFICULTAD DEL JUEGO
 * DESDE AQUI, CARGAR/LEER FICHERO NIVEL MAX DISPONIBLE EL JUGADOR DEFAULT 1.
 */

class GestorNiveles {

  private int level;
  private int maxMonsterEasy;
  private int maxMonsterShooter;
  private int maxScore;
  private int maxMeteoritos;
  private int maxMonsterWifi;
  private int maxMonsterBomb;
  private int maxLevel;
  private int maxShooterV2;
  private int timer;
  private int fase = 0;

  private final float timerFrames = (15*FRAMES);

  private Configuration config;

  private boolean isFinalProgress;

  public GestorNiveles() {
    println("GESTOR NIVELES INICIALIZADO");
    init_monsters(0, 0, 0, 0, 0, 0);
    this.level = 1;
    this.config = new Configuration();
    this.maxLevel = Integer.parseInt(this.config.getInfo("maxLevel"));
    this.fase = 0;
    this.isFinalProgress = false;
    this.timer = 0;
  }

  public void update() {
    updateLevel();
  }
  //OPTIMIZAR CODIGO -> FUNCION RESET VARIABLES.
  private void updateLevel() {
    switch(this.level) {
    case 1:
      //MONSTEREASY, SHOOTER, METEORITOS, WIFI, BOMB, shooter v2.
      init_monsters(20, 5, 0, 0, 0, 0);
      this.maxScore = 200;
      systemSound.beforeStop();
      systemSound.play(1);
      this.fase = 0;
      this.timer = 0;
      break;
    case 2:
      setMaxLevel(this.level);
      init_monsters(5, 0, 5, 8, 0, 0);
      this.maxScore = 500;
      systemSound.beforeStop();
      systemSound.play(2);
      this.fase = 0;
      this.timer = 0;
      break;
    case 3:
      setMaxLevel(this.level);
      init_monsters(30, 0, 0, 0, 1, 0);
      this.maxScore = 1100;
      systemSound.beforeStop();
      systemSound.play(3);
      this.fase = 0;
      this.timer = 0;
      this.isFinalProgress = false;
      updateProgress();//NOW
      break;
    case 4:
    //NIVEL DE BOSS UNICO
      setMaxLevel(this.level);
      init_monsters(0, 0, 0, 0, 0, 0);
      this.maxScore = 0;
      systemSound.beforeStop();
      //systemSound.play(3);
      this.fase = 0;
      this.timer = 0;
      this.isFinalProgress = false;
      //updateProgress();//NOW
      break;
    default:
      this.level = 1;
      updateLevel();
      break;
    }
  }

  //A PARTIR DE LV3
  public void updateProgress() {
    if (!this.isFinalProgress) progresLvl();
    debugValue("FASE: ", this.fase, 50, 50);
  }

  //DAR PROGRESION DE DIFICULTAD EN LOS NIVELES 3 Y SUPERIORES
  private void progresLvl() {
    switch(this.level) {
    case 3:
      timer++;
      if (timer >= timerFrames) {
        this.fase++;
        timer = 0;
        switch(this.fase) {
        case 1:
          //MONSTEREASY, SHOOTER, METEORITOS, WIFI, BOMB, shooter v2.
          init_monsters(5, 0, 0, 0, 2, 1);
          break;
        case 2:
          init_monsters(3, 0, 0, 2, 2, 1);
          break;
        case 3:
          init_monsters(0, 0, 0, 8, 1, 2);
          break;
        case 4:
          init_monsters(15, 0, 0, 0, 1, 2);
          break;
        case 5:
          init_monsters(0, 0, 0, 15, 1, 1);
          break;
        case 6:
          init_monsters(20, 0, 0, 15, 1, 1);
          this.isFinalProgress = true;
          break;
        }
      }
      break;
    case 4:
      //FUTUROS LVLS
      break;
    }
  }


  public void setLevel(int lv) {
    this.level = lv;
  }

  public int getLevel() {
    return this.level;
  }

  public int getMaxMonsterEasy() {
    return this.maxMonsterEasy;
  }

  public int getMaxMonsterShooter() {
    return this.maxMonsterShooter;
  }

  public int getMaxScore() {
    return this.maxScore;
  }

  public int getMaxMeteoritos() {
    return this.maxMeteoritos;
  }

  public int getMaxLevel() {
    return this.maxLevel;
  }

  public int getMaxMonsterBomb() {
    return this.maxMonsterBomb;
  }

  public int getMaxShooterV2() {
    return this.maxShooterV2;
  }

  public void setMaxLevel(int maxLvl) {
    if (this.maxLevel < maxLvl) {
      this.maxLevel = maxLvl;
      this.config.update("maxLevel", ""+this.maxLevel);
    }
  }

  public int getMaxMonsterWifi() {
    return this.maxMonsterWifi;
  }

  private void init_monsters(int me, int ms, int mm, int mw, int mb, int msv2) {
    this.maxMonsterEasy = me;
    this.maxMonsterShooter = ms;
    this.maxMeteoritos = mm;
    this.maxMonsterWifi = mw;
    this.maxMonsterBomb = mb;
    this.maxShooterV2 = msv2;
  }
}
abstract class Habilities {
  
  protected PVector pos;
  protected boolean isActive;
  protected boolean isReady;
  protected boolean isEquiped; //PARA EL FUTURO SISTEMA DE INVENTARIO
  protected int lvlRequired;
  protected int timer;
  protected float timerFrame;
  protected float coldown;
  protected int c;
  
 public abstract void update();
 public abstract void timer();
 public abstract void paint();
 public abstract float getRad();
 public abstract void setIsEquiped(boolean isEquiped);
 public abstract ArrayList<Object> getObjects();
}
/*
  * Metodos globales
 */

/*
  *******************************************************
 *
 * Representa el area de los obstaculos colisionables
 *
 *******************************************************
 */
public void debugArea(float rad) {
  stroke(255);
  strokeWeight(4);
  noFill();
  ellipse(0, 0, rad, rad);
}

public void debugAreaPoint(float rad, float w, float h) {
  stroke(0xffF50A0A);
  strokeWeight(4);
  noFill();
  ellipse(w, h, rad, rad);
}

/*
  *******************************************************
 *
 * Calculo de margenes del mapa con los objetos o
 * ser vivos colisionables
 *
 *******************************************************
 */

public float range(float p, float min, float max) {
  if (p<min) {
    p = min;
  }
  if (p>max) {
    p = max;
  }
  return p;
}

/*
  *******************************************************
 *
 * Muestra x valores en la esquina de la ventana
 *
 *******************************************************
 */

public void debugValue(String typeValue, int value, int posX, int posY) {
  fill(255);
  textSize(18);
  text(typeValue +" " + value, posX, posY);
}

/*
  *******************************************************
 *
 * DEBUG DATOS GENERICOS FPS, POSICIONES, ETC.
 *
 *******************************************************
 */

public void showFPS(boolean isShow) {
  if (isShow) {
    fill(57, 255, 20);
    textAlign(BASELINE);
    textSize(18);
    text("FPS: " + PApplet.parseInt(frameRate), 20, 20);
  }
}

public void showPositions(float posX, float posY) {
  fill(155);
  textSize(18);
  text("POS X: " + PApplet.parseInt(posX), 20, 40);
  text("POS Y: " + PApplet.parseInt(posY), 20, 60);
}

/*
  *******************************************************
 *
 * CASTEO DE OBJETOS
 *
 *******************************************************
 */

public ArrayList<Ball> ObjectsToBalls(Player player) {
  ArrayList<Ball> balls = new ArrayList<Ball>();

  for (int i = 0; i < player.getHability(1).getObjects().size(); i++) {
    balls.add((Ball)player.getHability(1).getObjects().get(i));
  }

  return balls;
}

/*
  *******************************************************
 *
 * CALCULAR COLISION CIRCLE VS RECT
 *
 *******************************************************
 */

public boolean circleRectColission(float cx, float cy, float radius, float rx, float ry, float rw, float rh) {

  // temporary variables to set edges for testing
  float testX = cx;
  float testY = cy;

  // which edge is closest?
  if (cx < rx)         testX = rx;      // left edge
  else if (cx > rx+rw) testX = rx+rw;   // right edge
  if (cy < ry)         testY = ry;      // top edge
  else if (cy > ry+rh) testY = ry+rh;   // bottom edge

  // get distance from closest edges
  float distX = cx-testX;
  float distY = cy-testY;
  float distance = sqrt( (distX*distX) + (distY*distY) );

  // if the distance is less than the radius, collision!
  if (distance <= radius) {
    return true;
  }
  return false;
}
class Map {

  PShader nebula, galaxy;
  private GestorNiveles gn;

  public Map(GestorNiveles gn) {
    this.gn = gn;
    init();
  }

  public void init() {
    noStroke();
    nebula = loadShader("nebula.glsl");
    nebula.set("resolution", PApplet.parseFloat(WIDTH), PApplet.parseFloat(HEIGHT));

    galaxy = loadShader("galaxy.glsl");
    galaxy.set("resolution", PApplet.parseFloat(WIDTH), PApplet.parseFloat(HEIGHT));
  }

  public void update() {
    switch(gn.getLevel()) {
    case 1:
      fill(255);
      nebula.set("time", millis() / 500.0f);//NULL ? AL COMPLETAR EL 2N NIVEL  
      shader(nebula);
      rect(0, 0, WIDTH, HEIGHT);
      resetShader();
      break;
    case 2:
      fill(255);
      galaxy.set("time", millis() / 5000.0f);//NULL ? AL COMPLETAR EL 2N NIVEL  
      shader(galaxy);
      rect(0, 0, WIDTH, HEIGHT);
      resetShader();
      break;
    case 3:
      fill(255);
      galaxy.set("time", millis() / 5000.0f);//NULL ? AL COMPLETAR EL 2N NIVEL  
      shader(galaxy);
      rect(0, 0, WIDTH, HEIGHT);
      resetShader();
      break;
    default:
      background(0);
      break;
    }
  }
}
class Meteorito extends Monster {

  private int contador = 0;
  private char direct;

  public Meteorito(Player player, PVector pos, PVector tDirection, char direct) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 4;
    init_monster(player, tDirection);
    this.score = 2;
    this.rad = METEORITO_RAD;
    this.isFollower = false;
    this.direct = direct;
    this.id = 3;
  }

  public void init_monster(Player player, PVector tDirection) {
    setPlayer(player);
    setTarget(tDirection);
    c = color(0xff3399cc);
  }

  @Override
    public void updatePaint(ArrayList<Bala> balas) {
    pos = new PVector(pos.x, pos.y);
    colision(balas);
    paint();
  }

  public void paint() {
    noFill();
    strokeWeight(4);
    stroke(c);

    pushMatrix();
    translate(pos.x, pos.y);
    contador++;
    rotate(contador);

    for (int i = 0; i<4; i++) {
      rotate(HALF_PI*i);
      triangle(0, 0, 1*30f, 1*30f, 0, 1.41f*30f);//RAD NO ES TAMAÑO REAL.
    }
    //debugArea(rad);
    popMatrix();
  }

  public void colision(ArrayList<Bala> balas) {
    //INTERACCIONA CON EL PLAYER
    if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+rad/2) {
      this.player.decreaseLife();
    }
    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+rad/2) {
        balas.get(i).isDie = true;
      }
      i++;
    }
    if (this.player.getHability(1).isEquiped) {
      ArrayList<Ball> balls = ObjectsToBalls(this.player);
      int ind = 0;
      while (!this.isDie && ind < balls.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+rad/2) {
          balls.get(ind).isDie = true;
        }
        ind++;
      }
    }
    switch(this.direct) {
    case 'D':
      if (this.pos.y > HEIGHT+this.rad) {
        this.isDie = true;
      }
      break;
    case 'L':
      if (this.pos.x <= -this.rad) {
        this.isDie = true;
      }
      break;
    }
  }
}
class Monster {
  protected int id;
  protected PVector pos, speed, acc;
  protected float maxSpeed;
  protected int c;
  protected PVector target;
  protected int health;
  protected boolean inmortal = true;
  protected boolean animationDestroy = true;

  protected Player player;
  protected int score;
  private int inmortalTimer;
  private float inmortalTimerFrame = FRAMES;

  float rad = 20f;

  protected boolean isDie = false;
  protected boolean isMovil = true;
  protected boolean isFollower = true;

  public void setPlayer(Player player) {
    this.player = player;
  }

  public void setLoc(PVector loc) {
    this.pos = new PVector(loc.x, loc.y);
  }

  public void setTarget(PVector ta) {
    this.target = new PVector(ta.x, ta.y);
  }

  public void move() {
    calAcc();
    calSpeed();
    calPos();
    this.acc = new PVector();
  }

  private void calAcc() {
    PVector findTarget = new PVector(this.target.x-this.pos.x, this.target.y-this.pos.y);
    this.acc.add(findTarget);
  }

  private void calSpeed() {
    this.speed.add(this.acc);
    this.speed.limit(this.maxSpeed);
  }

  private void calPos() {
    this.pos.add(this.speed);
  }

  public void update() {
    if (this.isMovil) {
      if (this.isFollower) {
        setTarget(player.pos);
      }
      move();
    }

    timerInmortal();
  }

  public void updatePaint(ArrayList<Bala> balas) {
    println("NADA");
  }

  private void timerInmortal() {
    this.inmortalTimer++;
    if (this.inmortalTimer > this.inmortalTimerFrame) {
      this.inmortal = false;
      this.inmortalTimer = 0;
    }
  }
}
class MonsterBoss extends Monster{

  /*
  * BOSS. Inicialmente se posicionara moviendose
  * Contendra disparos e invocara naves
  */
  boolean isStarted;
  private PVector hitBox = new PVector(WIDTH,HEIGHT/2);
  private int shield = 40;
  private int fase = 1;
  boolean loadShooters = false;
  private int puntoDebil = color(0xffF50A0A);
  private int shieldColor = color(0xff9B9B9B);
  private float timer;
  private final float timerFrame = (0.5f*FRAMES);
  boolean animationDead;
  
  public MonsterBoss(Player player,PVector pos){
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 1;
    init_monster(player);
    this.score = 50;
    this.isStarted = false;
    this.rad = 560f;
    this.animationDead = true;
  }
  
  public void init_monster(Player player){
    setPlayer(player);
    c = color(255);
  }
  
  public void updateBoss(ArrayList<Bala> balas){
   if(pos.x >= WIDTH-(WIDTH/4) && !this.isStarted){
     pos = new PVector(pos.x, 0);
   }else{
     this.isStarted = true;
     pos = new PVector(WIDTH-(WIDTH/4),0);
     
     //MECANICAS BOSS
     colision(balas);
   }
  }
  
  public void paint(){
    timerColor();
    noStroke();
    int w = 60;
    int h = HEIGHT-(HEIGHT/4 + HEIGHT/4);
    
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);//POSICIONAMIENTO
 
    fill(puntoDebil);
    ellipse(WIDTH/4,HEIGHT/2,this.rad-300f,this.rad-300f);//PUNTO DEBIL CENTRAL
    
    fill(255);
    triangle( WIDTH/4, 0, WIDTH/4, HEIGHT/4, 0, HEIGHT/4);
    rect((WIDTH/4)-w,HEIGHT/4,w,h);
    triangle(0, HEIGHT-(HEIGHT/4), WIDTH/4, HEIGHT-(HEIGHT/4), WIDTH/4, HEIGHT);
    fill(shieldColor, 50);
    stroke(c);
    strokeWeight(4);
    if(shield > 0 && this.fase <= 3){
      stroke(shieldColor);
      ellipse(WIDTH/4,HEIGHT/2,this.rad,this.rad);
    }

    //debugAreaPoint(this.rad-80f,WIDTH/4,HEIGHT/2);
    //debugAreaPoint(this.rad,WIDTH/4,HEIGHT/2);
    popMatrix();
    
    
  }
  
   public void timerColor(){
    timer++;
    if(this.puntoDebil == color(0xffD66D0B) && timer >= timerFrame){
      this.puntoDebil = color(0xffF50A0A);
      timer = 0;
    }
    if(this.shieldColor == COLOR_DMG && timer >= timerFrame){
      this.shieldColor = color(0xff9B9B9B);
      timer = 0;
    }
  }
  
  public void colision(ArrayList<Bala> balas){
    if(this.fase != 3){
        //INTERACCIONA CON EL PLAYER
      if(PVector.dist(hitBox,this.player.pos)<=this.player.r/2+rad/2){
        this.player.decreaseLife();
      }
    }else{
      //INTERACCIONA CON EL PLAYER
      if(PVector.dist(hitBox,this.player.pos)<=this.player.r/2+(rad-80f)/2){
        this.player.decreaseLife();
      }
    }
    
    int i = 0;
    while(!this.isDie && i < balas.size()){
      //INTERSECCION ENTRE BALA Y BICHO
      if(this.fase < 3){
        if(PVector.dist(hitBox,balas.get(i).pos)<=balas.get(i).rad/2+this.rad/2){
          if(shield > 0 && this.fase == 2){
            this.shieldColor = COLOR_DMG;
            shield--;
          }
          balas.get(i).isDie = true;
        }
      }else if(this.fase == 4){
        if(PVector.dist(hitBox,balas.get(i).pos)<=balas.get(i).rad/2+(this.rad-220f)/2){
          if(shield > 0){
            this.puntoDebil = color(0xffD66D0B);
            shield--;
          }
          balas.get(i).isDie = true;
        }
      }

      i++;
    }
  }
  
  public boolean getIsStarted(){
    return this.isStarted;
  }
  
  public void setHitBox(int w, int h){
    this.hitBox = new PVector(w,h);
  }
  
  public int getFase(){
    return this.fase;
  }
  
  public void setFase(int fase){
    this.fase = fase;
  }
  
  public void setShield(int shield){
    this.shield = shield;
  }

}


class MonsterBossV2 extends Monster {
  /*
  * BOSS centro disparando.
  *
  */

  boolean isStarted;
  private final int maxHealth = 80;
  private int health;
  private int fase = 1;
  private float timer;
  private final float timerFrame = FRAMES/4;
  private ArrayList<Ball> balls;
  private int timerBurst = 0;
  private int timerBurstFrame = (5*FRAMES);
  private boolean shieldActive = true;
  private int[] numBalls = {8, 16, 20, 24};
  private int timerRage = 0;
  private final float timerRageFrames = (5*FRAMES);
  private int timerColor = 0;
  private final float timerColorFrame = (0.2f*FRAMES);
  private boolean isValid = false;
  private int probabilityRage = 3;// 1/X
  private int timerProbRage = 0;
  private final float timerProbRageFrames = (10*FRAMES);

  protected boolean needShips = true;
  protected boolean isRage;
  
  boolean animationDead;


  public MonsterBossV2(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 3;
    init_monster(player);
    this.score = 50;
    this.isStarted = false;
    this.rad = 100f;
    this.balls = new ArrayList<Ball>();
    this.isRage = false;
    this.health = this.maxHealth;
    this.animationDead = true;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    setTarget(new PVector(CENTRO_VENTANA_X, CENTRO_VENTANA_Y));
    c = color(255);
  }

  public void updateBoss(ArrayList<Bala> balas) {
    if (pos.y < CENTRO_VENTANA_Y-2 && !this.isStarted) {
      pos = new PVector(CENTRO_VENTANA_X, pos.y);
    } else {
      this.isStarted = true;
      pos = new PVector(CENTRO_VENTANA_X, CENTRO_VENTANA_Y);
      //DISPAROS EN EXPANSIVO SOLO CENTRO DE PANTALLA
      timerShot();
    }
    if (!this.isRage) {
      timerProbRage++;
      if (this.health <= 20) {
        this.timerBurstFrame = (2*FRAMES);
      }
    }
    if (this.maxHealth-(this.maxHealth/4) >= this.health && !this.isRage && timerProbRage >= timerProbRageFrames && !this.shieldActive) {
      boolean enter = new Random().nextInt(this.probabilityRage)==0;
      if (enter) {
        this.isRage = true;
        this.c = COLOR_RAGE;
        this.timerBurstFrame = FRAMES/2;
      }
      this.timerProbRage = 0;
    }

    if (this.isRage) {
      timerRage++;
      if (timerRage >= timerRageFrames) {
        this.isRage = false;
        this.c = color(255);
        this.timerRage = 0;
        this.timerBurstFrame = (5*FRAMES);
      }
    }

    //debugValue("IS RAGE",int(this.isRage),20,110);
    updateBalls();
    colision(balas);
  }

  private void updateBalls() {
    for (int i = 0; i < balls.size(); i++) {
      Ball ball = balls.get(i);
      ball.update();
      if (ball.isDie) {
        balls.remove(i);
      }
    }
  }

  public void paint() {
    timerColor();

    stroke(0);
    strokeWeight(1);
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);//POSICIONAMIENTO
    ellipse(0, 0, this.rad, this.rad);
    ellipse(-20, 0, 16, 32);
    ellipse(10, 0, 16, 32);
    if (this.shieldActive) {
      fill(COLOR_INMORTAL, 50);
      stroke(COLOR_INMORTAL);
      strokeWeight(4);
      ellipse(0, 0, this.rad+20f, this.rad+20f);
    }
    popMatrix();
    //debugValue("VIDA BOSS", this.health, 20, 90);
  }

  public void timerShot() {
    stroke(255);
    strokeWeight(4);
    timer++;
    timerBurst++;
    if (timer >= timerFrame && timerBurst < timerBurstFrame) {
      this.balls.add(new Ball(this.pos, this.player.pos, 6));
      timer = 0;
    }

    if (timerBurst >= timerBurstFrame) {
      float points = randomBalls();
      float pointAngle = 360/points;

      for (float angle = 0; angle < 360; angle = angle+pointAngle) {
        float x = cos(radians(angle)) * CENTRO_VENTANA_X;
        float y = sin(radians(angle)) * CENTRO_VENTANA_Y;
        this.balls.add(new Ball(this.pos, new PVector(x+CENTRO_VENTANA_X, y+CENTRO_VENTANA_Y), 4));
      }
      timer = -80;
      timerBurst = 0;
    }
  }

  public void timerColor() {
    if (this.c == COLOR_DMG) {
      timerColor++;
      if (timerColor >= timerColorFrame) {
        if (this.isRage) {
          this.c = color(COLOR_RAGE);
        } else {
          this.c = color(255);
        }
        timerColor = 0;
      }
    }
  }

  public void colision(ArrayList<Bala> balas) {
    if (this.player.getHability(0).isActive) {//OPTIMIZAR -> PELIGRO DE ERROR
      int ind = 0;
      while (!over && ind < balls.size()) {
        //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER CON ESCUDO
        if (PVector.dist(this.player.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+this.player.getHability(0).getRad()/2) {
          balls.get(ind).isDie = true;
        }
        ind++;
      }
    } else {
      int ind = 0;
      while (!over && ind < balls.size()) {
        //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
        if (PVector.dist(this.player.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+this.player.r/2) {
          balls.get(ind).isDie = true;
          this.player.decreaseLife();
        }
        ind++;
      }
    }


    if (!this.shieldActive && PVector.dist(this.pos, this.player.pos)<=this.player.r/2+this.rad/2) {
      this.player.decreaseLife();
    } else if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+(this.rad+20)/2) {
      this.player.decreaseLife();
    }

    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (this.shieldActive) {
        if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+(this.rad+20f)/2) {
          balas.get(i).isDie = true;
        }
      } else {
        if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+this.rad/2) {
          if (health > 0) {
            health--;
            this.isValid = true;
            this.c = color(COLOR_DMG);
          } else {
            this.isDie = true;
          }
          balas.get(i).isDie = true;
        }
      }
      i++;
    }
  }

  public void timerShield() {
    if (this.isValid) {
      switch(this.health) {
      case 20:
        this.needShips = true;
        this.shieldActive = true;
        this.isValid = false;
        break;
      case 40:
        this.needShips = true;
        this.shieldActive = true;
        this.isValid = false;
        break;
      case 60:
        this.needShips = true;
        this.shieldActive = true;
        this.isValid = false;
        break;
      }
    }
  }

  private int randomBalls() {
    int index = PApplet.parseInt(random(numBalls.length));
    return numBalls[index];
  }

  public boolean getIsStarted() {
    return this.isStarted;
  }

  public int getFase() {
    return this.fase;
  }

  public void setFase(int fase) {
    this.fase = fase;
  }

  public void setShieldActive(boolean shieldActive) {
    this.shieldActive = shieldActive;
  }

  public boolean getShieldActive() {
    return this.shieldActive;
  }
}
class MonsterBossV3 extends Monster {

  /*
  ************************************************************* 
   *      COPY FOLLOWER
   *************************************************************
   */
  boolean isStarted;
  private int health;
  private int fase = 1;
  boolean loadShooters = false;
  private float timer;
  private final float timerFrame = (0.5f*FRAMES);
  boolean animationDead;
  private float timerShot;
  private final float timerShotFrames = (FRAMES/2);
  private ArrayList<Ball> balls;
  private boolean shieldActive;

  public MonsterBossV3(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 2;
    init_monster(player);
    this.score = 50;
    //this.isStarted = false;
    this.rad = BOSS_V3_RAD;
    this.animationDead = true;
    this.health = 250;
    this.balls = new ArrayList<Ball>();
    this.isMovil = true;
    this.shieldActive = false;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = COLOR_ORANGE;
  }

  public void updateBoss(ArrayList<Bala> balas) {
    pos = new PVector(pos.x, pos.y);

    //MECANICAS BOSS
    timerShot();
    updateBalls();
    colision(balas);
  }

  public void paint() {
    //timerColor();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(PI/4);
    body();
    if (this.shieldActive) {
      shield();
    }
    popMatrix();
  }

  private void body() {
    noFill();
    strokeWeight(3);
    stroke(COLOR_INMORTAL);
    rect(-this.rad/2, -this.rad/2, this.rad, this.rad);
    fill(this.c);
    ellipse(0, 0, this.rad/2, this.rad/2);
  }

  private void shield() {
    fill(COLOR_INMORTAL, 50);
    stroke(COLOR_INMORTAL);
    strokeWeight(4);
    ellipse(0, 0, this.rad+20f, this.rad+20f);
  }

  private void timerShot() {
    timerShot++;
    if (timerShot >= timerShotFrames) {
      addBalls();
      timerShot = 0;
    }
  }

  private void updateBalls() {
    for (int i = 0; i < balls.size(); i++) {
      Ball ball = balls.get(i);
      ball.update();
      if (ball.isDie) {
        balls.remove(i);
      }
    }
  }

  public void timerColor() {
    timer++;
    //if (this.puntoDebil == color(#D66D0B) && timer >= timerFrame) {
    //this.puntoDebil = color(#F50A0A);
    timer = 0;
    //}
  }

  public void colision(ArrayList<Bala> balas) {
    //ESCUDO DETECTOR DE BALAS CON CD


    //INTERACCIONA CON EL PLAYER
    if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+rad/2) {
      this.player.decreaseLife();
    }

    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+rad/2) {
        balas.get(i).isDie = true;
        this.health--;
        if (this.health <= 0) {
          this.isDie = true;
        }
        // this.c = COLOR_DMG;
      }
      i++;
    }

    if (this.player.getHability(1).isEquiped) {
      ArrayList<Ball> ballsP = ObjectsToBalls(this.player);

      int index = 0;
      while (!this.isDie && index < ballsP.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, ballsP.get(index).pos)<=ballsP.get(index).rad/2+rad/2) {
          ballsP.get(index).isDie = true;
          if (this.health <= 0) {
            this.isDie = true;
          }
          //  this.c = COLOR_DMG;
        }
        index++;
      }
    }

    //VALIDAR BALLS DEL ENEMIGO AUN ESTANDO MUERTO.
    if (this.player.getHability(0).isActive) {//OPTIMIZAR -> PELIGRO DE ERROR
      int ind = 0;
      while (!over && ind < balls.size()) {
        //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER CON ESCUDO
        if (PVector.dist(this.player.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+this.player.getHability(0).getRad()/2) {
          balls.get(ind).isDie = true;
        }
        ind++;
      }
    } else {
      int ind = 0;
      while (!over && ind < this.balls.size()) {
        //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
        if (PVector.dist(this.player.pos, this.balls.get(ind).pos)<=this.balls.get(ind).rad/2+this.player.r/2) {
          this.balls.get(ind).isDie = true;
          this.player.decreaseLife();
        }
        ind++;
      }
    }
  }


  public boolean getIsStarted() {
    return this.isStarted;
  }

  public int getFase() {
    return this.fase;
  }

  public void setFase(int fase) {
    this.fase = fase;
  }

  private void addBalls() {
    Ball ball = new Ball(this.pos, this.player.pos, 8);
    ball.setColor(COLOR_ORANGE);
    ball.setRad(20f);
    this.balls.add(ball);
  }
}
class Monster_easy extends Monster {

  private int timerColor = 0;
  private final float timerColorFrame = (0.2f*FRAMES);

  public Monster_easy(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 1;
    init_monster(player);
    this.score = 3;
    this.id = 0;
    this.rad = MONSTER_EASY_RAD;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = color(0xff3399cc);
  }

  @Override
    public void updatePaint(ArrayList<Bala> balas) {
    pos = new PVector(pos.x, pos.y);
    if (!this.inmortal) {
      colision(balas);
    }
    paint();
  }

  public void paint() {
    if (this.inmortal) {
      timerColor();
    } else {
      this.c = color(0xff3399cc);
    }
    noFill();
    strokeWeight(4);
    stroke(c);

    pushMatrix();
    translate(pos.x, pos.y);
    rect(-7, -7, 15, 15);
    //debugArea(rad);
    popMatrix();
  }

  public void colision(ArrayList<Bala> balas) {
    //INTERACCIONA CON EL PLAYER
    if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+rad/2) {
      this.player.decreaseLife();
    }
    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+rad/2) {
        balas.get(i).isDie = true;
        this.isDie = true;
      }
      i++;
    }

    if (this.player.getHability(1).isEquiped) {
      ArrayList<Ball> balls = ObjectsToBalls(this.player);
      int ind = 0;
      while (!this.isDie && ind < balls.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+rad/2) {
          balls.get(ind).isDie = true;
          this.isDie = true;
        }
        ind++;
      }
    }
  }

  private void timerColor() {
    this.timerColor++;
    if (this.timerColor > this.timerColorFrame) {
      if (this.c == color(0xff3399cc)) {
        this.c = COLOR_INMORTAL;
      } else {
        this.c = color(0xff3399cc);
      }
      this.timerColor = 0;
    }
  }
  
}
class Monster_shooter extends Monster {

  /*
  * MONSTRUO NO MOVIL, QUE DISPARARA EN UNA UNICA DIRECCION QUE SE DETERMINARA POR UN RANDOM DE 8.
   */

  GestorDisparosEnemigos gde;
  private float timer;
  private final float timerFrame = (0.5f*FRAMES);

  private int timerColor = 0;
  private final float timerColorFrame = (0.2f*FRAMES);

  public Monster_shooter(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 0;
    init_monster(player);
    this.score = 5;
    this.health = 1;
    this.gde = new GestorDisparosEnemigos(this.pos);
    this.isMovil = false;
    this.id = 1;
    this.rad = MONSTER_SHOT_RAD;
  }

  public Monster_shooter(Player player, PVector pos, int health) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 0;
    init_monster(player);
    this.score = 0;
    this.health = health;
    this.gde = new GestorDisparosEnemigos(this.pos);
    this.rad = 35f;
    this.isMovil = false;
    this.isFollower = false;
    this.id = 1;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = color(0xffFAD91C);
  }

  @Override
    public void updatePaint(ArrayList<Bala> balas) {
    //pos = new PVector(pos.x, pos.y);
    if (!this.inmortal) {
      gde.update();
      colision(balas, gde.getBalas());
    }
    paint();
  }

  public void paint() {
    timerColor();
    fill(c);
    strokeWeight(4);
    stroke(c);

    pushMatrix();
    translate(pos.x, pos.y);
    ellipse(0, 0, rad, rad);
    //debugArea(rad);
    popMatrix();
  }

  public void timerColor() {
    timer++;
    if (this.c == COLOR_DMG && timer >= timerFrame) {
      this.c = color(0xffFAD91C);
      timer = 0;
    }

    if (this.inmortal) {
      this.timerColor++;
      if (this.timerColor > this.timerColorFrame) {
        if (this.c == color(0xffFAD91C)) {
          this.c = COLOR_INMORTAL;
        } else {
          this.c = color(0xffFAD91C);
        }
        this.timerColor = 0;
      }
    } else if (this.c == COLOR_INMORTAL) {
      this.c = color(0xffFAD91C);
    }
  }

  public void colision(ArrayList<Bala> balas, ArrayList<Bala> balasE) {
    //INTERACCIONA CON EL PLAYER
    if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+rad/2) {
      this.player.decreaseLife();
    }

    int ind = 0;
    while (!over && ind < balasE.size()) {
      //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
      if (PVector.dist(this.player.pos, balasE.get(ind).pos)<=balasE.get(ind).rad/2+this.player.r/2) {
        balasE.get(ind).isDie = true;
        this.player.decreaseLife();
      }
      ind++;
    }

    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+rad/2) {
        balas.get(i).isDie = true;
        this.health--;
        if (this.health <= 0) {
          this.isDie = true;
        }
        this.c = COLOR_DMG;
      }
      i++;
    }
    
    if (this.player.getHability(1).isEquiped) {
      ArrayList<Ball> balls = ObjectsToBalls(this.player);
      int index = 0;
      while (!this.isDie && index < balls.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, balls.get(index).pos)<=balls.get(index).rad/2+rad/2) {
          balls.get(index).isDie = true;
          this.isDie = true;
        }
        ind++;
      }
    }
    
  }

  public void setTarget(String target) {
    this.gde.setTarget(target);
  }
}
class MonsterWifi extends Monster {
  private int timerColor = 0;
  private final float timerColorFrame = (0.2f*FRAMES);
  private int timerAnimation = 0;
  private int timerAnimationFrame = 3;
  private int cont = 0;

  public MonsterWifi(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 2;
    init_monster(player);
    this.score = 8;
    this.rad = MONSTER_WIFI_RAD;
    this.id = 2;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = color(0xff3BF200);
  }

  @Override
    public void updatePaint(ArrayList<Bala> balas) {
    pos = new PVector(pos.x, pos.y);
    if (!this.inmortal) {
      colision(balas);
    }
    paint();
  }

  public void paint() {
    if (this.inmortal) {
      timerColor();
    } else {
      this.c = color(0xff3BF200);
    }

    noStroke();
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);
    ellipse(0, 0, this.rad-20f, this.rad-20f);
    //debugArea(rad);
    timer();
    popMatrix();
  }

  public void timer() {
    noFill();
    stroke(c);
    strokeWeight(1);
    timerAnimation++;
    if (timerAnimation >= timerAnimationFrame) {
      cont++;
      if (cont%2==0) {
        ellipse(0, 0, 30, 30);
        cont = 0;
      } else {
        ellipse(0, 0, 30, 30);
        ellipse(0, 0, 40, 40);
      }

      timerAnimation = 0;
    }
  }

  public void colision(ArrayList<Bala> balas) {
    //INTERACCIONA CON EL PLAYER
    if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+rad/2) {
      //finalScore = this.player.getScore();
      //over = true;
      this.player.decreaseLife();
    }
    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+rad/2) {
        balas.get(i).isDie = true;
        this.isDie = true;
      }
      i++;
    }
    if (this.player.getHability(1).isEquiped) {
      ArrayList<Ball> balls = ObjectsToBalls(this.player);
      int ind = 0;
      while (!this.isDie && ind < balls.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+rad/2) {
          balls.get(ind).isDie = true;
          this.isDie = true;
        }
        ind++;
      }
    }
  }

  private void timerColor() {
    this.timerColor++;
    if (this.timerColor > this.timerColorFrame) {
      if (this.c == color(0xff3BF200)) {
        this.c = COLOR_INMORTAL;
      } else {
        this.c = color(0xff3BF200);
      }
      this.timerColor = 0;
    }
  }
}
class MultiShot extends Habilities {
  public ArrayList<Ball> balls;
  private Player player;

  MultiShot(Player player) {
    this.pos = new PVector(0, 0);
    this.isReady = true;
    this.coldown = (8*FRAMES);//AMPLIAR CD MUCHO
    this.lvlRequired = 3;
    this.player = player;
    this.balls = new ArrayList<Ball>();
  }

  public void update() {
    Actionhability();
    updateBalls();
    timer();
    move();
    paint();
  }
  public void timer() {
    if (!this.isReady) {
      this.timer++;
      if (this.timer >= this.coldown) {
        this.isReady = true;
        this.timer = 0;
      }
    }
  }

  public void paint() {
    if (this.isReady) {
      //DIBUJAR HABILIDAD EN LA INTERFAZ
      noFill();
      stroke(0xffF79E0C);
      strokeWeight(2);
      ellipse(CENTRO_VENTANA_X+30, 40, 20, 20);
    }
  }

  private void Actionhability() {
    if (this.isReady) {
      if (KEYBOARD.activeMultiShot) {
        addShoot();
        this.isReady = false;
      }
    }
  }

  private void updateBalls() {
    for (int i = 0; i < balls.size(); i++) {
      Ball ball = balls.get(i);
      ball.update();
      if (ball.isDie) {
        balls.remove(i);
      }
    }
  }

  private void addShoot() {
    float points = 16;
    float pointAngle = 360/points;

    for (float angle = 0; angle < 360; angle = angle+pointAngle) {
      float x = cos(radians(angle)) * CENTRO_VENTANA_X;
      float y = sin(radians(angle)) * CENTRO_VENTANA_Y;
      Ball ball = new Ball(this.pos, new PVector(x+this.pos.x, y+this.pos.y), 6);
      ball.setColor(color(255));
      this.balls.add(ball);
    }
  }

  public void move() {
    this.pos = this.player.pos;
  }

  public float getRad() {
    return 0;
  }

  public void setIsEquiped(boolean isEquiped) {
    this.isEquiped = isEquiped;
  }
  
  public ArrayList<Object> getObjects(){
    ArrayList<Object> objects = new ArrayList<Object>();
    
    for(int i = 0; i < this.balls.size(); i++){
      objects.add(this.balls.get(i));
    }
    
    return objects;
  }
  
}
class Particle{
  private PVector position, speed, acc;
  private float lifespan;
  private int c;
  
  Particle(PVector pos, int cl){
    this.acc = new PVector(random(-1,1), random(-1,1));
    this.speed = new PVector(0, 0);
    this.position = pos.copy();
    this.lifespan = 255.0f;
    this.c = cl;
  }
  
  public void update(){
    move();
    lifespan -= 10.0f;
    paint();
  }
  
  private void move(){
    this.speed.add(this.acc);
    this.position.add(this.speed);    
  }
  
  public void paint(){
    noStroke();
    fill(c, lifespan);
    pushMatrix();
    translate(this.position.x, this.position.y);
    rect(0,0,3,3);
    popMatrix();
  }
  
  public boolean isDead(){
    if (this.lifespan < 0.0f) {
      return true;
    } else {
      return false;
    }
  }
}
class ParticleSystem {
  ArrayList<Particle> particles;
  PVector origin;

  ParticleSystem(PVector position) {
    this.origin = position.copy();
    particles = new ArrayList<Particle>();
  }

  public void addParticle(int i, int c) {
    for (int y = 0; y < i; y++) {
      particles.add(new Particle(origin, c));
    }
  }

  public void update() {
    for (int i = particles.size()-1; i >= 0; i--) {
      Particle p = particles.get(i);
      p.update();
      if (p.isDead()) {
        particles.remove(i);
      }
    }
  }
  
  public boolean isEmpty(){
    if(particles.isEmpty()){
      return true;
    }else{
      return false;
    }
  }
}
class Player { //<>// //<>// //<>//
  private PVector pos, speed, acc;

  private float maxSpeed;
  final float r = 30f;
  //final float radShield = r+20f;

  private boolean isAuto;

  private int score;
  private int targetAutoX;
  private int targetAutoY;
  private int health;
  private final int MAX_HABILITIES = 2;
  private int lvl;

  private Habilities[] habilities;

  public Player(int x, int y) {
    pos = new PVector(x, y);
    this.speed = new PVector(0, 0);
    this.acc = new PVector(0, 0);
    this.maxSpeed = 5;
    this.score = 0;
    this.health = 1;
    this.isAuto = false;
    this.targetAutoX = CENTRO_VENTANA_X;
    this.targetAutoY = CENTRO_VENTANA_Y;
    this.lvl = gestorNiveles.getLevel();
    this.habilities = new Habilities[MAX_HABILITIES];
    addHabilities();
  }

  public void update() {
    if (!this.isAuto) {
      direction();
      move();
    } else {
      autoMove();
    }
    validateHabilities();
    updateHabilities();
    paint();
  }

  public void paint() {
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(PI/4);
    body();
    popMatrix();
    //showPositions(this.pos.x, this.pos.y);
  }

  private void body() {
    noStroke();
    fill(COLOR_ORANGE);
    rect(-10, -10, 20, 20);
  }

  private void move() {
    if (this.acc.x == 0 && this.acc.y == 0) {
      speed.mult(0.9f);
    } else { 
      speed.add(this.acc);
      speed.limit(maxSpeed);
    }      
    pos.add(speed);
    collision();
  }

  private void direction() {
    PVector direction = new PVector(0, 0);

    if (KEYBOARD.up) {
      direction.add(new PVector(0, -1));
    }
    if (KEYBOARD.left) {
      direction.add(new PVector(-1, 0));
    }
    if (KEYBOARD.down) {
      direction.add(new PVector(0, 1));
    }
    if (KEYBOARD.right) {
      direction.add(new PVector(1, 0));
    } 

    this.acc = direction;
  }

  public void autoMove() {
    PVector findTarget = new PVector(this.targetAutoX-this.pos.x+2, this.targetAutoY-this.pos.y+2);

    if (abs(PApplet.parseInt(findTarget.x)) >= 0 && abs(PApplet.parseInt(findTarget.x)) <= 4 && abs(PApplet.parseInt(findTarget.y)) <= 4 && abs(PApplet.parseInt(findTarget.y)) >= 0 ) {
      return;
    }

    this.acc.add(findTarget);

    this.speed.add(this.acc);
    this.speed.limit(this.maxSpeed);

    this.pos.add(this.speed);

    this.acc = new PVector();
  }

  private void collision() {
    float x = range(this.pos.x, r/2, WIDTH-r/2);
    float y = range(this.pos.y, r/2, HEIGHT-r/2);

    this.pos = new PVector(x, y);
  }

  public void setScore(int score) {
    this.score += score;
  }

  public int getScore() {
    return this.score;
  }

  public void showScore() {
    fill(255);
    textSize(18f);
    text("SCORE: " + this.score, CENTRO_VENTANA_X-10, 20);
  }
  //OPTIMIZABLE
  public void updateHabilities() {
    if (gestorNiveles.getLevel() >= 2) {
      for (int i = 0; i < habilities.length; i++) {
        if (!habilities[i].isEquiped) {
          if (habilities[i].lvlRequired <= gestorNiveles.getLevel()) {
            habilities[i].setIsEquiped(true);
          } else {
            continue;
          }
        }
        habilities[i].update();
      }
    }
  }

  public void addHabilities() {
      habilities[0] = new Shield(this);
      habilities[1] = new MultiShot(this);
  }

  public void validateHabilities() {
    if (gestorNiveles.getLevel() != this.lvl) {
      this.lvl = gestorNiveles.getLevel();
      addHabilities();
    }
  }

  public Habilities getHability(int id) {
    return habilities[id];
  }

  public void setAutoMove(boolean isAuto) {
    this.isAuto = isAuto;
  }

  public boolean getIsAutoMove() {
    return this.isAuto;
  }

  public void setTargetAutoX(int x) {
    this.targetAutoX = x;
  }

  public void setTargetAutoY(int y) {
    this.targetAutoY = y;
  }

  public void reset() {
    pos = new PVector(CENTRO_VENTANA_X, CENTRO_VENTANA_Y);
    this.speed = new PVector(0, 0);
    this.acc = new PVector(0, 0);
    this.score = 0;
    this.isAuto = false;
    this.targetAutoX = CENTRO_VENTANA_X;
    this.targetAutoY = CENTRO_VENTANA_Y;
    this.habilities = new Habilities[MAX_HABILITIES];
    addHabilities();
  }

  public void decreaseLife() {
    if ((this.health - 1) <= 0) {
      this.health = 0;
    } else {
      this.health -= 1;
    }
    if (this.health == 0) {
      finalScore = this.score;
      over = true;
    }
  }
}
class Seccion {

  private String title;
  private int posX;
  private int posY;
  private float sizeText;

  public Seccion(String title, int posX, int posY) {
    this.title = title;
    this.posX = posX;
    this.posY = posY;
    this.sizeText = 32f;
  }

  public Seccion(String title, int posX, int posY, float sizeText) {
    this.title = title;
    this.posX = posX;
    this.posY = posY;
    this.sizeText = sizeText;
  }

  public void update() {
    noStroke();
    fill(255);
    textAlign(CENTER);
    textSize(this.sizeText);
    text(this.title, posX, posY);
  }

  //CONTORNO
  public void selected() {
    fill(255);
    rect(this.posX-150, this.posY-15, 25, 10);
  }
}
class SeccionLvl {
  private String title;
  private String score;
  private int posX;
  private int posY;
  private float sizeText;
  private int c;
  private int colorSelected;
  private boolean isSelected;

  public SeccionLvl(String title, String score, int posX, int posY) {
    this.title = title;
    this.score = score;
    this.posX = posX;
    this.posY = posY;
    this.sizeText = 32f;
    this.c = color(255);
    this.colorSelected = color(COLOR_ORANGE);
    this.isSelected = false;
  }

  public void update() {
    pushMatrix();
    translate(posX, posY);
    rotate(PI/4);
    stroke(255);

    if (this.isSelected) {
      fill(COLOR_ORANGE);
    } else {
      noFill();
    }

    rectMode(CENTER);
    rect(0, 0, 128, 128);
    rectMode(CORNER);
    popMatrix();

    fill(c);
    textSize(18f);
    textAlign(CENTER);
    text(this.title, posX, posY-20);
    text("SCORE: " + this.score, posX, posY+20);
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }
  
  public int getPosX(){
    return this.posX;
  }
  
  public int getPosY(){
    return this.posY;
  }
}
class SelectLvl implements EstadoJuego {
  // LOAD JSON DADAS DE LAS PARTIDAS QUE ESTEN COMPLETADAS PARA MOSTRAR EN LA SELECCION LA INFO
  //private Seccion[] secciones;
  private SeccionLvl[] secciones;

  private int position;
  private int maxPositions;

  private float keyTimer = 0;
  private final float timeFrame = 0.2f*FRAMES;

  private boolean onChange = false;

  private Configuration config;

  private ArrayList<DataLvl> datas;

  public SelectLvl() {
    this.config = new Configuration();
    this.maxPositions = Integer.parseInt(this.config.getInfo("maxLevel"));
    this.secciones = new SeccionLvl[maxPositions];
    this.datas = ssd.loadData();
    position = 0;
    initSections();
  }

  public void update() {
    if (this.maxPositions == 1) {
      gestorNiveles.update();
      isSelection = false;
      inGame = true;
    } else {
      allSections();
      timer();
      if (onChange) {
        movePosition();
      }
    }
  }

  private void initSections() {
    if (this.datas.size() < maxPositions) {
      for (int i = 0; i < maxPositions; i++) {
        try {
          this.datas.get(i);
        }
        catch(Exception ex) {
          this.datas.add(new DataLvl(i+1, 0));
        }
      }
    }

    int dist = 64;
    int separation = 110;
    for (int i = 0; i < maxPositions; i++) {
      this.secciones[i] = new SeccionLvl("LV " + this.datas.get(i).getLvl(), "" + this.datas.get(i).getScore(), separation, CENTRO_VENTANA_Y);
      separation += (WIDTH/4)+dist;
    }
  }

  public int getSeccionActual() {
    return this.position;
  }

  private void allSections() {
    for (int i = 0; i < this.secciones.length; i++) {
      secciones[i].update();
      if (i == position) {
        secciones[i].setSelected(true);
        actionMenu();
      } else {
        secciones[i].setSelected(false);
      }
    }


    if (this.maxPositions >= 4 && position < 3 && this.maxPositions != position) {
      pushMatrix();
      translate(WIDTH-85, CENTRO_VENTANA_Y-100);
      triangle(70, 18.75f, 43, 0, 43, 37.5f);
      popMatrix();
    }

    if (position > 2) {
      pushMatrix();
      translate(0, CENTRO_VENTANA_Y-100);
      triangle(15, 18.75f, 43, 0, 43, 37.5f);
      popMatrix();
    }
  }

  private void timer() {
    keyTimer++;
    if (keyTimer > timeFrame && (KEYBOARD.left || KEYBOARD.right)) {
      onChange = true;
      keyTimer = 0;
    } else {
      onChange = false;
    }
  }

  private void movePosition() {
    if (KEYBOARD.right && position < (maxPositions-1)) {
      if (position >= 2) {
        for (int i = 0; i < this.secciones.length; i++) {
          this.secciones[i].setPosX(this.secciones[i].getPosX()-(WIDTH/4)-64);
        }
      }
      position++;
    } else if (KEYBOARD.left && position > 0) {
      if (position >= 3) {
        for (int i = 0; i < this.secciones.length; i++) {
          this.secciones[i].setPosX(this.secciones[i].getPosX()+(WIDTH/4)+64);
        }
      }
      position--;
    }
  }

  private void actionMenu() {   
    if (KEYBOARD.enter) {
      switch(this.position) {
      case 0:
        gestorNiveles.setLevel(1);
        gestorNiveles.update();
        isSelection = false;
        inGame = true;
        break;
      case 1:
        gestorNiveles.setLevel(2);
        gestorNiveles.update();
        isSelection = false;
        inGame = true;
        break;
      case 2:
        gestorNiveles.setLevel(3);
        gestorNiveles.update();
        isSelection = false;
        inGame = true;        
        break;
      case 3:
        gestorNiveles.setLevel(4);
        gestorNiveles.update();
        isSelection = false;
        inGame = true;        
        break;
      }
    }
  }
}
class Shield extends Habilities {

  final float radShield;
  private Player player;

  private int timerColor;

  Shield(Player player) {
    this.pos = new PVector(0, 0);
    this.timer = 0;
    this.timerFrame = (4*FRAMES);
    this.coldown = (12*FRAMES);
    this.isActive = false;
    this.isReady = true;
    this.c = COLOR_INMORTAL;
    this.radShield = player.r+20f;
    this.player = player;
    this.isEquiped = false;
    this.lvlRequired = 2;
  }

  public void update() {
    Actionhability();
    move();
    paint();
    timer();
  }

  public void timer() {
    if (this.isActive) {
      this.timer++;
      if (this.timer >= this.timerFrame) {
        this.isActive = false;
        this.timer = 0;
      }
    } else if (!this.isReady) {
      this.timer++;
      if (this.timer >= this.coldown) {
        this.isReady = true;
        this.timer = 0;
      }
    }
  }

  public void paint() {
    if (this.isActive) {
      pushMatrix();
      translate(pos.x, pos.y);
      fill(this.c, 50);
      if (this.timer >= (this.timerFrame/2)) {
        animation();
      }
      stroke(this.c);
      strokeWeight(2);
      ellipse(0, 0, this.radShield, this.radShield);
      popMatrix();
    }
    if (this.isReady) {
      showHability();
    }
  }

  private void animation() {
    timerColor++;
    if (this.c == COLOR_INMORTAL && timerColor >= 10) {
      this.c = color(0);
      timerColor = 0;
    } else if (timerColor >= 10) {
      this.c = COLOR_INMORTAL;
      timerColor = 0;
    }
  }

  public void showHability() {
    noFill();
    stroke(255);
    strokeWeight(2);
    ellipse(CENTRO_VENTANA_X, 40, 20, 20);
  }

  private void Actionhability() {
    if (this.isReady) {
      if (KEYBOARD.activeShield) {
        this.isActive = true;
        this.isReady = false;
      }
    }
  }
  public void move() {
    this.pos = this.player.pos;
  }

  public float getRad() {
    return this.radShield;
  }

  public void setIsEquiped(boolean isEquiped) {
    this.isEquiped = isEquiped;
  }

  public ArrayList<Object> getObjects() {
    return null;
  }
}
class ShooterV2 extends Monster {

  /*
  * MONSTRUO NO MOVIL, QUE DISPARARA EN UNA UNICA DIRECCION QUE SE DETERMINARA POR UN RANDOM DE 8.
   */

  //GestorDisparosEnemigos gde;
  private float timer;
  private final float timerFrame = (0.5f*FRAMES);
  private float timerShot;
  private final float timerShotFrames = (2*FRAMES);

  private int timerColor = 0;
  private final float timerColorFrame = (0.2f*FRAMES);
  private ArrayList<Ball> balls;

  public ShooterV2(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 2;
    init_monster(player);
    this.score = 12;
    this.health = 3;
    this.balls = new ArrayList<Ball>();
    this.isMovil = true;
    this.id = 5;
    this.rad = SHOOTER_V2_RAD;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = color(0xffFAD91C);
  }

  @Override
    public void updatePaint(ArrayList<Bala> balas) {
    pos = new PVector(pos.x, pos.y);
    if (!this.inmortal) {
      timerShot();
      updateBalls();
      colision(balas);
    }
    paint();
  }

  private void timerShot() {
    timerShot++;
    if (timerShot >= timerShotFrames) {
      addBalls();
      timerShot = 0;
    }
  }

  private void updateBalls() {
    for (int i = 0; i < balls.size(); i++) {
      Ball ball = balls.get(i);
      ball.update();
      if (ball.isDie) {
        balls.remove(i);
      }
    }
  }

  public void paint() {
    timerColor();
    fill(c);
    strokeWeight(3);
    stroke(COLOR_INMORTAL);

    pushMatrix();
    translate(pos.x, pos.y);
    ellipse(0, 0, rad, rad);
    //debugArea(rad);
    popMatrix();
  }

  public void timerColor() {
    timer++;
    if (this.c == COLOR_DMG && timer >= timerFrame) {
      this.c = color(0xffFAD91C);
      timer = 0;
    }

    if (this.inmortal) {
      this.timerColor++;
      if (this.timerColor > this.timerColorFrame) {
        if (this.c == color(0xffFAD91C)) {
          this.c = COLOR_INMORTAL;
        } else {
          this.c = color(0xffFAD91C);
        }
        this.timerColor = 0;
      }
    } else if (this.c == COLOR_INMORTAL) {
      this.c = color(0xffFAD91C);
    }
  }

  public void colision(ArrayList<Bala> balas) {
    //INTERACCIONA CON EL PLAYER
    if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+rad/2) {
      this.player.decreaseLife();
    }

    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+rad/2) {
        balas.get(i).isDie = true;
        this.health--;
        if (this.health <= 0) {
          this.isDie = true;
        }
        this.c = COLOR_DMG;
      }
      i++;
    }

    if (this.player.getHability(1).isEquiped) {
      ArrayList<Ball> ballsP = ObjectsToBalls(this.player);

      int index = 0;
      while (!this.isDie && index < ballsP.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, ballsP.get(index).pos)<=ballsP.get(index).rad/2+rad/2) {
          ballsP.get(index).isDie = true;
          this.isDie = true;
        }
        index++;
      }
    }

    //VALIDAR BALLS DEL ENEMIGO AUN ESTANDO MUERTO.
    if (this.player.getHability(0).isActive) {//OPTIMIZAR -> PELIGRO DE ERROR
      int ind = 0;
      while (!over && ind < balls.size()) {
        //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER CON ESCUDO
        if (PVector.dist(this.player.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+this.player.getHability(0).getRad()/2) {
          balls.get(ind).isDie = true;
        }
        ind++;
      }
    } else {
      int ind = 0;
      while (!over && ind < this.balls.size()) {
        //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
        if (PVector.dist(this.player.pos, this.balls.get(ind).pos)<=this.balls.get(ind).rad/2+this.player.r/2) {
          this.balls.get(ind).isDie = true;
          this.player.decreaseLife();
        }
        ind++;
      }
    }
  }

  private void addBalls() {
    Ball ball = new Ball(this.pos, this.player.pos, 8);
    ball.setColor(COLOR_ORANGE);
    ball.setRad(20f);
    this.balls.add(ball);
  }
}
class SystemSaveData {
  private JSONObject json;
  private JSONArray data;
  
  private boolean isLoad;
  private boolean isExist;
  boolean isSaved;
  
  private ArrayList<DataLvl> datas;
  
  public SystemSaveData(){
    this.isLoad = false;
    this.isExist = false;
    this.isSaved = false;
    this.data = new JSONArray();
    this.json = new JSONObject();
    this.datas = new ArrayList<DataLvl>();
    init_json();
  }
  
  private void init_json(){
    try{
      loadJSON();
      this.isLoad = true;
    }catch(Exception ex){
      JSONObject dataLvl = new JSONObject();
      
      dataLvl.setInt("Lv", 1);
      dataLvl.setInt("Score", 0);
      
      this.data.setJSONObject(0, dataLvl);
      this.json.setJSONArray("Scores", this.data);
      
      saveJSONObject(this.json,ROUTE_SAVE);
      this.isLoad = false;
    }
  }
  
  public ArrayList<DataLvl> loadData(){
    loadJSON();
    
    for(int i = 0; i < this.data.size(); i++){
      JSONObject dataScore = this.data.getJSONObject(i);
      
      int lvl = dataScore.getInt("Lv");
      int score = dataScore.getInt("Score");
      
      this.datas.add(new DataLvl(lvl, score));      
    }
    
    return this.datas;
  }
  
  public void update(DataLvl data){
    loadJSON();
    
    for(int i = 0; i < this.data.size(); i++){
      JSONObject score = this.data.getJSONObject(i);

      if(score.getInt("Lv") == data.getLvl()){
        this.isExist = true;
        if(score.getInt("Score") < data.getScore()){
          score.setInt("Score", data.getScore());
          
          this.data.setJSONObject(i, score);
          this.json = new JSONObject();
          this.json.setJSONArray("Scores", this.data);
          saveJSONObject(this.json, ROUTE_SAVE);
          this.isLoad = false;
          break;
        }
      }
    }
    
    if(!this.isExist){
      int size = this.data.size();
      JSONObject score = new JSONObject();
      
      score.setInt("Lv", data.getLvl());
      score.setInt("Score", data.getScore());
      
      this.data.setJSONObject(size, score);
      this.json.setJSONArray("Scores", this.data);
      saveJSONObject(this.json, ROUTE_SAVE);
      this.isLoad = false;
    }else{
      this.isExist = false;
    }
    
  }
  
  private void loadJSON(){
    if(!this.isLoad){
      this.json = loadJSONObject(ROUTE_SAVE);
      this.data = this.json.getJSONArray("Scores");    
    }
  }
}


class SystemSound {

  private final SoundFile[] sounds = new SoundFile[4];
  private final float timeFrame = 30;
  private int keyTimer = 0;
  private boolean onChange = false;
  private float volume;
  private int volumeTotal = 50;

  SystemSound(PApplet applet) {
    this.volume = 0.5f;
    this.sounds[0] = new SoundFile(applet, "data/Sounds/TitleScreen.wav");
    this.sounds[1] = new SoundFile(applet, "data/Sounds/Level1.wav");
    this.sounds[2] = new SoundFile(applet, "data/Sounds/Level2.wav");
    this.sounds[3] = new SoundFile(applet, "data/Sounds/Level3.wav");
  }

  public void play(int id) {
    if (!this.sounds[id].isPlaying()) {
      this.sounds[id].loop();
      this.sounds[id].amp(this.volume);
    }
  }

  public void stop(int id) {
    if (this.sounds[id].isPlaying()) {
      this.sounds[id].stop();
    }
  }
  
  public void beforeStop(){
    for(int i = 0; i < this.sounds.length; i++){
     stop(i);
    }
  }

  public void update() {
    timer();
    if (onChange)changeVolume();
    paint();
  }

  public void changeVolume() {
    if (KEYBOARD.left && this.volumeTotal > 0) {
      this.volumeTotal -= 10;
    } else if (KEYBOARD.right && this.volumeTotal < 100) {
      this.volumeTotal += 10;
    }
    this.volume = (float)this.volumeTotal/100;
    this.sounds[0].amp(this.volume);
  }

  public void paint() {
    textSize(24f);
    textAlign(CENTER);
    text("VOLUME: " + this.volumeTotal + "%", CENTRO_VENTANA_X, CENTRO_VENTANA_Y);
  }

  private void timer() {
    keyTimer++;
    if (keyTimer > timeFrame && (KEYBOARD.left || KEYBOARD.right)) {
      onChange = true;
      keyTimer = 0;
    } else {
      onChange = false;
    }
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
