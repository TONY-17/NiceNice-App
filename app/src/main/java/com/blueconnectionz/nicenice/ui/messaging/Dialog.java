package com.blueconnectionz.nicenice.ui.messaging;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.databinding.FragmentDashboardBinding;
import com.blueconnectionz.nicenice.recyclerviews.chat.MsgAdapter;
import com.blueconnectionz.nicenice.recyclerviews.chat.MsgItem;

import java.util.ArrayList;
import java.util.List;

public class Dialog extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textDashboard;
        final RecyclerView recyclerView = binding.messagesList;
        MsgAdapter msgAdapter = new MsgAdapter(messages(), getActivity());
        recyclerView.setAdapter(msgAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


        // Filter the displayed messages by different status's
        final ImageView filterMessages = binding.filterMessages;
        filterMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(getContext(), filterMessages);
                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        Toast.makeText(getContext(), "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        return root;
    }

    private List<MsgItem> messages() {
        List<MsgItem> m1 = new ArrayList<>();
        m1.add(new MsgItem(
                "Tshego Pilusa",
                "Unfortunately that's too small",
                "4pm",
                0,
                true,
                false,
                false));

        m1.add(new MsgItem(
                "Tshego Pilusa",
                "Unfortunately that's too small",
                "4pm",
                0,
                false,
                true,
                false));

        m1.add(new MsgItem(
                "Tshego Pilusa",
                "Unfortunately that's too small",
                "4pm",
                0,
                false,
                false,
                true));

        m1.add(new MsgItem(
                "Tshego Pilusa",
                "I will take the offer I will take the offer I will take the offer ",
                "4pm",
                0,
                false,
                false,
                false));

        m1.add(new MsgItem(
                "Tshego Pilusa",
                "Unfortunately that's too small",
                "4pm",
                0,
                false,
                true,
                false));

        m1.add(new MsgItem(
                "Tshego Pilusa",
                "Unfortunately that's too small",
                "4pm",
                0,
                false,
                true,
                false));
        m1.add(new MsgItem(
                "Tshego Pilusa",
                "Unfortunately that's too small",
                "4pm",
                0,
                false,
                true,
                false));
        m1.add(new MsgItem(
                "Tshego Pilusa",
                "Unfortunately that's too small",
                "4pm",
                0,
                false,
                true,
                false));
        m1.add(new MsgItem(
                "Tshego Pilusa",
                "Unfortunately that's too small",
                "4pm",
                0,
                false,
                true,
                false));
        m1.add(new MsgItem(
                "Tshego Pilusa",
                "Unfortunately that's too small",
                "4pm",
                0,
                true,
                false,
                false));

        return m1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}