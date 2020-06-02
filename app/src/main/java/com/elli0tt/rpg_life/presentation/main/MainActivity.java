package com.elli0tt.rpg_life.presentation.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.WallpaperManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.elli0tt.rpg_life.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    private ConstraintLayout mainLayout;

    public static final String CHANNEL_ID = "channel 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mainLayout = findViewById(R.id.main_layout);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.character_screen ||
                    id == R.id.skills_screen ||
                    id == R.id.quests_screen ||
                    id == R.id.statistics_screen ||
                    id == R.id.settings_screen
            ) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        setupToolbar();
        createNotificationChannel();

//        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
//        mainLayout.setBackground(wallpaperManager.getDrawable());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
