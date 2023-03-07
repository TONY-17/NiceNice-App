package com.blueconnectionz.nicenice.recyclerviews.discover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.cardetails.PostDetails;

import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private List<HomeItem> homeItemList;

    public HomeAdapter(List<HomeItem> homeItemList) {
        this.homeItemList = homeItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_car_details, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImageView carImage = holder.carImage;
        Glide.with(carImage)
                .asBitmap()
                .placeholder(new ColorDrawable(carImage.getContext().getResources().getColor(R.color.light_gray, null)))
                .load(homeItemList.get(position).getImage())
                .into(carImage);
        /*
         * Will store data in the format {OWNER-NAME CAR-NAME - LOCATION}
         * Ex. {Katlego’s Toyota - Tembisa}
         */
        TextView ownerDetails = holder.carOwner;
        ownerDetails.setText(homeItemList.get(position).getOwner() + " "
                +
                homeItemList.get(position).getCar()
                + " - " +
                homeItemList.get(position).getLocation());

        TextView carWeeklyCheckIn = holder.weekly;
        Locale SOUTH_AFRICA = new Locale("en", "ZA");
        NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(SOUTH_AFRICA);

        carWeeklyCheckIn.setText(String.valueOf(dollarFormat.format(Double.valueOf(homeItemList.get(position).getWeeklyCheckInAmount()))));

        TextView numViews = holder.views;
        numViews.setText(String.valueOf(homeItemList.get(position).getViews()) + " views");

        TextView connections = holder.connections;
        connections.setText(String.valueOf(homeItemList.get(position).getConnections()));

        TextView age = holder.age;
        if (homeItemList.get(position).getAge() == -1) {
            age.setText("Today");
        } else {
            age.setText(String.valueOf(homeItemList.get(position).getAge()) + " d. ago");
        }


        if (!homeItemList.get(position).isOnPlatform()) {
            holder.onPlatform.setImageResource(R.drawable.ic_baseline_close_24);
        }

        if (homeItemList.get(position).isRequiresDeposit()) {
            holder.depositRequired.setImageResource(R.drawable.ic_baseline_close_24);
            holder.deposit.setText("Deposit Required");
        }

        if(!homeItemList.get(position).isAvailable()){
            holder.isAvailable.setVisibility(View.VISIBLE);
        }

        MaterialCardView parent = holder.parentCardView;
        parent.setOnClickListener(view -> {
            Intent i = new Intent(parent.getContext(), PostDetails.class);
            i.putExtra("carId", homeItemList.get(position).getId());
            i.putExtra("image", homeItemList.get(position).getImage());
            i.putExtra("price",String.valueOf(dollarFormat.format(Double.valueOf(homeItemList.get(position).getWeeklyCheckInAmount()))));
            i.putExtra("description", homeItemList.get(position).getDescription());
            i.putExtra("location", homeItemList.get(position).getLocation());
            i.putExtra("make", homeItemList.get(position).getCar());
            i.putExtra("model", homeItemList.get(position).getOwner());
            i.putExtra("views", homeItemList.get(position).getViews());
            i.putExtra("connections", homeItemList.get(position).getConnections());
            i.putExtra("age", homeItemList.get(position).getAge());
            i.putExtra("insurance", homeItemList.get(position).isHasInsurance());
            i.putExtra("uber", homeItemList.get(position).isActiveOnHailingPlatforms());
            i.putExtra("tracker", homeItemList.get(position).isHasTracker());
            i.putExtra("available", homeItemList.get(position).isAvailable());
            parent.getContext().startActivity(i);
        });


    }

    @Override
    public int getItemCount() {
        return homeItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView carOwner;
        TextView weekly;
        MaterialCardView parentCardView;

        TextView views, connections, age, deposit, platform;

        ImageView onPlatform;
        ImageView depositRequired;


        MaterialCardView isAvailable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.carImage);
            carOwner = itemView.findViewById(R.id.driverName);
            parentCardView = itemView.findViewById(R.id.parentCard);
            weekly = itemView.findViewById(R.id.carWeeklyCheckIn);

            views = itemView.findViewById(R.id.viewsTXT);
            connections = itemView.findViewById(R.id.connectionsTXT);
            age = itemView.findViewById(R.id.driverJoinedDate);
            deposit = itemView.findViewById(R.id.driverLocation);
            platform = itemView.findViewById(R.id.driverReferences);

            onPlatform = itemView.findViewById(R.id.imageView7);
            depositRequired = itemView.findViewById(R.id.imageView6);

            isAvailable = itemView.findViewById(R.id.isAvailable2);
        }
    }

}
