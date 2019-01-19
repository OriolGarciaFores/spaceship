class GestorControles{
  
 Boolean left = false;
 Boolean right = false;
 Boolean up = false;
 Boolean down = false;
 Boolean enter = false;
 
 Boolean shotUp = false;
 Boolean shotDown = false;
 Boolean shotLeft = false;
 Boolean shotRight = false;
 
 Boolean isRun = false;
 
 void keyDown(int p_key){
   
   switch(p_key){
     case KeyEvent.VK_A:
       left = true;
     break;
     case KeyEvent.VK_D:
       right = true;
     break;
     case KeyEvent.VK_W:
       up = true;
     break;
     case KeyEvent.VK_S:
       down = true;
     break;
     case KeyEvent.VK_ENTER:
       enter = true;
     break;
     case UP:
       shotUp = true;
     break;
     case DOWN:
       shotDown = true;
     break;
     case LEFT:
       shotLeft = true;
     break;
     case RIGHT:
       shotRight = true;
     break;
   }
 }
 
 void keyUp(int p_key){
   switch(p_key){
     case KeyEvent.VK_A:
       left = false;
     break;
     case KeyEvent.VK_D:
       right = false;
     break;
     case KeyEvent.VK_W:
       up = false;
     break;
     case KeyEvent.VK_S:
       down = false;
     break;
     case KeyEvent.VK_ENTER:
       enter = false;
     break;
     case UP:
       shotUp = false;
     break;
     case DOWN:
       shotDown = false;
     break;
     case LEFT:
       shotLeft = false;
     break;
     case RIGHT:
       shotRight = false;
     break;
   }
 }


}