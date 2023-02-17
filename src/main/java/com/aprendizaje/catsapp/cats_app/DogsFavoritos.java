package com.aprendizaje.catsapp.cats_app;

public class DogsFavoritos {

    private String id;
    private String image_id;
    private String apiKey = "live_1ewFO5Kz36CIRGcQNWTJM7zvhCOaBjDaQS2Qahmu8jypwcwgkTmwmBCoB0qn8Bz2";
    private Imagex image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Imagex getImage() {
        return image;
    }

    public void setImage(Imagex image) {
        this.image = image;
    }
}
