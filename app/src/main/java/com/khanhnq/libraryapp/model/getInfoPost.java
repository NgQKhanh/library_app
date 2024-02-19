package com.khanhnq.libraryapp.model;

import java.util.Date;

public class getInfoPost {
    private String userID;
    public getInfoPost(String userID) {
        this.userID = userID;
    }
    public static class reservationPost{
        private String userID;
        private Date date;
        private Integer shift;
        public reservationPost(String userID, Date date, Integer shift) {
            this.userID = userID;
            this.date = date;
            this.shift = shift;
        }
    }
}
