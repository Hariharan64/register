package com.example.qradmin;

// Admin.java (Model Class)
public class Admin {
    public String phone;

    public Admin() {
        // Default constructor required for calls to DataSnapshot.getValue(Admin.class)
    }

    public Admin(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}