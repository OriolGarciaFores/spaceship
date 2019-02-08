class GestorJuego implements EstadoJuego{
  
  //VARIABLES
  /*OPTIMIZABLE LA CARGA SE HACE AL INICIAR EL PROGRAMA
  *
  */
  
  private final Map map;
  private final Player player;
  private final GestorMonsters gm;
  private final GestorDisparos gd;
  
  public GestorJuego(){
    println("Gestor Juego inicializado");
    this.map = new Map(gestorNiveles);
    this.player = new Player(CENTRO_VENTANA_X,CENTRO_VENTANA_Y);
    this.gm = new GestorMonsters(this.player);
    this.gd = new GestorDisparos(this.player);
  }

 void update(){
   if(!over && !isLvlComplete){
     this.map.update();
     this.player.update();
     this.gd.update();
     this.gm.update(gd.getBalas());
     this.player.showScore();
   }
 }
}
