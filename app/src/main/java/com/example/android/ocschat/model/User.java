package com.example.android.ocschat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User class used to hold more info about user for scalability
 * will be Connected with fire-base User class through id property
 */
public class User implements Serializable {

    private String id;
    private String name;   //TODO: remove this property
    private String firstName;
    private String lastName;
    private Integer age;
    private List<Friend> friends;
    private boolean hasImage;
    private String education;   //Student, Diploma, Bachelor, master, or PHD
    private String educationOrganization;  //University, institute, or school
    private String major;
    private String work;
    private String company;

    public User() {
        //No-arg constructor Required for firebase database operations
        this.friends = new ArrayList<>();
    }

    public User(String id, String name) {   //TODO: Remove this constructor
        this.id = id;
        this.name = name;
        this.friends = new ArrayList<>();
        this.hasImage = false;
    }

    public User(String id, String name, boolean hasImage) {   //TODO: Remove this constructor
        this.id = id;
        this.name = name;
        this.friends = new ArrayList<>();
        this.hasImage = hasImage;
    }

    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<>();
        this.hasImage = false;
    }

    public User(String id, String firstName, String lastName, boolean hasImage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friends = new ArrayList<>();
        this.hasImage = hasImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }  //TODO: remove this method

    public void setName(String name) {
        this.name = name;
    }  //TODO: remove this method

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducationOrganization() {
        return educationOrganization;
    }

    public void setEducationOrganization(String educationOrganization) {
        this.educationOrganization = educationOrganization;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
