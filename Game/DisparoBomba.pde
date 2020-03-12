class DisparoBomba{

  private float timer;
  private final float timerFrame = (1*FRAMES);
  private final color COLOR_BOMB = color(#00FFD2);

  public boolean isDestroy;

  private ArrayList<Ball> balls;
  
  private PVector pos, direction;
  private float rotation, speed;
  private float rad = 25f;
  public boolean isDie = false;
  
  public DisparoBomba(PVector pos, PVector direction, float speed){
    this.pos = new PVector(pos.x, pos.y);
    this.direction = new PVector(direction.x, direction.y);
    rotation = atan2(this.direction.y - this.pos.y, this.direction.x - this.pos.x) / PI * 180;
    this.speed = speed;
    this.balls = new ArrayList<Ball>();
  }
  
  void update(){
    if(!this.isDestroy) calPos();
    calDie();
    if(!this.isDestroy) paint();
    if(this.isDestroy)updateBalls();
  }
  
  
  void paint() {
    pushMatrix();
    translate(this.pos.x, this.pos.y);
    fill(COLOR_BOMB);
    noStroke();
    ellipse(0, 0, this.rad, this.rad);
    popMatrix();
  }
  
  
   private void calPos() {
    this.pos.x = this.pos.x + cos(rotation/180*PI)*speed;
    this.pos.y = this.pos.y + sin(rotation/180*PI)*speed;
  }


  private void calDie() {
    if (this.pos.y <=0 || this.pos.x <= 0 || this.pos.y >= HEIGHT || this.pos.x >= WIDTH || this.isDestroy && this.balls.isEmpty()) {
      this.isDie = true;
    }else if(!this.isDestroy){
      timer++;
      if(timer >= timerFrame){
        this.isDestroy = true;
        addBalls();
        timer = 0;
      }
    }
  }
  
  private void addBalls(){
    float points = 8;
    float pointAngle = 360/points;

    for (float angle = 0; angle < 360; angle = angle+pointAngle) {
      float x = cos(radians(angle)) * CENTRO_VENTANA_X;
      float y = sin(radians(angle)) * CENTRO_VENTANA_Y;
      Ball ball = new Ball(this.pos, new PVector(x+this.pos.x, y+this.pos.y), 2);
      //ball.setColor(color(COLOR_ORANGE));
      this.balls.add(ball);
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
  
 public ArrayList<Ball> getBallsBomb(){
   return this.balls;
 }
  
}
