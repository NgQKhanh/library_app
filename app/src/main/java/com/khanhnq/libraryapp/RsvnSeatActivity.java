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

public class RsvnSeatActivity extends AppCompatActivity {
    ImageView selectedSeat, btnBack;
    TextView title, selectedSeatText;
    Button btnConfirm;
    RelativeLayout map, result;
    private float dX, dY;
    private int lastAction;
    int selectedChair = 0, selectedTable = 0;
    List<Integer> naSeats = new ArrayList<>();
    List<Integer> naList = new ArrayList<>();
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
        btnConfirm = findViewById(R.id.btnConfirmSeat);
        title.setText(R.string.select_seat);

        // Lấy dữ liệu
        Common user = (Common) getApplication();
        //Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("date");
        int selectedShift = intent.getIntExtra("shift",0);
        int selectedRoom = intent.getIntExtra("room",0);

        setupTableSet(R.id.table_set_1);
        setupTableSet(R.id.table_set_2);
        setupTableSet(R.id.table_set_3);
        setupTableSet(R.id.table_set_4);

//        naSeats.add(102);
//        naSeats.add(310);
//        naSeats.add(401);
        getBookingInfo ( selectedDate, selectedShift, selectedRoom);

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

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedSeat == null ) {
                    Toast.makeText(RsvnSeatActivity.this, "Chưa chọn chỗ ngồi", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(RsvnSeatActivity.this, ConfirmBookingSeatActivity.class);
                    intent.putExtra("date",  selectedDate);
                    intent.putExtra("shift", selectedShift);
                    intent.putExtra("chair", selectedChair);
                    intent.putExtra("table", selectedTable);
                    intent.putExtra("room", selectedRoom);
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
            String tableSetName = getResources().getResourceEntryName(tableSetId);
            String chairName = getResources().getResourceEntryName(seat.getId());
            for(int i=1; i<= 4; i++){
                if(Objects.equals(tableSetName, "table_set_" + i)) {
                    selectedTable = i;
                }
            }
            for(int i=1; i<= 10; i++){
                if(Objects.equals(chairName, "chair" + i)) {
                    selectedChair = i;
                }
            }
            result.setVisibility(View.VISIBLE);
            selectedSeatText.setText(getString(R.string.selected_chair_and_table, selectedChair, selectedTable));
            selectedSeat = seat;
            seat.setColorFilter(Color.parseColor("#FFFF5722"));
        }
    }

    //Lấy thông tin chỗ ngồi của phòng
    public void getBookingInfo (String date, int shift, int room)
    {
        ApiService.apiservice.getRsvnSeat(date, shift, room).enqueue(new Callback<infoResponse.bookingSeat>() {
            @Override
            public void onResponse(Call<infoResponse.bookingSeat> call, Response<infoResponse.bookingSeat> response) {
                if (response.isSuccessful()) {
                    infoResponse.bookingSeat list = response.body();
                    if (list == null) {
                        Toast.makeText(RsvnSeatActivity.this, R.string.data_not_found, Toast.LENGTH_SHORT).show();
                    } else {
                        naSeats = list.getList();
                        setNonAvaiSeats(naSeats);
                    };
                }
            }
            @Override
            public void onFailure(Call<infoResponse.bookingSeat> call, Throwable t) {

            }
        });
    }
}