class Seccion{
  
 private String title;
 private int posX;
 private int posY;
 private float sizeText;
 
 public Seccion(String title, int posX, int posY){
   this.title = title;
   this.posX = posX;
   this.posY = posY;
   this.sizeText = 32f;
 }
 
 public Seccion(String title, int posX, int posY, float sizeText){
   this.title = title;
   this.posX = posX;
   this.posY = posY;
   this.sizeText = sizeText;
 }
 
 void update(){
   fill(255);
   textAlign(CENTER);
   textSize(this.sizeText);
   text(this.title,posX,posY);
 }
 
 //CONTORNO
 void selected(){
   int rWidth = 25;
   int rHeight = 10;

   fill(255);
   rect(this.posX-150,this.posY-15,rWidth,rHeight);
 }

}
