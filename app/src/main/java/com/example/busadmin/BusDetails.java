package com.example.busadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;

public class BusDetails extends Fragment {

    private LinearLayout container;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Get reference to the container LinearLayout
        this.container = view.findViewById(R.id.container);

        // Fetch data from Firebase and add views dynamically
        fetchDataFromFirebase();

        return view;
    }

    private void fetchDataFromFirebase() {
        db.collection("Bus")  // Assuming "Bus" is your Firestore collection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear any existing views
                        container.removeAllViews();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String busNumber = document.getString("bus");
                            String busName = document.getString("destination");
                            List<String> places = (List<String>) document.get("places");

                            // Create a view for each bus and add it to the container
                            View busView = createBusView(busNumber, busName, places);
                            container.addView(busView);
                        }
                    } else {
                        // Handle the error
                        Toast.makeText(getContext(), "Error fetching bus data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private View createBusView(String busNumber, String busName, List<String> places) {
        // Inflate the bus item row layout
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bus_item_row, container, false);

        // Set bus data into the views
        TextView busNumberTextView = view.findViewById(R.id.bus_number);
        TextView busNameTextView = view.findViewById(R.id.bus_name1);
        LinearLayout placesContainer = view.findViewById(R.id.places_container1);

        busNumberTextView.setText("Bus Number : "+busNumber);
        busNameTextView.setText("Destination : " + busName);

        // Add places to the places container
        for (String place : places) {
            TextView placeTextView = new TextView(getContext());
            placeTextView.setText(place);
            placeTextView.setTextSize(14);
            placesContainer.addView(placeTextView);
        }

        // Set up click listener to toggle the visibility of places
        view.setOnClickListener(v -> {
            // Toggle visibility of places
            if (placesContainer.getVisibility() == View.GONE) {
                placesContainer.setVisibility(View.VISIBLE);
            } else {
                placesContainer.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
