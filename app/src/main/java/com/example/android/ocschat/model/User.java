package com.example.android.ocschat.model;

/**
 * User class used to hold more info about user for scalability
 * will be Connected with firebase User class through id property
 */
public class User {

    private String id;
    private String name;

    public User(String id, String name) {
        this.setId(id);
        this.setName(name);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
