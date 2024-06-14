package com.khanhnq.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.khanhnq.libraryapp.api.ApiService;
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
    TextView title, showDate;
    Spinner showShift;
    Button btnConfirm, btnDel;
    String[] shift = {"Kíp sáng", "Kíp chiều"};
    Integer selectedShift;
    CategoryAdapter categoryAdapter;
    private Date selectedDate;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        //Lấy dữ liệu từ Intent
        Intent loginInfo = getIntent();
        Bundle user = loginInfo.getBundleExtra("user");
        String UID = user.getString("userID");

        // Ánh xạ id
        btnBack = findViewById(R.id.back_icon);
        showDate = findViewById(R.id.showPickedDate);
        showShift = findViewById(R.id.showPickedShift);
        btnConfirm = findViewById(R.id.btnRsvnConfirm);
        btnDel = findViewById(R.id.btnRsvnDel);
        title = findViewById(R.id.toolbarTitle);
        title.setText("Đặt chỗ");

        // Hiển thị thông tin đặt chỗ phòng đọc
        getReservationInfo(UID);

        // Bấm chọn ngày
        showDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        // Bấm chọn kíp
        categoryAdapter = new CategoryAdapter(this,R.layout.select_shift,R.layout.category_shift,getListCategory());
        showShift.setAdapter(categoryAdapter);
        showShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                selectedShift = i+1;
                }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Xác nhận đăng ký
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSelectedDate() == null) {
                    Toast.makeText(ReservationsActivity.this, "Chưa chọn ngày đăng ký", Toast.LENGTH_SHORT).show();
                }
                else {
                    Date selectedDate = getSelectedDate();
                    confirmRsvn(UID, selectedDate, selectedShift);
                }
            }
        });

        //Hủy đăng ký
        builder = new AlertDialog.Builder(this);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Thông báo")
                        .setMessage("Bạn có muốn hủy đăng ký không?")
                        .setCancelable(true)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delRsvn(UID);
                                }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
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

    // Lấy thông tin đăng ký
    private void getReservationInfo(String userID){
        getInfoPost postData = new getInfoPost(userID);
        ApiService.apiservice.getReservationInfo(postData).enqueue(new Callback<infoResponse.RsvnInfo>() {
            @Override
            public void onResponse(Call<infoResponse.RsvnInfo> call, Response<infoResponse.RsvnInfo> response) {
                // Thông tin đăng ký chung
                infoResponse.RsvnInfo data = response.body();
                List<String> dateList = data.getDateArray();
                List<Integer> shift1List = data.getShift1Array();
                List<Integer> shift2List = data.getShift2Array();

                for (int i = 0; i < dateList.size(); i++) {
                    String date = dateList.get(i);
                    String shift1 = String.valueOf(shift1List.get(i));
                    String shift2 = String.valueOf(shift2List.get(i));

                    addRsvnRoomInfo(formatDate(date), shift1, shift2);
                }
                // Thông tin đăng ký của người dùng
                List<infoResponse.RsvnInfo.userReservation.Reservation> userList = data.getUserReservation().getRsvn();
                String text = "Bạn chưa đăng ký phòng đọc.";
                if(userList.size() > 0){
                    text = "Ngày đăng ký: ";
                    for (int i = 0; i < userList.size(); i++) {
                        text = text + "\n" + formatDate(userList.get(i).getDate()) + ", kíp: " + userList.get(i).getShift();
                    }
                    TextView showUserRsvn = findViewById(R.id.showUserInfo);
                    showUserRsvn.setText(text);
                }
            }
            @Override
            public void onFailure(Call<infoResponse.RsvnInfo> call, Throwable t) {
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                showDate.setText(simpleDateFormat.format(c.getTime()));
                selectedDate = c.getTime();
            }
        },year, month, day);
        datePickerDialog.show();
    }
    // Phương thức để trả về ngày được chọn
    private Date getSelectedDate() {
        return selectedDate;
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

    //Xác nhận đặt chỗ
    private void confirmRsvn (String userID, Date date, Integer shift){
        getInfoPost.reservationPost post = new getInfoPost.reservationPost(userID, date, shift);
        ApiService.apiservice.confirmRsvn(post).enqueue(new Callback<infoResponse.confirmRsvn>() {
            @Override
            public void onResponse(Call<infoResponse.confirmRsvn> call, Response<infoResponse.confirmRsvn> response) {
                if ("ok".equals(response.body().getStatus().toString())) {
                    Toast.makeText(ReservationsActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    TableLayout tableLayout = findViewById(R.id.reservationTable);
                    tableLayout.removeAllViews();
                    getReservationInfo(userID);
                }
                else if("exceeded".equals(response.body().getStatus().toString())) {
                    Toast.makeText(ReservationsActivity.this, "Vượt quá số lượng đăng ký", Toast.LENGTH_SHORT).show();
                }
                else if("unavailable".equals(response.body().getStatus().toString())) {
                    Toast.makeText(ReservationsActivity.this, "Chưa mở đăng ký", Toast.LENGTH_SHORT).show();
                }
                else if("error".equals(response.body().getStatus().toString())) {
                    Toast.makeText(ReservationsActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<infoResponse.confirmRsvn> call, Throwable t) {
            }
        });
    }

    // Hàm HỦY đăng ký
    private void delRsvn (String userID){
        getInfoPost postData = new getInfoPost(userID);
        ApiService.apiservice.delRsvn(postData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                Log.d("debug", String.valueOf(statusCode));
                if (response.isSuccessful()) {
//                    TableLayout tableLayout = findViewById(R.id.reservationTable);
//                    tableLayout.removeAllViews();
//                    getReservationInfo(userID);
                    Toast.makeText(ReservationsActivity.this, "Hủy đăng ký thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReservationsActivity.this, "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReservationsActivity.this, "Đã có lỗi xảy raaaa!", Toast.LENGTH_SHORT).show();
            }
        });
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
    private List<Category> getListCategory(){
        List<Category> list = new ArrayList<>();
        list.add(new Category("Kíp sáng"));
        list.add(new Category("Kíp chiều"));
        return list;
    }
}