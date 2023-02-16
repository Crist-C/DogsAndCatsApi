module com.aprendizaje.catsapp.cats_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires okhttp;
    requires com.google.gson;

    opens com.aprendizaje.catsapp.cats_app to com.google.gson, javafx.fxml;
    exports com.aprendizaje.catsapp.cats_app;
}