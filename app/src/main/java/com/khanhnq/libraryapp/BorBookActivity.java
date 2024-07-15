package com.khanhnq.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.khanhnq.libraryapp.api.ApiService;
import com.khanhnq.libraryapp.model.Common;
import com.khanhnq.libraryapp.model.getInfoPost;
import com.khanhnq.libraryapp.model.infoResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorBookActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bor_book);

        // Ánh xạ id
        btnBack = findViewById(R.id.back_icon);
        title = findViewById(R.id.toolbarTitle);
        title.setText("Mượn sách");

        // Lấy dữ liệu
        Common user = (Common) getApplication();
        String userID = user.getUserID();

        //lấy dữ liệu sách đang mượn
        getBorrowedBookInfo(userID);

        // nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // HTTP POST: lấy thông tin sách mượn của ngươi dùng
    private void getBorrowedBookInfo(String userID){
        ApiService.apiservice.getBorBookInfo(userID).enqueue(new Callback<infoResponse.BBList>() {
            @Override
            public void onResponse(Call<infoResponse.BBList> call, Response<infoResponse.BBList> response) {
                infoResponse.BBList list = response.body();
                int count = 1;
                List<infoResponse.BBList.BorrowedBook> borBookList = list.getBorrowedBookList();
                for (infoResponse.BBList.BorrowedBook book : borBookList) {
                    String bookName = book.getBookName();
                    String borrowDate = book.getBorrowDate();
                    String dueDate = book.getDueDate();

                    addRowBBTable(count, bookName, borrowDate, dueDate);
                    count++;
                }
            }
            @Override
            public void onFailure(Call<infoResponse.BBList> call, Throwable t) {
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

    // Hàm chèn hàng vào bảng sách mượn
    private void addRowBBTable(int stt, String bookname, String borrowedDate, String dueDate){
        // Lấy đối tượng TableLayout từ layout
        TableLayout tableLayout = findViewById(R.id.borBookTable);
        float[] columnWeights = { 0.8f, 3f, 2.5f, 2.5f }; // Độ rộng cóc cột

        // Tạo một TableRow mới
        TableRow newRow = new TableRow(this);

            // Thêm TextView cho cột STT
            TextView sttTV = new TextView(this);
            sttTV.setText(Integer.toString(stt));
            sttTV.setTextSize(10);
            sttTV.setGravity(Gravity.CENTER);
            sttTV.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[0]));
            newRow.addView(sttTV);

            // Thêm TextView cho cột Tên sách
            TextView booknameTV = new TextView(this);
            booknameTV.setText(bookname);
            booknameTV.setTextSize(10);
            booknameTV.setPadding(5,5,5,5);
            booknameTV.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[1]));
            newRow.addView(booknameTV);

            // Thêm TextView cho cột Ngày mượn
            TextView borDateTV = new TextView(this);
            borDateTV.setText(borrowedDate);
            borDateTV.setTextSize(10);
            borDateTV.setGravity(Gravity.CENTER);
            borDateTV.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[2]));
            newRow.addView(borDateTV);

            // Thêm TextView cho cột Ngày hết hạn
            TextView dueDateTV = new TextView(this);
            dueDateTV.setText(dueDate);
            dueDateTV.setTextSize(10);
            dueDateTV.setGravity(Gravity.CENTER);
            dueDateTV.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[3]));
            newRow.addView(dueDateTV);

        // Thêm TableRow mới vào TableLayout
        tableLayout.addView(newRow);
    }
}