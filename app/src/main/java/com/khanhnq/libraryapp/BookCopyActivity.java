package com.khanhnq.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BookCopyActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView title, bookNameId, authorId, categoryId, publisherId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_copy);

        // Ánh xạ id
        btnBack = findViewById(R.id.back_icon);
        title = findViewById(R.id.toolbarTitle);
        bookNameId = findViewById(R.id.bookName);
        authorId = findViewById(R.id.author);
        categoryId = findViewById(R.id.category);
        publisherId = findViewById(R.id.publisher);

        title.setText("Chi tiết");

        //Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String bookName = intent.getStringExtra("bookName");
        String author = intent.getStringExtra("author");
        String id = intent.getStringExtra("id");
        String publisher = intent.getStringExtra("publisher");
        String category = intent.getStringExtra("category");

        // Hiển thị thông tin
        bookNameId.setText(bookName);
        authorId.setText(author);
        publisherId.setText(publisher);
        categoryId.setText(category);

        // Nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}