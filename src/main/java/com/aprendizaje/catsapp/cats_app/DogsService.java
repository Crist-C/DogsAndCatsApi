package com.aprendizaje.catsapp.cats_app;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

public class DogsService {

    public static void verPerros() throws IOException {

        String stringJsonResponse;
        Gson gson = new Gson();
        Dogs perro;
        Image image = null;
        // Construimos y ejecutamos la peticion al api para traer los datos desde el servidor

        OkHttpClient client = new OkHttpClient();                               // Creamos el objeto cliente
        // En este caso no se requieren los siguientes parámetros
        MediaType mediaType = MediaType.parse("text/plain");              // Indicamos que el formato en que enviamos los datos son texto plano
        RequestBody body = RequestBody.create(mediaType, "");            // Creamos el body de la petición que contiene el formato de envío (mediaType)
                                                                                // y no enviamos nada ya que este endpoint no nos solicita algún parámetro.
        Request request = new Request.Builder()                                 // Construimos la petición pasandole el URL y el método de consumo.
                .url("https://api.thedogapi.com/v1/images/search")
                .method("GET",null)
                .build();
        Response response = client.newCall(request).execute();                  // Ejecutamos la petición


        // Obtenemos el cuerpo de la respuesta como un string y lo mostramos en consola
        stringJsonResponse = response.body().string();
        System.out.println("Json recibido: " + stringJsonResponse);


        // con GSON construimos un vector de objetos tipo Dogs a partir del strigJsonResponse y retornamos el primer objeto del vector
        // ya que en este caso solo retorna un solo objeto. (Esto se hace para no eliminar los corchetes del formato JSON)
        perro = gson.fromJson(stringJsonResponse, Dogs[].class)[0];

        mostrarMenu(perro, mostrarPerro(perro));

    }
    public static void marcarPerroFavorito(Dogs perro) {

        try{

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\":\""+perro.getId()+"\"\r\n\r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thedogapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", perro.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();
            
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
    public static void verPerrosFavoritos(String apiKey) {
        String stringJsonResponse;
        Gson gson = new Gson();
        DogsFavoritos[] dogsFavoritos;
        try{

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thedogapi.com/v1/favourites")
                    .method("GET", null)
                    .addHeader("x-api-key", apiKey)
                    .build();
            Response response = client.newCall(request).execute();

            stringJsonResponse = response.body().string();
            System.out.println("Json Response Array: " + stringJsonResponse);
            ArrayList<Dogs> dogs = new ArrayList<>();
            dogsFavoritos = gson.fromJson(stringJsonResponse, DogsFavoritos[].class);

            if(dogsFavoritos.length > 0){
                for (DogsFavoritos perro :
                        dogsFavoritos) {
                    System.out.println(" Dog URL: "+perro.getImage().getUrl());
                }

                int min = 1;
                int max = dogsFavoritos.length;
                int aleatorio = (int) (Math.random() * ((max-min)+1)) + min;
                int indice = aleatorio -1;

                Dogs perro = new Dogs();
                perro.setId(dogsFavoritos[indice].getImage().getId());
                perro.setUrl(dogsFavoritos[indice].getImage().getUrl());

                mostrarMenuFavoritos(dogsFavoritos[indice], mostrarPerro(perro));
            }


        }catch (IOException e){
            throw new RuntimeException(e);
        }


    }



    public static ImageIcon mostrarPerro(Dogs perro)
    {
        // Redimensionamos la imagen en caso de necesitar
        try {
            // Nos conectamos a la URL que contiene la imagen
            URL url = new URL(perro.getUrl());
            HttpsURLConnection httpcon = (HttpsURLConnection)url.openConnection();
            httpcon.addRequestProperty("User-Agent", "");

            // Leemos y almaceamos la imagen desde el URL
            BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
            ImageIcon fondoPerro = new ImageIcon(bufferedImage);

            // En caso de que la imagen tenga un ancho mayor a 800 pixeles la redimensionamos
            if(fondoPerro.getIconWidth() > 800) {
                Image fondo = fondoPerro.getImage();
                Image imagenModificada = fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                fondoPerro = new ImageIcon(imagenModificada);
            }
            return fondoPerro;

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void mostrarMenu(Dogs perro, ImageIcon fondoPerro) throws IOException {
        // Mostramos un nuevo sub menú en donde podemos tendremos las sigueintes opociones
        String menu = """
                    Opciones:\s
                     1. Ver otra imagen
                     2. Favorito
                     3. Volver
                     4. ver perros favoritos
                    """;

        String[] botones = {"ver otra imagen", "favorito", "ver perros favoritos", "volver"};
        String id_perro = perro.getId();
        String opcionEscogida = (String) JOptionPane.showInputDialog(null, menu,id_perro,JOptionPane.INFORMATION_MESSAGE, fondoPerro, botones, botones[0]);

        switch (opcionEscogida) {
            case "ver otra imagen" -> verPerros();
            case "favorito" -> marcarPerroFavorito(perro);
            case "ver perros favoritos" -> verPerrosFavoritos(perro.getApiKey());
            default -> {
            }
        }
    }

    private static void mostrarMenuFavoritos(DogsFavoritos perro, ImageIcon fondoPerro) {
        // Mostramos un nuevo sub menú en donde podemos tendremos las sigueintes opociones
        String menu = """
                    Opciones:\s
                     1. Ver otra imagen
                     2. Eliminar Favorito
                     3. Volver
                    """;

        String[] botones = {"ver otra imagen", "eliminar favorito", "volver"};
        String id_perro = perro.getId();
        String opcionEscogida = (String) JOptionPane.showInputDialog(null, menu,id_perro,JOptionPane.INFORMATION_MESSAGE, fondoPerro, botones, botones[0]);

        switch (opcionEscogida) {
            case "ver otra imagen" -> verPerrosFavoritos(perro.getApiKey());
            case "eliminar favorito" -> eliminarPerroFavorito(perro);
            default -> {
            }
        }
    }

    private static void eliminarPerroFavorito(DogsFavoritos dogFavorito){

        try {

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thedogapi.com/v1/favourites/"+dogFavorito.getId())
                    .method("DELETE", null)
                    .addHeader("x-api-key", dogFavorito.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();
            String stringJsonResponse = response.body().string();

            if(stringJsonResponse.contains("SUCCESS"))
                System.out.println("Perro eliminado de favoritos exitosamente");
            else
                System.out.println("El perro no fue eliminado, intente nuevamente");

            verPerrosFavoritos(dogFavorito.getApiKey());

        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}

