package com.khanhnq.libraryapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class infoResponse {

    // Lấy list sách đang mượn
    public class BBList {
        private List<BorrowedBook> borrowedBookList;
        public List<BorrowedBook> getBorrowedBookList() {
            return borrowedBookList;
        }
        public class BorrowedBook {
            private String bookName;
            private String borrowDate;
            private String dueDate;
            public String getBookName() {
                return bookName;
            }
            public String getBorrowDate() {
                return borrowDate;
            }
            public String getDueDate() {
                return dueDate;
            }
        }
    }

    //Lấy thông tin phòng đọc
    public class RRInfo {
        private readingRoom readingRoom;
        public readingRoom getReadingRoom() {
            return readingRoom;
        }
        public class readingRoom {
            private int userNumber;
            public int getUserNumber() {
                return userNumber;
            }
        }
    }

    //Lấy thông tin đặt chỗ
    public class RsvnInfo{
        private List<String> dateArray;
        private List<Integer> shift1Array;
        private List<Integer> shift2Array;
        private userReservation userReservation;
        public List<String> getDateArray() {return dateArray;}
        public List<Integer> getShift1Array() {return shift1Array;}
        public List<Integer> getShift2Array() {return shift2Array;}
        public userReservation getUserReservation() {return userReservation; }
        public class userReservation{
            private List<Reservation> rsvn;
            public List<Reservation> getRsvn() { return rsvn;}
            public class Reservation {
                private String date;
                private String shift;
                public String getDate() { return date; }
                public String getShift() { return shift; }
            }
        }
    }

    // Confirm Reservation
    public class confirmRsvn{
        private String status;
        public String getStatus() {
            return status;
        }
    }
}


