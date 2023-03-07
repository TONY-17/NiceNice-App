package com.blueconnectionz.nicenice.owner.dashboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.utils.Common;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.List;
import java.util.ResourceBundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SingleOwnerCar singleOwnerCar = singleOwnerCarList.get(position);

        Glide.with(holder.carImage)
                .asBitmap()
                .placeholder(new ColorDrawable(activity.getResources().getColor(R.color.light_gray, null)))
                .load(singleOwnerCar.getImage())
                .into(holder.carImage);

        holder.numConnections.setText(String.valueOf(singleOwnerCar.getConnections()));
        holder.carName.setText(singleOwnerCar.getName());
       // holder.carInfo.setText(singleOwnerCar.getInfo());
        if(singleOwnerCar.getStatus().contains("Not Available")){
            holder.statusCard.setCardBackgroundColor(Color.RED);
            holder.car.setOnClickListener(view -> Toast.makeText(activity,"Contact support to re-activate",Toast.LENGTH_LONG).show());
        }else{
            holder.car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PopupDialog.getInstance(activity)
                                    .setStyle(Styles.STANDARD)
                                    .setHeading("Update car state")
                                    .setDescription("Are you sure you want to update to unavailable state?")
                                    .setPopupDialogIcon(R.drawable.ic_baseline_remove_circle_outline_24)
                                    .setPopupDialogIconTint(R.color.red)
                                    .setCancelable(false)
                                    .setPositiveButtonBackground(com.saadahmedsoft.popupdialog.R.drawable.bg_red_10)
                                    .showDialog(new OnDialogButtonClickListener() {
                                        @Override
                                        public void onPositiveClicked(Dialog dialog) {
                                            super.onPositiveClicked(dialog);
                                            deactivateCar(holder.getItemId());
                                        }

                                        @Override
                                        public void onNegativeClicked(Dialog dialog) {
                                            super.onNegativeClicked(dialog);
                                        }
                                    });
                        }
                    });

                }
            });
        }

        holder.status.setText(singleOwnerCar.getStatus());
        // When owner clicks an individual car

    }

    @Override
    public int getItemCount() {
        return singleOwnerCarList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView carName, status,numConnections;
        MaterialCardView car;
        MaterialCardView statusCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.ownerCarImg);
            carName = itemView.findViewById(R.id.ownerCarName);
           // carInfo = itemView.findViewById(R.id.ownerCarInfo);
            status = itemView.findViewById(R.id.ownerCarStatus);
            car = itemView.findViewById(R.id.ownerCar);
            numConnections = itemView.findViewById(R.id.numConnections);
            statusCard = itemView.findViewById(R.id.materialCardView13);
        }
    }


    private void deactivateCar(Long carID){
        Call<ResponseBody> deactivate = RetrofitClient.getRetrofitClient().getAPI().disableCar(carID);
        deactivate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Common.statusToast(1,"Thank you",activity);
                    Intent intent = activity.getIntent();
                    activity.finish();
                    activity.startActivity(intent);
                }else{
                    Toast.makeText(activity,"Contact support to DE-activate",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity,"Contact support to DE-activate",Toast.LENGTH_LONG).show();
            }
        });
    }
}
