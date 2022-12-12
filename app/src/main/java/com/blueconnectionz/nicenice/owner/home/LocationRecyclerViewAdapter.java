package com.blueconnectionz.nicenice.owner.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
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
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.getstream.chat.android.client.ChatClient;
import io.getstream.chat.android.client.channel.ChannelClient;
import io.getstream.chat.android.client.models.Channel;
import io.getstream.chat.android.client.models.User;
import io.getstream.chat.android.client.utils.Result;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder> {

    private List<SingleRecyclerViewLocation> locationList;
    private Activity activity;

    public LocationRecyclerViewAdapter(List<SingleRecyclerViewLocation> locationList, Activity activity) {
        this.locationList = locationList;
        this.activity = activity;
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
        //holder.image.setImageResource(single.getImage());
        Glide.with(holder.image)
                .asBitmap()
                .placeholder(new ColorDrawable(activity.getResources().getColor(R.color.light_gray, null)))
                //.load(single.getImage())
                .load("https://images.unsplash.com/photo-1664575600850-c4b712e6e2bf?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80")
                .into(holder.image);
        holder.name.setText(single.getName());
        holder.location.setText(single.getLocation());
        holder.joinedDate.setText(single.getJoinedDate());
        holder.references.setText(single.getNumReferences() + " last owner references");
        holder.views.setText(single.getViews() + " views");

        holder.constraintLayout.setOnClickListener(view -> activity.runOnUiThread(() -> openDialogAndConnect(single.getName(), single.getId())));


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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.driverImg);
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

    private void openWhatsApp(String driver) {
        try {
            String number = "+27 0732811089";
            String message = "Hello support@blueconnectionz I would like to connect with " + driver;
            PackageManager packageManager = activity.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                activity.startActivity(i);
            } else {
                // Snackbar.make(activity.getView(),"No WhatsApp",Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("ERROR WHATSAPP", e.toString());
            //Snackbar.make(getView(),"No WhatsApp",Snackbar.LENGTH_LONG).show();
        }
    }

    private void connectWithDriver(Long driverID) {
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
                        if (!data.equals("NOT ENOUGH CREDIT")) {

                            // The API response contains user IDs that should be allowed to communicate
                            JSONObject jsonObject = new JSONObject(data);
                            String ownerID = jsonObject.getString("ownerID");
                            String driverID = jsonObject.getString("driverID");

                            ChatClient client = MyApp.client;

                            // Client requires User object
                            User user = new User();
                            int atIndex = ownerID.indexOf('@');
                            user.setId(ownerID.replaceAll("[.]", ""));
                            // Only the first portion of the email will be the display name
                            user.setName(ownerID.substring(0, atIndex));
                            user.setImage("https://bit.ly/2TIt8NR");

                            // List of users in the channel
                            Map<String, Object> extraData = new HashMap<>();
                            extraData.put("name", "Chat with [ " + driverID + " ]");
                            List<String> memberIds = new LinkedList<>();
                            memberIds.add(ownerID.replaceAll("[.]", ""));
                            memberIds.add(driverID.replaceAll("[.]", ""));

                            io.getstream.chat.android.client.call.Call<Channel> newChannel =  client.createChannel("messaging", "", memberIds, extraData);
                            newChannel.enqueue(result -> {
                                if(result.isSuccess()){
                                    Channel newChannel1 = result.data();
                                    activity.startActivity(ChannelActivity.newIntent(activity, newChannel1));
                                }else{
                                    // Handle result.error()
                                    System.out.println("MESSAGE ERR " + result.error());
                                }
                            });



                            client.connectUser(user, client.devToken(user.getId())).enqueue((result) -> {
                                if (result.isSuccess()) {
                                    // Handle success
                                    System.out.println("USER CONNECTED ):");
                                } else {
                                    // Handler error
                                    System.out.println("USER DIS-CONNECTED ):");
                                }
                            });

                        } else {

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        System.out.println("CONNECTION DRIVER : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(activity, "CONNECTION FAILED", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("CONNECTION DRIVER : " + t.getMessage());
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