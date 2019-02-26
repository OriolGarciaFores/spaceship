class ShooterV2 extends Monster {

  /*
  * MONSTRUO NO MOVIL, QUE DISPARARA EN UNA UNICA DIRECCION QUE SE DETERMINARA POR UN RANDOM DE 8.
   */

  //GestorDisparosEnemigos gde;
  private float timer;
  private final float timerFrame = (0.5*FRAMES);
  private float timerShot;
  private final float timerShotFrames = (2*FRAMES);

  private int timerColor = 0;
  private final float timerColorFrame = (0.2*FRAMES);
  private ArrayList<Ball> balls;

  public ShooterV2(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 2;
    init_monster(player);
    this.score = 12;
    this.health = 3;
    this.balls = new ArrayList<Ball>();
    this.isMovil = true;
    this.id = 5;
    this.rad = SHOOTER_V2_RAD;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = color(#FAD91C);
  }

  @Override
    public void updatePaint(ArrayList<Bala> balas) {
    pos = new PVector(pos.x, pos.y);
    if (!this.inmortal) {
      timerShot();
      updateBalls();
      colision(balas);
    }
    paint();
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

  public void paint() {
    timerColor();
    fill(c);
    strokeWeight(3);
    stroke(COLOR_INMORTAL);

    pushMatrix();
    translate(pos.x, pos.y);
    ellipse(0, 0, rad, rad);
    //debugArea(rad);
    popMatrix();
  }

  public void timerColor() {
    timer++;
    if (this.c == COLOR_DMG && timer >= timerFrame) {
      this.c = color(#FAD91C);
      timer = 0;
    }

    if (this.inmortal) {
      this.timerColor++;
      if (this.timerColor > this.timerColorFrame) {
        if (this.c == color(#FAD91C)) {
          this.c = COLOR_INMORTAL;
        } else {
          this.c = color(#FAD91C);
        }
        this.timerColor = 0;
      }
    } else if (this.c == COLOR_INMORTAL) {
      this.c = color(#FAD91C);
    }
  }

  public void colision(ArrayList<Bala> balas) {
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
        this.c = COLOR_DMG;
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
          this.isDie = true;
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

  private void addBalls() {
    Ball ball = new Ball(this.pos, this.player.pos, 8);
    ball.setColor(COLOR_ORANGE);
    ball.setRad(20f);
    this.balls.add(ball);
  }
}
