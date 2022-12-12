package com.blueconnectionz.nicenice;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.driver.home.HomeFragment;
import com.blueconnectionz.nicenice.recyclerviews.discover.FilterAdapter;
import com.blueconnectionz.nicenice.recyclerviews.discover.HomeItem;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class DriverFilter extends AppCompatActivity {
    ImageView filteredView;
    TextView filteredView1;
    MaterialCardView back;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_filter);
        Common.setStatusBarColor(getWindow(),this, Color.WHITE);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        filteredView1 = findViewById(R.id.filtered2);
        filteredView = findViewById(R.id.filtered1);
        back = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.recyclerView);

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
        back.setOnClickListener(view -> DriverFilter.super.onBackPressed());
    }

    private void filter(String newText) {
        ArrayList<HomeItem> filtered = new ArrayList<>();
        FilterAdapter filterAdapter = new FilterAdapter(filtered,this);
        recyclerView.setAdapter(filterAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        for(HomeItem item : HomeFragment.items){
            if(item.getCar().toLowerCase().contains(newText.toLowerCase())
                    || item.getLocation().toLowerCase().contains(newText.toLowerCase())
            || item.getOwner().toLowerCase().contains(newText.toLowerCase())){
                filtered.add(item);
            }
        }
        if(filtered.isEmpty()){
            filteredView.setVisibility(View.VISIBLE);
            filteredView1.setVisibility(View.VISIBLE);
        }else{
            // add to adapter;
            filteredView.setVisibility(View.GONE);
            filteredView1.setVisibility(View.GONE);
            filterAdapter.filter(filtered);
        }
    }
}