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
  public void debugArea(float rad){
    stroke(255);
    strokeWeight(4);
    noFill();
    ellipse(0,0,rad,rad);
  }
  
  public void debugAreaPoint(float rad, float w, float h){
    stroke(#F50A0A);
    strokeWeight(4);
    noFill();
    ellipse(w,h,rad,rad);
  }
  
  /*
  *******************************************************
  *
  * Calculo de margenes del mapa con los objetos o
  * ser vivos colisionables
  *
  *******************************************************
  */
  
  public float range(float p, float min, float max){
    if(p<min){p = min;}
    if(p>max){p = max;}
    return p;
  }
  
    /*
  *******************************************************
  *
  * Muestra x valores en la esquina de la ventana
  *
  *******************************************************
  */
  
  public void debugValue(String typeValue, int value, int posX, int posY){
    fill(255);
    textSize(18);
    text(typeValue +" " + value,  posX, posY);
  }
