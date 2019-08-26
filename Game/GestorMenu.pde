class GestorMenu implements EstadoJuego {

  private final Seccion[] secciones;
  private final Seccion[] seccionesControls;

  private int position;
  private final int maxPositions = 5;

  private float keyTimer = 0;
  private final float timeFrame = 0.2*FRAMES;

  private boolean onChange = false;
  private boolean isMenuControls = false;
  private boolean isMenuAudio = false;

  public GestorMenu() {
    println("GESTOR MENU INIT");
    this.secciones = new Seccion[maxPositions];
    this.position = 0;
    this.seccionesControls = new Seccion[12];
    if(!inGame)systemSound.play(0);
    initSections();
  }

  public void update() {
    showMenus();
  }

  private void initSections() {
    this.secciones[0] = new Seccion("PLAY", CENTRO_VENTANA_X, CENTRO_VENTANA_Y-120);
    Seccion survival = new Seccion("SURVIVAL", CENTRO_VENTANA_X, CENTRO_VENTANA_Y-50);
    survival.setColor(100);
    this.secciones[1] = survival;
    this.secciones[2] = new Seccion("AUDIO", CENTRO_VENTANA_X, CENTRO_VENTANA_Y+20);
    this.secciones[3] = new Seccion("CONTROLS", CENTRO_VENTANA_X, CENTRO_VENTANA_Y+90);
    this.secciones[4] = new Seccion("EXIT", CENTRO_VENTANA_X, CENTRO_VENTANA_Y+160);

    //MENU DE CONTROLES
    float size = 18f;
    this.seccionesControls[0] = new Seccion("-------- MOVIMIENTOS --------", CENTRO_VENTANA_X, 80, size);
    this.seccionesControls[1] = new Seccion("ARRIBA        W", CENTRO_VENTANA_X, 110, size);
    this.seccionesControls[2] = new Seccion("IZQUIERDA    A", CENTRO_VENTANA_X, 140, size);
    this.seccionesControls[3] = new Seccion("ABAJO          S", CENTRO_VENTANA_X, 170, size);
    this.seccionesControls[4] = new Seccion("DERECHA      D", CENTRO_VENTANA_X, 200, size);//start
    this.seccionesControls[5] = new Seccion("-------- DISPARAR ----------", CENTRO_VENTANA_X, 250, size);
    this.seccionesControls[6] = new Seccion("ARRIBA        ↑", CENTRO_VENTANA_X, 280, size);
    this.seccionesControls[7] = new Seccion("IZQUIERDA    →", CENTRO_VENTANA_X, 310, size);
    this.seccionesControls[8] = new Seccion("ABAJO         ↓", CENTRO_VENTANA_X, 340, size);
    this.seccionesControls[9] = new Seccion("DERECHA      ←", CENTRO_VENTANA_X, 370, size);
    this.seccionesControls[10] = new Seccion("----- HABILIDADES --------", CENTRO_VENTANA_X, 420, size);
    this.seccionesControls[11] = new Seccion("ESCUDO     Q", CENTRO_VENTANA_X, 450, size);
  }

  public int getSeccionActual() {
    return this.position;
  }

  private void allSections() {
    for (int i = 0; i < this.secciones.length; i++) {
      secciones[i].update();
      if (i == position) {
        secciones[i].selected();
        actionMenu();
      }
    }
  }

  private void timer() {
    keyTimer++;
    if (keyTimer > timeFrame && (KEYBOARD.up || KEYBOARD.down)) {
      onChange = true;
      keyTimer = 0;
    } else {
      onChange = false;
    }
  }

  private void movePosition() {
    if (KEYBOARD.down && position < (maxPositions-1)) {
      position++;
    } else if (KEYBOARD.up && position > 0) {
      position--;
    }
  }

  private void actionMenu() {   
    if (KEYBOARD.enter) {
      switch(this.position) {
      case 0:
        isSelection = true;
        delay(300);
        break;
      case 2:
        this.isMenuAudio = true;
        delay(300);
        break;
      case 3:
        isMenuControls = true;
        delay(300);
        break;
      case 4:
        exit();
        break;
      }
    }
  }

  private void showMenus() {
    if (this.isMenuControls) {
      if (KEYBOARD.enter) {
        isMenuControls = false;
        delay(300);
      }
      showControls();
      textSize(24f);
      textAlign(CENTER);
      text("ENTER to exit", CENTRO_VENTANA_X, HEIGHT-10);
    } else if (this.isMenuAudio) {
      systemSound.update();
      if (KEYBOARD.enter) {
        this.isMenuAudio = false;
        delay(300);
      }
      textSize(24f);
      textAlign(CENTER);
      text("ENTER to exit", CENTRO_VENTANA_X, HEIGHT-10);
    } else {
      allSections();
      timer();
      if (onChange) {
        movePosition();
      }
    }
  }

  private void showControls() {
    for (int i = 0; i < this.seccionesControls.length; i++) {
      seccionesControls[i].update();
    }
  }
}
