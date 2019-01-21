class Bala{
  private PVector pos;
  private String direct;
  private int speed;
  private color c;
  private boolean isDie;
  
  float rad = 8f;
  
  public Bala(PVector loc, String direc){
    this.pos = new PVector(loc.x, loc.y);
    this.speed = 8;
    this.c = color(190);
    this.direct = direc;
    this.isDie = false;
  }
  
  public Bala(PVector loc, String direc, color c){
    this.pos = new PVector(loc.x, loc.y);
    this.speed = 8;
    this.c = c;
    this.direct = direc;
    this.isDie = false;
  }
  
  void update(){
    move();
    paint();
    calDie();
  }
  
  void paint(){
    pushMatrix();
    translate(pos.x, pos.y);
    strokeWeight(4);
    stroke(c);
    fill(c);
    rect(-2,-2,5,5);
    //debugArea(rad);
    popMatrix();
  }
  
  private void move(){      
     switch(this.direct){
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

  private void calDie(){
    if(this.pos.y <=0 || this.pos.x <= 0 || this.pos.y >= HEIGHT || this.pos.x >= WIDTH){
      isDie = true;
    }
  }
  
  public boolean isDie(){
    return this.isDie;
  }
  
}
