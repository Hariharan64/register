package com.example.qradmin;




import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ListView attendanceListView;
    private DatabaseReference databaseReference;
    private List<String> attendanceRecords;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        attendanceListView = findViewById(R.id.attendanceListView);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");

        // Initialize attendance records list and adapter
        attendanceRecords = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendanceRecords);
        attendanceListView.setAdapter(adapter);

        // Fetch attendance data from Firebase
        fetchAttendanceData();
    }

    private void fetchAttendanceData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendanceRecords.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dateSnapshot : userSnapshot.getChildren()) {
                        String date = dateSnapshot.getKey();
                        String punchInTime = dateSnapshot.child("punchInTime").getValue(String.class);
                        String punchOutTime = dateSnapshot.child("punchOutTime").getValue(String.class);
                        String totalWorkingTime = dateSnapshot.child("totalWorkingTime").getValue(String.class);

                        String record = "Date: " + date +
                                "\nPunch In: " + (punchInTime != null ? punchInTime : "N/A") +
                                "\nPunch Out: " + (punchOutTime != null ? punchOutTime : "N/A") +
                                "\nTotal Time: " + (totalWorkingTime != null ? totalWorkingTime : "N/A");

                        attendanceRecords.add(record);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
