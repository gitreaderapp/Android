package com.gitreader.presentorImpl;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gitreader.R;
import com.gitreader.adapter.AllBooksAdapter;
import com.gitreader.database.DBHelper;
import com.gitreader.model.Book;
import com.gitreader.presentor.MainActivityPresentor;
import com.gitreader.view.MainActivityView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by admin on 1/5/2018.
 */

public class MainActivityPresentorImpl implements MainActivityPresentor {
    Activity activity;
    MainActivityView mainActivityView;

    public MainActivityPresentorImpl(Activity activity, MainActivityView mainActivityView) {
        this.activity = activity;
        this.mainActivityView = mainActivityView;
    }

    @Override
    public void loadBooks() {
        DBHelper dbHelper = new DBHelper(activity);
        ArrayList<Book> allBooks = dbHelper.getAllBooks();

        if (allBooks.size() > 0) {
            AllBooksAdapter allBooksAdapter = new AllBooksAdapter(activity, allBooks, mainActivityView);
            mainActivityView.setAdapter(allBooksAdapter);
        } else {
            mainActivityView.addNew();
        }

    }

    @Override
    public void deleteBook(final String bookURL) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Material_Light_Dialog_Alert);

        dialog.setContentView(R.layout.dialog_delete_alert);
        Button btnDelete = (Button) dialog.findViewById(R.id.btnDelte);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(activity);
                try {
                    String url = bookURL.replace("/index.html", "");
                    File file = new File(url);
                    Log.e("bookURLFile", url);

                    if (file.isDirectory()) {
                        String[] children = file.list();
                        for (String aChildren : children) {
                            Log.e("book delete aChildren", String.valueOf(new File(file, aChildren).delete()));
                        }
                    }

                    Log.e("book delete", String.valueOf(file.delete()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbHelper.delete(bookURL);
                loadBooks();
                dialog.dismiss();
            }
        });
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();


    }
}
