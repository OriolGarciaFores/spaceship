class GestorMenu implements EstadoJuego{

 private Seccion[] secciones;
 private Seccion[] seccionesControls;
 private int position;
 
 private float keyTimer = 0;
 private float timeFrame = 0.2*FRAMES;
 private boolean onChange = false;
 private boolean isMenuControls = false;
  
 public GestorMenu(){
   this.secciones = new Seccion[3];
   position = 0;
   this.seccionesControls = new Seccion[8];
   
   initSections();
 }
 
 public void update(){
   background(0);
   if(!isMenuControls){
      allSections();
     timer();
     if(onChange){
       movePosition();
     }
   }else{
     if(KEYBOARD.enter){
       isMenuControls = false;
       delay(300);
     }
     showControls();
     textSize(24f);
     textAlign(CENTER);
     text("ENTER to exit", CENTRO_VENTANA_X, 400);
   }
 }
 
 private void initSections(){
   this.secciones[0] = new Seccion("PLAY",CENTRO_VENTANA_X,CENTRO_VENTANA_Y-50);
   this.secciones[1] = new Seccion("CONTROLS",CENTRO_VENTANA_X,CENTRO_VENTANA_Y+20);
   this.secciones[2] = new Seccion("EXIT",CENTRO_VENTANA_X,CENTRO_VENTANA_Y+90);
   
   //MENU DE CONTROLES
   float size = 18f;
   this.seccionesControls[0] = new Seccion("ARRIBA        W", CENTRO_VENTANA_X, 110, size);
   this.seccionesControls[1] = new Seccion("IZQUIERDA    A", CENTRO_VENTANA_X, 140, size);
   this.seccionesControls[2] = new Seccion("ABAJO          S", CENTRO_VENTANA_X, 170, size);
   this.seccionesControls[3] = new Seccion("DERECHA      D", CENTRO_VENTANA_X, 200, size);//start
   this.seccionesControls[4] = new Seccion("DISPARAR ARRIBA        ↑", CENTRO_VENTANA_X, 230, size);
   this.seccionesControls[5] = new Seccion("DISPARAR IZQUIERDA    →", CENTRO_VENTANA_X, 260, size);
   this.seccionesControls[6] = new Seccion("DISPARAR ABAJO         ↓", CENTRO_VENTANA_X, 290, size);
   this.seccionesControls[7] = new Seccion("DISPARAR DERECHA      ←", CENTRO_VENTANA_X, 320, size);
 }
 
 public int getSeccionActual(){
   return this.position;
 }
 
 private void allSections(){
   for(int i = 0; i < this.secciones.length; i++){
     secciones[i].update();
     if(i == position){
       secciones[i].selected();
       actionMenu();
     }
   }
 }
 
 private void timer(){
   keyTimer++;
   if(keyTimer > timeFrame && (KEYBOARD.up || KEYBOARD.down)){
     onChange = true;
     keyTimer = 0;
   }else{
     onChange = false;
   }
 }
 
 private void movePosition(){
   if(KEYBOARD.up && position != 0 && position==1){position=0;}else
   if(KEYBOARD.down && position != 1 && position==0){position=1;}else
   if(KEYBOARD.down && position == 1 && position != 2){position=2;}else
   if(KEYBOARD.up && position == 2 && position != 1){position=1;}
 }
 
 private void actionMenu(){   
   if(KEYBOARD.enter){
     switch(this.position){
       case 0:
           inGame = true;
         break;
       case 1:
           isMenuControls = true;
           delay(300);
         break;
       case 2:
           exit();
         break;
     }
   }
 }
 
 private void showControls(){
   for(int i = 0; i < this.seccionesControls.length; i++){
     seccionesControls[i].update();
   }
 }
}