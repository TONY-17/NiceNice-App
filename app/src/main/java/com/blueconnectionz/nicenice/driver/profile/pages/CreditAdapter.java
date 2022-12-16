package com.blueconnectionz.nicenice.driver.profile.pages;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.R;

import java.util.List;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.MyViewHolder> {
    List<Transaction> transactionList;
    Activity activity;
    public CreditAdapter(List<Transaction> transactionList, Activity activity) {
        this.transactionList = transactionList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transaction, parent, false);
        return new CreditAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TextView transactionType = holder.type;
        ImageView transactionImage = holder.image;
        TextView transactionAmount = holder.amount;
        transactionAmount.setText(String.valueOf(transactionList.get(position).getAmount()));
        if(transactionList.get(position).isAdminTopUp()){
            transactionType.setText("Topped up Credits");
            transactionImage.setImageResource(R.drawable.ic_baseline_attach_money_24);

        }else{
            transactionType.setText("Spent Credits");
            transactionImage.setImageResource(R.drawable.ic_baseline_money_off_24);
            transactionAmount.setTextColor(Color.RED);
        }
        TextView transactionDate = holder.date;
        transactionDate.setText(transactionList.get(position).getDate());


    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView type,amount,date;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.transactionType);
            amount = itemView.findViewById(R.id.transactionAmount);
            date = itemView.findViewById(R.id.transactionDate);
            image = itemView.findViewById(R.id.transactionImage);
        }

    }
}
