class Particle{
  private PVector position, speed, acc;
  private float lifespan;
  private color c;
  
  Particle(PVector pos, color cl){
    this.acc = new PVector(random(-1,1), random(-1,1));
    this.speed = new PVector(0, 0);
    this.position = pos.copy();
    this.lifespan = 255.0;
    this.c = cl;
  }
  
  void update(){
    move();
    lifespan -= 10.0;
    paint();
  }
  
  private void move(){
    this.speed.add(this.acc);
    this.position.add(this.speed);    
  }
  
  void paint(){
    noStroke();
    fill(c, lifespan);
    pushMatrix();
    translate(this.position.x, this.position.y);
    rect(0,0,3,3);
    popMatrix();
  }
  
  boolean isDead(){
    if (this.lifespan < 0.0) {
      return true;
    } else {
      return false;
    }
  }
}
