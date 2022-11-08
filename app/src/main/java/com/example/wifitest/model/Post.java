package com.example.wifitest.model;

import java.util.Date;

public class Post extends PostId{
    private String user,caption,image;
    private Date time;

    public String getUser() {
        return user;
    }

    public String getCaption() {
        return caption;
    }

    public Date getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }
}
