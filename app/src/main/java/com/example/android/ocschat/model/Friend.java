package com.example.android.ocschat.model;

import java.io.Serializable;

public class Friend implements Serializable {
    private String id;
    private FriendState state;

    public Friend(){
        //Required for firebase database operations
    }

    public Friend(String id) {
        this.id = id;
        this.state = FriendState.NORMAL;
    }

    public Friend(String id, FriendState state) {
        this.id = id;
        this.state = state;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FriendState getState() {
        return state;
    }

    public void setState(FriendState state) {
        this.state = state;
    }
}
