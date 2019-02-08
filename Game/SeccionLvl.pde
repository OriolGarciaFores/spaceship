class SeccionLvl {
  private String title;
  private String score;
  private int posX;
  private int posY;
  private float sizeText;
  private color c;
  private color colorSelected;
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

  void update() {
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

  void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }

  void setPosX(int posX) {
    this.posX = posX;
  }

  void setPosY(int posY) {
    this.posY = posY;
  }
  
  int getPosX(){
    return this.posX;
  }
  
  int getPosY(){
    return this.posY;
  }
}
