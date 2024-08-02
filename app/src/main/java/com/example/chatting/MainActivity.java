package com.example.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextCourse;
    private Button buttonSubmit, buttonView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextStudentName);
        editTextPhone = findViewById(R.id.editTextStudentPhone);
        editTextCourse = findViewById(R.id.editTextStudentCourse);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonView = findViewById(R.id.buttonView);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("students");

        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String course = editTextCourse.getText().toString().trim();

                if (!name.isEmpty() && !phone.isEmpty() && !course.isEmpty()) {
                    String id = databaseReference.push().getKey();
                    Student student = new Student(name, phone, course);
                    if (id != null) {
                        databaseReference.child(id).setValue(student).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                buttonView.setVisibility(View.VISIBLE); // Make the View Details button visible
                            } else {
                                Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    editTextName.setText("");
                    editTextPhone.setText("");
                    editTextCourse.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                startActivity(intent);
            }
        });
    }
}