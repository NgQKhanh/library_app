package com.khanhnq.libraryapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class infoResponse {

    // Lấy list sách đang mượn
    public class BBList {
        private List<BorrowedBook> borrowedBookList;
        public List<BorrowedBook> getBorrowedBookList() { return borrowedBookList; }
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
    public static class RRInfo {
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

    //Lấy thông tin đặt chỗ của phòng
    public class RsvnInfo{

        private UserReservation userReservation;
        private RsvnInPeriod rsvnInPeriod;
        public UserReservation getUserReservation() {return userReservation; }
        public RsvnInPeriod getRsvnInPeriod() {return rsvnInPeriod; }
        public class UserReservation{
            private List<Reservation> uSeats;
            public List<Reservation> getRsvn() { return uSeats;}
            public class Reservation {
                private String date;
                private String shift;
                private int seat;
                private String room;
                public String getDate() { return date; }
                public String getShift() { return shift; }
                public int getSeat() { return seat; }
                public String getRoom() { return room; }
            }
        }
        public class RsvnInPeriod{
            private List<String> dateArray;
            private List<Integer> shift1Array;
            private List<Integer> shift2Array;
            public List<String> getDateArray() {return dateArray;}
            public List<Integer> getShift1Array() {return shift1Array;}
            public List<Integer> getShift2Array() {return shift2Array;}

        }
    }

    // Xác nhận đặt chỗ
    public class confirmRsvn{
        private String status;
        public String getStatus() {
            return status;
        }
    }

    // Tìm kiếm đầu sách
    public class searchTitleList{
        private List<searchTitle> list;

        public List<searchTitle> getList() {return list;}

        public class searchTitle{
            private String id;
            private String bookName;
            private String author;
            private String publisher;
            private String category;

            public String getId() { return id;}
            public String getBookName() { return bookName;}
            public String getAuthor() { return author; }
            public String getPublisher() { return publisher; }
            public String getCategory() { return category;}
        }
    }

    // Tìm kiếm bản copy
    public class searchCopyList{
        private List<searchCopy> list;
        public List<searchCopy> getList() {return list;}

        public class searchCopy{
            private String bookID;
            private String status;
            private String id;
            private String location;

            public String getBookId() { return bookID;}
            public String getStatus() { return status; }
            public String getId() { return id;}
            public String getLocation() { return location; }
        }
    }

    // Đặt chỗ ngồi
    public class bookingSeat{
        private List<Integer> seats;
        public List<Integer> getList() {return seats;}
    }

    // Đặt thông báo sách
    public class rsvnBook{
        private String message;
        public String getStatus() {
            return message;
        }
    }
}


