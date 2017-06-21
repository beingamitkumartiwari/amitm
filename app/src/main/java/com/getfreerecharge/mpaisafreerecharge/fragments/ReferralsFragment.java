package com.getfreerecharge.mpaisafreerecharge.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;


/**
 * Created by DEVEN SINGH on 7/1/2015.
 */
public class ReferralsFragment extends Fragment implements View.OnClickListener {

    private TextView refCodeTv;
    private MyPrefs myPrefs;
    private ImageView refShareMsg;
    private ImageView refShareWhatsApp;
    private ImageView refShareOthers;
    private LinearLayout llRef;
    private TextView refNotYet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_referrals, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
    }

    private void initComponents(View view) {
        myPrefs = new MyPrefs(getActivity());
        llRef = (LinearLayout) view.findViewById(R.id.llRef);
        refCodeTv = (TextView) view.findViewById(R.id.myRefCode);
        refShareMsg = (ImageView) view.findViewById(R.id.refShareMsg);
        refShareWhatsApp = (ImageView) view.findViewById(R.id.refShareWhatsApp);
        refShareOthers = (ImageView) view.findViewById(R.id.refShareOthers);
        refNotYet = (TextView) view.findViewById(R.id.refCodeNotYet);
        refShareMsg.setOnClickListener(this);
        refShareWhatsApp.setOnClickListener(this);
        refShareOthers.setOnClickListener(this);
        if (myPrefs.isFirstOfferCompleted()) {
            updateUI();
        } else {
            if (Float.parseFloat(myPrefs.getWalletBalance()) > 0) {
                updateUI();
                myPrefs.setFirstOfferCompleted(true);
            }
        }
    }

    private void updateUI() {
        llRef.setVisibility(View.VISIBLE);
        refNotYet.setVisibility(View.GONE);
        refCodeTv.setText(myPrefs.getReferralCode());
    }

    @Override
    public void onClick(View v) {
        if (v == refShareMsg) {
            shareWithSmsApp(myPrefs.getReferralCode());
        }
        if (v == refShareWhatsApp) {
            shareWithWhatsApp(myPrefs.getReferralCode());
        }
        if (v == refShareOthers) {
            shareWithCreateChooser(myPrefs.getReferralCode());
        }
    }

    private void shareWithSmsApp(String referralCode) {
        String sharingText = "This is awesome app to get free recharge  https://play.google.com/store/apps/details?id=com.getfreerecharge.mpaisafreerecharge \nyou should try it. Use my referral code " + "\"" + referralCode + "\"" + " to get extra 5 Rs. on first offer completion.";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity());
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
            if (defaultSmsPackageName != null) {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            getActivity().startActivity(sendIntent);
        } else {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.putExtra("sms_body", sharingText);
            sendIntent.setType("vnd.android-dir/mms-sms");
            getActivity().startActivity(sendIntent);
        }
    }

    private void shareWithWhatsApp(String referralCode) {
        PackageManager pm = getActivity().getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String sharingText = "This is awesome app to get free recharge  https://play.google.com/store/apps/details?id=com.getfreerecharge.mpaisafreerecharge \nyou should try it. Use my referral code " + "\"" + referralCode + "\"" + " to get extra 5 Rs. on first offer completion.";
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void shareWithCreateChooser(String referralCode) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String sharingText = "This is awesome app to get free recharge  https://play.google.com/store/apps/details?id=com.getfreerecharge.mpaisafreerecharge \nyou should try it. Use my referral code " + "\"" + referralCode + "\"" + " to get extra 5 Rs. on first offer completion.";
        intent.putExtra(Intent.EXTRA_TEXT, sharingText);
        startActivity(Intent.createChooser(intent, "Share with"));
    }


}
