package com.blueconnectionz.nicenice.owner.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.messaging.Dialog;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.owner.dashboard.ChatActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    View root;
    RecyclerView recyclerView;
    ArrayList<SingleRecyclerViewLocation> driversList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.owner_fragment_home, container, false);
        TextView HEADER_TEXT = root.findViewById(R.id.searchCardView);
        String text = "<font color=#FF000000>Find </font> <font color=#C117BC> DRIVERS </font> <font color=#FF000000> For Your Fleet</font>";
        HEADER_TEXT.setText(Html.fromHtml(text));
        recyclerView = root.findViewById(R.id.rv_on_top_of_map);
        fetchDrivers();
        initRecyclerView();

        MaterialCardView notifications = root.findViewById(R.id.notifications);
        notifications.setOnClickListener(view ->
                startActivity(new Intent(getContext(), ChatActivity.class))
        );
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void initRecyclerView() {

    }

    private void fetchDrivers(){
        driversList = new ArrayList<>();
        Call<ResponseBody> allDrivers = RetrofitClient.getRetrofitClient().getAPI().getAllDrivers();
        allDrivers.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String data = response.body().string();
                        System.out.println("ALL DRIVERS " + data);
                        JSONArray jsonArray = new JSONArray(data);
                        for(int i = 0; i < jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Long id = jsonObject.getLong("id");
                            String fullName = jsonObject.getString("fullName");
                            int views = jsonObject.getInt("views");
                            int age = jsonObject.getInt("age");
                            String location = jsonObject.getString("location");
                            int numReferences = jsonObject.getInt("numReferences");
                            String imageURL = jsonObject.getString("imageURL");

                            SingleRecyclerViewLocation singleLocation = new SingleRecyclerViewLocation();
                            singleLocation.setId(id);
                            singleLocation.setName(fullName);
                            singleLocation.setImage(imageURL);
                            singleLocation.setJoinedDate(age + "d. ago");
                            singleLocation.setNumReferences(numReferences);
                            singleLocation.setLocation(location);
                            singleLocation.setViews(views);

                            driversList.add(singleLocation);

                        }
                        LocationRecyclerViewAdapter locationAdapter =
                                new LocationRecyclerViewAdapter(driversList, getActivity());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(locationAdapter);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), "FAILED TO FETCH DRIVERS", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "FAILED TO FETCH DRIVERS", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String driverName(int position) {
        List<String> names = new ArrayList<>();
        names.add("Molokane Tlaka");
        names.add("Ntuthuko Nkomo");
        names.add("Renaldo Francis");
        names.add("Mkhari Harvey");
        names.add("Tumelo Selepe");
        names.add("Bongani  Moyo");
        names.add("Makhosonke Robbson");
        names.add("Karabo Mashishi");
        return names.get(position);
    }


    private String driverLocation(int position) {
        List<String> names = new ArrayList<>();
        names.add("Daveyton ");
        names.add("Diepsloot ");
        names.add("Windmill Park");
        names.add("Germiston ");
        names.add("Malvern east ");
        names.add("Vosloorus ");
        names.add("Half Way House Midrand ");
        names.add("Soshanguve ");
        return names.get(position);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }


}