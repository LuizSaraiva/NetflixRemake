package com.project.netflixremake.model;

public class Movie {

    private int coverUrl;
    private String title;
    private String desc;
    private String cast;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public int getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(int coverUrl) {
        this.coverUrl = coverUrl;
    }
}
