package com.github.panda3.panda3player.model;

/**
 * Created by VuYeK on 06.12.2016.
 */
public class Movie {
    private long id;
    private String uri;
    private int progress;
    private int favourite;

    public Movie(String uri) {

        this.uri = uri;
    }

    public Movie(String uri, int progress) {
        this.uri = uri;
        this.progress = progress;
    }

    public Movie(String uri, int progress, int favourite) {
        this.uri = uri;
        this.progress = progress;
        this.favourite = favourite;
    }

    public Movie(long id, String uri, int progress, int favourite) {
        this.id = id;
        this.uri = uri;
        this.progress = progress;
        this.favourite = favourite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (id != movie.id) return false;
        if (progress != movie.progress) return false;
        if (favourite != movie.favourite) return false;
        return uri != null ? uri.equals(movie.uri) : movie.uri == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + uri.hashCode();
        result = 31 * result + progress;
        result = 31 * result + favourite;
        return result;
    }
}
