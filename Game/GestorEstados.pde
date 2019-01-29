public class GestorEstados {
  //VARIABLES
  private final EstadoJuego[] estados;
  private EstadoJuego estadoActual;

  private int estado;


  public GestorEstados() {
    this.estado = -1;
    this.estados = new EstadoJuego[5];

    startEstado();
    startEstadoActual();
  }

  private void startEstado() {
    this.estados[0] = new GestorMenu();
    this.estados[1] = new SelectLvl();
    this.estados[2] = new GestorJuego();
    this.estados[3] = new GestorGameOver();
    this.estados[4] = new GestorLvlComplete();
  }

  private void startEstadoActual() {
    this.estadoActual = estados[0];
  }

  public void update() {
    this.estadoActual.update();
  }

  public void setEstado(final int estado) {
    this.estadoActual = estados[estado];
    this.estado = estado;
  }

  public EstadoJuego getEstadoActual() {
    return this.estadoActual;
  }

  public int getIndexEstadoActual() {
    return this.estado;
  }

  public void resetGestorJuego() {
    this.estados[1] = new GestorJuego();
  }
}
