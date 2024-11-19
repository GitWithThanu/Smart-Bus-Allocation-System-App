package com.example.busadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;

public class Request extends Fragment {

    private LinearLayout container;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Get reference to the container LinearLayout
        this.container = view.findViewById(R.id.container);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fetch data from Firebase and update the views every time the fragment resumes
        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        db.collection("Request")  // Assuming "users" is your Firestore collection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear any existing views
                        container.removeAllViews();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String location = document.getString("location");
                            String busNumber = document.getString("busNumber");
                            String request = document.getString("time");

                            // Create a view for each student and add it to the container
                            View studentView = createStudentView(name, location, busNumber, request);
                            container.addView(studentView);
                        }
                    } else {
                        // Handle the error
                        System.out.println("Error getting documents: " + task.getException());
                    }
                });
    }

    private View createStudentView(String name, String location, String busNumber, String time) {
        // Inflate the student detail row layout
        View view = LayoutInflater.from(getContext()).inflate(R.layout.student_request_row
                , container, false);

        // Set data into the views
        TextView nameTextView = view.findViewById(R.id.student_name_in_admin);
        TextView locationTextView = view.findViewById(R.id.student_location_in_admin);
        TextView busNumberTextView = view.findViewById(R.id.bus_number_in_admin);
        TextView timeTextview = view.findViewById(R.id.student_request_time);

        nameTextView.setText(name);
        locationTextView.setText(location);
        busNumberTextView.setText(busNumber);
        timeTextview.setText(time);

        return view;
    }
}
