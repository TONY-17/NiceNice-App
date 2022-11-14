package com.blueconnectionz.nicenice.recyclerviews.msgs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.recyclerviews.discover.HomeAdapter;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{

    List<MsgItem> msgItemList;
    Activity activity;
    public MsgAdapter(List<MsgItem> msgItemList, Activity activity) {
        this.msgItemList = msgItemList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_msg_item, parent, false);
        return new MsgAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView owner = holder.owner;
        owner.setText(msgItemList.get(position).getOwner());
        TextView message = holder.message;
        message.setText(msgItemList.get(position).getLastMessage());
        TextView time = holder.time;
        time.setText(msgItemList.get(position).getTime());
        ImageView profilePicture = holder.profilePicture;

        MaterialCardView statusCardView = holder.msgStatusCardView;
        MaterialCardView sideCard = holder.newMsgCardView;
        ImageView rightIcon = holder.msgRightIcon;
        ImageView checkIcon = holder.msgCheckIcon;

        if(msgItemList.get(position).rejected){
            activity.runOnUiThread(() -> {
                owner.setTextColor(Color.BLACK);
                statusCardView.setCardBackgroundColor(Color.RED);
                //statusCardView.setRadius(statusCardView.getRadius()/2);
                rightIcon.setVisibility(View.VISIBLE);
                sideCard.setVisibility(View.GONE);
                holder.parent.setCardBackgroundColor(Color.WHITE);
            });
        }

        if(msgItemList.get(position).accepted){
            activity.runOnUiThread(() -> {
                owner.setTextColor(Color.BLACK);
                statusCardView.setCardBackgroundColor(Color.GREEN);
                checkIcon.setVisibility(View.VISIBLE);
                rightIcon.setVisibility(View.GONE);
                sideCard.setVisibility(View.GONE);
                //statusCardView.setRadius(statusCardView.getRadius()/2);
                holder.parent.setCardBackgroundColor(Color.WHITE);
            });
        }

        if(msgItemList.get(position).newMessage){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkIcon.setVisibility(View.VISIBLE);

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return msgItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView owner, message, time;
        ImageView profilePicture;
        // Views that will change depending on the status of the message
        MaterialCardView msgStatusCardView, newMsgCardView, parent;
        ImageView msgRightIcon, msgCheckIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            owner = itemView.findViewById(R.id.carOwner);
            message = itemView.findViewById(R.id.chatLastMessage);
            time = itemView.findViewById(R.id.chatLastTime);
            profilePicture = itemView.findViewById(R.id.chatImage);
            // Changing views
            msgStatusCardView = itemView.findViewById(R.id.msgStatusCardView);
            newMsgCardView = itemView.findViewById(R.id.newMsgCardView);
            msgRightIcon = itemView.findViewById(R.id.msgRightIcon);
            msgCheckIcon = itemView.findViewById(R.id.msgCheckIcon);
            parent = itemView.findViewById(R.id.parentMsgCardView);
        }
    }
}
