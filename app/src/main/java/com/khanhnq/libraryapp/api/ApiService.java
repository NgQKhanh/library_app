package com.khanhnq.libraryapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.khanhnq.libraryapp.model.getInfoPost;
import com.khanhnq.libraryapp.model.infoResponse;
import com.khanhnq.libraryapp.model.loginPost;
import com.khanhnq.libraryapp.model.loginResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    Gson gson = new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    ApiService apiservice = new Retrofit.Builder()
            .baseUrl("http://192.168.1.4:3000/app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("login")
    Call<loginResponse> loginAuth(@Body loginPost loginData);
    @POST("getBBList")
    Call<infoResponse.BBList> getBorBookInfo (@Body getInfoPost getInfopost);
    @GET("getRRInfo")
    Call<infoResponse.RRInfo> getReadingRoomInfo ();

    // API đặt chỗ ngồi
    @GET("getRsvnCountInPeriod")
    Call<infoResponse.RsvnInfo.RsvnInPeriod> getRsvnInPeriod (
            @Query("room") int room
    );
    @GET("getUserSeatRsvn")
    Call<infoResponse.RsvnInfo.UserReservation> getUserRsvn(
            @Query("userID") String userID
    );
    @GET("getSeatRsvnByRoom")
    Call<infoResponse.bookingSeat> getRsvnSeat(
            @Query("date") String date,
            @Query("shift") int shift,
            @Query("room") int room
    );
    @POST("cfrRsvn")
    Call<infoResponse.confirmRsvn> confirmRsvn (@Body getInfoPost.reservationPost reservationPost);
    @POST("delrsvn")
    Call<Void> delRsvn (@Body getInfoPost getInfopost);

    //API tra cứu
    @GET("search")
    Call<infoResponse.searchTitleList> searchBook(
            @Query("type") String type,
            @Query("key") String key,
            @Query("field") String field
    );
    @GET("search")
    Call<infoResponse.searchCopyList> searchBookCopy(
            @Query("type") String type,
            @Query("id") String id
    );

    //API đặt sách
    @GET("reserveBook")
    Call<infoResponse.rsvnBook> reserveBook(
            @Query("userID") String userID,
            @Query("bookID") String bookID
    );
}


