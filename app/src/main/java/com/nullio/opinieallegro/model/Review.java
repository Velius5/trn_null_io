package com.nullio.opinieallegro.model;

import java.util.List;

/**
 * Created by bartoszlach on 25.03.2017.
 */
public class Review {
    private final List<String> photoUrls;
    private final String content;

    public Review() {
        photoUrls = null;
        content = null;
    }

    public Review(List<String> photoUrls, String content) {
        this.photoUrls = photoUrls;
        this.content = content;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public String getContent() {
        return content;
    }
}
