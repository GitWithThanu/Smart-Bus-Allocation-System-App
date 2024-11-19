package com.example.busadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView username, password;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn = findViewById(R.id.btn);

        username.setHint("Enter your username");

        password.setHint("Enter your password");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                // Check if the username and password are not empty
                if (!user.isEmpty() && !pass.isEmpty()) {
                    if(user.equals("admin") && pass.equals("1234")) {
                        Intent intent = new Intent(MainActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        checkRegisterNumber(user, pass);
                    }
                    // Perform login action (this is where you would normally handle authentication)
                    // You can add further actions here, like navigating to another activity
                } else {
                    // Show an error message if fields are empty
                    Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserData(String registerNumber, String name, String location, String busNumber) {
        // Create a new user map
        Map<String, Object> user = new HashMap<>();
        user.put("registerNumber", registerNumber);
        user.put("name", name);
        user.put("location", location);
        user.put("busNumber", busNumber);

        // Add a new document with a generated ID to the "Users" collection
        db.collection("Users")
                .add(user)
                .addOnSuccessListener(documentReference ->
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId())
                )
                .addOnFailureListener(e ->
                        Log.w("Firestore", "Error adding document", e)
                );
    }

    private void addBusData(String bus, String place, String[] location) {
        // Create a new user map
        Map<String, Object> user = new HashMap<>();
        user.put("bus", bus);
        user.put("destination", place);
        user.put("places", Arrays.asList(location));

        // Add a new document with a generated ID to the "Users" collection
        db.collection("Bus")
                .add(user)
                .addOnSuccessListener(documentReference ->
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId())
                )
                .addOnFailureListener(e ->
                        Log.w("Firestore", "Error adding document", e)
                );
    }

    private void checkRegisterNumber(String registerNumber, String password) {
        CollectionReference usersCollection = db.collection("Users");

        // Query to check if the register number exists
        usersCollection.whereEqualTo("registerNumber", registerNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean userExists = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userExists = true;
                            // Fetch student details from the database
                            String storedPassword = "1234";  // Assuming the password is stored here
                            String busNumber = document.getString("busNumber");  // Bus number
                            String location = document.getString("location");  // Location
                            String name = document.getString("name");  // Name
                            String registerNumberFromDB = document.getString("registerNumber");  // Register number

                            if (storedPassword != null && storedPassword.equals(password)) {
                                // Create an Intent to pass the student details to the next activity
                                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                                intent.putExtra("busNumber", busNumber);
                                intent.putExtra("location", location);
                                intent.putExtra("name", name);
                                intent.putExtra("registerNumber", registerNumberFromDB);

                                // Start StudentActivity
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (!userExists) {
                            Toast.makeText(MainActivity.this, "Register number not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error checking register number", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
