class GestorJuego implements EstadoJuego{
  
  //VARIABLES
  /*OPTIMIZABLE LA CARGA SE HACE AL INICIAR EL PROGRAMA
  *
  */
  
  Map map;
  Player player;
  GestorMonsters gm;
  GestorDisparos gd;
  
  public GestorJuego(){
    println("Gestor Juego inicializado");
    this.map = new Map();
    this.player = new Player(CENTRO_VENTANA_X,CENTRO_VENTANA_Y);
    
    this.gm = new GestorMonsters(this.player);
    this.gd = new GestorDisparos(this.player);
  }

 void update(){
   if(!over){
     this.map.update();
     this.player.update();
     this.gd.update();
     this.gm.update(gd.getBalas());
   }
 }
}
