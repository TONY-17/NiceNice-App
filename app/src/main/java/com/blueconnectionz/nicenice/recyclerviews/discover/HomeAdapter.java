package com.blueconnectionz.nicenice.recyclerviews.discover;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.ui.PostDetails;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView carImage = holder.carImage;
/*        Glide.with(carImage)
                .asBitmap()
                .placeholder(new ColorDrawable(carImage.getContext().getResources().getColor(R.color.light_gray, null)))
                .load(homeItemList.get(position).getImage())
                .into(carImage);
        System.out.println("IMAGE " + homeItemList.get(position).getImage());*/
        /*
         * Will store data in the format {OWNER-NAME CAR-NAME - LOCATION}
         * Ex. {Katlego’s Toyota - Tembisa}
         */
        TextView ownerDetails = holder.carOwner;
        ownerDetails.setText(homeItemList.get(position).getOwner() + "'s "
                +
                homeItemList.get(position).getCar()
                + " - " +
                homeItemList.get(position).getLocation());


        MaterialCardView parent = holder.parentCardView;
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.getContext().startActivity(new Intent(parent.getContext(), PostDetails.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView carOwner;

        MaterialCardView parentCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.carImage);
            carOwner = itemView.findViewById(R.id.carNameLocation);
            parentCardView = itemView.findViewById(R.id.parentCard);
        }
    }

}
