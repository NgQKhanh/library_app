// MainActivity.java
package com.khanhnq.libraryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.khanhnq.libraryapp.api.ApiService;
import com.khanhnq.libraryapp.model.Common;
import com.khanhnq.libraryapp.model.infoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSeatActivity extends AppCompatActivity {
    ImageView selectedSeat, btnBack;
    TextView title, selectedSeatText;
    Button btnConfirm;
    RelativeLayout map, result;
    private float dX, dY;
    private int lastAction;
    int userSelectedSeat;
    List<Integer> naSeats = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_seat);

        // Ánh xạ id
        title = findViewById(R.id.toolbarTitle);
        btnBack = findViewById(R.id.back_icon);
        map = findViewById(R.id.map);
        result = findViewById(R.id.showSelectedSeat);
        selectedSeatText = findViewById(R.id.selectedSeatText);
        btnConfirm = findViewById(R.id.btnConfirm);
        title.setText(R.string.select_seat);

        // Lấy dữ liệu
        Common user = (Common) getApplication();
        String userID = user.getUserID();
        //Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String shift = intent.getStringExtra("shift");

        Log.d("Debug booking seat", "Data: " + userID + date + shift);

        setupTableSet(R.id.table_set_1);
        setupTableSet(R.id.table_set_2);
        setupTableSet(R.id.table_set_3);
        setupTableSet(R.id.table_set_4);

        naSeats.add(102);
        naSeats.add(310);
        naSeats.add(401);

        setNonAvaiSeats(naSeats);

        // Dragable bản đồ
        map.setOnTouchListener((view, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    lastAction = MotionEvent.ACTION_DOWN;
                    break;

                case MotionEvent.ACTION_MOVE:
                    view.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    lastAction = MotionEvent.ACTION_MOVE;
                    break;

                case MotionEvent.ACTION_UP:
                    if (lastAction == MotionEvent.ACTION_DOWN) {
                        // Handle click event if needed
                    }
                    break;
                default:
                    return false;
            }
            return true;
        });

        searchBook ( "2024-06-23", shift, "1");

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedSeat == null ) {
                    Toast.makeText(BookSeatActivity.this, "Chưa chọn chỗ ngồi", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(BookSeatActivity.this, ConfirmBookingSeatActivity.class);
                    intent.putExtra("date",  date);
                    intent.putExtra("shift", shift);
                    intent.putExtra("seat",  userSelectedSeat);
                    startActivity(intent);
                }
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
    // Setup onCLick cho từng bàn
    private void setupTableSet(int tableSetId) {
        View tableSet = findViewById(tableSetId);
        if (tableSet instanceof ViewGroup) {
            ViewGroup tableSetLayout = (ViewGroup) tableSet;
            for (int i = 0; i < tableSetLayout.getChildCount(); i++) {
                View child = tableSetLayout.getChildAt(i);
                if (child instanceof ImageView) {
                    ImageView chair = (ImageView) child;
                    chair.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectSeat(chair,tableSetId);
                            Log.d("Debug select seat","DA CHON: " + userSelectedSeat);
                        }
                    });
                }
            }
        }
    }

    // Set non available
    private void setNonAvailabe(int tableNum, int chairNum) {
        int tableSetId = 1;
        switch (tableNum) {
            case 1:
                tableSetId = R.id.table_set_1;
                break;
            case 2:
                tableSetId = R.id.table_set_2;
                break;
            case 3:
                tableSetId = R.id.table_set_3;
                break;
            case 4:
                tableSetId = R.id.table_set_4;
                break;
        }
        String chairName = "chair" + chairNum;
        View tableSet = findViewById(tableSetId);
        if (tableSet instanceof ViewGroup) {
            ViewGroup tableSetLayout = (ViewGroup) tableSet;
            for (int i = 0; i < tableSetLayout.getChildCount(); i++) {
                View child = tableSetLayout.getChildAt(i);
                if (child instanceof ImageView) {
                    String resourceName = getResources().getResourceEntryName(child.getId());
                    if (resourceName.equals(chairName)) {
                        ImageView chairView = (ImageView) child;
                        chairView.setColorFilter(Color.GRAY);
                        chairView.setTag("unselectable");
                        break;
                    }
                }
            }
        }
    }

    //Set non available cho list seat
    private void setNonAvaiSeats(List<Integer> naSeats){
        for (Integer seat : naSeats) {
            int chair = seat%100;
            int table = (seat - chair)/100;
            setNonAvailabe(table, chair);
        }
    }

    //Chọn chỗ ngồi
    private void selectSeat(ImageView seat, int tableSetId) {
        if("selectable".equals(seat.getTag())) {
            if (selectedSeat != null) {
                selectedSeat.setColorFilter(Color.parseColor("#03C60B")); // bỏ chọn chỗ đã chọn trước đó
            }
            int table = 0, chair = 0;
            String tableSetName = getResources().getResourceEntryName(tableSetId);
            String chairName = getResources().getResourceEntryName(seat.getId());
            for(int i=1; i<= 4; i++){
                if(Objects.equals(tableSetName, "table_set_" + i)) {
                    userSelectedSeat = i*100;
                    table = i;
                }
            }
            for(int i=1; i<= 10; i++){
                if(Objects.equals(chairName, "chair" + i)) {
                    userSelectedSeat += i;
                    chair = i;
                }
            }
            result.setVisibility(View.VISIBLE);
            selectedSeatText.setText("Đã chọn: bàn " + table + " ghế "+ chair);
            selectedSeat = seat;
            seat.setColorFilter(Color.parseColor("#FFFF5722"));
        }
    }

    //Lấy thông tin chỗ ngồi đã đặt
    public void searchBook (String date, String shift, String room)
    {
        ApiService.apiservice.getBookingSeat(date, shift, room).enqueue(new Callback<infoResponse.bookingSeat>() {
            @Override
            public void onResponse(Call<infoResponse.bookingSeat> call, Response<infoResponse.bookingSeat> response) {
                if (response.isSuccessful()) {
                    infoResponse.bookingSeat list = response.body();
                    if (list == null) {
                        Toast.makeText(BookSeatActivity.this, "Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
                    } else {
                           naSeats = list.getList();
//                            for (Integer seat : naSeats) {
//                                Log.d("DEBUGGGGGG", "Test: " );
//                            }
                        };
                    }
                }
            @Override
            public void onFailure(Call<infoResponse.bookingSeat> call, Throwable t) {

            }
        });
    }
}