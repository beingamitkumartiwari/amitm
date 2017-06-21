package com.getfreerecharge.mpaisafreerecharge.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppConstants;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by DEVEN SINGH on 10/30/2015.
 */
public class Dfragment extends DialogFragment {

    NetworkImageView iv_campaign;
    TextView tv_appname;
    TextView tv_description;
    Button bt_earn;
    ImageView bt_close;
    String appname;
    String desciption;
    String imageurl;
    String playStoreLink;
    String payout;
    String campaignType;

//    CustomProgressDialog customProgressDialog;

    @SuppressLint("ValidFragment")
    public Dfragment(){

    }
    @SuppressLint("ValidFragment")
    public Dfragment(final String appname, String desciption, String imageurl, final String playStoreLink, final String payout, final String campaignType){
          this.appname=appname;
          this.desciption=desciption;
          this.imageurl=imageurl;
          this.playStoreLink=playStoreLink;
          this.payout=payout;
          this.campaignType=campaignType;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.campaign_dialog, container, false);

        iv_campaign = (NetworkImageView) rootView.findViewById(R.id.networkimage_campaignimage);
        tv_appname = (TextView) rootView.findViewById(R.id.textview_campainappname);
        tv_description = (TextView) rootView.findViewById(R.id.textview_campaigndesc);
        bt_earn = (Button) rootView.findViewById(R.id.button_earncampaign);
        bt_close = (ImageView) rootView.findViewById(R.id.button_campaignclose);
        iv_campaign.setImageUrl(imageurl, ApplicationFL.getInstance().getImageLoader());
        tv_appname.setText(appname);
        tv_description.setText(desciption);
        bt_earn.setText(payout);
        bt_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                String pay=payout.replaceAll("\\D+","");
                String clickid = String.valueOf(currentTimeMillis);
                String subid2 = AppConstants.getImeiNum(getActivity()) + "mtech" + String.valueOf(currentTimeMillis) + "Appname" + "mpa" + "Payout"+ pay + "Campaigntype" +campaignType;
                sendClickidForCampaign(clickid, subid2, playStoreLink,pay);
//                customProgressDialog.show();
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
//
        return rootView;
    }

    private void sendClickidForCampaign(final String clickid, final String subid2, final String playStoreLink,final String pay) {
        String url =(new AppUrls(getActivity(), new MyPrefs(getActivity())).getHostName(18));
        url = url + "deviceid=" + AppConstants.getImeiNum(getActivity()) + "&appname=" + "mpa" + "Payout" +pay+"Campaigntype" + campaignType;
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                getDialog().dismiss();
                String link = playStoreLink + clickid + "/?sub_id2=" + subid2;
                Intent campaignApps = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(campaignApps);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (customProgressDialog != null)
//                    customProgressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in loading offer.Try again", Toast.LENGTH_SHORT).show();
            }
        });
        customJsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        ApplicationFL.getInstance().addToRequestQueue(customJsonObjectRequest);
    }
}
