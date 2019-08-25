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
  private float timer;
  private final float timerFrame = (0.5*FRAMES);
  boolean animationDead;
  private float timerShot;
  private final float timerShotFrames = (FRAMES/2);
  private ArrayList<Ball> balls;
  private boolean shieldActive;

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
    this.health = 250;
    this.balls = new ArrayList<Ball>();
    this.isMovil = true;
    this.shieldActive = false;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = COLOR_ORANGE;
  }

  public void updateBoss(ArrayList<Bala> balas) {
    pos = new PVector(pos.x, pos.y);

    //MECANICAS BOSS
    timerShot();
    updateBalls();
    colision(balas);
  }

  public void paint() {
    //timerColor();
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(PI/4);
    body();
    if (this.shieldActive) {
      shield();
    }
    popMatrix();
  }

  private void body() {
    noFill();
    strokeWeight(3);
    stroke(COLOR_INMORTAL);
    rect(-this.rad/2, -this.rad/2, this.rad, this.rad);
    fill(this.c);
    ellipse(0, 0, this.rad/2, this.rad/2);
  }

  private void shield() {
    fill(COLOR_INMORTAL, 50);
    stroke(COLOR_INMORTAL);
    strokeWeight(4);
    ellipse(0, 0, this.rad+20f, this.rad+20f);
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
    //if (this.puntoDebil == color(#D66D0B) && timer >= timerFrame) {
    //this.puntoDebil = color(#F50A0A);
    timer = 0;
    //}
  }

  public void colision(ArrayList<Bala> balas) {
    //ESCUDO DETECTOR DE BALAS CON CD


    //INTERACCIONA CON EL PLAYER
    if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+rad/2) {
      this.player.decreaseLife();
    }

    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+rad/2) {
        balas.get(i).isDie = true;
        this.health--;
        if (this.health <= 0) {
          this.isDie = true;
        }
        // this.c = COLOR_DMG;
      }
      i++;
    }

    if (this.player.getHability(1).isEquiped) {
      ArrayList<Ball> ballsP = ObjectsToBalls(this.player);

      int index = 0;
      while (!this.isDie && index < ballsP.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, ballsP.get(index).pos)<=ballsP.get(index).rad/2+rad/2) {
          ballsP.get(index).isDie = true;
          if (this.health <= 0) {
            this.isDie = true;
          }
          //  this.c = COLOR_DMG;
        }
        index++;
      }
    }

    //VALIDAR BALLS DEL ENEMIGO AUN ESTANDO MUERTO.
    if (this.player.getHability(0).isActive) {//OPTIMIZAR -> PELIGRO DE ERROR
      int ind = 0;
      while (!over && ind < balls.size()) {
        //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER CON ESCUDO
        if (PVector.dist(this.player.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+this.player.getHability(0).getRad()/2) {
          balls.get(ind).isDie = true;
        }
        ind++;
      }
    } else {
      int ind = 0;
      while (!over && ind < this.balls.size()) {
        //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
        if (PVector.dist(this.player.pos, this.balls.get(ind).pos)<=this.balls.get(ind).rad/2+this.player.r/2) {
          this.balls.get(ind).isDie = true;
          this.player.decreaseLife();
        }
        ind++;
      }
    }
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
