class GestorLvlComplete implements EstadoJuego{
  //SAVE DATA JSON
  
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
  private boolean isSaved = false;
  private String description;

  public GestorLvlComplete(){
    this.title = "";
    this.bornTimer = 0;
    this.subTitle = "";
    this.sizeTitle = ("Complete").length();
    this.positionChar = 0;
    this.animationTimer = 0;
    this.cSubtitle = color(255);
  }
  
  void update(){
    if(!this.isSaved){
      //SAVE DATA JSON
      ssd.update(new DataLvl(finalLvl, finalScore));
      this.isSaved = true;
    }
    
    if(this.title.length() != this.sizeTitle){
      timer();
    }else{
      if(KEYBOARD.enter ){
        //NEXT LVL
        this.isSaved = false;
        isLvlComplete = false;
        if(finalLvl >= MAX_LVLS){
          //RELOAD JSON ?
          isSelection = true;
          delay(300);
        }
      }
    }
    paint();
  }
  
  void paint(){
    background(0);
    fill(255);
    textAlign(CENTER);
    textSize(32f);
    text(this.title, CENTRO_VENTANA_X,CENTRO_VENTANA_Y-200);
    
    if(!this.subTitle.equals("")){
      newHability();
      fill(255);
      textSize(32f);
      text("SCORE: " + finalScore, CENTRO_VENTANA_X,CENTRO_VENTANA_Y+50);
      animationSubtitle();
      fill(cSubtitle);
      textSize(18f);
      text(this.subTitle, CENTRO_VENTANA_X,CENTRO_VENTANA_Y+80);
    }
  }
  
  private void newHability(){
    switch(finalLvl){
      case 1:
        strokeWeight(2);
        stroke(255);
        noFill();
        ellipse(CENTRO_VENTANA_X, CENTRO_VENTANA_Y-140, 30, 30);
        textSize(18f);
        description = "Nueva habilidad desbloqueada!\n Ahora al pulsar Q podrás activar un escudo que evitaras\n ser golpeado por proyectiles durante unos segundos.";
        text(this.description, CENTRO_VENTANA_X, CENTRO_VENTANA_Y-100);
      break;
      case 2:
        strokeWeight(2);
        stroke(#FF5500);
        noFill();
        ellipse(CENTRO_VENTANA_X, CENTRO_VENTANA_Y-140, 30, 30);
        textSize(18f);
        description = "Nueva habilidad desbloqueada!\n Ahora al pulsar E podrás disparar una onda expansiva de balas.";
        text(this.description, CENTRO_VENTANA_X, CENTRO_VENTANA_Y-100);
      break;
    }
  }
  
  private void timer(){
    bornTimer++;
    if(this.bornTimeFrame < this.bornTimer){
      this.positionChar++;
      switch(this.positionChar){
        case 1:
          this.title = "C";
          break;
        case 2:
          this.title += "O";
          break;
        case 3:
          this.title += "M";
          break;
        case 4:
          this.title += "P";
          break;
        case 5:
          this.title +=  "L"; 
          break;
        case 6:
          this.title += "E";
          break;
        case 7:
          this.title += "T";
          break;
        case 8:
          this.title += "E";
        break;
      
      }
      bornTimer = 0;
    }
    
    if(this.title.length() == this.sizeTitle){
      this.subTitle = "ENTER to next level " + (finalLvl+1);
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
