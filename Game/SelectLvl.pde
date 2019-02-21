class SelectLvl implements EstadoJuego {
  // LOAD JSON DADAS DE LAS PARTIDAS QUE ESTEN COMPLETADAS PARA MOSTRAR EN LA SELECCION LA INFO
  //private Seccion[] secciones;
  private SeccionLvl[] secciones;

  private int position;
  private int maxPositions;

  private float keyTimer = 0;
  private final float timeFrame = 0.2*FRAMES;

  private boolean onChange = false;

  private Configuration config;

  private ArrayList<DataLvl> datas;

  public SelectLvl() {
    this.config = new Configuration();
    this.maxPositions = Integer.parseInt(this.config.getInfo("maxLevel"));
    this.secciones = new SeccionLvl[maxPositions];
    this.datas = ssd.loadData();
    position = 0;
    initSections();
  }

  void update() {
    if (this.maxPositions == 1) {
      gestorNiveles.update();
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
    if (this.datas.size() < maxPositions) {
      for (int i = 0; i < maxPositions; i++) {
        try {
          this.datas.get(i);
        }
        catch(Exception ex) {
          this.datas.add(new DataLvl(i+1, 0));
        }
      }
    }

    int dist = 64;
    int separation = 110;
    for (int i = 0; i < maxPositions; i++) {
      this.secciones[i] = new SeccionLvl("LV " + this.datas.get(i).getLvl(), "" + this.datas.get(i).getScore(), separation, CENTRO_VENTANA_Y);
      separation += (WIDTH/4)+dist;
    }
  }

  public int getSeccionActual() {
    return this.position;
  }

  private void allSections() {
    for (int i = 0; i < this.secciones.length; i++) {
      secciones[i].update();
      if (i == position) {
        secciones[i].setSelected(true);
        actionMenu();
      } else {
        secciones[i].setSelected(false);
      }
    }
  }

  private void timer() {
    keyTimer++;
    if (keyTimer > timeFrame && (KEYBOARD.left || KEYBOARD.right)) {
      onChange = true;
      keyTimer = 0;
    } else {
      onChange = false;
    }
  }

  private void movePosition() {
    if (KEYBOARD.right && position < (maxPositions-1)) {
      if (position >= 2) {
        for (int i = 0; i < this.secciones.length; i++) {
          println(this.secciones[i].getPosX());
          this.secciones[i].setPosX(this.secciones[i].getPosX()-(WIDTH/4)-64);
        }
      }
      position++;
    } else if (KEYBOARD.left && position > 0) {
      if (position >= 3) {
        for (int i = 0; i < this.secciones.length; i++) {
          println(this.secciones[i].getPosX());
          this.secciones[i].setPosX(this.secciones[i].getPosX()+(WIDTH/4)+64);
        }
      }
      position--;
    }
  }

  private void actionMenu() {   
    if (KEYBOARD.enter) {
      switch(this.position) {
      case 0:
        gestorNiveles.setLevel(1);
        gestorNiveles.update();
        isSelection = false;
        inGame = true;
        break;
      case 1:
        gestorNiveles.setLevel(2);
        gestorNiveles.update();
        isSelection = false;
        inGame = true;
        break;
      case 2:
        gestorNiveles.setLevel(3);
        gestorNiveles.update();
        isSelection = false;
        inGame = true;        
      break;
      }
    }
  }
}
