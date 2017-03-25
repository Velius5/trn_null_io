package com.nullio.opinieallegro.model;

import java.util.List;

/**
 * Created by bartoszlach on 25.03.2017.
 */
public class ReviewDisplay {
    private final String content;
    private final List<String> photoUrls;

    public ReviewDisplay(String content, List<String> photoUrls) {
        this.content = content;
        this.photoUrls = photoUrls;
    }

    public ReviewDisplay() {
        this.content = null;
        this.photoUrls = null;
    }

    public String getContent() {
        return content;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }
}
