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
import java.util.List;

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
        carWeeklyCheckIn.setText("R " + homeItemList.get(position).getWeeklyCheckInAmount());

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

        MaterialCardView parent = holder.parentCardView;
        parent.setOnClickListener(view -> {
            Call<ResponseBody> updateViews = RetrofitClient.getRetrofitClient().getAPI().updateNumViews(
                    homeItemList.get(position).getId(), LandingPage.userID);
            updateViews.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Intent i = new Intent(parent.getContext(), PostDetails.class);
                        i.putExtra("image", homeItemList.get(position).getImage());
                        i.putExtra("price", homeItemList.get(position).getWeeklyCheckInAmount());
                        i.putExtra("description", homeItemList.get(position).getDescription());
                        i.putExtra("location", homeItemList.get(position).getLocation());
                        i.putExtra("make", homeItemList.get(position).getCar());
                        i.putExtra("model", homeItemList.get(position).getOwner());
                        parent.getContext().startActivity(i);
                    } else {
                        try {
                            System.out.println("ERROR MSG " + response.errorBody().string());
                            Toast.makeText(connections.getContext(), "FAILED TO UPDATE ID " + homeItemList.get(position).getId(), Toast.LENGTH_SHORT).show();
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    System.out.println("ERROR MSG 2 " + t.getMessage());
                    Toast.makeText(connections.getContext(), "FAILED TO UPDATE", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
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
        }
    }

}
