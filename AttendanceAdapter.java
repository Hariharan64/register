package com.example.qradmin;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AttendanceAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> records;

    public AttendanceAdapter(Context context, List<String> records) {
        super(context, 0, records);
        this.context = context;
        this.records = records;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.attendance_item, parent, false);
        }

        // Get the data item for this position
        String record = getItem(position);

        // Find the TextViews and set the data
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView punchInTextView = convertView.findViewById(R.id.punchInTextView);
        TextView punchOutTextView = convertView.findViewById(R.id.punchOutTextView);
        TextView totalTimeTextView = convertView.findViewById(R.id.totalTimeTextView);

        // Parse the record into components
        String[] parts = record.split("\n");
        if (parts.length >= 4) {
            dateTextView.setText(parts[0]);
            punchInTextView.setText(parts[1]);
            punchOutTextView.setText(parts[2]);
            totalTimeTextView.setText(parts[3]);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
