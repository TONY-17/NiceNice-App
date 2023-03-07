package com.blueconnectionz.nicenice.driver.cardetails;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blueconnectionz.nicenice.ChannelActivity;
import com.blueconnectionz.nicenice.MyApp;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.getstream.chat.android.client.ChatClient;
import io.getstream.chat.android.client.models.Channel;
import io.getstream.chat.android.client.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Checkout extends AppCompatActivity {

    TextView offerValue;/*
    Slider changeOfferValue;*/
    MaterialButton connect;
    View loadingView;
    AVLoadingIndicatorView avLoadingIndicatorView;


    TextView otherDriverOffers;
    TextView balances;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);
        ImageView backButtton = findViewById(R.id.imageView3);
        backButtton.setOnClickListener(view -> Checkout.super.onBackPressed());

        loadingView = findViewById(R.id.loadingView);
        avLoadingIndicatorView = findViewById(R.id.avi);
        connect = findViewById(R.id.connect);
        offerValue = findViewById(R.id.offerValue);
        /*
        changeOfferValue = findViewById(R.id.seekBar);
        changeOfferValue.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                offerValue.setText("R " + changeOfferValue.getValue());
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                offerValue.setText("R " + changeOfferValue.getValue());
            }
        });
*/
        otherDriverOffers = findViewById(R.id.textView21);
        balances = findViewById(R.id.balances);
        fetchCarData();
        connect.setOnClickListener(view -> {
            Common.setStatusBarColor(getWindow(), Checkout.this, getResources().getColor(R.color.background, null));
            loadingView.setVisibility(View.VISIBLE);
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            connectWithOwner();
        });

    }


    private void fetchCarData() {
        Call<ResponseBody> carData = RetrofitClient.getRetrofitClient().getAPI().getCarData(
                LandingPage.userID,
                getIntent().getLongExtra("carId", 0));

        carData.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        JSONObject jsonObject = new JSONObject(data);

                        int balance = jsonObject.getInt("balance");
                        int dealCost = jsonObject.getInt("dealCost");
                        String driverRequest = jsonObject.getString("driverRequest");

                        int peopleInDeal = jsonObject.getInt("peopleInDeal");
                        int peopleWhoAboveMin = jsonObject.getInt("peopleWhoAboveMin");

                        offerValue.setText(driverRequest);

                        otherDriverOffers.setText(peopleInDeal + " drivers are in this deal.");
                        balances.setText(":  " + balance + " Credits\n:  " + dealCost + " Credits\n:  " + driverRequest + " p/w");


                        avLoadingIndicatorView.setVisibility(View.GONE);
                        connect.setEnabled(true);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Common.statusToast(2, "NO LONGER AVAILABLE", Checkout.this);
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.statusToast(2, "NO LONGER AVAILABLE", Checkout.this);
                return;
            }
        });
    }


    private void connectWithOwner() {
        Call<ResponseBody> connect = RetrofitClient.getRetrofitClient().getAPI().connectWithOwner(
                LandingPage.userID,
                getIntent().getLongExtra("carId", 0));
        connect.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loadingView.setVisibility(View.GONE);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    Common.setStatusBarColor(getWindow(), Checkout.this, Color.WHITE);

                    try {

                        String data = response.body().string();
                        JSONObject jsonObject = new JSONObject(data);
                        String ownerID = jsonObject.getString("ownerID");
                        String driverID = jsonObject.getString("driverID");
                        String token = jsonObject.getString("token");

                        ChatClient client = MyApp.client;

                        // Client requires User object
                        User user = new User();
                        String email = driverID.toLowerCase();
                        int atIndex = email.indexOf('@');
                        user.setId(email.replaceAll("[.]", ""));
                        user.setName(email.substring(0, atIndex));
                        // replace with Avatar API collection
                        String profileURL = "https://avatars.dicebear.com/api/adventurer/" + Common.randomString() + ".svg";
                        user.setImage(profileURL);

                        // List of users in the channel
                        Map<String, Object> extraData = new HashMap<>();
                        extraData.put("name", "Let me be your driver");
                        List<String> memberIds = new LinkedList<>();
                        memberIds.add(ownerID.replaceAll("[.]", ""));
                        memberIds.add(driverID.replaceAll("[.]", ""));

                        io.getstream.chat.android.client.call.Call<Channel> newChannel = client.createChannel("messaging", "", memberIds, extraData);
                        newChannel.enqueue(result -> {
                            if (result.isSuccess()) {
                                Channel newChannel1 = result.data();
                                startActivity(ChannelActivity.newIntent(Checkout.this, newChannel1));
                                finish();
                            } else {
                                System.out.println("MESSAGE ERR " + result.error());
                            }
                        });


                        client.connectUser(user, token).enqueue((result) -> {
                            if (result.isSuccess()) {
                                // Handle success
                                System.out.println("USER CONNECTED ):");
                            } else {
                                // Handler error
                                System.out.println("USER DIS-CONNECTED ):");
                            }
                        });

                    } catch (NullPointerException | JSONException | IOException e ) {
                        e.printStackTrace();
                        Common.statusToast(2, "LOAD CREDITS FIRST", Checkout.this);
                        return;

                    }
                } else {
                    Common.statusToast(2, "NO LONGER AVAILABLE", Checkout.this);
                    return;

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.statusToast(2, "NO LONGER AVAILABLE", Checkout.this);
                return;
            }
        });
    }
}