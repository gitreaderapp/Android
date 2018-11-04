package com.gitreader.model;

/**
 * Created by admin on 1/5/2018.
 */

public class Book {
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookURL() {
        return bookURL;
    }

    public void setBookURL(String bookURL) {
        this.bookURL = bookURL;
    }

    public String bookName, bookURL;
}
