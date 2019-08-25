abstract class Habilities {
  
  protected PVector pos;
  protected boolean isActive;
  protected boolean isReady;
  protected boolean isEquiped; //PARA EL FUTURO SISTEMA DE INVENTARIO
  protected int lvlRequired;
  protected int timer;
  protected float timerFrame;
  protected float coldown;
  protected color c;
  
 abstract void update();
 abstract void timer();
 abstract void paint();
 abstract float getRad();
 abstract void setIsEquiped(boolean isEquiped);
 abstract ArrayList<Object> getObjects();
}
