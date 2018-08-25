package com.example.android.ocschat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User class used to hold more info about user for scalability
 * will be Connected with firebase User class through id property
 */
public class User implements Serializable {

    private String id;
    private String name;
    private List<Friend> friends;
    private HasImage hasImage;

    public User() {
        //Required for firebase database operations
        this.friends = new ArrayList<>();
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.friends = new ArrayList<>();
        this.setHasImage(HasImage.NO_IMAGE);
    }

    public User(String id, String name, HasImage hasImage) {
        this.id = id;
        this.name = name;
        this.friends = new ArrayList<>();
        this.setHasImage(hasImage);
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

    public List<Friend> getFriends(){
        return friends;
    }

    public void addFriend(Friend friend){
        friends.add(friend);
    }

    public boolean deleteFriend(Friend friend){
        if(friends.contains(friend)){
            friends.remove(friend);
            return true;
        }
        return false;
    }

    public HasImage getHasImage() {
        return hasImage;
    }

    public void setHasImage(HasImage hasImage) {
        this.hasImage = hasImage;
    }
}
