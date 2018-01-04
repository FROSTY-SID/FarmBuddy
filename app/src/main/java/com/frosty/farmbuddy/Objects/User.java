package com.frosty.farmbuddy.Objects;

public class User {

    public String firstName;
    public String lastName;
    public Location_fb location;
    public String phone;
    public String email;
    public String photoUrl;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstName,String lastName, String email,Location_fb location,String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.location = location;
    }

}