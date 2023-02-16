package com.aprendizaje.catsapp.cats_app;
import javax.swing.JOptionPane;
import java.io.IOException;

public class Inicio {

    public static void main(String[] args) throws IOException {
        int opcionMenu = -1;
        String[] botones = {"1. Ver perros", "2. Salir"};
        String opcion;

        do{
            // Menú principal
            opcion = (String) JOptionPane.showInputDialog(null, "Java Dogs", "Menú prinipal", JOptionPane.INFORMATION_MESSAGE,
                    null, botones, botones[0]);

            switch (opcion){
                case "1. Ver perros":
                    DogsService.verPerros();
                    break;
                default:
                        break;
            }


        }while (!opcion.equals(botones[1]));


    }
}
