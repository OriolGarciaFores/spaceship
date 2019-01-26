class Map{

  PShader nebula;
  private GestorNiveles gn;
  
  public Map(GestorNiveles gn){
    this.gn = gn;
    init();
  }
  
  void init(){
    switch(gn.getLevel()){
      case 1:
        noStroke();
        nebula = loadShader("nebula.glsl");
        nebula.set("resolution", float(WIDTH), float(HEIGHT));
      break;
      case 2:
        noStroke();
        nebula = loadShader("nebula.glsl");
        nebula.set("resolution", float(WIDTH), float(HEIGHT));
      break;
    }
  }
  
  public void update(){
    switch(gn.getLevel()){
      case 1:
        fill(255);
        nebula.set("time", millis() / 500.0);//NULL ? AL COMPLETAR EL 2N NIVEL  
        shader(nebula);
        rect(0,0,WIDTH,HEIGHT);
        resetShader();
      break;
      case 2:
        fill(255);
        nebula.set("time", millis() / 500.0);//NULL ? AL COMPLETAR EL 2N NIVEL  
        shader(nebula);
        rect(0,0,WIDTH,HEIGHT);
        resetShader();
      break;
    }
  }

}
