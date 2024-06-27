package com.khanhnq.libraryapp.model;

import android.app.Application;

public class Common extends Application {
    private Boolean isLogin;
    private String userID;
    private String username;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Boolean getLogin() { return isLogin; }

    public void setLogin(Boolean login) { isLogin = login;}
}
