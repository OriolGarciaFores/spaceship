class Shield extends Habilities {

  final float radShield;
  private Player player;

  private int timerColor;

  Shield(Player player) {
    this.pos = new PVector(0, 0);
    this.timer = 0;
    this.timerFrame = (4*FRAMES);
    this.coldown = (12*FRAMES);
    this.isActive = false;
    this.isReady = true;
    this.c = COLOR_INMORTAL;
    this.radShield = player.r+20f;
    this.player = player;
    this.isEquiped = false;
    this.lvlRequired = 2;
  }

  void update() {
    Actionhability();
    move();
    paint();
    timer();
  }

  void timer() {
    if (this.isActive) {
      this.timer++;
      if (this.timer >= this.timerFrame) {
        this.isActive = false;
        this.timer = 0;
      }
    } else if (!this.isReady) {
      this.timer++;
      if (this.timer >= this.coldown) {
        this.isReady = true;
        this.timer = 0;
      }
    }
  }

  void paint() {
    if (this.isActive) {
      pushMatrix();
      translate(pos.x, pos.y);
      noFill();
      if (this.timer >= (this.timerFrame/2)) {
        animation();
      }
      stroke(this.c);
      strokeWeight(2);
      ellipse(0, 0, this.radShield, this.radShield);
      popMatrix();
    }
    if (this.isReady) {
      showHability();
    }
  }

  private void animation() {
    timerColor++;
    if (this.c == COLOR_INMORTAL && timerColor >= 10) {
      this.c = color(0);
      timerColor = 0;
    } else if (timerColor >= 10) {
      this.c = COLOR_INMORTAL;
      timerColor = 0;
    }
  }

  void showHability() {
    noFill();
    stroke(255);
    strokeWeight(2);
    ellipse(CENTRO_VENTANA_X, 40, 20, 20);
  }

  private void Actionhability() {
    if (this.isReady) {
      if (KEYBOARD.activeShield) {
        this.isActive = true;
        this.isReady = false;
      }
    }
  }
  void move() {
    this.pos = this.player.pos;
  }

  float getRad() {
    return this.radShield;
  }

  void setIsEquiped(boolean isEquiped) {
    this.isEquiped = isEquiped;
  }

  ArrayList<Object> getObjects() {
    return null;
  }
}
