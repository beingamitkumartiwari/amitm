package com.getfreerecharge.mpaisafreerecharge.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DEVEN SINGH on 10/7/2015.
 */
public class LuckyDrawWinner extends Fragment implements View.OnClickListener {

    private Button installParticipate;
    private NetworkImageView appImage;
    private TextView luckyWinner;
    private TextView winnerPrizeDetails;
    private ProgressBar progressBarLuckyDraw;
    private MyPrefs myPrefs;
    String appLink;
    boolean isShown = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lucky_draw, container, false);
        initComponents(rootView);
        if (isShown) {
            ApplicationFL.getInstance().addToRequestQueue(requestLuckyDrawData(new AppUrls(getActivity(), myPrefs).getHostName(17)), "LuckyDraw");
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            isShown = true;
            ApplicationFL.getInstance().addToRequestQueue(requestLuckyDrawData(new AppUrls(getActivity(), myPrefs).getHostName(17)), "LuckyDraw");
        } else {
            isShown = false;
        }
    }

    private void initComponents(View rootView) {
        myPrefs = new MyPrefs(getActivity());
        progressBarLuckyDraw = (ProgressBar) rootView.findViewById(R.id.lucky_draw_progress);
        installParticipate = (Button) rootView.findViewById(R.id.install_participate);
        appImage = (NetworkImageView) rootView.findViewById(R.id.lucky_draw_img);
        luckyWinner = (TextView) rootView.findViewById(R.id.lucky_winner);
        winnerPrizeDetails = (TextView) rootView.findViewById(R.id.winner_prize_details);
        installParticipate.setOnClickListener(this);
        if (myPrefs.isParticipated()) {
            installParticipate.setText("Already Participated");
            installParticipate.setEnabled(false);
        }
    }

    private CustomJsonObjectRequest requestLuckyDrawData(String url) {
        CustomJsonObjectRequest customJsonArrayRequest = new CustomJsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("luckyDraw", response.toString());
                if (progressBarLuckyDraw != null)
                    progressBarLuckyDraw.setVisibility(View.GONE);
                try {
                    if (response.getString("responseCode").equalsIgnoreCase("1")) {
                        appLink = response.getString("appurl");
                        luckyWinner.setText("\"" + response.getString("WinnerName") + "\"");
                        winnerPrizeDetails.setText(response.getString("PrizeDetail"));
                        appImage.setImageUrl(response.getString("iconurl"), ApplicationFL.getInstance().getImageLoader());
                        if (Integer.parseInt(response.getString("CurrentDate")) > myPrefs.getLuckyDrawDate()) {
                            myPrefs.setIsParticipated(false);
                            installParticipate.setEnabled(true);
                            myPrefs.setLuckyDrawDate(Integer.parseInt(response.getString("CurrentDate")));
                            myPrefs.setLuckyDrawAppPackage(response.getString("packagename"));
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("luckyDraw", error.toString());
                if (progressBarLuckyDraw != null)
                    progressBarLuckyDraw.setVisibility(View.GONE);
            }
        });
        customJsonArrayRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        return customJsonArrayRequest;
    }


    @Override
    public void onClick(View v) {
        Intent drawApps = new Intent(Intent.ACTION_VIEW, Uri.parse(appLink));
        startActivity(drawApps);
    }
}
