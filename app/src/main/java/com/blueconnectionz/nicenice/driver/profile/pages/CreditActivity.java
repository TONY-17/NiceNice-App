package com.blueconnectionz.nicenice.driver.profile.pages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueconnectionz.nicenice.CustomWebView;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditActivity extends AppCompatActivity {

    TextView creditBalance;
    RecyclerView recyclerView;
    AVLoadingIndicatorView avLoadingIndicatorView;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        Common.setStatusBarColor(getWindow(),this,getResources().getColor(R.color.colorBlue,null));

        ImageView backButton = findViewById(R.id.imageView13);
        avLoadingIndicatorView = findViewById(R.id.avi);
        backButton.setOnClickListener(view -> CreditActivity.super.onBackPressed());
        creditBalance = findViewById(R.id.textView);
        MaterialButton loadCredit = findViewById(R.id.loadMoreCredit);
        loadCredit.setOnClickListener(view -> {
            // Open web view to load credits
            startActivity(new Intent(CreditActivity.this, CustomWebView.class));
        });

        recyclerView = findViewById(R.id.pastTransactions);

        // Show a list of past transactions -> History of the admin loading credits to the owner/driver account
        fetchPastTransactions();
    }

    private void fetchPastTransactions(){
        Call<ResponseBody> getTransactions = RetrofitClient.getRetrofitClient().getAPI().allTransactions(LandingPage.userID);
        getTransactions.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        avLoadingIndicatorView.setVisibility(View.GONE);
                        String data = response.body().string();
                        JSONObject jsonObject = new JSONObject(data);
                        int balance = jsonObject.getInt("balance");
                        creditBalance.setText(String.valueOf(balance));
                        JSONArray transactions = jsonObject.getJSONArray("transactions");

                        List<Transaction> transactionList = new ArrayList<>();
                        for(int i = 0; i < transactions.length(); i++){
                           JSONObject current = transactions.getJSONObject(i);
                           String createdAt = current.getString("createdAt");
                           int amount = current.getInt("amount");
                           boolean adminTopUp = current.getBoolean("adminTopUp");

                           transactionList.add(new Transaction(createdAt,amount,adminTopUp));
                        }


                        Collections.reverse(transactionList);
                        CreditAdapter creditAdapter = new CreditAdapter(transactionList, CreditActivity.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CreditActivity.this));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(creditAdapter);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    return;
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                return;
            }
        });

    }
}