package com.khanhnq.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.khanhnq.libraryapp.api.ApiService;
import com.khanhnq.libraryapp.model.getInfoPost;
import com.khanhnq.libraryapp.model.infoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadingRoomActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView title, RR1_Num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_room);

        // Ánh xạ id
        btnBack = findViewById(R.id.back_icon);
        title = findViewById(R.id.toolbarTitle);
        RR1_Num = findViewById(R.id.RR1_Num);
        title.setText("Phòng đọc");

        //Hiển thị thông tin
        getReadingRoomInfo();

        // nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Hàm lấy dữ liệu
    private void getReadingRoomInfo(){
        ApiService.apiservice.getReadingRoomInfo().enqueue(new Callback<infoResponse.RRInfo>() {
            @Override
            public void onResponse(Call<infoResponse.RRInfo> call, Response<infoResponse.RRInfo> response) {
                infoResponse.RRInfo.readingRoom rrInfo = response.body().getReadingRoom();
                 int userNumber = rrInfo.getUserNumber();
                 String num = "Số người đang sử dụng: " + String.valueOf(userNumber) + "/30";
                 RR1_Num.setText(num);
            }
            @Override
            public void onFailure(Call<infoResponse.RRInfo> call, Throwable t) {
            }
        });
    }
}