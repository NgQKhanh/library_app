package com.khanhnq.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.khanhnq.libraryapp.component.CategoryAdapter;
import com.khanhnq.libraryapp.component.Category;
import com.khanhnq.libraryapp.component.BookTitleList;
import com.khanhnq.libraryapp.component.BookListAdapter;
import com.khanhnq.libraryapp.databinding.ActivityMainBinding;
import com.khanhnq.libraryapp.model.infoResponse;
import com.khanhnq.libraryapp.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView title;
    CategoryAdapter categoryAdapter;
    Spinner showField;
    ListView listview;
    Integer selectedField;
    String[] searchField = {"bookName","author","category","publisher"};
    SearchView searchView;
    BookListAdapter bookListAdapter;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_search);

        // Ánh xạ id
        btnBack = findViewById(R.id.back_icon);
        title = findViewById(R.id.toolbarTitle);
        showField = findViewById(R.id.showPickedField);
        searchView = findViewById(R.id.searchView);
        listview = findViewById(R.id.listView);

        title.setText("Tra cứu");
        listview.setClickable(true);

        // nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Bấm chọn search field
        categoryAdapter = new CategoryAdapter(this, R.layout.search_field, R.layout.category_shift, getListCategory());
        showField.setAdapter(categoryAdapter);
        showField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id)
            {
                selectedField = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //Bấm tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String key) {
                Log.d("Test Press","Press search button haha");
                searchBook("title",key,searchField[selectedField]);
                searchBookCopy("Copy","1");
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    // HTTP Get: Tìm kiếm theo đầu sách
    public void searchBook (String type, String key, String field)
    {
        ArrayList<BookTitleList> bookList = new ArrayList<>();
        ApiService.apiservice.searchBook(type, key, field).enqueue(new Callback<infoResponse.searchTitleList>() {
            @Override
            public void onResponse(Call<infoResponse.searchTitleList> call, Response<infoResponse.searchTitleList> response) {
                if (response.isSuccessful()) {
                    infoResponse.searchTitleList list = response.body();
                    if (list == null) {
                        Toast.makeText(SearchActivity.this, "Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                    else if (list.getList().isEmpty()) {
                        Toast.makeText(SearchActivity.this, "Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        List<infoResponse.searchTitleList.searchTitle> searchList = list.getList();
                        for (infoResponse.searchTitleList.searchTitle book : searchList) {
                            String id = book.getId();
                            String bookName = book.getBookName();
                            String author = book.getAuthor();
                            String publisher = book.getPublisher();
                            String category = book.getCategory();
                            Log.d("Debug search","Book Name: " + bookName);
                            bookList.add(new BookTitleList(id,bookName,author,category,publisher));
                        }
                        bookListAdapter = new BookListAdapter(SearchActivity.this,bookList);
                        listview.setAdapter(bookListAdapter);
                        listview.setClickable(true);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // Lấy đối tượng BookTitleList từ danh sách bookList tại vị trí click
                                BookTitleList selectedBook = bookList.get(position);

                                // Tạo Intent để chuyển Activity
                                Intent intent = new Intent(SearchActivity.this, BookCopyActivity.class);

                                // Đặt dữ liệu vào Intent
                                intent.putExtra("id", selectedBook.getId());
                                intent.putExtra("bookName", selectedBook.getBookName());
                                intent.putExtra("author", selectedBook.getAuthor());
                                intent.putExtra("publisher", selectedBook.getPublisher());
                                intent.putExtra("category", selectedBook.getCategory());
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<infoResponse.searchTitleList> call, Throwable t) {
                t.printStackTrace();
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
                    if (list == null) {
                        Toast.makeText(SearchActivity.this, "Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        List<infoResponse.searchCopyList.searchCopy> searchList = list.getList();
                        for (infoResponse.searchCopyList.searchCopy book : searchList) {
                            String bookID = book.getBookId();
                            String status = book.getStatus();
                            String id = book.getId();
                            String location = book.getLocation();
                            Log.d("Debug search","Book ID: " + bookID);
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

    // Category
    private List<Category> getListCategory(){
        List<Category> list = new ArrayList<>();
        list.add(new Category("Tên sách"));
        list.add(new Category("Tác giả"));
        list.add(new Category("Thể loại"));
        list.add(new Category("Nhà xuất bản"));
        return list;
    }
}