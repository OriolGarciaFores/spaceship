class SelectLvl implements EstadoJuego {
  // LOAD JSON DADAS DE LAS PARTIDAS QUE ESTEN COMPLETADAS PARA MOSTRAR EN LA SELECCION LA INFO
  private Seccion[] secciones;

  private int position;
  private int maxPositions;

  private float keyTimer = 0;
  private final float timeFrame = 0.2*FRAMES;

  private boolean onChange = false;

  private Configuration config;

  public SelectLvl() {
    this.config = new Configuration();
    this.maxPositions = Integer.parseInt(this.config.getInfo("maxLevel"));
    this.secciones = new Seccion[maxPositions];
    position = 0;
    initSections();
  }

  void update() {
    if (this.maxPositions == 1) {
      isSelection = false;
      inGame = true;
    } else {
      allSections();
      timer();
      if (onChange) {
        movePosition();
      }
    }
  }

  private void initSections() {
    int lv = 1;
    int separation = CENTRO_VENTANA_Y-50;
    for (int i = 0; i < maxPositions; i++) {
      this.secciones[i] = new Seccion("LV " + lv, CENTRO_VENTANA_X, separation);
      lv++;
      separation += 70;
    }
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
    switch(maxPositions) {
    case 2:
      if (KEYBOARD.up && position != 0) {
        position=0;
      } else
        if (KEYBOARD.down && position != 1) {
          position=1;
        }
      break;
    case 3:
      if (KEYBOARD.up) {
        switch(position) {
        case 0:
          position = 0;
          break;
        case 1:
          position = 0;
          break;
        case 2:
          position = 1;
          break;
        }
      } else if (KEYBOARD.down) {
        switch(position) {
        case 0:
          position = 1;
          break;
        case 1:
          position = 2;
          break;
        case 2:
          position = 2;
          break;
        }
      }
      break;
    }
  }

  private void actionMenu() {   
    if (KEYBOARD.enter) {
      switch(this.position) {
      case 0:
        gestorNiveles.setLevel(1);
        isSelection = false;
        inGame = true;
        break;
      case 1:
        gestorNiveles.setLevel(2);
        isSelection = false;
        inGame = true;
        break;
      }
    }
  }
}
