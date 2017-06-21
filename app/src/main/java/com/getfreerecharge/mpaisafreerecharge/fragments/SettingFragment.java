package com.getfreerecharge.mpaisafreerecharge.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.app_tour.AppTourPagerActivity;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;

/**
 * Created by DEVEN SINGH on 7/16/2015.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    ImageButton feedbackButton;
    ImageButton shareButton;
    ImageButton rateUs;
    ImageButton tourButton;
    ImageButton faqButton;
    ImageButton aboutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
    }

    private void initComponents(View view) {
        feedbackButton = (ImageButton) view.findViewById(R.id.feedback);
        shareButton = (ImageButton) view.findViewById(R.id.share_app);
        rateUs = (ImageButton) view.findViewById(R.id.rate_us);
        tourButton = (ImageButton) view.findViewById(R.id.appTour);
        faqButton = (ImageButton) view.findViewById(R.id.faq);
        aboutButton = (ImageButton) view.findViewById(R.id.about_us);
        feedbackButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        rateUs.setOnClickListener(this);
        tourButton.setOnClickListener(this);
        faqButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == feedbackButton) {
            sendFeedback();
        }
        if (v == shareButton) {
            shareApp();
        }
        if (v == rateUs) {
            Intent intentPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.getfreerecharge.mpaisafreerecharge"));
            startActivity(intentPage);
        }
        if (v == tourButton) {
            Intent intent = new Intent(getActivity(), AppTourPagerActivity.class);
            intent.putExtra("tourKey", "Setting");
            startActivity(intent);
        }
        if (v == faqButton) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(new AppUrls(getActivity(),new MyPrefs(getActivity())).getHostName(13))));
        }
        if (v == aboutButton) {
            dialogAboutUs();
        }
    }

    private void dialogAboutUs() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        Button exitButton = (Button) dialog.findViewById(R.id.exitAboutBt);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void sendFeedback() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL,
                new String[]{"support@mpaisa.info"});
        email.putExtra(Intent.EXTRA_SUBJECT, "mPaisa: Free Recharge Feedback");
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email,
                "Choose an Email client:"));
    }

    private void shareApp() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "This application is awesome you should try it." + "\n" + "https://play.google.com/store/apps/details?id=com.getfreerecharge.mpaisafreerecharge";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
