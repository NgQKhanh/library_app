package com.khanhnq.libraryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView userName, borrowedBookList;
    CardView btnLogout, btnMyAccount, btnSearch, btnRRoom, btnRes, btnBBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ id
        userName = findViewById(R.id.username);
        btnLogout = findViewById(R.id.btnLogout);
        btnMyAccount = findViewById(R.id.btnMyAccount);
        btnSearch = findViewById(R.id.btnSearch);
        btnBBook = findViewById(R.id.btnBorrowedBook);
        btnRRoom = findViewById(R.id.btnReadingRoom);
        btnRes = findViewById(R.id.btnReservation);

        //Lấy dữ liệu từ Intent
        String UID = getIntent().getBundleExtra("user").getString("userID");
        String username = getIntent().getBundleExtra("user").getString("username");
        Bundle user = new Bundle();
        user.putString("userID",UID);
        user.putString("username",username);

//        // Lưu thông tin đăng nhập
//        SharedPreferences sharedPreferences = getSharedPreferences(UID, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Thông tin tên người dùng
        userName.setText(username);

        // Nút bấm Tài khoản
        btnMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        // Nút bấm Tra cứu
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // Nút bấm Mượn sách
        btnBBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent và đóng gói dữ liệu
                Intent intent = new Intent(MainActivity.this, BorBookActivity.class);
                intent.putExtra("user",user);
                //Chuyển trang
                startActivity(intent);
            }
        });

        // Nút bấm Phòng đọc
        btnRRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadingRoomActivity.class);
                startActivity(intent);
            }
        });

        // Nút bấm Đăng ký
        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReservationsActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        // Nút bấm logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}