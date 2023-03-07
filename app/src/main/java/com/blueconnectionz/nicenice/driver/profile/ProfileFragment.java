package com.blueconnectionz.nicenice.driver.profile;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.blueconnectionz.nicenice.CustomWebView;
import com.blueconnectionz.nicenice.MainActivity;
import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.driver.profile.pages.ChangePassword;
import com.blueconnectionz.nicenice.driver.profile.pages.CreditActivity;
import com.blueconnectionz.nicenice.driver.profile.pages.ProfileInformation;
import com.blueconnectionz.nicenice.network.RetrofitClient;
import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    // Indicates in the opened activity that a driver opened the page
    public static boolean isDriver = false;
    SwitchMaterial switchMaterial;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        switchMaterial = root.findViewById(R.id.switchMaterial);
        avLoadingIndicatorView = root.findViewById(R.id.avi);
        checkAvailabilityStatus();
        // Edit profile page
        root.findViewById(R.id.profileCardView).setOnClickListener(view -> {
            isDriver = true;
            startActivity(new Intent(getContext(), ProfileInformation.class));
        });
        // Allows a user to update their password
        root.findViewById(R.id.changePasswordCardView).setOnClickListener(view -> {
            isDriver = true;
            startActivity(new Intent(getContext(), ChangePassword.class));
        });
        // Opens the loading credit page
        root.findViewById(R.id.creditsCardView).setOnClickListener(view -> startActivity(new Intent(getContext(), CreditActivity.class)));
        // Opens the Terms and Conditions Dialog
        root.findViewById(R.id.termsCardView).setOnClickListener(view -> openTermsDialog());
        // Opens WhatsApp Help center
        root.findViewById(R.id.feedBack).setOnClickListener(view -> openWhatsApp());
        // Logs out the user
        root.findViewById(R.id.logOutUser).setOnClickListener(view -> openLogoutView());

        // Go on or offline
        switchMaterial.setOnCheckedChangeListener((compoundButton, b) -> {
            Call<ResponseBody> changeStatus = RetrofitClient.getRetrofitClient().getAPI().changeStatus(LandingPage.userID,
                    b);
            changeStatus.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){

                    }else{

                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        });


        return root;
    }

    private void openTermsDialog() {
        isDriver = true;
        getActivity().runOnUiThread(() ->
                new DroidDialog.Builder(getContext())
                        .icon(R.drawable.ic_outline_library_books_24)
                        .title("Terms & Conditions")
                        .animation(AnimUtils.AnimFadeInOut)
                        .content(getString(R.string.termsAndConditions)).color(ContextCompat.getColor(getContext(), R.color.green),
                                ContextCompat.getColor(getContext(), R.color.white),
                                ContextCompat.getColor(getContext(), R.color.green))
                        .cancelable(true, true)
                        .positiveButton("OK", droidDialog -> droidDialog.dismiss())
                        .show());
    }

    private void openWhatsApp() {
        try {
            isDriver = true;
            String number = "+27 0732811089";
            String message = "Hello support@blueconnectionz";
            PackageManager packageManager = getActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            } else {
                Snackbar.make(getView(), "No WhatsApp", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("ERROR WHATSAPP", e.toString());
            Snackbar.make(getView(), "No WhatsApp", Snackbar.LENGTH_LONG).show();
        }
    }

    private void openLogoutView() {
        isDriver = true;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PopupDialog.getInstance(getContext())
                        .setStyle(Styles.STANDARD)
                        .setHeading("Logout")
                        .setDescription("Are you sure you want to logout?")
                        .setPopupDialogIcon(R.drawable.ic_outline_logout_24)
                        .setPopupDialogIconTint(R.color.red)
                        .setCancelable(false)
                        .setPositiveButtonBackground(com.saadahmedsoft.popupdialog.R.drawable.bg_red_10)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveClicked(Dialog dialog) {
                                super.onPositiveClicked(dialog);
                                startActivity(new Intent(getContext(), LandingPage.class));
                                getActivity().finish();
                            }

                            @Override
                            public void onNegativeClicked(Dialog dialog) {
                                super.onNegativeClicked(dialog);
                            }
                        });
            }
        });
    }

    private void checkAvailabilityStatus() {
        Call<ResponseBody> isAvailable = RetrofitClient.getRetrofitClient().getAPI().isAvailable(LandingPage.userID);
        isAvailable.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        System.out.println("PROFILE INFO " + data);
                        if (data.contains("false")) {
                            getActivity().runOnUiThread(() -> {
                                switchMaterial.setVisibility(View.VISIBLE);
                                switchMaterial.setChecked(false);
                                avLoadingIndicatorView.setVisibility(View.GONE);
                            });

                        } else if (data.contains("true")) {
                            getActivity().runOnUiThread(() -> {
                                switchMaterial.setVisibility(View.VISIBLE);
                                switchMaterial.setChecked(true);

                                avLoadingIndicatorView.setVisibility(View.GONE);
                            });
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    getActivity().runOnUiThread(() -> {
                        switchMaterial.setVisibility(View.VISIBLE);
                        switchMaterial.setEnabled(false);

                        avLoadingIndicatorView.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getActivity().runOnUiThread(() -> {
                    switchMaterial.setVisibility(View.VISIBLE);
                    switchMaterial.setEnabled(false);

                    avLoadingIndicatorView.setVisibility(View.GONE);
                });
            }
        });
    }

}