package com.blueconnectionz.nicenice.driver.cardetails;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.utils.Common;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetails extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        // Set the status bar color to white
        Common.setStatusBarColor(getWindow(), this, Color.WHITE);

        updateNumberOfViews();

        ImageView backButton = findViewById(R.id.imageView3);
        backButton.setOnClickListener(view -> PostDetails.super.onBackPressed());

        // Animate the bottom card from bottom to its current location
        MaterialCardView checkInCardView = findViewById(R.id.checkInCardView);
        checkInCardView.setAnimation(Common.viewBottomToOriginalAnim(this));
        MaterialButton connect = findViewById(R.id.connect);
        // Manipulate views based on scrolling events
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView2);
        TextView carName = findViewById(R.id.carNameTop);
        View view = findViewById(R.id.view6);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            double scrollViewHeight = nestedScrollView.getChildAt(0).getBottom() - nestedScrollView.getHeight();
            double getScrollY = nestedScrollView.getScrollY();
            double scrollPosition = (getScrollY / scrollViewHeight) * 100d;

            if (scrollPosition >= 20) {
                runOnUiThread(() -> {
                    carName.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                });

            } else {
                runOnUiThread(() -> {
                    carName.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                });

            }

        });


        if(!getIntent().getBooleanExtra("available",true)){
            findViewById(R.id.isAvailable).setVisibility(View.VISIBLE);
            //connect.setVisibility(View.GONE);
        }


        TextView headerName = findViewById(R.id.textView11);
        TextView car = findViewById(R.id.textView12);
        ImageView carImage = findViewById(R.id.imageView5);
        TextView description = findViewById(R.id.textView17);
        TextView price = findViewById(R.id.price);
        TextView location = findViewById(R.id.textView7);

        TextView views = findViewById(R.id.viewsTXT);
        TextView age = findViewById(R.id.driverJoinedDate);
        TextView connections = findViewById(R.id.connectionsTXT);


        headerName.setText(getIntent().getStringExtra("make"));
        car.setText(getIntent().getStringExtra("model"));
        Glide.with(carImage)
                .asBitmap()
                .placeholder(new ColorDrawable(carImage.getContext().getResources().getColor(R.color.light_gray, null)))
                .load(getIntent().getStringExtra("image"))
                .into(carImage);

        views.setText(String.valueOf(getIntent().getIntExtra("views",0)) + " views");
        age.setText(String.valueOf(getIntent().getIntExtra("age",0)) + " d. ago");
        connections.setText(String.valueOf(getIntent().getIntExtra("connections",0)));

        description.setText(getIntent().getStringExtra("description"));
        price.setText(getIntent().getStringExtra("price"));
        location.setText(getIntent().getStringExtra("location"));
        carName.setText(getIntent().getStringExtra("model") + " " + getIntent().getStringExtra("make"));

        connect.setOnClickListener(view1 -> /*Toast.makeText(this,"IN DEV",Toast.LENGTH_LONG).show()*/
                featureNotAvailable());
    }

    private void featureNotAvailable() {
        Intent i = new Intent(PostDetails.this, Checkout.class);
        i.putExtra("carId",getIntent().getLongExtra("carId",0));
        startActivity(i);
    }


    private void updateNumberOfViews(){
        Long carID = getIntent().getLongExtra("carId",0);
        Call<ResponseBody> updateViews = RetrofitClient.getRetrofitClient().getAPI()
                .updateViews(carID, LandingPage.userID);
        updateViews.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("UPDATED VIEWS");

                } else {
                    try {
                        System.out.println("ERROR MSG " + response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("ERROR MSG 2 " + t.getMessage());
            }
        });
    }

}