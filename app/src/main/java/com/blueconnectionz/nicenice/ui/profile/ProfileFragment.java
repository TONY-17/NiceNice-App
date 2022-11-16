package com.blueconnectionz.nicenice.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.ui.profile.pages.ChangePassword;
import com.blueconnectionz.nicenice.ui.profile.pages.ProfileInformation;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        MaterialCardView editProfile = root.findViewById(R.id.profileCardView);
        editProfile.setOnClickListener(view -> startActivity(new Intent(getContext(), ProfileInformation.class)));

        MaterialCardView changePassword = root.findViewById(R.id.changePasswordCardView);
        changePassword.setOnClickListener(view -> startActivity(new Intent(getContext(), ChangePassword.class)));

        MaterialCardView loadCredits = root.findViewById(R.id.creditsCardView);
        loadCredits.setOnClickListener(view -> startActivity(new Intent(getContext(),ProfileInformation.class)));

        MaterialCardView terms = root.findViewById(R.id.termsCardView);
        terms.setOnClickListener(view -> startActivity(new Intent(getContext(),ProfileInformation.class)));

        MaterialCardView logOut = root.findViewById(R.id.logOutUser);
        logOut.setOnClickListener(view -> startActivity(new Intent(getContext(),ProfileInformation.class)));
        return root;
    }
}