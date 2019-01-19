class GestorGameOver implements EstadoJuego {
  
  private String title;
  private String subTitle;
  private float bornTimeFrame = 0.2*FRAMES;
  private int bornTimer;
  private int sizeTitle;
  private int positionChar;
  private float animationTimeFrame = 0.5*FRAMES;
  private int animationTimer;
  private int contador = 0;
  private color cSubtitle;

  public GestorGameOver(){
    this.title = "";
    this.bornTimer = 0;
    this.subTitle = "";
    this.sizeTitle = ("GAME OVER").length();
    this.positionChar = 0;
    this.animationTimer = 0;
    this.cSubtitle = color(255);
  }
  
  void update(){
    if(this.title.length() != this.sizeTitle){
      timer();
    }else{
      if(KEYBOARD.space ){
        outGameOver = true;
      }
    }
    paint();
  }
  
  void paint(){
    background(0);
    fill(255);
    textAlign(CENTER);
    textSize(32f);
    text(this.title, CENTRO_VENTANA_X,CENTRO_VENTANA_Y);
    
    if(!this.subTitle.equals("")){
      text("SCORE: " + finalScore, CENTRO_VENTANA_X,CENTRO_VENTANA_Y+50);
      //TEMPORAL
      if(endGame){
        text("END DEMO", CENTRO_VENTANA_X,CENTRO_VENTANA_Y+120);
      }
      animationSubtitle();
      fill(cSubtitle);
      textSize(18f);
      text(this.subTitle, CENTRO_VENTANA_X,CENTRO_VENTANA_Y+80);
    }
  }
  
  private void timer(){
    bornTimer++;
    if(this.bornTimeFrame < this.bornTimer){
      this.positionChar++;
      switch(this.positionChar){
        case 1:
          this.title = "G";
          break;
        case 2:
          this.title += "A";
          break;
        case 3:
          this.title += "M";
          break;
        case 4:
          this.title += "E ";
          break;
        case 5:
          this.title +=  "O"; 
          break;
        case 6:
          this.title += "V";
          break;
        case 7:
          this.title += "E";
          break;
        case 8:
          this.title += "R";
        break;
      
      }
      bornTimer = 0;
    }
    
    if(this.title.length() == this.sizeTitle){
      this.subTitle = "Space to restart";
    }
  }
  
  private void animationSubtitle(){
    this.animationTimer++;
    if(this.animationTimer > this.animationTimeFrame){
      this.contador++;
      
      if(this.contador%2==0){
        this.contador = 0;
        cSubtitle = color(0);
      }else{
        cSubtitle = color(255);
      }
      this.animationTimer = 0;
    }
    
  }

}
