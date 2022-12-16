package com.blueconnectionz.nicenice.owner.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.owner.AddNewCar;
import com.blueconnectionz.nicenice.owner.OwnerMainActivity;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerDashboardFragment extends Fragment {

    View root;
    MaterialCardView addNewCar;

    TextView totalCars, totalAccepted,totalRejected;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.owner_fragment_dashboard, container, false);
        totalCars = root.findViewById(R.id.totalCars);
        totalAccepted = root.findViewById(R.id.textView41);
        totalRejected = root.findViewById(R.id.totalRejected);
        ownerCars();

        addNewCar = root.findViewById(R.id.addANewCar);
        addNewCar.setOnClickListener(view -> startActivity(new Intent(getContext(), AddNewCar.class)));

        return root;
    }

    private void ownerCars() {

        Call<ResponseBody> allOwnerCars = RetrofitClient.getRetrofitClient().getAPI().getAllOwnersCars(LandingPage.userID);
        allOwnerCars.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {

                        List<SingleOwnerCar> carList = new ArrayList<>();
                        String data = response.body().string();
                        JSONObject jsonObject = new JSONObject(data);
                        int totalListings = jsonObject.getInt("totalListings");
                        int accepted = jsonObject.getInt("accepted");
                        int rejected = jsonObject.getInt("rejected");


                        totalCars.setText(String.valueOf(totalListings));
                        totalAccepted.setText(String.valueOf(accepted));
                        totalRejected.setText(String.valueOf(rejected));

                        JSONArray ownerCars = jsonObject.getJSONArray("ownerCars");
                        for (int i = 0; i < ownerCars.length(); i++) {

                            JSONObject singleCar = ownerCars.getJSONObject(i);
                            String image = singleCar.getString("image");
                            String name = singleCar.getString("name");
                            int numConnections = singleCar.getInt("numConnections");
                            boolean available = singleCar.getBoolean("available");

                            String status = "";
                            if(available == true){
                                status = "Available";
                            }else {
                                status = "Not Available";
                            }

                            carList.add(new SingleOwnerCar(image, name, String.valueOf(numConnections), status));
                        }

                        CarRecyclerViewAdapter adapter = new CarRecyclerViewAdapter(carList, getActivity());
                        RecyclerView recyclerView = root.findViewById(R.id.ownerCars);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        SnapHelper snapHelper = new LinearSnapHelper();
                        snapHelper.attachToRecyclerView(recyclerView);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}