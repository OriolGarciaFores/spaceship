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
void debugArea(float rad) {
  stroke(255);
  strokeWeight(4);
  noFill();
  ellipse(0, 0, rad, rad);
}

void debugAreaPoint(float rad, float w, float h) {
  stroke(#F50A0A);
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

float range(float p, float min, float max) {
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

void debugValue(String typeValue, int value, int posX, int posY) {
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

void showFPS(boolean isShow) {
  if (isShow) {
    fill(57, 255, 20);
    textAlign(BASELINE);
    textSize(18);
    text("FPS: " + int(frameRate), 20, 20);
  }
}

void showPositions(float posX, float posY) {
  fill(155);
  textSize(18);
  text("POS X: " + int(posX), 20, 40);
  text("POS Y: " + int(posY), 20, 60);
}

/*
  *******************************************************
 *
 * CASTEO DE OBJETOS
 *
 *******************************************************
 */

ArrayList<Ball> ObjectsToBalls(Player player) {
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

boolean circleRectColission(float cx, float cy, float radius, float rx, float ry, float rw, float rh) {

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
