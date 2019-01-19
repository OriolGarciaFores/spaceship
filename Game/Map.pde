class Map{

  PShader nebula;
  
  public Map(){
    setup();
  }
  
  void setup(){
    noStroke();
    nebula = loadShader("nebula.glsl");
    nebula.set("resolution", float(WIDTH), float(HEIGHT));
  }
  
  public void update(){
    nebula.set("time", millis() / 500.0);  
    shader(nebula);  
    rect(0,0,WIDTH,HEIGHT);
    resetShader();
  }

}
