package com.blueconnectionz.nicenice.owner.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.blueconnectionz.nicenice.MyApp;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.driver.messaging.Dialog;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.owner.dashboard.ChatActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.wang.avi.AVLoadingIndicatorView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.getstream.chat.android.client.ChatClient;
import io.getstream.chat.android.client.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    View root;
    RecyclerView recyclerView;
    ArrayList<SingleRecyclerViewLocation> driversList;

    ShimmerFrameLayout shimmerFrameLayout;

    LottieAnimationView noDriver1;
    TextView noDriver2;

    AVLoadingIndicatorView avLoadingIndicatorView;
    TextView unreadCount;

    MaterialCardView unreadCountView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.owner_fragment_home, container, false);
        TextView HEADER_TEXT = root.findViewById(R.id.searchCardView);
        String text = "<font color=#FF000000>Find </font> <font color=#C117BC> DRIVERS </font> <font color=#FF000000> For Your Fleet</font>";
        HEADER_TEXT.setText(Html.fromHtml(text));
        recyclerView = root.findViewById(R.id.rv_on_top_of_map);

        unreadCount = root.findViewById(R.id.messagesCount);
        unreadCountView = root.findViewById(R.id.unreadMC);

        noDriver1 = root.findViewById(R.id.imageView18);
        noDriver2 = root.findViewById(R.id.txtN);
        avLoadingIndicatorView= root.findViewById(R.id.avi);
        shimmerFrameLayout = root.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        fetchDrivers();
        initRecyclerView();

        User user = new User();
        String email = LandingPage.userEmail.toLowerCase();
        user.setId(email.replaceAll("[.]", ""));

        ChatClient client = MyApp.client;
        client.connectUser(user, client.devToken(user.getId())).enqueue(result -> {
            if (result.isSuccess()) {
                User userRes = result.data().getUser();
                int totalUnreadCount = userRes.getTotalUnreadCount();
                unreadCountView.setVisibility(View.VISIBLE);
                unreadCount.setText(String.valueOf(totalUnreadCount));
            }
        });

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
        Call<ResponseBody> allDrivers = RetrofitClient.getRetrofitClient().getAPI().getAllDrivers(LandingPage.userID);
        allDrivers.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String data = response.body().string();
                        System.out.println("ALL DRIVERS " + data);
                        if(data.contains("No drivers yet")){
                            noDriver1.setVisibility(View.VISIBLE);
                            noDriver2.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }else{

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
                                boolean online = jsonObject.getBoolean("online");
                                SingleRecyclerViewLocation singleLocation = new SingleRecyclerViewLocation();
                                singleLocation.setId(id);
                                singleLocation.setName(fullName);
                                singleLocation.setImage(imageURL);
                                singleLocation.setJoinedDate(age + "d. ago");
                                singleLocation.setNumReferences(numReferences);
                                singleLocation.setLocation(location);
                                singleLocation.setViews(views);
                                singleLocation.setAvailable(online);

                                driversList.add(singleLocation);

                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            }
                            LocationRecyclerViewAdapter locationAdapter =
                                    new LocationRecyclerViewAdapter(driversList, getActivity(),avLoadingIndicatorView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(locationAdapter);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(), "FAILED TO FETCH DRIVERS", Toast.LENGTH_SHORT).show();
                    noDriver1.setVisibility(View.VISIBLE);
                    noDriver2.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "FAILED TO FETCH DRIVERS", Toast.LENGTH_SHORT).show();
                noDriver1.setVisibility(View.VISIBLE);
                noDriver2.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }


}