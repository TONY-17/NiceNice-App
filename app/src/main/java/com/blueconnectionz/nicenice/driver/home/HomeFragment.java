package com.blueconnectionz.nicenice.driver.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.blueconnectionz.nicenice.DriverFilter;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.databinding.FragmentHomeBinding;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.recyclerviews.discover.HomeAdapter;
import com.blueconnectionz.nicenice.recyclerviews.discover.HomeItem;
import com.blueconnectionz.nicenice.utils.Common;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.stone.vega.library.VegaLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public static List<HomeItem> items = new ArrayList<>();
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;

    LottieAnimationView filteredView;
    TextView filteredView1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        shimmerFrameLayout = root.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();



        filteredView1 = root.findViewById(R.id.filtered2);
        filteredView = root.findViewById(R.id.filtered1);
        recyclerView = binding.carsListRV;


        final MaterialCardView filter = binding.filterCardView;
        filter.setAnimation(Common.viewBottomToOriginalAnim(getContext()));

        final MaterialCardView search = binding.searchCardView;
        search.setOnClickListener(view12 -> startActivity(new Intent(getContext(), DriverFilter.class)));

        fetchAllCars();

        recyclerView.setOnScrollChangeListener((view1, i, i1, i2, i3) -> {
            float scrollPercentage = recyclerViewScrollPercentage(recyclerView);
            if (scrollPercentage >= 10) {
                getActivity().runOnUiThread(() -> {
                    // filter.setVisibility(View.GONE);
                    search.setStrokeWidth(4);
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    //filter.setVisibility(View.VISIBLE);
                    search.setStrokeWidth(2);
                });
            }
        });


        return root;
    }

    private void fetchAllCars() {
        Call<ResponseBody> allCars = RetrofitClient.getRetrofitClient().getAPI().getAllCars();
        allCars.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            String data = response.body().string();

                            if(!data.contains("No cars available")){
                                JSONArray jsonArray = new JSONArray(data);
                                List<HomeItem> homeItemList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    JSONObject carJsonObj = jsonObject.getJSONObject("car");
                                    Long id = carJsonObj.getLong("id");
                                    String make = carJsonObj.getString("make");
                                    String model = carJsonObj.getString("model");
                                    String year = carJsonObj.getString("year");
                                    String city = carJsonObj.getString("city");
                                    String description = carJsonObj.getString("description");
                                    //String description = "Description ";


                                    String weeklyTarget = carJsonObj.getString("weeklyTarget");
                                    boolean depositRequired = carJsonObj.getBoolean("depositRequired");
                                    boolean hasInsurance = carJsonObj.getBoolean("hasInsurance");
                                    boolean hasTracker = carJsonObj.getBoolean("hasTracker");
                                    boolean available = carJsonObj.getBoolean("available");
                                    boolean activeOnHailingPlatforms = carJsonObj.getBoolean("activeOnHailingPlatforms");
                                    String views = carJsonObj.getString("views");
                                    String age = carJsonObj.getString("age");
                                    String numConnections = carJsonObj.getString("numConnections");


                                    String imageURL = jsonObject.getString("imageURL");

                                    homeItemList.add(new HomeItem(id, imageURL, make, model, city, weeklyTarget, depositRequired, description,
                                            Integer.valueOf(views), Integer.valueOf(numConnections), Integer.valueOf(age), activeOnHailingPlatforms,
                                            hasInsurance,hasTracker,activeOnHailingPlatforms,available));

                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                }
                                HomeAdapter homeAdapter = new HomeAdapter(homeItemList);
                                recyclerView.setLayoutManager(new VegaLayoutManager());
                                //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(homeAdapter);
                                items = homeItemList;
                            }

                            // We do have cars in the system
                            else{

                                filteredView.setVisibility(View.VISIBLE);
                                filteredView1.setVisibility(View.VISIBLE);
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            }






                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    });

                } else {

                    filteredView.setVisibility(View.VISIBLE);
                    filteredView1.setVisibility(View.VISIBLE);

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                filteredView.setVisibility(View.VISIBLE);
                filteredView1.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

    }


    private float recyclerViewScrollPercentage(RecyclerView recyclerView) {
        int offset = recyclerView.computeVerticalScrollOffset();
        int extent = recyclerView.computeVerticalScrollExtent();
        int range = recyclerView.computeVerticalScrollRange();
        float percentage = (100.0f * offset / (float) (range - extent));
        return percentage;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();

        shimmerFrameLayout.stopShimmer();
    }
}