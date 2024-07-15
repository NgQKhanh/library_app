package com.khanhnq.libraryapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khanhnq.libraryapp.api.ApiService;
import com.khanhnq.libraryapp.model.Common;
import com.khanhnq.libraryapp.model.getInfoPost;
import com.khanhnq.libraryapp.model.infoResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmBookingSeatActivity extends AppCompatActivity {
    TextView title, username, seat, time;
    ImageView btnBack;
    Button btnConfirm;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking_seat);

        // Ánh xạ id
        title = findViewById(R.id.toolbarTitle);
        btnBack = findViewById(R.id.back_icon);
        username = findViewById(R.id.username);
        seat = findViewById(R.id.seat);
        time = findViewById(R.id.time);
        btnConfirm = findViewById(R.id.btnConfirm);
        title.setText(R.string.confirm_booking_seat);

        // Lấy dữ liệu
        Common user = (Common) getApplication();
        String usernameText = user.getUsername();
        String userID = user.getUserID();
        //Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("date");
        int selectedShift = intent.getIntExtra("shift",0);
        int selectedChair = intent.getIntExtra("chair",0);
        int selectedTable = intent.getIntExtra("table",0);
        int selectedRoom = intent.getIntExtra("room",0);
        int selectSeat;
        selectSeat = selectedChair + selectedTable*100;

        Log.d("CONFIRM SELECT SHIFT" , "show" + selectedShift);

        //Hiển thị thông tin
        username.setText(usernameText);
        time.setText(getString(R.string.selected_date_and_shift, selectedDate, selectedShift));
        seat.setText(getString(R.string.selected_seat_and_room, selectedChair, selectedTable, selectedRoom));

        //Nút xác nhận
        builder = new AlertDialog.Builder(this);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle(R.string.announce)
                    .setMessage("Xác nhận đặt chỗ phòng đọc")
                    .setCancelable(true)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmRsvn (userID, selectedDate, selectedShift, selectSeat, selectedRoom);
                        }
                    })
                    .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
                }
        });

        // Nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //Gửi thông tin đặt chỗ lên server
    private void confirmRsvn (String userID, String date, int shift, int seat, int room){
        getInfoPost.reservationPost post = new getInfoPost.reservationPost(userID, date, shift, seat, room);
        ApiService.apiservice.confirmRsvn(post).enqueue(new Callback<infoResponse.confirmRsvn>() {
            @Override
            public void onResponse(Call<infoResponse.confirmRsvn> call, Response<infoResponse.confirmRsvn> response) {
                if (response.body() != null){
                    String status = response.body().getStatus();
                    if ("ok".equals(status)) {
                        Toast.makeText(ConfirmBookingSeatActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    }
                    else if("exceeded".equals(status)) {
                        Toast.makeText(ConfirmBookingSeatActivity.this, "Vượt quá số lượng đăng ký", Toast.LENGTH_SHORT).show();
                    }
                    else if("unavailable".equals(status)) {
                        Toast.makeText(ConfirmBookingSeatActivity.this, "Chưa mở đăng ký", Toast.LENGTH_SHORT).show();
                    }
                    else if("error".equals(status)) {
                        Toast.makeText(ConfirmBookingSeatActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<infoResponse.confirmRsvn> call, Throwable t) {
            }
        });
    }
}