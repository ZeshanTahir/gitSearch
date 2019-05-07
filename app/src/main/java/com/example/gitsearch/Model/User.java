package com.example.gitsearch.Model;

import java.util.Objects;

public class User {
    private String mUserName, mEmail, mAvatarUrl;

    public User() {
    }

    public User(String mUserName, String mEmail, String mAvatarUrl) {
        this.mUserName = mUserName;
        this.mEmail = mEmail;
        this.mAvatarUrl = mAvatarUrl;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmAvatarUrl() {
        return mAvatarUrl;
    }

    public void setmAvatarUrl(String mAvatarUrl) {
        this.mAvatarUrl = mAvatarUrl;
    }
}
