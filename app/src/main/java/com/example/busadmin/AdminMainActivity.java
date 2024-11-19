package com.example.busadmin;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;  // Add this import to use ContextCompat
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Initialize ViewPager2 and TabLayout
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Set adapter for ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Attach the TabLayout with the ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Students");
                        tab.setIcon(ContextCompat.getDrawable(AdminMainActivity.this, R.drawable.drawable_username));
                        break;
                    case 1:
                        tab.setText("Bus");
                        tab.setIcon(ContextCompat.getDrawable(AdminMainActivity.this, R.drawable.baseline_directions_bus_24));
                        break;
                    case 2:
                        tab.setText("Request");
                        tab.setIcon(ContextCompat.getDrawable(AdminMainActivity.this, R.drawable.baseline_contact_emergency_24));
                        break;
                }
            }
        }).attach();
    }
}
