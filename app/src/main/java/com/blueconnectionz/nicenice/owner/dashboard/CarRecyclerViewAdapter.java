package com.blueconnectionz.nicenice.owner.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.blueconnectionz.nicenice.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CarRecyclerViewAdapter extends RecyclerView.Adapter<CarRecyclerViewAdapter.ViewHolder> {
    List<SingleOwnerCar> singleOwnerCarList;
    Activity activity;
    public CarRecyclerViewAdapter(List<SingleOwnerCar> singleOwnerCarList, Activity activity) {
        this.singleOwnerCarList = singleOwnerCarList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_car_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SingleOwnerCar singleOwnerCar = singleOwnerCarList.get(position);
        holder.carImage.setImageResource(singleOwnerCar.getImage());
        holder.carName.setText(singleOwnerCar.getName());
       // holder.carInfo.setText(singleOwnerCar.getInfo());
        holder.status.setText(singleOwnerCar.getStatus());
        // When owner clicks an individual car
        //holder.car.setOnClickListener(view -> activity.startActivity(new Intent(activity.getApplicationContext(), AmityChatHomePageActivity.class)));
    }

    @Override
    public int getItemCount() {
        return singleOwnerCarList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView carName, status;
        MaterialCardView car;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.ownerCarImg);
            carName = itemView.findViewById(R.id.ownerCarName);
           // carInfo = itemView.findViewById(R.id.ownerCarInfo);
            status = itemView.findViewById(R.id.ownerCarStatus);
            car = itemView.findViewById(R.id.ownerCar);
        }
    }
}
