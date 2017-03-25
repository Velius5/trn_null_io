package com.nullio.opinieallegro.model;

/**
 * Created by bartoszlach on 25.03.2017.
 */
public class Item {
    private final String title;
    private final String photoUrl;

    public Item(String title, String photoUrl) {
        this.title = title;
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
