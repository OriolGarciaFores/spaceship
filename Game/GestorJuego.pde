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
    this.map = new Map(gestorNiveles);
    this.player = new Player(CENTRO_VENTANA_X,CENTRO_VENTANA_Y);
    this.gm = new GestorMonsters(this.player, gestorNiveles);
    this.gd = new GestorDisparos(this.player);
  }

 void update(){
   if(!over && !isLvlComplete){
     gestorNiveles.update();
     this.map.update();
     this.player.update();
     this.gd.update();
     this.gm.update(gd.getBalas());
     this.player.showScore();
     this.player.showHabilities();
   }
 }
}
