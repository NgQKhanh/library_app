package com.khanhnq.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import com.khanhnq.libraryapp.api.ApiService;
import com.khanhnq.libraryapp.model.loginResponse;
import com.khanhnq.libraryapp.model.loginPost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private TextView debug;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ id
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnlogin);

        //Nút bấm đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });
    }

    // Hàm xử lý nút bấm đăng nhập
    private void clickLogin(){
        // Lấy dữ liệu
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        username = "k";
        password = "1";
        loginPost postData = new loginPost(username, password);

        // Gửi yêu cầu đăng nhập sử dụng Retrofit và ApiService
        ApiService.apiservice.loginAuth(postData).enqueue(new Callback<loginResponse>() {
            @Override
            public void onResponse(Call<loginResponse> call, Response<loginResponse> response) {
                loginResponse result = response.body();
                if((result != null)&&(result.isStatus())){
                    // Đăng nhập thành công

                    // Tạo Intent và đóng gói dữ liệu
                    Intent loginInfo = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle user = new Bundle();
                    user.putString("userID",result.getUserID());
                    user.putString("username",result.getUsername());
                    loginInfo.putExtra("user",user);

                    // Chuyển hướng activity
                    startActivities(new Intent[]{loginInfo});
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<loginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    };
};

