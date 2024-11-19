package com.example.busadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class StudentDetail extends Fragment {

    private LinearLayout container;
    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_detail, container, false);
        db = FirebaseFirestore.getInstance();
        this.container = view.findViewById(R.id.container);

        attachRealtimeListener(); // Attach listener to keep UI in sync with Firestore

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detachRealtimeListener(); // Clean up listener when fragment is destroyed
    }

    private void attachRealtimeListener() {
        listenerRegistration = db.collection("Users")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        System.out.println("Listen failed: " + e);
                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        String id = dc.getDocument().getId();
                        String name = dc.getDocument().getString("name");
                        String location = dc.getDocument().getString("location");
                        String busNumber = dc.getDocument().getString("busNumber");

                        switch (dc.getType()) {
                            case ADDED:
                                addStudentView(id, name, location, busNumber);
                                break;
                            case MODIFIED:
                                updateStudentView(id, name, location, busNumber);
                                break;
                            case REMOVED:
                                removeStudentView(id);
                                break;
                        }
                    }
                });
    }

    private void detachRealtimeListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }

    private void addStudentView(String id, String name, String location, String busNumber) {
        View studentView = createStudentView(id, name, location, busNumber);
        studentView.setTag(id); // Tagging view with student ID for easy retrieval
        container.addView(studentView);
    }

    private void updateStudentView(String id, String name, String location, String busNumber) {
        View viewToUpdate = container.findViewWithTag(id);
        if (viewToUpdate != null) {
            TextView nameTextView = viewToUpdate.findViewById(R.id.student_name_in_admin);
            TextView locationTextView = viewToUpdate.findViewById(R.id.student_location_in_admin);
            TextView busNumberTextView = viewToUpdate.findViewById(R.id.bus_number_in_admin);

            nameTextView.setText(name);
            locationTextView.setText(location);
            busNumberTextView.setText(busNumber);
        }
    }

    private void removeStudentView(String id) {
        View viewToRemove = container.findViewWithTag(id);
        if (viewToRemove != null) {
            container.removeView(viewToRemove);
        }
    }

    private View createStudentView(String id, String name, String location, String busNumber) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.student_detail_row, container, false);

        TextView nameTextView = view.findViewById(R.id.student_name_in_admin);
        TextView locationTextView = view.findViewById(R.id.student_location_in_admin);
        TextView busNumberTextView = view.findViewById(R.id.bus_number_in_admin);

        nameTextView.setText(name);
        locationTextView.setText(location);
        busNumberTextView.setText(busNumber);

        // Set up click listener to open StudentDetailsActivity
        view.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StudentDetailsActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);
            intent.putExtra("location", location);
            intent.putExtra("busNumber", busNumber);
            startActivity(intent);
        });

        return view;
    }
}
