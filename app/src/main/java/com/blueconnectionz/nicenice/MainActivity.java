package com.blueconnectionz.nicenice;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.blueconnectionz.nicenice.databinding.ActivityMainBinding;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(),this, Color.WHITE);
        BottomNavigationView navView = findViewById(R.id.nav_view_driver);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // Open the custom chat activity
/*        View item = findViewById(R.id.navigation_history);
        item.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, AmityChatHomePageActivity.class))

        );*/
        NavigationUI.setupWithNavController(navView, navController);
    }

}