package com.khanhnq.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.khanhnq.libraryapp.R;

public class MyAccountActivity extends AppCompatActivity {
    ImageView btnBack;
    Button btnUpdatePrf, btnUpdatePW;
    TextView title, name, address, number, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        // Ánh xạ id
        btnBack = findViewById(R.id.back_icon);
        btnUpdatePrf = findViewById(R.id.update_profile);
        btnUpdatePW = findViewById(R.id.update_password);
        title = findViewById(R.id.toolbarTitle);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        number = findViewById(R.id.number);
        email = findViewById(R.id.email);

        //Lấy dữ liệu từ Intent
        String username = getIntent().getBundleExtra("user").getString("username");

        // Hiển thị thông tin
        title.setText("Tài khoản");
        name.setText("Họ và tên: " + username);
        address.setText("Địa chỉ: " + "Hà Nội");
        number.setText("Số điện thoại: "+ "0917-xxx-xxx");
        email.setText("Email: "+ "khanh.nq191909@sis.hust.edu.vn");

        // nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}