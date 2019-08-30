class Player { //<>//
  private PVector pos, speed, acc;

  private float maxSpeed;
  final float r = 30f;
  //final float radShield = r+20f;

  private boolean isAuto;

  private int score;
  private int targetAutoX;
  private int targetAutoY;
  private int health;
  private final int MAX_HABILITIES = 2;
  private int lvl;

  private Habilities[] habilities;

  public Player(int x, int y) {
    pos = new PVector(x, y);
    this.speed = new PVector(0, 0);
    this.acc = new PVector(0, 0);
    this.maxSpeed = 5;
    this.score = 0;
    this.health = 1;
    this.isAuto = false;
    this.targetAutoX = CENTRO_VENTANA_X;
    this.targetAutoY = CENTRO_VENTANA_Y;
    this.lvl = gestorNiveles.getLevel();
    this.habilities = new Habilities[MAX_HABILITIES];
    addHabilities();
  }

  void update() {
    if (!this.isAuto) {
      direction();
      move();
    } else {
      autoMove();
    }
    validateHabilities();
    updateHabilities();
    paint();
  }

  void paint() {
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(PI/4);
    body();
    popMatrix();
    //showPositions(this.pos.x, this.pos.y);
  }

  private void body() {
    noStroke();
    fill(COLOR_ORANGE);
    rect(-10, -10, 20, 20);
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
  //OPTIMIZABLE
  public void updateHabilities() {
    if (gestorNiveles.getLevel() >= 2) {
      for (int i = 0; i < habilities.length; i++) {
        if (!habilities[i].isEquiped) {
          if (habilities[i].lvlRequired <= gestorNiveles.getLevel()) {
            habilities[i].setIsEquiped(true);
          } else {
            continue;
          }
        }
        habilities[i].update();
      }
    }
  }

  void addHabilities() {
      habilities[0] = new Shield(this);
      habilities[1] = new MultiShot(this);
  }

  void validateHabilities() {
    if (gestorNiveles.getLevel() != this.lvl) {
      this.lvl = gestorNiveles.getLevel();
      addHabilities();
    }
  }

  public Habilities getHability(int id) {
    return habilities[id];
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
    this.habilities = new Habilities[MAX_HABILITIES];
    addHabilities();
  }

  public void decreaseLife() {
    if ((this.health - 1) <= 0) {
      this.health = 0;
    } else {
      this.health -= 1;
    }
    if (this.health == 0) {
      finalScore = this.score;
      over = true;
    }
  }
}
