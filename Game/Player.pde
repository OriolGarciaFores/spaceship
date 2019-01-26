class Player {
  private PVector pos, speed, acc;
  private float maxSpeed;
  private int score;
  private boolean isAuto;
  private int targetAutoX;
  private int targetAutoY;

  //HABILIDADES
  private boolean haveShield;
  private boolean activeShield;
  private int timerShield = 0;
  private final float timerShieldFrame = (4*FRAMES);
  private final float coldownShield = (12*FRAMES);


  float r = 30f;
  float radShield = r+20f;

  public Player(int x, int y) {
    pos = new PVector(x, y);
    this.speed = new PVector(0, 0);
    this.acc = new PVector(0, 0);
    this.maxSpeed = 5;
    this.score = 0;
    this.isAuto = false;
    this.targetAutoX = CENTRO_VENTANA_X;
    this.targetAutoY = CENTRO_VENTANA_Y;
    this.haveShield = false;
    this.activeShield = false;
  }

  void update() {
    if (!this.isAuto) {
      direction();
      move();
    } else {
      autoMove();
    }
    timer();
    hability();
    paint();
  }

  void paint() {
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(PI/4);
    body();
    //A PARTIR DE LV2
    shield();
    //debugArea(r*5); //AREA no respawn enemigos.
    //debuArea(r);
    popMatrix();
    if (isShowPositions) {
      showPositions();
    }
    //showHabilities();
    //showScore();
  }

  private void body() {
    noStroke();
    fill(230, 100, 50);
    rect(-10, -10, 20, 20);
  }

  //ESCUDO ANTI-BALAS
  private void shield() {
    if (this.activeShield) {
      noFill();
      stroke(COLOR_INMORTAL);
      strokeWeight(2);
      ellipse(0, 0, this.radShield, this.radShield);
    }
  }

  private void move() {
    if (this.acc.x == 0 && this.acc.y == 0) {
      speed.mult(0.9);
    } else { 
      speed.add(this.acc);
      speed.limit(maxSpeed);
    }      
    pos.add(speed);
    collision();
  }

  private void direction() {
    PVector direction = new PVector(0, 0);

    if (KEYBOARD.up) {
      direction.add(new PVector(0, -1));
    }
    if (KEYBOARD.left) {
      direction.add(new PVector(-1, 0));
    }
    if (KEYBOARD.down) {
      direction.add(new PVector(0, 1));
    }
    if (KEYBOARD.right) {
      direction.add(new PVector(1, 0));
    } 

    this.acc = direction;
  }

  public void autoMove() {
    PVector findTarget = new PVector(this.targetAutoX-this.pos.x+2, this.targetAutoY-this.pos.y+2);

    if (abs(int(findTarget.x)) >= 0 && abs(int(findTarget.x)) <= 4 && abs(int(findTarget.y)) <= 4 && abs(int(findTarget.y)) >= 0 ) {
      return;
    }

    this.acc.add(findTarget);

    this.speed.add(this.acc);
    this.speed.limit(this.maxSpeed);

    this.pos.add(this.speed);

    this.acc = new PVector();
  }

  private void collision() {
    float x = range(this.pos.x, r/2, WIDTH-r/2);
    float y = range(this.pos.y, r/2, HEIGHT-r/2);

    this.pos = new PVector(x, y);
  }

  public void showPositions() {
    fill(155);
    textSize(18);
    text("POS X: " + int(this.pos.x), 20, 40);
    text("POS Y: " + int(this.pos.y), 20, 60);
  }

  private void hability() {
    if (this.haveShield) {
      if (KEYBOARD.activeShield) {
        this.activeShield = true;
        this.haveShield = false;
      }
    }
  }

  private void timer() {
    if (gestorNiveles.getLevel() >= 2) {
      if (this.activeShield) {
        this.timerShield++;
        if (this.timerShield >= this.timerShieldFrame) {
          this.activeShield = false;
          this.timerShield = 0;
        }
      } else if (!this.haveShield) {
        this.timerShield++;
        if (this.timerShield >= this.coldownShield) {
          this.haveShield = true;
          this.timerShield = 0;
        }
      }
    }
  }

  public void setScore(int score) {
    this.score += score;
  }

  public int getScore() {
    return this.score;
  }

  public void showScore() {
    fill(255);
    textSize(18f);
    text("SCORE: " + this.score, CENTRO_VENTANA_X-10, 20);
  }

  public void showHabilities() {
    if (this.haveShield) {
      noFill();
      stroke(255);
      strokeWeight(2);
      ellipse(CENTRO_VENTANA_X, 40, 20, 20);
    }
  }

  public void setAutoMove(boolean isAuto) {
    this.isAuto = isAuto;
  }

  public boolean getIsAutoMove() {
    return this.isAuto;
  }

  public void setTargetAutoX(int x) {
    this.targetAutoX = x;
  }

  public void setTargetAutoY(int y) {
    this.targetAutoY = y;
  }

  public void reset() {
    pos = new PVector(CENTRO_VENTANA_X, CENTRO_VENTANA_Y);
    this.speed = new PVector(0, 0);
    this.acc = new PVector(0, 0);
    this.score = 0;
    this.isAuto = false;
    this.targetAutoX = CENTRO_VENTANA_X;
    this.targetAutoY = CENTRO_VENTANA_Y;
    this.haveShield = false;
    this.activeShield = false;
  }
}
