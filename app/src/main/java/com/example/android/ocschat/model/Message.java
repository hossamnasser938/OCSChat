package com.example.android.ocschat.model;

import java.util.Date;

public class Message {

    private String text;
    private String fromUser;
    private String toUser;
    private long date;

    public Message() {

    }

    public Message(String text, String fromUserId, String toUserId) {
        this.text = text;
        this.setFromUser(fromUserId);
        this.setToUser(toUserId);
        this.date = new Date().getTime();;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
}