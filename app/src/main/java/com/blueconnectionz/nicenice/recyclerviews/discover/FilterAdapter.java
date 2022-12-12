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

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    List<HomeItem> items;
    Activity activity;

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


    public void filter(List<HomeItem> items){
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

            activity.startActivity(i);
            activity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
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
