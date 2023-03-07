package com.blueconnectionz.nicenice;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.blueconnectionz.nicenice.databinding.ActivityMainBinding;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.getstream.chat.android.client.ChatClient;
import io.getstream.chat.android.client.models.User;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);
        BottomNavigationView navView = findViewById(R.id.nav_view_driver);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // Open the custom chat activity
        View item = findViewById(R.id.navigation_history);

        BadgeDrawable badge = navView.getOrCreateBadge(R.id.navigation_history);
        badge.setVisible(true);

        User user = new User();
        String email = LandingPage.userEmail.toLowerCase();
        user.setId(email.replaceAll("[.]", ""));

        ChatClient client = MyApp.client;
        client.connectUser(user, client.devToken(user.getId())).enqueue(result -> {
            if (result.isSuccess()) {
                runOnUiThread(() -> {
                    User userRes = result.data().getUser();
                    int totalUnreadCount = userRes.getTotalUnreadCount();
                    badge.setNumber(totalUnreadCount);
                });
            }
        });

        NavigationUI.setupWithNavController(navView, navController);
    }

}