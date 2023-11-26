package com.example.aep;
public class Profile {
    private String phoneNumber;
    private String place;
    private String bloodGroup;
    private String playerType;
    private int age;

    // Default constructor (required for Firestore)
    public Profile() {
    }

    // Constructor for creating a profile with specific values
    public Profile(String phoneNumber, String place, String bloodGroup, String playerType, int age) {
        this.phoneNumber = phoneNumber;
        this.place = place;
        this.bloodGroup = bloodGroup;
        this.playerType = playerType;
        this.age = age;
    }

    // Getters and setters (required for Firestore)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

