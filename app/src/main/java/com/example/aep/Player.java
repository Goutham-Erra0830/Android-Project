package com.example.aep;
public class Player {
    private String email;
    private String full_name;
    private String password; // Note: Storing passwords directly is not recommended in production apps
    private String user_type;

    // Empty constructor required for Firestore
    public Player() {
        // Default constructor required for Firestore
    }

    // Constructor with relevant fields
    public Player(String email, String full_name, String password, String user_type) {
        this.email = email;
        this.full_name = full_name;
        this.password = password;
        this.user_type = user_type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    // Getter methods
    public String getEmail() {
        return email;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_type() {
        return user_type;
    }

    // Add other getter methods as needed
}
