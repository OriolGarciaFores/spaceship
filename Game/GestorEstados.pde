public class GestorEstados{
  //VARIABLES
  private EstadoJuego[] estados;
  private EstadoJuego estadoActual;
  
  private int estado;
  
  
  public GestorEstados(){
    estado = -1;
    startEstado();
    startEstadoActual();
    
  }
  
  private void startEstado(){
    estados = new EstadoJuego[3];
    
    estados[0] = new GestorMenu();
    estados[1] = new GestorJuego();
    estados[2] = new GestorGameOver();
    
  }
  
  private void startEstadoActual(){
    this.estadoActual = estados[0];
  }
  
  public void update(){
    this.estadoActual.update();
  }
  
  public void setEstado(final int estado){
    this.estadoActual = estados[estado];
    this.estado = estado;
  }
  
  public EstadoJuego getEstadoActual(){
    return this.estadoActual;
  }
  
  public int getIndexEstadoActual(){
    return this.estado;
  }
  
  public void resetGestorJuego(){
    estados[1] = new GestorJuego();
  }

}
