import java.util.Properties;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

class Configuration extends Properties {

  public Configuration() {
    createSave();
  }

  //SI YA EXISTE EL FICHERO NO SE SOBRESCRIBE.
  private void createSave() {
    File directory = new File(ROUTE_DIRECTORY_CONFIG);
    directory.mkdir();
    Path path = Paths.get(ROUTE_CONFIG);
    try {
      Files.createFile(path);
      update("maxLevel", "1");
    } 
    catch(IOException ex) {
      println("Error file is exist.");
    }
  }

  public void update(String keyProperty, String valueProperty) {
    try {
      OutputStream output = new FileOutputStream(ROUTE_CONFIG);

      setProperty(keyProperty, valueProperty);
      store(output, null);
    } 
    catch(IOException io) {
      println("ERROR WRITE FILE");
    }
  }

  public String getInfo(String property) {
    InputStream input = null;
    String value = "";

    try {
      input = new FileInputStream(ROUTE_CONFIG);

      this.load(input);

      value = getProperty(property);
    } 
    catch(IOException ex) {
      println("ERROR GET PROPERTY");
    } 
    finally {
      if (input != null) {
        try {
          input.close();
        } 
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return value;
  }
}
