package com.blueconnectionz.nicenice.owner.profile;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blueconnectionz.nicenice.R;
import com.blueconnectionz.nicenice.driver.entry.LandingPage;
import com.blueconnectionz.nicenice.driver.profile.pages.ChangePassword;
import com.blueconnectionz.nicenice.driver.profile.pages.CreditActivity;
import com.blueconnectionz.nicenice.driver.profile.pages.ProfileInformation;
import com.blueconnectionz.nicenice.owner.OwnerMainActivity;
import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.net.URLEncoder;

public class OwnerProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.owner_fragment_profile, container, false);
        MaterialCardView editProfile = root.findViewById(R.id.profileCardView);
        editProfile.setOnClickListener(view -> startActivity(new Intent(getContext(), ProfileInformation.class)));

        MaterialCardView changePassword = root.findViewById(R.id.changePasswordCardView);
        changePassword.setOnClickListener(view -> startActivity(new Intent(getContext(), ChangePassword.class)));

        MaterialCardView loadCredits = root.findViewById(R.id.creditsCardView);
        loadCredits.setOnClickListener(view -> startActivity(new Intent(getContext(), CreditActivity.class)));

        MaterialCardView terms = root.findViewById(R.id.termsCardView);
        terms.setOnClickListener(view ->
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
                                .show()));

        MaterialCardView helpCenter = root.findViewById(R.id.feedBack);
        helpCenter.setOnClickListener(view -> openWhatsApp());


        MaterialCardView logOut = root.findViewById(R.id.logOutUser);
        logOut.setOnClickListener(view -> openLogoutView());



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void openWhatsApp(){
        try{
            String number = "+27 0732811089";
            String message = "Hello support@blueconnectionz";
            PackageManager packageManager = getActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ number +"&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                Snackbar.make(getView(),"No WhatsApp",Snackbar.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            Log.e("ERROR WHATSAPP",e.toString());
            Snackbar.make(getView(),"No WhatsApp",Snackbar.LENGTH_LONG).show();
        }
    }

    private void openLogoutView(){
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

    private void openCreditsView(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PopupDialog.getInstance(getContext())
                        .setStyle(Styles.ALERT)
                        .setHeading("Pending")
                        .setDescription("This feature is not yet available")
                        .setCancelable(false)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                            }
                        });
            }
        });
    }
}