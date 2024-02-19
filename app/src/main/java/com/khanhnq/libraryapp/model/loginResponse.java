package com.khanhnq.libraryapp.model;

public class loginResponse {
    private boolean status;
    private String userID;
    private String username;
    private String test;

    public loginResponse(boolean status, String userID, String username) {
        this.status = status;
        this.userID = userID;
        this.username = username;
    }
    public boolean isStatus() {
        return status;
    }
    public String getUserID() {
        return userID;
    }
    public String getUsername() {return username;}
}
