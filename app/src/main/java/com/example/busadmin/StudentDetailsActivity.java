package com.example.busadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentDetailsActivity extends AppCompatActivity {

    private TextView nameTextView, locationTextView, busNumberTextView;
    private Button removeButton;
    private String studentId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        db = FirebaseFirestore.getInstance();

        nameTextView = findViewById(R.id.student_name);
        locationTextView = findViewById(R.id.student_location);
        busNumberTextView = findViewById(R.id.bus_number);
        removeButton = findViewById(R.id.remove_button);

        studentId = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String location = getIntent().getStringExtra("location");
        String busNumber = getIntent().getStringExtra("busNumber");

        nameTextView.setText(name);
        locationTextView.setText(location);
        busNumberTextView.setText(busNumber);

        removeButton.setOnClickListener(v -> removeStudentFromDatabase());
    }

    private void removeStudentFromDatabase() {
        db.collection("Users").document(studentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Student removed successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after deletion
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting student: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
