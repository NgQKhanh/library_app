package com.khanhnq.libraryapp.model;

import java.util.Date;

public class getInfoPost {
    private String userID;
    public getInfoPost(String userID) {
        this.userID = userID;
    }
    public static class reservationPost{
        private String userID;
        private String date;
        private int shift;
        private int seat;
        private int room;
        public reservationPost(String userID, String date, int shift, int seat, int room) {
            this.userID = userID;
            this.date = date;
            this.shift = shift;
            this.seat = seat;
            this.room = room;
        }
    }
}
