class Seccion {

  private String title;
  private int posX;
  private int posY;
  private float sizeText;
  private color c;

  public Seccion(String title, int posX, int posY) {
    this.title = title;
    this.posX = posX;
    this.posY = posY;
    this.sizeText = 32f;
    this.c = color(255);
  }

  public Seccion(String title, int posX, int posY, float sizeText) {
    this.title = title;
    this.posX = posX;
    this.posY = posY;
    this.sizeText = sizeText;
  }

  void update() {
    noStroke();
    fill(c);
    textAlign(CENTER);
    textSize(this.sizeText);
    text(this.title, posX, posY);
  }


  void selected() {
    fill(255);
    rect(this.posX-150, this.posY-15, 25, 10);
  }
  
  public void setColor(color c){
    this.c = c;
  }
}
