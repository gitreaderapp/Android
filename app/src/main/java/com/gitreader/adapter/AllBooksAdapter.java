package com.gitreader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gitreader.R;
import com.gitreader.activities.MainActivity;
import com.gitreader.model.Book;
import com.gitreader.view.MainActivityView;

import java.util.List;

/**
 * Created by admin on 1/5/2018.
 */

public class AllBooksAdapter extends RecyclerView.Adapter<AllBooksAdapter.Holder> {

    private List<Book> mCategoriesList;
    MainActivityView categoryView;

    public class Holder extends RecyclerView.ViewHolder {
        public TextView mCategories;
        LinearLayout llForegroundView;
        ImageView imgDelete;

        public Holder(View view) {
            super(view);
            mCategories = (TextView) view.findViewById(R.id.txtBookName);
            llForegroundView = (LinearLayout) view.findViewById(R.id.root);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        }
    }

    public AllBooksAdapter(Context context, List<Book> mCategoriesList, MainActivityView categoryView) {
        this.mCategoriesList = mCategoriesList;
        this.categoryView = categoryView;
    }

    public String getItem(int position) {
        return mCategoriesList.get(position).getBookURL();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_books_inflated_view, parent, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        Book book = mCategoriesList.get(position);
        holder.mCategories.setText(/*"Book" + (position + 1)*/book.getBookName());
        holder.llForegroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryView.onSelectBook(mCategoriesList.get(position).getBookURL());
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryView.onDelete(mCategoriesList.get(position).getBookURL());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCategoriesList != null) {
            return mCategoriesList.size();
        }
        return 0;
    }
}

