package com.blueconnectionz.nicenice.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.databinding.FragmentHomeBinding;
import com.blueconnectionz.nicenice.recyclerviews.discover.HomeAdapter;
import com.blueconnectionz.nicenice.recyclerviews.discover.HomeItem;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // View bottom - top animation
        final Animation[] animation = new Animation[1];
        animation[0] = AnimationUtils.loadAnimation(getContext(),
                R.anim.bottom_to_original);

        final RecyclerView recyclerView = binding.carsListRV;
        final View view = binding.view7;
        final MaterialCardView filter = binding.filterCardView;
        filter.setAnimation(animation[0]);
        final MaterialCardView search = binding.searchCardView;
        HomeAdapter homeAdapter = new HomeAdapter(populateData());
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setOnScrollChangeListener((view1, i, i1, i2, i3) -> {
            float scrollPercentage = recyclerViewScrollPercentage(recyclerView);
            if(scrollPercentage >= 10){
                getActivity().runOnUiThread(() -> {
                    //view.setVisibility(View.VISIBLE);
                    filter.setVisibility(View.GONE);
                   /* search.setStrokeColor(getResources().getColor(R.color.card_stroke,null));
                    search.setStrokeWidth(2);*/
                    search.setElevation(8f);
                });
            }else{
                getActivity().runOnUiThread(() -> {
                   // view.setVisibility(View.GONE);
                    filter.setVisibility(View.VISIBLE);
/*                    search.setStrokeColor(getResources().getColor(R.color.card_stroke,null));
                    search.setStrokeWidth(0);*/
                    search.setElevation(0f);
                });
            }
        });
        return root;
    }

    private List<HomeItem> populateData() {
        String image1 = "https://images.pexels.com/photos/170811/pexels-photo-170811.jpeg?auto=compress&cs=tinysrgb&w=600";
        List<HomeItem> data = new ArrayList<>();
        data.add(new HomeItem(image1, "Tony", "Toyota", "Tembisa", "R1280", true));
        data.add(new HomeItem(image1, "Kamo", "Suzuki", "Soweto", "R140", true));
        data.add(new HomeItem(image1, "Eazy", "Suzuki", "Soweto", "R240", true));
        data.add(new HomeItem(image1, "Katlego", "Renault", "Tembisa", "R500", true));
        return data;
    }


    private float recyclerViewScrollPercentage(RecyclerView recyclerView){
        int offset = recyclerView.computeVerticalScrollOffset();
        int extent = recyclerView.computeVerticalScrollExtent();
        int range = recyclerView.computeVerticalScrollRange();
        float percentage = (100.0f * offset / (float)(range - extent));
        return  percentage;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}