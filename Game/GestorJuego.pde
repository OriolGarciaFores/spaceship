class GestorJuego implements EstadoJuego{
  
  //VARIABLES
  /*OPTIMIZABLE LA CARGA SE HACE AL INICIAR EL PROGRAMA
  *
  */
  
  Map map;
  Player player;
  GestorMonsters gm;
  GestorDisparos gd;
  GestorNiveles gn;
  
  public GestorJuego(){
    println("Gestor Juego inicializado");
    this.gn = new GestorNiveles();
    this.map = new Map(gn);
    this.player = new Player(CENTRO_VENTANA_X,CENTRO_VENTANA_Y);
    
    this.gm = new GestorMonsters(this.player, gn);
    this.gd = new GestorDisparos(this.player);
  }

 void update(){
   if(!over){
     this.gn.update();
     this.map.update();
     this.player.update();
     this.gd.update();
     this.gm.update(gd.getBalas());
   }
 }
}
