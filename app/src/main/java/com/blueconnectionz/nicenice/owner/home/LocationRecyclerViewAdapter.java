package com.blueconnectionz.nicenice.owner.home;

import android.app.Activity;
import android.app.Dialog;
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

import com.blueconnectionz.nicenice.ChannelActivity;
import com.blueconnectionz.nicenice.MyApp;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.blueconnectionz.nicenice.owner.dashboard.ChatActivity;
import com.blueconnectionz.nicenice.utils.Common;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.getstream.chat.android.client.ChatClient;
import io.getstream.chat.android.client.models.Channel;
import io.getstream.chat.android.client.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder> {

    private List<SingleRecyclerViewLocation> locationList;
    private Activity activity;
    AVLoadingIndicatorView avLoadingIndicatorView;

    public LocationRecyclerViewAdapter(List<SingleRecyclerViewLocation> locationList, Activity activity,AVLoadingIndicatorView avLoadingIndicatorView) {
        this.locationList = locationList;
        this.activity = activity;
        this.avLoadingIndicatorView = avLoadingIndicatorView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_driver_details, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SingleRecyclerViewLocation single = locationList.get(position);

        Glide.with(holder.image)
                .asBitmap()
                .placeholder(new ColorDrawable(activity.getResources().getColor(R.color.light_gray, null)))
                .load(single.getImage())
                .into(holder.image);
        holder.name.setText(single.getName());
        holder.location.setText(single.getLocation());
        holder.joinedDate.setText(single.getJoinedDate());
        holder.references.setText(single.getNumReferences() + "+ last owner references");
        holder.views.setText(single.getViews() + " views");

        if(!single.isAvailable()){
            holder.notAvailable.setVisibility(View.VISIBLE);
            holder.constraintLayout.setOnClickListener(view -> Toast.makeText(activity,"NOT AVAILABLE",Toast.LENGTH_LONG).show());
        }else {
            holder.notAvailable.setVisibility(View.GONE);
            holder.constraintLayout.setOnClickListener(view ->
                    activity.runOnUiThread(() ->
                    openDialogAndConnect(single.getName(), single.getId())));
        }

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name, location, joinedDate, references, views;
        ItemClickListener clickListener;
        MaterialCardView constraintLayout;
        MaterialCardView notAvailable;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.driverImg);
            notAvailable = itemView.findViewById(R.id.isAvailable2);
            name = itemView.findViewById(R.id.driverName);
            location = itemView.findViewById(R.id.driverLocation);
            joinedDate = itemView.findViewById(R.id.driverJoinedDate);
            references = itemView.findViewById(R.id.driverReferences);
            constraintLayout = itemView.findViewById(R.id.parentCard);
            views = itemView.findViewById(R.id.viewsTXT);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getLayoutPosition());
        }

        public void setClickListener(ItemClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }


    private void connectWithDriver(Long driverID) {
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        Call<ResponseBody> connect = RetrofitClient.getRetrofitClient().getAPI().connectWithDriver(
                LandingPage.userID,
                driverID);
        connect.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        // The owner has enough credit to chat with the driver
                        if (!data.equals("NOT ENOUGH CREDIT") && !data.equals("ALREADY CONNECTED")) {

                            try{
                                // The API response contains user IDs that should be allowed to communicate
                                JSONObject jsonObject = new JSONObject(data);
                                String ownerID = jsonObject.getString("ownerID");
                                String driverID = jsonObject.getString("driverID");
                                String token = jsonObject.getString("token");

                                ChatClient client = MyApp.client;

                                // Client requires User object
                                User user = new User();
                                String email = ownerID.toLowerCase();
                                int atIndex = email.indexOf('@');
                                user.setId(email.replaceAll("[.]", ""));
                                user.setName(email.substring(0, atIndex));
                                // replace with Avatar API collection
                                String profileURL = "https://avatars.dicebear.com/api/adventurer/" + Common.randomString() + ".svg";
                                user.setImage(profileURL);

                                // List of users in the channel
                                Map<String, Object> extraData = new HashMap<>();
                                extraData.put("name", "Chat with [ " + driverID + " ]");
                                List<String> memberIds = new LinkedList<>();
                                memberIds.add(ownerID.replaceAll("[.]", ""));
                                memberIds.add(driverID.replaceAll("[.]", ""));

                                io.getstream.chat.android.client.call.Call<Channel> newChannel = client.createChannel("messaging", "", memberIds, extraData);
                                newChannel.enqueue(result -> {
                                    if (result.isSuccess()) {
                                        avLoadingIndicatorView.setVisibility(View.GONE);
                                        Channel newChannel1 = result.data();
                                        activity.startActivity(ChannelActivity.newIntent(activity, newChannel1));
                                    } else {
                                        avLoadingIndicatorView.setVisibility(View.GONE);
                                        Common.statusToast(2,"Failed. Please try again",activity);
                                        Intent intent = activity.getIntent();
                                        activity.finish();
                                        activity.startActivity(intent);
                                    }
                                });

                                client.connectUser(user, token).enqueue((result) -> {
                                    if (result.isSuccess()) {
                                        // Handle success
                                        avLoadingIndicatorView.setVisibility(View.GONE);
                                        System.out.println("USER CONNECTED ):");
                                    } else {
                                        // Handler error
                                        avLoadingIndicatorView.setVisibility(View.GONE);
                                        System.out.println("USER DIS-CONNECTED ):");
                                    }
                                });
                            }catch (JSONException e){
                                avLoadingIndicatorView.setVisibility(View.GONE);
                                Common.featureComingSoon(activity,data);
                            }




                        }
                        else if(data.contains("ALREADY CONNECTED")){
                            avLoadingIndicatorView.setVisibility(View.GONE);
                            Common.statusToast(1,"ALREADY CONNECTED",activity);
                            activity.startActivity(new Intent(activity, ChatActivity.class));
                        }
                        else {
                            avLoadingIndicatorView.setVisibility(View.GONE);
                            Common.featureComingSoon(activity,"Please load credits before connecting with driver");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    Toast.makeText(activity, "CONNECTION FAILED", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                Toast.makeText(activity, "CONNECTION FAILED", Toast.LENGTH_SHORT).show();
                return;
            }
        });


    }


    private void openDialogAndConnect(String name, Long driverID) {
        PopupDialog.getInstance(activity)
                .setStyle(Styles.STANDARD)
                .setHeading("Connect with " + name)
                .setDescription("150 credits will be deducted from your account.")
                .setPopupDialogIcon(R.drawable.ic_baseline_connect_without_contact_24)
                .setPopupDialogIconTint(R.color.main)
                .setCancelable(false)
                .setPositiveButtonBackground(R.drawable.main)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                        //openWhatsApp(single.getName());
                        connectWithDriver(driverID);
                    }

                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                    }
                });
    }

}
