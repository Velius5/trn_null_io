package com.nullio.opinieallegro.transfer;

public class OfferResponse {

    private String name;
    private String id;
    private MainImageResponse mainImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MainImageResponse getMain() {
        return mainImage;
    }

    public void setMain(MainImageResponse mainImage) {
        this.mainImage = mainImage;
    }
}
