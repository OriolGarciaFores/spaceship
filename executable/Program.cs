using System.Diagnostics;

class Program
{
    static void Main(string[] args)
    {
        try{
            
        Process process = new Process();
        process.StartInfo.FileName = @"java\bin\java.exe";
        process.StartInfo.Arguments = "-jar spaceship.jar";
        process.StartInfo.UseShellExecute = false;
        process.StartInfo.CreateNoWindow = true;
        process.Start();
        process.Close();
        } catch(Exception ex) {
            Console.WriteLine("Error al ejecutar el archivo spaceship.jar: " + ex.Message);
        } finally {
            Console.WriteLine("Proceso ejecución Spaceship finalizado.");
            Environment.Exit(0);
        }
        
    }
}
