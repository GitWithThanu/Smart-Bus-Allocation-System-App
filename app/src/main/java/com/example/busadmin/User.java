package com.example.busadmin;

public class User {
    private String name;
    private String location;
    private String busNumber;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String location, String busNumber) {
        this.name = name;
        this.location = location;
        this.busNumber = busNumber;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getBusNumber() { return busNumber; }
}
