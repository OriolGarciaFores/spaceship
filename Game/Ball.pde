class Ball{
  PVector pos, direction;
  float rotation, speed;
  private float rad = 20f;
  private color c = color(255);
  private boolean isDie = false;
  
  Ball(PVector pos, PVector direction, float speed){
    this.pos = new PVector(pos.x, pos.y);
    this.direction = new PVector(direction.x, direction.y);
    rotation = atan2(this.direction.y - this.pos.y,this.direction.x - this.pos.x) / PI * 180;
    this.speed = speed;
  }
  
  void update(){
    calPos();
    calDie();
    paint();
  }
  
  private void calPos(){
    this.pos.x = this.pos.x + cos(rotation/180*PI)*speed;
    this.pos.y = this.pos.y + sin(rotation/180*PI)*speed;
  }
  
  void paint(){
    pushMatrix();
    translate(this.pos.x, this.pos.y);
    fill(c);
    noStroke();
    ellipse(0,0,this.rad,this.rad);
    //debugArea(rad);
    popMatrix();
    
  }
  
  private void calDie(){
    if(this.pos.y <=0 || this.pos.x <= 0 || this.pos.y >= HEIGHT || this.pos.x >= WIDTH){
      this.isDie = true;
    }
  }
}
