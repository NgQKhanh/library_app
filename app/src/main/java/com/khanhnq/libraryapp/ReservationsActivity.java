package com.khanhnq.libraryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.khanhnq.libraryapp.api.ApiService;
import com.khanhnq.libraryapp.model.Common;
import com.khanhnq.libraryapp.model.getInfoPost;
import com.khanhnq.libraryapp.model.infoResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.khanhnq.libraryapp.component.CategoryAdapter;
import com.khanhnq.libraryapp.component.Category;

public class ReservationsActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView title, showDate, btnPickSeat;
    Spinner dropList_Shift, dropList_roomToRsvn, dropList_roomToGetInfo;
    Button btnDel;
    int selectedShift = 1, selectedRoom = 1;
    CategoryAdapter shiftAdapter,roomAdapter;
    private Date selectDate;
    String selectedDate;
    LinearLayout userInfo, userInfoDetail, rsrvnForm, rsrvnFormDetail,info, infoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        // Lấy dữ liệu
        Common user = (Common) getApplication();
        String userID = user.getUserID();

        // Ánh xạ id
        btnBack = findViewById(R.id.back_icon);
        showDate = findViewById(R.id.showPickedDate);
        dropList_Shift = findViewById(R.id.dropListShift);
        dropList_roomToGetInfo = findViewById(R.id.dropListRoomToGetInfo);
        dropList_roomToRsvn = findViewById(R.id.dropListRoomToRsvn);
        title = findViewById(R.id.toolbarTitle);
        btnPickSeat = findViewById(R.id.pickSeat);
        userInfo = findViewById(R.id.userInfo);
        userInfoDetail = findViewById(R.id.userInfoDetail);
        rsrvnForm = findViewById(R.id.rsrvnForm);
        rsrvnFormDetail = findViewById(R.id.rsrvnFormDetail);
        info = findViewById(R.id.info);
        infoDetail = findViewById(R.id.infoDetail);
        title.setText(R.string.reservation);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Bấm chọn phòng để hiển thị thông tin số lượng đặt chỗ
        roomAdapter = new CategoryAdapter(this,R.layout.select_room,
                                                       R.layout.category_item,
                                                       getCategoryRoom());
        dropList_roomToGetInfo.setAdapter(roomAdapter);
        dropList_roomToGetInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                selectedRoom = i;
                getRoomRsvnInPeriod(selectedRoom);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Hiển thị thong tin đặt chỗ của người dùng
        getUserRsvnInfo(userID);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Form đăng ký

            // Bấm chọn ngày
            showDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickDate();
                }
            });

            // Bấm chọn kíp
            shiftAdapter = new CategoryAdapter(this,R.layout.select_shift,
                                                            R.layout.category_item,
                                                            getCategoryShift());
            dropList_Shift.setAdapter(shiftAdapter);
            dropList_Shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                        selectedShift = i+1;
                    }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // Bấm chọn phòng
            roomAdapter = new CategoryAdapter(this,R.layout.select_room,
                                                           R.layout.category_item,
                                                           getCategoryRoom());
            dropList_roomToRsvn.setAdapter(roomAdapter);
            dropList_roomToRsvn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedRoom = i+1;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // Bấm chọn chỗ ngồi
            btnPickSeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectDate == null ) {
                        Toast.makeText(ReservationsActivity.this, "Chưa chọn ngày đăng ký", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(ReservationsActivity.this, RsvnSeatActivity.class);
                        intent.putExtra("date",  selectedDate);
                        intent.putExtra("shift", selectedShift);
                        intent.putExtra("room", selectedRoom);
                        startActivity(intent);
                    }
                }
            });

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Ẩn hiện menu
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userInfoDetail.getVisibility() == View.VISIBLE) {
                    userInfoDetail.setVisibility(View.GONE);
                }
                else {
                    userInfoDetail.setVisibility(View.VISIBLE);
                }
            }
        });

        rsrvnForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rsrvnFormDetail.getVisibility() == View.VISIBLE) {
                    rsrvnFormDetail.setVisibility(View.GONE);
                }
                else {
                    rsrvnFormDetail.setVisibility(View.VISIBLE);
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(infoDetail.getVisibility() == View.VISIBLE) {
                    infoDetail.setVisibility(View.GONE);
                }
                else {
                    infoDetail.setVisibility(View.VISIBLE);
                }
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Lấy thông tin đăng ký của phòng trong khoảng tg đăng ký
    private void getRoomRsvnInPeriod(int room){
        ApiService.apiservice.getRsvnInPeriod(room).enqueue(new Callback<infoResponse.RsvnInfo.RsvnInPeriod>() {
            @Override
            public void onResponse(Call<infoResponse.RsvnInfo.RsvnInPeriod> call, Response<infoResponse.RsvnInfo.RsvnInPeriod> response) {
                infoResponse.RsvnInfo.RsvnInPeriod data = response.body();
                if(data != null){
                    List<String> dateList = data.getDateArray();
                    List<Integer> shift1List = data.getShift1Array();
                    List<Integer> shift2List = data.getShift2Array();

                    //clear bảng
                    TableLayout tableLayout = findViewById(R.id.reservationTable);
                    int rowCount = tableLayout.getChildCount();
                    for (int i = 1; i < rowCount; i++) {
                        tableLayout.removeViewAt(1);
                    }

                    for (int i = 0; i < dateList.size(); i++) {
                        String date =  String.valueOf(dateList.get(i));
                        String shift1 = String.valueOf(shift1List.get(i));
                        String shift2 = String.valueOf(shift2List.get(i));

                        addRsvnRoomInfo(date, shift1, shift2);
                    }
                }
                else{
                    Toast.makeText(ReservationsActivity.this,"Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<infoResponse.RsvnInfo.RsvnInPeriod> call, Throwable t) {
                Toast.makeText(ReservationsActivity.this,"Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Lấy thông tin đăng ký của người dùng
    private void getUserRsvnInfo(String userID){
        ApiService.apiservice.getUserRsvn(userID).enqueue(new Callback<infoResponse.RsvnInfo.UserReservation>() {
            @Override
            public void onResponse(Call<infoResponse.RsvnInfo.UserReservation> call, Response<infoResponse.RsvnInfo.UserReservation> response) {

                infoResponse.RsvnInfo.UserReservation data = response.body();
                if(data != null){
                    List<infoResponse.RsvnInfo.UserReservation.Reservation> userRsvn = data.getRsvn();
                    String text = "Bạn chưa đăng ký phòng đọc.";
                    if (!userRsvn.isEmpty()) {
                        Log.d("[RSVN] DEBUG: ", "user" + userRsvn);
                        text = "------------------------------------\n";
                        for (int i = 0; i < userRsvn.size(); i++) {
                            text = text + "Ngày: "  + userRsvn.get(i).getDate()  + "\n"
                                        + "Kíp: "   + userRsvn.get(i).getShift() + "\n"
                                        + seatCode2String(userRsvn.get(i).getSeat()) + "\n"
                                        + "Phòng: " + userRsvn.get(i).getRoom()  + "\n";
                            text = text + "------------------------------------\n";
                        }
                        TextView showUserRsvn = findViewById(R.id.showUserInfo);
                        showUserRsvn.setText(text);
                    }
                }
                else {
                    Toast.makeText(ReservationsActivity.this,"Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<infoResponse.RsvnInfo.UserReservation> call, Throwable t) {
                Toast.makeText(ReservationsActivity.this,"Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Tạo bảng chọn ngày
    private void pickDate() {
        showDate = findViewById(R.id.showPickedDate);
        //Lấy ngày tháng hiện tại
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                 c.set(y,m,d);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                showDate.setText(simpleDateFormat.format(c.getTime()));
                selectDate = c.getTime();
                selectedDate = simpleDateFormat.format(c.getTime());
            }
        },year, month, day);
        datePickerDialog.show();
    }

    // Hàm chèn hàng vào bảng thông tin đăt chỗ
    private void addRsvnRoomInfo(String date, String shift1, String shift2){
        // Lấy đối tượng TableLayout từ layout
        TableLayout tableLayout = findViewById(R.id.reservationTable);
        float[] columnWeights = { 2.5f, 2f, 2f }; // Độ rộng cóc cột

        // Tạo một TableRow mới
        TableRow newRow = new TableRow(this);

        // Thêm TextView cho cột Ngày
        TextView dateCol = new TextView(this);
        dateCol.setText(date);
        dateCol.setTextSize(10);
        dateCol.setGravity(Gravity.CENTER);
        dateCol.setPadding(5,5,5,5);
        dateCol.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[0]));
        newRow.addView(dateCol);

        // Thêm TextView cho cột Kíp sáng
        TextView shift1Col = new TextView(this);
        shift1Col.setText(shift1+"/30");
        shift1Col.setTextSize(10);
        shift1Col.setGravity(Gravity.CENTER);
        shift1Col.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[1]));
        newRow.addView(shift1Col);

        // Thêm TextView cho cột Kíp chiều
        TextView shift2Col = new TextView(this);
        shift2Col.setText(shift2+"/30");
        shift2Col.setTextSize(10);
        shift2Col.setGravity(Gravity.CENTER);
        shift2Col.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[2]));
        newRow.addView(shift2Col);

        // Thêm TableRow mới vào TableLayout
        tableLayout.addView(newRow);
    }

    //Hàm format date
    public static String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = inputFormat.parse(dateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Category
    private List<Category> getCategoryShift(){
        List<Category> list = new ArrayList<>();
        list.add(new Category("Kíp sáng"));
        list.add(new Category("Kíp chiều"));
        return list;
    }

    private List<Category> getCategoryRoom(){
        List<Category> list = new ArrayList<>();
        list.add(new Category("Phòng 205"));
        list.add(new Category("Phòng 305"));
        return list;
    }

    private String seatCode2String(int code){
        String seat = "Bàn Ghế";
        seat = "Bàn: " + code/100 + " Ghế: " + code%100;
        return seat;
    }
}