package com.example.busadmin;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class StudentActivity extends AppCompatActivity {

    private TextView timeTextView;
    private FirebaseFirestore db;
    private TextView timeTextView2;
    private Button openTimePickerButton;
    String time;
    int count = 0;
    private Button submit;
    String registerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String busNumber = intent.getStringExtra("busNumber");
        String location = intent.getStringExtra("location");
        String name = intent.getStringExtra("name");
        registerNumber = intent.getStringExtra("registerNumber");

        checkRegisterNumberExists(registerNumber);

        String studentDetails = "Student Name : " + name + "\n" +
                "Student Id : " + registerNumber + "\n" +
                "Bus Number : " + busNumber + "\n" +
                "Student Location : " + location;


        timeTextView = findViewById(R.id.timeTextView);
        timeTextView2 = findViewById(R.id.textview_content);
        timeTextView2.setText(studentDetails);
        openTimePickerButton = findViewById(R.id.openTimePickerButton);
        submit = findViewById(R.id.button_request);

        submit.setOnClickListener(v -> {
            if (time != null && count == 0) {
                addUserData(registerNumber, name, location, busNumber, time);
                count++;
            } else {
                count--;
                removeStudentFromDatabase(registerNumber);
                submit.setText("Submit your request");
            }
        });
        openTimePickerButton.setOnClickListener(v -> {
            // Get the current time
            int hour = 12;
            int minute = 0;

            // Create the TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(StudentActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // Display the selected time
                            timeTextView.setText("Selected Time: " + hourOfDay + ":" + minute);
                            time = hourOfDay + " : " + minute;
                        }
                    }, hour, minute, true);


            // Show the dialog
            timePickerDialog.show();
        });

        timeTextView.setOnClickListener(v -> {
            // Get the current time
            int hour = 12;
            int minute = 0;

            // Create the TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(StudentActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // Display the selected time
                            timeTextView.setText("Selected Time: " + hourOfDay + ":" + minute);
                            time = hourOfDay + " : " + minute;
                        }
                    }, hour, minute, true);


            // Show the dialog
            timePickerDialog.show();
        });
    }


    private void addUserData(String registerNumber, String name, String location, String busNumber, String t) {
        // Create a new user map
        Map<String, Object> user = new HashMap<>();
        user.put("registerNumber", registerNumber);
        user.put("name", name);
        user.put("location", location);
        user.put("busNumber", busNumber);
        user.put("time", t);

        // Add a new document with a generated ID to the "Users" collection
        db.collection("Request")
                .add(user)
                .addOnSuccessListener(documentReference ->
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId())
                )
                .addOnFailureListener(e ->
                        Log.w("Firestore", "Error adding document", e)
                );
        submit.setText("Remove Your Request");
    }

    private void checkRegisterNumberExists(String registerNumber) {
        db.collection("Request")
                .whereEqualTo("registerNumber", registerNumber)  // Query for documents with the given register number
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // If documents are found with the given register number
                        Log.d("Firestore", "Register number exists!");
                        // You can also retrieve the document data here if needed
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            // Get data from document
                            String name = document.getString("name");
                            String location = document.getString("location");
                            String busNumber = document.getString("busNumber");
                            time = document.getString("time");
                            timeTextView.setText("Selected Time: " + time);
                            count = 1;
                            submit.setText("Remove Your Request");
                            Log.d("Firestore", "User data: " + name + ", " + location + ", " + busNumber + ", " + time);
                        }
                    } else {
                        Log.d("Firestore", "Register number not found.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error checking register number", e);
                });
    }



    private void removeStudentFromDatabase(String registerNumber) {
        db.collection("Request")
                .whereEqualTo("registerNumber", registerNumber) // Query for documents with this register number
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            // Delete each document that matches the register number
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Student removed successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error deleting student: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                    );
                        }
                        finish(); // Close the activity after deletion
                    } else {
                        Toast.makeText(this, "No student found with this register number", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error retrieving student: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}


