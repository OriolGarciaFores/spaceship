class MonsterBossV3 extends Monster {

  /*
  ************************************************************* 
   *      COPY FOLLOWER
   *************************************************************
   */
  boolean isStarted;
  private int health;
  private int fase = 1;
  boolean loadShooters = false;
  private color puntoDebil = color(#F50A0A);
  private float timer;
  private final float timerFrame = (0.5*FRAMES);
  boolean animationDead;
  private float timerShot;
  private final float timerShotFrames = (FRAMES/2);
  private ArrayList<Ball> balls;

  public MonsterBossV3(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 2;
    init_monster(player);
    this.score = 50;
    //this.isStarted = false;
    this.rad = BOSS_V3_RAD;
    this.animationDead = true;
    this.health = 500;
    this.balls = new ArrayList<Ball>();
    this.isMovil = true;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = color(255);
  }

  public void updateBoss(ArrayList<Bala> balas) {
    // if (this.isStarted) {
    // this.isStarted = true;
    pos = new PVector(pos.x, pos.y);

    //MECANICAS BOSS
    timerShot();
    updateBalls();
    colision(balas);
    //}
  }

  public void paint() {
    timerColor();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(PI/4);
    body();
    popMatrix();
  }

  private void body() {
    noFill();
    strokeWeight(3);
    stroke(COLOR_INMORTAL);
    rect(-this.rad/2, -this.rad/2, this.rad, this.rad);
    fill(COLOR_ORANGE);
    ellipse(0, 0, this.rad/2, this.rad/2);
  }

  private void timerShot() {
    timerShot++;
    if (timerShot >= timerShotFrames) {
      addBalls();
      timerShot = 0;
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

  public void timerColor() {
    timer++;
    if (this.puntoDebil == color(#D66D0B) && timer >= timerFrame) {
      this.puntoDebil = color(#F50A0A);
      timer = 0;
    }
  }

  public void colision(ArrayList<Bala> balas) {
  }

  public boolean getIsStarted() {
    return this.isStarted;
  }

  public int getFase() {
    return this.fase;
  }

  public void setFase(int fase) {
    this.fase = fase;
  }

  private void addBalls() {
    Ball ball = new Ball(this.pos, this.player.pos, 8);
    ball.setColor(COLOR_ORANGE);
    ball.setRad(20f);
    this.balls.add(ball);
  }
}
