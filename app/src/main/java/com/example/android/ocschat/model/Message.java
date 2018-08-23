package com.example.android.ocschat.model;

import java.util.Date;

public class Message {

    private String text;
    private String fromUserId;
    private String toUserId;
    private long date;

    public Message() {

    }

    public Message(String text, String fromUserId, String toUserId) {
        this.text = text;
        this.setFromUserId(fromUserId);
        this.setToUserId(toUserId);
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

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}