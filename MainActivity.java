package com.example.qradmin;




import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPhoneNumber, editTextName, editTextDesignation, editTextEmployeeCode;
    private Button buttonAddUser;
    private ProgressBar progressBar;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextName = findViewById(R.id.editTextName);
        editTextDesignation = findViewById(R.id.editTextDesignation);
        editTextEmployeeCode = findViewById(R.id.editTextEmployeeCode);
        buttonAddUser = findViewById(R.id.buttonAddUser);
        progressBar = findViewById(R.id.progressBar);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        buttonAddUser.setOnClickListener(v -> {
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            String designation = editTextDesignation.getText().toString().trim();
            String employeeCode = editTextEmployeeCode.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 10) {
                editTextPhoneNumber.setError("Please enter a valid phone number");
                editTextPhoneNumber.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(name)) {
                editTextName.setError("Please enter a name");
                editTextName.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(designation)) {
                editTextDesignation.setError("Please enter a designation");
                editTextDesignation.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(employeeCode)) {
                editTextEmployeeCode.setError("Please enter an employee code");
                editTextEmployeeCode.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            addUserToDatabase(name, designation, employeeCode, phoneNumber);
        });
    }

    private void addUserToDatabase(String name, String designation, String employeeCode, String phoneNumber) {
        User user = new User(name, designation, employeeCode, phoneNumber);

        databaseReference.child(phoneNumber).setValue(user)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearFields() {
        editTextPhoneNumber.setText("");
        editTextName.setText("");
        editTextDesignation.setText("");
        editTextEmployeeCode.setText("");
    }
}