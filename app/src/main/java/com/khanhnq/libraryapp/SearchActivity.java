package com.khanhnq.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.khanhnq.libraryapp.R;

public class SearchActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Ánh xạ id
        btnBack = findViewById(R.id.back_icon);
        title = findViewById(R.id.toolbarTitle);

        title.setText("Tra cứu");

        // nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}