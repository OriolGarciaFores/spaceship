import java.util.Random;

class MonsterBossV2 extends Monster {
  /*
  * BOSS centro disparando.
  *
  */

  boolean isStarted;
  private final int maxHealth = 80;
  private int health;
  private int fase = 1;
  private float timer;
  private final float timerFrame = FRAMES/4;
  private ArrayList<Ball> balls;
  private int timerBurst = 0;
  private int timerBurstFrame = (5*FRAMES);
  private boolean shieldActive = true;
  private int[] numBalls = {8, 16, 20, 24};
  private int timerRage = 0;
  private final float timerRageFrames = (5*FRAMES);
  private int timerColor = 0;
  private final float timerColorFrame = (0.2*FRAMES);
  private boolean isValid = false;
  private int probabilityRage = 3;// 1/X
  private int timerProbRage = 0;
  private final float timerProbRageFrames = (10*FRAMES);

  protected boolean needShips = true;
  protected boolean isRage;
  
  boolean animationDead;


  public MonsterBossV2(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 3;
    init_monster(player);
    this.score = 50;
    this.isStarted = false;
    this.rad = 100f;
    this.balls = new ArrayList<Ball>();
    this.isRage = false;
    this.health = this.maxHealth;
    this.animationDead = true;
  }

  public void init_monster(Player player) {
    setPlayer(player);
    setTarget(new PVector(CENTRO_VENTANA_X, CENTRO_VENTANA_Y));
    c = color(255);
  }

  public void updateBoss(ArrayList<Bala> balas) {
    if (pos.y < CENTRO_VENTANA_Y-2 && !this.isStarted) {
      pos = new PVector(CENTRO_VENTANA_X, pos.y);
    } else {
      this.isStarted = true;
      pos = new PVector(CENTRO_VENTANA_X, CENTRO_VENTANA_Y);
      //DISPAROS EN EXPANSIVO SOLO CENTRO DE PANTALLA
      timerShot();
    }
    if (!this.isRage) {
      timerProbRage++;
      if (this.health <= 20) {
        this.timerBurstFrame = (2*FRAMES);
      }
    }
    if (this.maxHealth-(this.maxHealth/4) >= this.health && !this.isRage && timerProbRage >= timerProbRageFrames) {
      boolean enter = new Random().nextInt(this.probabilityRage)==0;
      if (enter) {
        this.isRage = true;
        this.c = COLOR_RAGE;
        this.timerBurstFrame = FRAMES/2;
      }
      this.timerProbRage = 0;
    }

    if (this.isRage) {
      timerRage++;
      if (timerRage >= timerRageFrames) {
        this.isRage = false;
        this.c = color(255);
        this.timerRage = 0;
        this.timerBurstFrame = (5*FRAMES);
      }
    }

    //debugValue("IS RAGE",int(this.isRage),20,110);
    updateBalls();
    colision(balas);
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

    stroke(0);
    strokeWeight(1);
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);//POSICIONAMIENTO
    ellipse(0, 0, this.rad, this.rad);
    ellipse(-20, 0, 16, 32);
    ellipse(10, 0, 16, 32);
    if (this.shieldActive) {
      noFill();
      stroke(COLOR_INMORTAL);
      strokeWeight(4);
      ellipse(0, 0, this.rad+20f, this.rad+20f);
    }
    popMatrix();
    //debugValue("VIDA BOSS", this.health, 20, 90);
  }

  public void timerShot() {
    stroke(255);
    strokeWeight(4);
    timer++;
    timerBurst++;
    if (timer >= timerFrame && timerBurst < timerBurstFrame) {
      this.balls.add(new Ball(this.pos, this.player.pos, 6));
      timer = 0;
    }

    if (timerBurst >= timerBurstFrame) {
      float points = randomBalls();
      float pointAngle = 360/points;

      for (float angle = 0; angle < 360; angle = angle+pointAngle) {
        float x = cos(radians(angle)) * CENTRO_VENTANA_X;
        float y = sin(radians(angle)) * CENTRO_VENTANA_Y;
        this.balls.add(new Ball(this.pos, new PVector(x+CENTRO_VENTANA_X, y+CENTRO_VENTANA_Y), 4));
      }
      timer = -80;
      timerBurst = 0;
    }
  }

  public void timerColor() {
    if (this.c == COLOR_DMG) {
      timerColor++;
      if (timerColor >= timerColorFrame) {
        if (this.isRage) {
          this.c = color(COLOR_RAGE);
        } else {
          this.c = color(255);
        }
        timerColor = 0;
      }
    }
  }

  public void colision(ArrayList<Bala> balas) {
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
      while (!over && ind < balls.size()) {
        //INTERSECCION ENTRE BALA ENEMIGA Y PLAYER
        if (PVector.dist(this.player.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+this.player.r/2) {
          balls.get(ind).isDie = true;
          this.player.decreaseLife();
        }
        ind++;
      }
    }


    if (!this.shieldActive && PVector.dist(this.pos, this.player.pos)<=this.player.r/2+this.rad/2) {
      this.player.decreaseLife();
    } else if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+(this.rad+20)/2) {
      this.player.decreaseLife();
    }

    int i = 0;
    while (!this.isDie && i < balas.size()) {
      //INTERSECCION ENTRE BALA Y BICHO
      if (this.shieldActive) {
        if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+(this.rad+20f)/2) {
          balas.get(i).isDie = true;
        }
      } else {
        if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+this.rad/2) {
          if (health > 0) {
            health--;
            this.isValid = true;
            this.c = color(COLOR_DMG);
          } else {
            this.isDie = true;
          }
          balas.get(i).isDie = true;
        }
      }
      i++;
    }
  }

  public void timerShield() {
    if (this.isValid) {
      switch(this.health) {
      case 20:
        this.needShips = true;
        this.shieldActive = true;
        this.isValid = false;
        break;
      case 40:
        this.needShips = true;
        this.shieldActive = true;
        this.isValid = false;
        break;
      case 60:
        this.needShips = true;
        this.shieldActive = true;
        this.isValid = false;
        break;
      }
    }
  }

  private int randomBalls() {
    int index = int(random(numBalls.length));
    return numBalls[index];
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

  public void setShieldActive(boolean shieldActive) {
    this.shieldActive = shieldActive;
  }

  public boolean getShieldActive() {
    return this.shieldActive;
  }
}
