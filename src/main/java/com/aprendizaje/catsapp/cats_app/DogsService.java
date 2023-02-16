package com.aprendizaje.catsapp.cats_app;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

public class DogsService {

    public static void verPerros() throws IOException {
        // Vamos a traer los datos de la API
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.thedogapi.com/v1/images/search")
                .method("GET",null)
                .build();
        Response response = client.newCall(request).execute();

        ResponseBody json;
        json = response.body();
        System.out.println("Json Antes: " + json);
        // Eliminamos el corchete inicial y el final para que quede como objeto JSON
        //json = json.substring(1, json.length()-1);
        System.out.println("Json Después: " + json);

        // Convertimos un objeto de la clase GSon a la clase Dogs
        Gson gson = new Gson();
        Dogs perro = gson.fromJson(String.valueOf(json), Dogs[].class)[0];

        // Redimensionamos la imagen en caso de necesitar
        Image image = null;
        try {
            URL url = new URL(perro.getUrl());
            HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            httpcon.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
            //ImageIcon fondoG = new ImageIcon(bufferedImage);

            ImageIcon fondoPerro = new ImageIcon(bufferedImage);

            if(fondoPerro.getIconWidth() > 800) {
                // redimencionamos
                Image fondo = fondoPerro.getImage();
                Image imagenModificada = fondo.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                fondoPerro = new ImageIcon(imagenModificada);
            }

            String menu = """
                    Opciones:\s
                     1. Ver otra imagen
                     2. Favorito
                     3. Volver
                    """;

            String[] botones = {"ver otra imagen", "favorito", "volver"};
            String id_perro = perro.getId();
            String opcionEscogida = (String) JOptionPane.showInputDialog(null, menu,id_perro,JOptionPane.INFORMATION_MESSAGE, fondoPerro, botones, botones[0]);

            switch (opcionEscogida)
            {
                case "ver otra imagen":
                    verPerros();
                    break;
                case "favorito":
                    favoriteDog(perro);
                    break;
                case "volver":
                    break;
            }

        }catch (IOException e){
            System.out.println(e);
        }


    }

    private static void favoriteDog(Dogs perro) {

        
    }
}

