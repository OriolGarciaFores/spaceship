class Bomb extends Monster {

  private float timer;
  private final float timerFrame = (0.5*FRAMES);

  private int timerColor = 0;
  private int timerFases = 0;
  private int fase = 1;
  private final float timerColorFrame = (0.2*FRAMES);
  private final color COLOR_BOMB = color(#00FFD2);

  private boolean isDestroy;

  private ArrayList<Ball> balls;

  public Bomb(Player player, PVector pos) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 0;
    init_monster(player);
    this.score = 5;
    this.health = 5;
    this.isMovil = false;
    this.id = 4;
    this.rad = MONSTER_BOMB_RAD;
    this.isDestroy = false;
    this.balls = new ArrayList<Ball>();
    this.animationDestroy = false; //Animacion de particulas al morir.
  }

  public void init_monster(Player player) {
    setPlayer(player);
    c = COLOR_BOMB;
  }

  @Override
    public void updatePaint(ArrayList<Bala> balas) {
    //pos = new PVector(pos.x, pos.y);
    if (!this.inmortal) {
      colision(balas);
    }
    dieAfterDestroy();
    if (!this.isDestroy) {
      paint();
    }
  }

  public void paint() {
    timerColor();
    fill(c);
    strokeWeight(2);
    stroke(0);

    pushMatrix();
    translate(this.pos.x, this.pos.y);

    for (int i = 0; i<8; i++) {
      rotate(QUARTER_PI*i);
      triangle(0, 0, this.rad/2, this.rad/2, 0, this.rad/2);
    }
    //debugArea(rad);
    popMatrix();
  }

  public void timerColor() {
    if (this.c == COLOR_DMG) {
      timer++;
      if (timer >= timerFrame) {
        this.c = COLOR_BOMB;
        timer = 0;
      }
    }

    if (this.inmortal) {
      this.timerColor++;
      if (this.timerColor > this.timerColorFrame) {
        if (this.c == COLOR_BOMB) {
          this.c = COLOR_INMORTAL;
        } else {
          this.c = COLOR_BOMB;
        }
        this.timerColor = 0;
      }
    } else if (this.c == COLOR_INMORTAL) {
      this.c = COLOR_BOMB;
    } else {
      //TIMER CAMBIAR COLORES PARA DESTURIR
      timerFases++;
      switch(fase) {
      case 1:
        changeFase(COLOR_BOMB, 2);
        break;
      case 2:
        changeFase(#00ED1D, 3);
        break;
      case 3:
        changeFase(#CDEA11, 4);
        break;
      case 4:
        changeFase(#FA9005, 5);
        break;
      case 5:
        changeFase(#FA2A05, 6);
        break;
      case 6:
        explosion();
      break;
        
      }
    }
  }

  public void colision(ArrayList<Bala> balas) {
    //INTERACCIONA CON EL PLAYER
    if (!this.isDie && this.balls.isEmpty() && !this.isDestroy) {

      if (PVector.dist(this.pos, this.player.pos)<=this.player.r/2+rad/2) {
        this.player.decreaseLife();
      }

      int i = 0;
      while (!this.isDestroy && i < balas.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, balas.get(i).pos)<=balas.get(i).rad/2+rad/2) {
          balas.get(i).isDie = true;
          this.health--;
          if (this.health <= 0) {
            //NO MUERE DEL TODO.
            //this.isDie = true;
            this.isDestroy = true;
            addBalls();
          }
          this.c = COLOR_DMG;
        }
        i++;
      }

      if (this.player.getHability(1).isEquiped) {
        ArrayList<Ball> balls = ObjectsToBalls(this.player);
        int ind = 0;
        while (!this.isDestroy && ind < balls.size()) {
          //INTERSECCION ENTRE BALA Y BICHO
          if (PVector.dist(this.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+rad/2) {
            balls.get(ind).isDie = true;
            this.health--;
            if (this.health <= 0) {
              //NO MUERE DEL TODO.
              //this.isDie = true;
              this.isDestroy = true;
              addBalls();
            }
          }
          ind++;
        }
      }
    } else if (!this.balls.isEmpty()) {
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
  }

  private void addBalls() {
    float points = 16;
    float pointAngle = 360/points;

    for (float angle = 0; angle < 360; angle = angle+pointAngle) {
      float x = cos(radians(angle)) * CENTRO_VENTANA_X;
      float y = sin(radians(angle)) * CENTRO_VENTANA_Y;
      Ball ball = new Ball(this.pos, new PVector(x+this.pos.x, y+this.pos.y), 4);
      ball.setColor(color(#FAD91C));
      this.balls.add(ball);
    }
  }

  private void dieAfterDestroy() {
    if (!this.isDie && this.isDestroy && this.balls.isEmpty()) {
      this.isDie = true;
    } else {
      updateBalls();
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

  private void changeFase(color c,int fase) {
    if (timerFases >= FRAMES) {
      println("ENTRO");
      println(fase);
      this.c = c;
      this.fase = fase;
      timerFases = 0;
    }
  }
  
  private void explosion(){
    if (timerFases >= FRAMES) {
      timerFases = 0;
      this.isDestroy = true;
      addBalls();
    }
  }
}
