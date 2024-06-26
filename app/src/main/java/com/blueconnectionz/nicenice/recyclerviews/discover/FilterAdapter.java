package com.blueconnectionz.nicenice.recyclerviews.discover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.cardetails.PostDetails;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    List<HomeItem> items;
    Activity activity;

    Locale SOUTH_AFRICA = new Locale("en", "ZA");
    NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(SOUTH_AFRICA);



    public FilterAdapter(List<HomeItem> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.filtered_item, parent, false);
        return new FilterAdapter.ViewHolder(view);
    }


    public void filter(List<HomeItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView top = holder.header;
        TextView bottom = holder.bottom;
        HomeItem item = items.get(position);
        top.setText(item.getCar());
        bottom.setText(item.getOwner() + " - " + item.getLocation());


        holder.constraintLayout.setOnClickListener(view -> {

            Intent i = new Intent(activity.getApplicationContext(), PostDetails.class);

            i.putExtra("image", items.get(position).getImage());
            i.putExtra("price", items.get(position).getWeeklyCheckInAmount());
            i.putExtra("description", items.get(position).getDescription());
            i.putExtra("location", items.get(position).getLocation());
            i.putExtra("make", items.get(position).getCar());
            i.putExtra("model", items.get(position).getOwner());

            i.putExtra("price",String.valueOf(dollarFormat.format(Double.valueOf(items.get(position).getWeeklyCheckInAmount()))));
            i.putExtra("carId", items.get(position).getId());
            i.putExtra("views", items.get(position).getViews());
            i.putExtra("connections", items.get(position).getConnections());
            i.putExtra("age", items.get(position).getAge());
            i.putExtra("insurance", items.get(position).isHasInsurance());
            i.putExtra("uber", items.get(position).isActiveOnHailingPlatforms());
            i.putExtra("tracker", items.get(position).isHasTracker());
            i.putExtra("available", items.get(position).isAvailable());


            activity.startActivity(i);
            activity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView bottom;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.headerText);
            bottom = itemView.findViewById(R.id.bottomText);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}
