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

  void update() {
    Actionhability();
    updateBalls();
    timer();
    move();
    paint();
  }
  void timer() {
    if (!this.isReady) {
      this.timer++;
      if (this.timer >= this.coldown) {
        this.isReady = true;
        this.timer = 0;
      }
    }
  }

  void paint() {
    if (this.isReady) {
      //DIBUJAR HABILIDAD EN LA INTERFAZ
      noFill();
      stroke(#F79E0C);
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

  void move() {
    this.pos = this.player.pos;
  }

  float getRad() {
    return 0;
  }

  void setIsEquiped(boolean isEquiped) {
    this.isEquiped = isEquiped;
  }
  
  ArrayList<Object> getObjects(){
    ArrayList<Object> objects = new ArrayList<Object>();
    
    for(int i = 0; i < this.balls.size(); i++){
      objects.add(this.balls.get(i));
    }
    
    return objects;
  }
  
}
