package me.azab.oa.booklisting;

/**
 * Created by omar on 5/21/2017.
 */

public class Book {

    private String title;
    private String[] authors;
    private String imageUrl;

    public Book(String title, String[] authors) {
        this.title = title;
        this.authors = authors;
    }

    public Book(String title, String[] authors, String imageUrl) {
        this.title = title;
        this.authors = authors;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
