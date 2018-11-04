package com.gitreader.view;

import com.gitreader.adapter.AllBooksAdapter;

/**
 * Created by admin on 1/5/2018.
 */
public interface MainActivityView {
    void onSelectBook(String bookURL);

    void setAdapter(AllBooksAdapter allBooksAdapter);

    void onDelete(String bookURL);

    void addNew();
}
