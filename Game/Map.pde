class Map{

  PShader nebula;
  private GestorNiveles gn;
  
  public Map(GestorNiveles gn){
    this.gn = gn;
    setup();
  }
  
  void setup(){
    switch(gn.getLevel()){
      case 1:
        noStroke();
        nebula = loadShader("nebula.glsl");
        nebula.set("resolution", float(WIDTH), float(HEIGHT));
      break;
      case 2:
        background(0);
      break;
    }
  }
  
  public void update(){
    switch(gn.getLevel()){
      case 1:
        nebula.set("time", millis() / 500.0);  
        shader(nebula);  
        rect(0,0,WIDTH,HEIGHT);
        resetShader();
      break;
      case 2:
        background(0);
      break;
    }
  }

}