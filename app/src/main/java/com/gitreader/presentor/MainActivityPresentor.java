package com.gitreader.presentor;

import android.app.Activity;

import com.gitreader.view.MainActivityView;

/**
 * Created by admin on 1/5/2018.
 */

public interface MainActivityPresentor {
    public void loadBooks();

    void deleteBook(String bookURL);
}
