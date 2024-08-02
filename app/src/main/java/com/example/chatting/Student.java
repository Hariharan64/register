package com.example.chatting;



public class Student {
    public String name;
    public String phone;
    public String course;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String name, String phone, String course) {
        this.name = name;
        this.phone = phone;
        this.course = course;
    }
}
