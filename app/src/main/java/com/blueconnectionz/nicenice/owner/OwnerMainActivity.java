package com.blueconnectionz.nicenice.owner;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.databinding.ActivityOwnerMainBinding;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OwnerMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_owner_main);
        Common.setStatusBarColor(getWindow(),this, Color.WHITE);

        BottomNavigationView navView = findViewById(R.id.nav_view_owner);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        //allMessages.setVisibility(View.GONE);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_owner_main);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

}