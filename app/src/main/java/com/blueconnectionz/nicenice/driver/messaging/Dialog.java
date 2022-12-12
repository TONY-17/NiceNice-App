package com.blueconnectionz.nicenice.driver.messaging;

import static java.util.Collections.singletonList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.blueconnectionz.nicenice.ChannelActivity;
import com.blueconnectionz.nicenice.MyApp;
import com.blueconnectionz.nicenice.databinding.FragmentDashboardBinding;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.owner.dashboard.ChatActivity;

import io.getstream.chat.android.client.ChatClient;
import io.getstream.chat.android.client.api.models.FilterObject;
import io.getstream.chat.android.client.logger.ChatLogLevel;
import io.getstream.chat.android.client.models.Filters;
import io.getstream.chat.android.client.models.User;
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType;
import io.getstream.chat.android.offline.plugin.configuration.Config;
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory;
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel;
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModelBinding;
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory;

public class Dialog extends Fragment {

    FragmentDashboardBinding binding = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Step 2 - Set up the client for API calls with the plugin for offline storage
        ChatClient client = MyApp.client;

        // Step 3 - Authenticate and connect the user
        User user = new User();
        // An email will be used an ID so the user can recieve messages
        String email = LandingPage.userEmail.toLowerCase();
        int atIndex = email.indexOf('@');
        user.setId(email.replaceAll("[.]", ""));
        // Only the first portion of the email will be the display name
        user.setName(email.substring(0, atIndex));
        user.setImage("https://bit.ly/2TIt8NR");

        client.connectUser(
                user,
                client.devToken(user.getId())
        ).enqueue();

        // Step 4 - Set the channel list filter and order
        // This can be read as requiring only channels whose "type" is "messaging" AND
        // whose "members" include our "user.id"
        FilterObject filter = Filters.and(
                Filters.eq("type", "messaging"),
                Filters.in("members", singletonList(user.getId()))
        );

        ViewModelProvider.Factory factory = new ChannelListViewModelFactory.Builder()
                .filter(filter)
                .sort(ChannelListViewModel.DEFAULT_SORT)
                .build();

        ChannelListViewModel channelsViewModel =
                new ViewModelProvider(this, factory).get(ChannelListViewModel.class);

        // Step 5 - Connect the ChannelListViewModel to the ChannelListView, loose
        //          coupling makes it easy to customize
        ChannelListViewModelBinding.bind(channelsViewModel, binding.channelListView, this);
        binding.channelListView.setChannelItemClickListener(channel -> {
            // TODO - start channel activity
            startActivity(ChannelActivity.newIntent(getContext(), channel));
        });




        return root;
    }

}
