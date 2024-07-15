package com.khanhnq.libraryapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khanhnq.libraryapp.api.ApiService;
import com.khanhnq.libraryapp.model.Common;
import com.khanhnq.libraryapp.model.infoResponse;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookCopyActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView title, bookNameId, authorId, categoryId, publisherId, notification;
    TableRow tableId;
    Button btnNotifyReceive;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_copy);

        // Ánh xạ id
        btnBack = findViewById(R.id.back_icon);
        title = findViewById(R.id.toolbarTitle);
        bookNameId = findViewById(R.id.bookName);
        authorId = findViewById(R.id.author);
        categoryId = findViewById(R.id.category);
        publisherId = findViewById(R.id.publisher);
        tableId = findViewById(R.id.tableId);
        notification = findViewById(R.id.notification);
        btnNotifyReceive = findViewById(R.id.notifyReceive);

        title.setText("Chi tiết");

        //Lấy dữ liệu
        Common user = (Common) getApplication();
        String userID = user.getUserID();

        Intent intent = getIntent();
        String bookName = intent.getStringExtra("bookName");
        String author = intent.getStringExtra("author");
        String id = intent.getStringExtra("id");
        String publisher = intent.getStringExtra("publisher");
        String category = intent.getStringExtra("category");

        // Hiển thị thông tin
        bookNameId.setText(bookName);
        authorId.setText(author);
        publisherId.setText(publisher);
        categoryId.setText(category);

        searchBookCopy("copy",id);

        //Nhận thông báo khi có sách
        builder = new AlertDialog.Builder(this);
        btnNotifyReceive.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                builder.setTitle(R.string.announce)
                        .setMessage("Nhận thông báo khi có sách: " + bookName)
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reserveBook(userID,id);
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

    // HTTP Get: Tìm kiếm bản sách
    public void searchBookCopy (String type,String id) {
        ApiService.apiservice.searchBookCopy(type, id).enqueue(new Callback<infoResponse.searchCopyList>() {
            @Override
            public void onResponse(Call<infoResponse.searchCopyList> call, Response<infoResponse.searchCopyList> response) {
                if (response.isSuccessful()) {
                    infoResponse.searchCopyList list = response.body();
                    // Không có data
                    if (list == null || list.getList().isEmpty()) {
                        tableId.setVisibility(View.GONE);
                        notification.setVisibility(View.VISIBLE);
                    }
                    // Hiển thị data
                    else {
                        tableId.setVisibility(View.VISIBLE);
                        notification.setVisibility(View.GONE);

                        List<infoResponse.searchCopyList.searchCopy> searchList = list.getList();
                        for (infoResponse.searchCopyList.searchCopy book : searchList) {
                            String bookID = book.getBookId();
                            String status = book.getStatus();
                            String location = book.getLocation();

                            if(Objects.equals(status, "0")){
                                status = "Có sẵn";
                            } else if (Objects.equals(status, "1")){
                                status = "Đang mượn";
                            }

                            addRowBBTable(bookID, status, location);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<infoResponse.searchCopyList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // Đặt thông báo khi có sách
    public void reserveBook (String userID, String bookID)
    {
        ApiService.apiservice.reserveBook(userID, bookID).enqueue(new Callback<infoResponse.rsvnBook>() {
            @Override
            public void onResponse(Call<infoResponse.rsvnBook> call, Response<infoResponse.rsvnBook> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    if(status.equals("success")) {
                        Toast.makeText(BookCopyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("available")) {
                        Toast.makeText(BookCopyActivity.this, "Sách đang có sẵn ở thư viện", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookCopyActivity.this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<infoResponse.rsvnBook> call, Throwable t) {          }
        });
    }

    // Hàm chèn hàng vào bảng thông tin sách
    private void addRowBBTable(String bookId, String status, String location){
        // Lấy đối tượng TableLayout từ layout
        TableLayout tableLayout = findViewById(R.id.bookCopyTable);
        float[] columnWeights = {1.5f, 2f, 2f}; // Độ rộng cóc cột

        // Tạo một TableRow mới
        TableRow newRow = new TableRow(this);

        // Thêm TextView cho cột bookId
        TextView bookIdTV = new TextView(this);
        bookIdTV.setText(bookId);
        bookIdTV.setTextSize(10);
        bookIdTV.setGravity(Gravity.CENTER);
        bookIdTV.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[0]));
        newRow.addView(bookIdTV);

        // Thêm TextView cho cột status
        TextView statusTV = new TextView(this);
        statusTV.setText(status);
        statusTV.setTextSize(10);
        statusTV.setGravity(Gravity.CENTER);
        statusTV.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[1]));
        newRow.addView(statusTV);

        // Thêm TextView cho cột location
        TextView locationTV = new TextView(this);
        locationTV.setText(location);
        locationTV.setTextSize(10);
        locationTV.setGravity(Gravity.CENTER);
        locationTV.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, columnWeights[2]));
        newRow.addView(locationTV);

        // Thêm TableRow mới vào TableLayout
        tableLayout.addView(newRow);
    }
}