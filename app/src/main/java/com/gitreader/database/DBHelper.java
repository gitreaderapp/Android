package com.gitreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gitreader.model.Book;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "git_reader";
    String TABLE_BOOKS = "tbl_books";
    String KEY_BOOK_URL = "book_url";
    String KEY_BOOK_NAME = "book_name";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BOOKS + " ( " + KEY_BOOK_NAME + " TEXT , " + KEY_BOOK_URL + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_BOOK_NAME, book.getBookName());
        contentValues.put(KEY_BOOK_URL, book.getBookURL());
        long result = db.insert(TABLE_BOOKS, null, contentValues);
    }

    public ArrayList<Book> getAllBooks() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Book> allBooks = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + TABLE_BOOKS + ";", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Book book = new Book();
            book.setBookName(res.getString(res.getColumnIndex(KEY_BOOK_NAME)));
            book.setBookURL(res.getString(res.getColumnIndex(KEY_BOOK_URL)));
            allBooks.add(book);
            res.moveToNext();
        }
        res.close();
        return allBooks;
    }

    public int getBookCount() {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_BOOKS + ";", null);
        res.moveToFirst();

        count = res.getCount();
        res.close();
        return count;
    }

    public void delete(String bookURL) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BOOKS, KEY_BOOK_URL + " = ?", new String[]{bookURL});
    }
}
