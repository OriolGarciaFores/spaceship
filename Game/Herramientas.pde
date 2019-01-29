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
