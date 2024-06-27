package com.khanhnq.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khanhnq.libraryapp.model.Common;

public class ConfirmBookingSeatActivity extends AppCompatActivity {
    TextView title, username, seat, time;
    ImageView btnBack;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_booking_seat);

        // Ánh xạ id
        title = findViewById(R.id.toolbarTitle);
        btnBack = findViewById(R.id.back_icon);
        username = findViewById(R.id.username);
        seat = findViewById(R.id.seat);
        time = findViewById(R.id.time);
        btnConfirm = findViewById(R.id.btnConfirm);
        title.setText(R.string.select_seat);

        // Lấy dữ liệu
        Common user = (Common) getApplication();
        String usernameText = user.getUsername();
        String userID = user.getUserID();
        //Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String shift = intent.getStringExtra("shift");
        String selectSeat = intent.getStringExtra("seat");

        username.setText(usernameText);
        time.setText("Ngày: " + date + " Kíp: " + shift);
        seat.setText("Bàn, ghế: " + selectSeat);
    }
}