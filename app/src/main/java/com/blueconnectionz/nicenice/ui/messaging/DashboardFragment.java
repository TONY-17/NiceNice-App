package com.blueconnectionz.nicenice.ui.messaging;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.databinding.FragmentDashboardBinding;
import com.blueconnectionz.nicenice.recyclerviews.msgs.MsgAdapter;
import com.blueconnectionz.nicenice.recyclerviews.msgs.MsgItem;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textDashboard;
        final RecyclerView recyclerView = binding.messagesList;
        MsgAdapter msgAdapter = new MsgAdapter(messages(),getActivity());
        recyclerView.setAdapter(msgAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        return root;
    }

    private List<MsgItem> messages(){
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
                "Unfortunately that's too small",
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