package com.nullio.opinieallegro.model;

/**
 * Created by bartoszlach on 25.03.2017.
 */
public class Item {
    private final int id;
    private final String title;
    private final String photoUrl;

    public Item(int id, String title, String photoUrl) {
        this.id = id;
        this.title = title;
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getId() {
        return id;
    }
}
