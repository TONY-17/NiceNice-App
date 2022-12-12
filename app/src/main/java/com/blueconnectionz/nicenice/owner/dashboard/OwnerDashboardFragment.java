package com.blueconnectionz.nicenice.owner.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.owner.AddNewCar;
import com.blueconnectionz.nicenice.owner.OwnerMainActivity;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class OwnerDashboardFragment extends Fragment {

    View root;
    MaterialCardView addNewCar;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.owner_fragment_dashboard, container, false);

        ownerCars();

        addNewCar = root.findViewById(R.id.addANewCar);
        addNewCar.setOnClickListener(view -> startActivity(new Intent(getContext(), AddNewCar.class)));

        return root;
    }

    private void  ownerCars(){
        CarRecyclerViewAdapter adapter = new CarRecyclerViewAdapter(createCars(),getActivity());
        RecyclerView recyclerView = root.findViewById(R.id.ownerCars);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }
    private List<SingleOwnerCar> createCars() {
        List<SingleOwnerCar> carList = new ArrayList<>();
        for(int i = 0; i < 0; i++){
            carList.add(new SingleOwnerCar(R.drawable.car_img2,"Car name " + i, "Car info " + i,"In progress"));
        }
        return carList;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}