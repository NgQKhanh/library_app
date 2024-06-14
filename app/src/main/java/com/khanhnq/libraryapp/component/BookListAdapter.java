package com.khanhnq.libraryapp.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.khanhnq.libraryapp.R;

public class BookListAdapter extends ArrayAdapter <BookTitleList>{
    public BookListAdapter(@NonNull Context context, ArrayList<BookTitleList> dataArrayList) {
        super(context, R.layout.search_list_item, dataArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        BookTitleList listData = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.search_list_item, parent, false);
        }
        TextView bookName = view.findViewById(R.id.bookName);
        TextView author = view.findViewById(R.id.author);

        bookName.setText(listData.getBookName());
        author.setText(listData.getAuthor());

        return view;
    }
}