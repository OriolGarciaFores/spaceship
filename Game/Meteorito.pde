class Meteorito extends Monster {

  private int contador = 0;
  private char direct;

  public Meteorito(Player player, PVector pos, PVector tDirection, char direct) {
    this.pos = new PVector(pos.x, pos.y);
    this.speed = new PVector();
    this.acc = new PVector();
    this.maxSpeed = 4;
    init_monster(player, tDirection);
    this.score = 2;
    this.rad = METEORITO_RAD;
    this.isFollower = false;
    this.direct = direct;
    this.id = 3;
  }

  public void init_monster(Player player, PVector tDirection) {
    setPlayer(player);
    setTarget(tDirection);
    c = color(#3399cc);
  }

  @Override
    public void updatePaint(ArrayList<Bala> balas) {
    pos = new PVector(pos.x, pos.y);
    colision(balas);
    paint();
  }

  public void paint() {
    noFill();
    strokeWeight(4);
    stroke(c);

    pushMatrix();
    translate(pos.x, pos.y);
    contador++;
    rotate(contador);

    for (int i = 0; i<4; i++) {
      rotate(HALF_PI*i);
      triangle(0, 0, 1*30f, 1*30f, 0, 1.41*30f);//RAD NO ES TAMAÑO REAL.
    }
    //debugArea(rad);
    popMatrix();
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
      }
      i++;
    }
    if (this.player.getHability(1).isEquiped) {
      ArrayList<Ball> balls = ObjectsToBalls(this.player);
      int ind = 0;
      while (!this.isDie && ind < balls.size()) {
        //INTERSECCION ENTRE BALA Y BICHO
        if (PVector.dist(this.pos, balls.get(ind).pos)<=balls.get(ind).rad/2+rad/2) {
          balls.get(ind).isDie = true;
        }
        ind++;
      }
    }
    switch(this.direct) {
    case 'D':
      if (this.pos.y > HEIGHT+this.rad) {
        this.isDie = true;
      }
      break;
    case 'L':
      if (this.pos.x <= -this.rad) {
        this.isDie = true;
      }
      break;
    }
  }
}
