package com.khanhnq.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.khanhnq.libraryapp.model.Common;

public class MainActivity extends AppCompatActivity {

    TextView userName;
    CardView btnLogout, btnMyAccount, btnSearch, btnRRoom, btnRes, btnBBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("Fetching FCM registration token failed", "Tag" + task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        // Log token
                        Log.d("TAG", token);
                    }
                });

        // Ánh xạ id
        userName = findViewById(R.id.username);
        btnLogout = findViewById(R.id.btnLogout);
        btnMyAccount = findViewById(R.id.btnMyAccount);
        btnSearch = findViewById(R.id.btnSearch);
        btnBBook = findViewById(R.id.btnBorrowedBook);
        btnRRoom = findViewById(R.id.btnReadingRoom);
        btnRes = findViewById(R.id.btnReservation);

        // Lấy dữ liệu
        Common user = (Common) getApplication();
        String username = user.getUsername();

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
                Intent intent = new Intent(MainActivity.this, BorBookActivity.class);
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
                startActivity(intent);
            }
        });

        // Nút bấm logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername("");
                user.setUserID("");
                user.setLogin(false);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}