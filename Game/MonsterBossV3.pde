class MonsterBossV3 extends Monster {

  /*
  ************************************************************* 
  *      COPY BOSS 1
  *************************************************************
  */
  boolean isStarted;
  private PVector hitBox = new PVector(WIDTH, HEIGHT/2);
  private int shield = 40;
  private int fase = 1;
  boolean loadShooters = false;
  private color puntoDebil = color(#F50A0A);
  private color shieldColor = color(#9B9B9B);
  private float timer;
  private final float timerFrame = (0.5*FRAMES);
  boolean animationDead;

  public MonsterBossV3(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 1;
    init_monster(player);
    this.score = 50;
    this.isStarted = false;
    this.rad = 560f;
    this.animationDead = true;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = color(255);
  }

  public void updateBoss(ArrayList<Bala> balas) {
    if (pos.x >= WIDTH-(WIDTH/4) && !this.isStarted) {
      pos = new PVector(pos.x, 0);
    } else {
      this.isStarted = true;
      pos = new PVector(WIDTH-(WIDTH/4), 0);

      //MECANICAS BOSS
      colision(balas);
    }
  }

  public void paint() {
    timerColor();
    noStroke();
    int w = 60;
    int h = HEIGHT-(HEIGHT/4 + HEIGHT/4);

    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);//POSICIONAMIENTO

    fill(puntoDebil);
    ellipse(WIDTH/4, HEIGHT/2, this.rad-300f, this.rad-300f);//PUNTO DEBIL CENTRAL

    fill(255);
    triangle( WIDTH/4, 0, WIDTH/4, HEIGHT/4, 0, HEIGHT/4);
    rect((WIDTH/4)-w, HEIGHT/4, w, h);
    triangle(0, HEIGHT-(HEIGHT/4), WIDTH/4, HEIGHT-(HEIGHT/4), WIDTH/4, HEIGHT);
    noFill();
    stroke(c);
    strokeWeight(4);
    if (shield > 0 && this.fase <= 3) {
      stroke(shieldColor);
      ellipse(WIDTH/4, HEIGHT/2, this.rad, this.rad);
    }

    //debugAreaPoint(this.rad-80f,WIDTH/4,HEIGHT/2);
    //debugAreaPoint(this.rad,WIDTH/4,HEIGHT/2);
    popMatrix();
  }

  public void timerColor() {
    timer++;
    if (this.puntoDebil == color(#D66D0B) && timer >= timerFrame) {
      this.puntoDebil = color(#F50A0A);
      timer = 0;
    }
    if (this.shieldColor == COLOR_DMG && timer >= timerFrame) {
      this.shieldColor = color(#9B9B9B);
      timer = 0;
    }
  }

  public void colision(ArrayList<Bala> balas) {
    if (this.fase != 3) {
      //INTERACCIONA CON EL PLAYER
      if (PVector.dist(hitBox, this.player.pos)<=this.player.r/2+rad/2) {
        this.player.decreaseLife();
      }
    } else {
      //INTERACCIONA CON EL PLAYER
      if (PVector.dist(hitBox, this.player.pos)<=this.player.r/2+(rad-80f)/2) {
        this.player.decreaseLife();
      }
    }

    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (this.fase < 3) {
        if (PVector.dist(hitBox, balas.get(i).pos)<=balas.get(i).rad/2+this.rad/2) {
          if (shield > 0 && this.fase == 2) {
            this.shieldColor = COLOR_DMG;
            shield--;
          }
          balas.get(i).isDie = true;
        }
      } else if (this.fase == 4) {
        if (PVector.dist(hitBox, balas.get(i).pos)<=balas.get(i).rad/2+(this.rad-220f)/2) {
          if (shield > 0) {
            this.puntoDebil = color(#D66D0B);
            shield--;
          }
          balas.get(i).isDie = true;
        }
      }

      i++;
    }
  }

  public boolean getIsStarted() {
    return this.isStarted;
  }

  public void setHitBox(int w, int h) {
    this.hitBox = new PVector(w, h);
  }

  public int getFase() {
    return this.fase;
  }

  public void setFase(int fase) {
    this.fase = fase;
  }

  public void setShield(int shield) {
    this.shield = shield;
  }
}
