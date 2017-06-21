package com.getfreerecharge.mpaisafreerecharge.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.activities.MainActivity;
import com.getfreerecharge.mpaisafreerecharge.activities.TransparentActivity;
import com.getfreerecharge.mpaisafreerecharge.adapters.ExpletusAdapter;
import com.getfreerecharge.mpaisafreerecharge.adapters.MinimobAdAdapter;
import com.getfreerecharge.mpaisafreerecharge.adapters.NativeAppAdapter;
import com.getfreerecharge.mpaisafreerecharge.adapters.NativeAppPojo;
import com.getfreerecharge.mpaisafreerecharge.adapters.OnItemButtonClickListner;
import com.getfreerecharge.mpaisafreerecharge.adapters.OnListItemClickListner;
import com.getfreerecharge.mpaisafreerecharge.pojos.Pojo_availableoffer;
import com.getfreerecharge.mpaisafreerecharge.pojos.SurveyCampaignPojo;
import com.getfreerecharge.mpaisafreerecharge.storage.MpFr_Database;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.NotificationAlert;
import com.getfreerecharge.mpaisafreerecharge.utils.ScrollingListView;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppConstants;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;
import com.getfreerecharge.mpaisafreerecharge.volley_works.ConnectionCheck;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonArrayRequest;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonObjectRequest;
import com.getfreerecharge.mpaisafreerecharge.woobi_work.MyEventListener;
import com.github.glomadrian.loadingballs.BallView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.nativex.monetization.MonetizationManager;
import com.nativex.monetization.business.reward.Reward;
import com.nativex.monetization.communication.RedeemRewardData;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.NativeXAdPlacement;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.listeners.RewardListener;
import com.nativex.monetization.listeners.SessionListener;
import com.nativex.monetization.mraid.AdInfo;
import com.supersonic.adapters.supersonicads.SupersonicConfig;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.sdk.OfferwallListener;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.woobi.Woobi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by DEVEN SINGH on 7/1/2015.
 */
public class OffersFragment extends Fragment implements View.OnClickListener, OnListItemClickListner, OfferwallListener, OnItemButtonClickListner {

    String TAG_INSERT_MONEY = "requestAddMoney";
    String TAG_JSON_MINIMOB = "requestOffersMinimob";
    String TAG_NATIVE = "nativeApps";
    TextView tvAdmobPayout;
    Button btAdmobFullpage;
    private CardView cvAdmob;
    private LinearLayout llSuperSonic;
    private LinearLayout llNativeX;
    private LinearLayout ll_minimob;
    private LinearLayout llNativeApps;
    private LinearLayout llWoobi;
    private ArrayList<SurveyCampaignPojo> listOfOffersExpletus;
    private ArrayList<Pojo_availableoffer> listOfOffersMinimob;
    private ArrayList<NativeAppPojo> nativeAppPojoArrayList;
    private ListView listViewMinimob;
    private ListView expletusListview;
    private ListView nativeAppListView;
    private ImageView iv_arrowminimob;
    private ImageView ivArrowNativeApps;
    private boolean showlistminimob = false;
    private boolean isNativeAppListShowing = false;
    private boolean offersminimob = false;
    private Dialog dialog;
    private MinimobAdAdapter adapterMinimob;
    //    private ProgressBar progressBarExpletus;
    private ProgressBar progressBar;
    private ProgressBar progressBar_woobi;
    private ProgressBar progressBar5;
    private MyPrefs myPrefs;

    boolean appReady = false;
    private Supersonic mMediationAgent;
    private Handler handler = new Handler();
    private MpFr_Database mpFr_database;

    /*woobi works */
    private static String myAppIdWoobi = "17056";
    private static final boolean VERBOSEWOOBI = true;
    private MyEventListener woobiEventlistener;

    /*woobi works*/
    FragmentManager fm;
    Handler handlerCampaign;
    int numbers[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private InterstitialAd interstitialAd1;
    AdRequest adRequest1;
    BallView ballanimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offers, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
        getConversionRate();
        initSuperSonicAds();
        initWoobi();
        initAds();
//        minimobOffersAccess();
        getGoogleAdId();
//        ApplicationFL.getInstance().addToRequestQueue(requestCampaignAppsData(new AppUrls(getActivity(),myPrefs).getHostName(16)),"CampaignAds");

    }

    private void getGoogleAdId() {
        new TaskGoogleAdId(getActivity()).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMediationAgent.setOfferwallListener(this);
        mMediationAgent.getOfferwallCredits();
        if (mMediationAgent != null) {
            mMediationAgent.onResume(getActivity());
        }
        MonetizationManager.createSession(getActivity(), myPrefs.getNativexId(), sessionListener);
        MonetizationManager.setRewardListener(rewardListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediationAgent != null) {
            mMediationAgent.onPause(getActivity());
        }
        if (mNativeXLoadSession != null) {
            handler.removeCallbacks(mNativeXLoadSession);
        }
        if (handlerCampaign != null) {
            handlerCampaign.removeCallbacks(mRunnable);
        }
    }

    private void initSuperSonicAds() {
        mMediationAgent = SupersonicFactory.getInstance();
        String mAppKey = myPrefs.getSupersonicId();
        SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
        mMediationAgent.initOfferwall(getActivity(), mAppKey, AppConstants.getImeiNum(getActivity()));
    }

    private void initWoobi() {
        Woobi.staging = false;
        Woobi.verbose = VERBOSEWOOBI;
        woobiEventlistener = new MyEventListener(getActivity());
        Woobi.init(getActivity(), myPrefs.getWoobiId(), woobiEventlistener);
    }

    private void initAds() {
        interstitialAd1 = new InterstitialAd(getActivity());
        interstitialAd1.setAdUnitId(myPrefs.getAdmobFullPageId());
        adRequest1 = new AdRequest.Builder().build();
    }

    private void initComponents(View view) {
        myPrefs = new MyPrefs(getActivity());
        mpFr_database = new MpFr_Database(getActivity());
        fm = getActivity().getSupportFragmentManager();
        tvAdmobPayout = (TextView) view.findViewById(R.id.payout_admob);
        btAdmobFullpage = (Button) view.findViewById(R.id.button_admobfullpage);
        ballanimation = (BallView) view.findViewById(R.id.animationball);
//        progressBarExpletus = (ProgressBar) view.findViewById(R.id.progressBarExpletus);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarOffer1);
        progressBar_woobi = (ProgressBar) view.findViewById(R.id.progressBarOffer_woobi);
        progressBar5 = (ProgressBar) view.findViewById(R.id.progressBarOffer5);
        cvAdmob = (CardView) view.findViewById(R.id.cvAdmob);
        llSuperSonic = (LinearLayout) view.findViewById(R.id.ll_superSonic);
        llNativeX = (LinearLayout) view.findViewById(R.id.ll_nativX);
        ll_minimob = (LinearLayout) view.findViewById(R.id.ll_minimob);
        llNativeApps = (LinearLayout) view.findViewById(R.id.ll_native_apps);
        llWoobi = (LinearLayout) view.findViewById(R.id.ll_woobi);
        iv_arrowminimob = (ImageView) view.findViewById(R.id.imageview_arrow);
        ivArrowNativeApps = (ImageView) view.findViewById(R.id.imageview_arrow_native_apps);
        listViewMinimob = (ListView) view.findViewById(R.id.listview_available_offers);
        expletusListview = (ListView) view.findViewById(R.id.listviewExpletus);
        nativeAppListView = (ListView) view.findViewById(R.id.native_app_list);
        listOfOffersExpletus = new ArrayList<SurveyCampaignPojo>();
        listOfOffersMinimob = new ArrayList<Pojo_availableoffer>();
        nativeAppPojoArrayList = new ArrayList<NativeAppPojo>();
        llSuperSonic.setOnClickListener(this);
        llNativeX.setOnClickListener(this);
        ll_minimob.setOnClickListener(this);
        llNativeApps.setOnClickListener(this);
        llWoobi.setOnClickListener(this);
        btAdmobFullpage.setOnClickListener(this);
    }

    private void getConversionRate() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceId", AppConstants.getImeiNum(getActivity()));
        CustomJsonObjectRequest conversionRateRequest = new CustomJsonObjectRequest(Request.Method.POST, new AppUrls(getActivity(), myPrefs).getHostName(4), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("responseCode").equals("1")) {
                        myPrefs.setWalletBalance(response.getString("remainingBalance"));
                        myPrefs.setConversionRate(Integer.parseInt(response.getString("conversionRate")));
                        myPrefs.setConversionRatePollfish(response.getString("polfishconversionRate"));
                        myPrefs.setSupersonicId(response.getString("supersonicId"));
                        myPrefs.setWoobiId(response.getString("woobiId"));
                        myPrefs.setNativexId(response.getString("nativeXId"));
                        requestForRemainingOffers(Long.parseLong(response.get("Timestamp").toString()));
                        myPrefs.setShowAdmob(response.get("ShowAd").toString());
                        myPrefs.setAdmobFullPageId(response.get("Addmobid").toString());
                        if (response.getString("verified").equals("1")) {
                            myPrefs.setVerifiedUser(true);
                        } else {
                            myPrefs.setVerifiedUser(false);
                        }
                        if (getActivity() != null) {
                            getActivity().invalidateOptionsMenu();
                        }
                        if (response.getString("showExpletus").equals("1")) {
                            openExpletusOffer();
                        } else {
                            if (myPrefs.getShowAdmob().equals("1")) {
                                cvAdmob.setVisibility(View.VISIBLE);
                            } else {
                                cvAdmob.setVisibility(View.GONE);
                            }
                        }
                        if (response.getString("Successfull").equals("1")) {
                            myPrefs.setShowCampaign(true);
                            myPrefs.setCampaignType(response.getString("CampaignType"));
                            myPrefs.setCampaignImgurl(response.getString("imgUrl"));
                            myPrefs.setCampaignAppname(response.getString("AppName"));
                            myPrefs.setCampaignDescription(response.getString("Description"));
                            myPrefs.setCampaignPlaystoreLink(response.getString("linkurl"));
                            myPrefs.setCampaignPayout(response.getString("payout"));
                            myPrefs.setCampaignPackagename(response.getString("PackageName"));
                            handlerCampaign = new Handler();
                            handlerCampaign.postDelayed(mRunnable, 3000);

                        } else {
                            myPrefs.setShowCampaign(false);
                        }

                    } else {
                        Toast.makeText(getActivity(), "Error! Try again.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error! Try again.", Toast.LENGTH_LONG).show();
                }
//                if (progressBar_woobi != null)
//                    progressBar_woobi.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBar_woobi != null)
                    progressBar_woobi.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Server error! Try again.", Toast.LENGTH_LONG).show();
            }
        });
        conversionRateRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        if (progressBar_woobi != null)
            progressBar_woobi.setVisibility(View.VISIBLE);
        ApplicationFL.getInstance().addToRequestQueue(conversionRateRequest);
    }

    private void openExpletusOffer() {
        if (listOfOffersExpletus.size() == 0) {
//                if (progressBar_woobi != null)
//                    progressBar_woobi.setVisibility(View.VISIBLE);
            ApplicationFL.getInstance().addToRequestQueue(requestExpletusOffer(new AppUrls(getActivity(), myPrefs).getHostName(19)), TAG_NATIVE);
        } else {
            expletusListview.setVisibility(View.VISIBLE);
        }
    }

    private void minimobOffersAccess() {
        if (showlistminimob == false) {
            if (offersminimob == false) {
                if (progressBar != null)
                    progressBar.setVisibility(View.VISIBLE);
                ApplicationFL.getInstance().addToRequestQueue(requestMiniMobAdArray(new AppUrls(getActivity(), myPrefs).getHostName(5)), TAG_JSON_MINIMOB);

            } else {
                iv_arrowminimob.setImageResource(R.mipmap.ic_down);
                listViewMinimob.setVisibility(View.VISIBLE);
                showlistminimob = true;
            }
        } else {
            showlistminimob = false;
            listViewMinimob.setVisibility(View.GONE);
            iv_arrowminimob.setImageResource(R.mipmap.ic_go);
        }
    }

    private void requestForRemainingOffers(long timeStamp) {

        if (timeStamp > myPrefs.getCurrentTimeStamp()) {
            myPrefs.setAdmobFullPageMoney(5);
            myPrefs.setCurrentTimeStamp(timeStamp);
        }
        tvAdmobPayout.setText(String.valueOf(myPrefs.getAdmobFullPageMoney()) + " Rs");

    }

    @Override
    public void onClick(View v) {

        if (v == btAdmobFullpage) {
            myPrefs.setAdClicked(false);
            if (new ConnectionCheck().isConnectionAvailable(getActivity())) {
                offerPopUp("To avail your best offer click Get Offer and click to your favourite ad.\n" +
                        "\n" +
                        "If you fail to do so you won't be get your offer.\"\n" +
                        "You will only get rewards for 5 ads per day in order 5, 4, 3, 2, and 1 Rupees.");
            } else {
                new ConnectionCheck().alertDialog(getActivity());
            }
        }
        if (v == llSuperSonic) {
            mMediationAgent.showOfferwall();
        }
        if (v == llNativeX) {
            progressBar_woobi.setVisibility(View.VISIBLE);
            handler.postDelayed(mNativeXLoadSession, 0);
        }
        if (v == ll_minimob) {
            minimobOffersAccess();
        }
        if (v == llNativeApps) {
            if (!myPrefs.isFirstOfferCompleted()) {
                Toast.makeText(getActivity(), "Complete first offer to unlock this offer section.", Toast.LENGTH_LONG).show();
            } else {
                if (isNativeAppListShowing == false) {
                    if (nativeAppPojoArrayList.size() == 0) {
                        if (progressBar5 != null)
                            progressBar5.setVisibility(View.VISIBLE);
                        ApplicationFL.getInstance().addToRequestQueue(requestNativeAppsData(new AppUrls(getActivity(), myPrefs).getHostName(14)), TAG_NATIVE);
                    } else {
                        ivArrowNativeApps.setImageResource(R.mipmap.ic_down);
                        nativeAppListView.setVisibility(View.VISIBLE);
                        isNativeAppListShowing = true;
                    }
                } else {
                    isNativeAppListShowing = false;
                    nativeAppListView.setVisibility(View.GONE);
                    ivArrowNativeApps.setImageResource(R.mipmap.ic_go);
                }
            }
        }
        if (v == llWoobi) {
            long value = System.currentTimeMillis();
            String myClientId = AppConstants.getImeiNum(getActivity()) + "mtech" + String.valueOf(value);
            Woobi.showOffers(getActivity(), myPrefs.getWoobiId(), myClientId);
            Woobi.setEventListener(woobiEventlistener);
        }
    }

    private void offerPopUp(String message) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Offer");
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Get Offer",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                ballanimation.setVisibility(View.VISIBLE);
                                interstitialAd1.loadAd(adRequest1);
                                addAdmobAdListner1();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(
                    android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();
    }

    private CustomJsonArrayRequest requestMiniMobAdArray(String miniMobUrl) {

        CustomJsonArrayRequest customJsonArrayRequest = new CustomJsonArrayRequest(Request.Method.GET, miniMobUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                iv_arrowminimob.setImageResource(R.mipmap.ic_down);
                listViewMinimob.setVisibility(View.VISIBLE);
                showlistminimob = true;
                offersminimob = true;
                listOfOffersMinimob.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject result = response.getJSONObject(i);
                        Pojo_availableoffer pojo = new Pojo_availableoffer();
                        pojo.setId(result.getString("id").toString());
                        pojo.setAppName(result.getString("name").toString());
                        pojo.setPayOut(convertPayoutIntoCreditsMinimob(result.getString("payout").toString()));
                        pojo.setImageUrl(result.getString("appIconLink").toString());
                        pojo.setPlayStoreLink(result.getString("objectiveUrl").toString());
                        pojo.setAppDescription(result.getString("appDescription").toString());
                        listOfOffersMinimob.add(pojo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterMinimob = new MinimobAdAdapter(getActivity(), listOfOffersMinimob, OffersFragment.this, ApplicationFL.getInstance().getImageLoader());
                listViewMinimob.setAdapter(adapterMinimob);
                ScrollingListView.setListViewHeightBasedOnChildren(listViewMinimob);
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error ", error.toString());
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
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
    public void setOnListItemClickListner_minimob(int position, String playstoreLink) {
        offer_detail_dialog(position);
//        Intent moreapps = new Intent(Intent.ACTION_VIEW, Uri.parse(playstoreLink));
//        startActivity(moreapps);
    }

    private String convertPayoutIntoCreditsMinimob(String value) {
        String pay = null;
        double totalCredits = Double.parseDouble(value) * myPrefs.getConversionRate();
        pay = String.valueOf(round(totalCredits, 2));
        return pay;
    }

    private double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private void addAdmobAdListner1() {
        interstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                ballanimation.setVisibility(View.GONE);
                if (interstitialAd1.isLoaded()) {
                    interstitialAd1.show();
                } else {
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                ballanimation.setVisibility(View.GONE);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();

                if (!myPrefs.isTransparentClosed()) {
                    myPrefs.setTransparentClosed(true);
                    Intent goToA = new Intent(getActivity(), MainActivity.class);
                    goToA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goToA);
                }
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.e("admob ", "leftapp");
                if (!myPrefs.isAdClicked()) {
                    float moneytoadd=((float)myPrefs.getAdmobFullPageMoney()/2);
                    System.out.println("herekamalhalfvalue " + moneytoadd);

                    if (myPrefs.getAdmobFullPageMoney() > 0){
                        requestForAddingBalance(String.valueOf(moneytoadd));
                        myPrefs.setAdClicked(true);
                        myPrefs.setAdmobFullPageMoney(myPrefs.getAdmobFullPageMoney() - 1);
                    }
                    tvAdmobPayout.setText(myPrefs.getAdmobFullPageMoney()+ " Rs");
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                int value = AppConstants.getRandomnumForAd(numbers);
                if (value == 1 || value == 3 || value == 5 || value == 7  || value==9 || value==10 ) {
                    startActivity(new Intent(getActivity(), TransparentActivity.class));
                }

            }
        });
    }

    private void offer_detail_dialog(final int position) {
//        dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.offers_fulldetail_dialog);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//        Window window = dialog.getWindow();
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        NetworkImageView icon = (NetworkImageView) dialog.findViewById(R.id.imageview_offer_fullinfo);
//        icon.setImageUrl(listOfOffersMinimob.get(position).getImageUrl(), ApplicationFL.getInstance().getImageLoader());
//        TextView appname = (TextView) dialog.findViewById(R.id.textview_appname_fullinfo);
//        appname.setText(listOfOffersMinimob.get(position).getAppName());
//        TextView description = (TextView) dialog.findViewById(R.id.textview_littleinfo_fullinfo);
//        description.setText(listOfOffersMinimob.get(position).getAppDescription());
//        TextView payout = (TextView) dialog.findViewById(R.id.textview_payout_fullinfo);
//        Button bt_install = (Button) dialog.findViewById(R.id.button_install_and_open);
//        payout.setText("Earn " + listOfOffersMinimob.get(position).getPayOut() + " Rs");
//        bt_install.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                long currentTimeMillis = System.currentTimeMillis();
//                String clickId = AppConstants.getImeiNum(getActivity()) + "mtech" + String.valueOf(currentTimeMillis) + "payOut" + listOfOffersMinimob.get(position).getPayOut() + "offerName" + listOfOffersMinimob.get(position).getAppName();
//                makeJsonObjectRequest_downloadMinimob(clickId, listOfOffersMinimob.get(position).getPlayStoreLink());
//            }
//        });

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.offers_fulldetail_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        NetworkImageView icon = (NetworkImageView) dialog.findViewById(R.id.imageview_offer_fullinfo);
        icon.setImageUrl(listOfOffersExpletus.get(position).getImageurl(), ApplicationFL.getInstance().getImageLoader());
        TextView appname = (TextView) dialog.findViewById(R.id.textview_appname_fullinfo);
        appname.setText(listOfOffersExpletus.get(position).getAppname());
        TextView description = (TextView) dialog.findViewById(R.id.textview_littleinfo_fullinfo);
        description.setText(listOfOffersExpletus.get(position).getDesciption());
        TextView payout = (TextView) dialog.findViewById(R.id.textview_payout_fullinfo);
        Button bt_install = (Button) dialog.findViewById(R.id.button_install_and_open);
        payout.setText("Earn " + listOfOffersExpletus.get(position).getPayout() + " Rs");
        bt_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                String clickId = AppConstants.getImeiNum(getActivity()) + "mtech" + String.valueOf(currentTimeMillis) + "payOut" + listOfOffersExpletus.get(position).getPayout() + "offerName" + listOfOffersExpletus.get(position).getAppname();
                makeJsonObjectRequest_downloadMinimob(clickId, listOfOffersExpletus.get(position).getPlayStoreLink());
            }
        });
    }


    private void makeJsonObjectRequest_downloadMinimob(final String clickid, final String playstoreLink) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceid", AppConstants.getImeiNum(getActivity()));
        params.put("clickid", clickid);
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, new AppUrls(getActivity(), myPrefs).getHostName(6), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                try {
                    if (response.get("Successfull").equals("1")) {
                        String[] separated = playstoreLink.split("&");
                        String link = "http://clicks.minimob.com/tracking/click?clickid=" + clickid + "&" + separated[1] + "&" + separated[2];
                        Intent moreapps = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(moreapps);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
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

    /**
     * NativeX ads
     */

    Runnable mNativeXLoadSession = new Runnable() {
        @Override
        public void run() {
            if (getActivity() == null)
                return;
            if (appReady) {
                progressBar_woobi.setVisibility(View.GONE);
                MonetizationManager.showReadyAd(getActivity(), NativeXAdPlacement.Game_Launch, onAdEventListener);
                handler.removeCallbacks(mNativeXLoadSession);
            } else {
                handler.postDelayed(mNativeXLoadSession, 500);
            }
        }
    };

    private SessionListener sessionListener = new SessionListener() {
        @Override
        public void createSessionCompleted(boolean success, boolean isOfferWallEnabled, String sessionId) {
            if (getActivity() == null)
                return;
            if (success) {
                appReady = true;
                MonetizationManager.fetchAd(getActivity(), NativeXAdPlacement.Game_Launch, onAdEventListener);
            } else {
                appReady = false;
            }
        }
    };

    private OnAdEventV2 onAdEventListener = new OnAdEventV2() {
        @Override
        public void onEvent(AdEvent event, AdInfo adInfo, String message) {
            switch (event) {
                case FETCHED:
                    break;
                case NO_AD:
                    break;
                case DISMISSED:
                    MonetizationManager.fetchAd(getActivity(), adInfo.getPlacement(), onAdEventListener);
                    break;
                case BEFORE_DISPLAY:
                    break;
                case USER_TOUCH:
                    break;
                case DOWNLOADING:
                    break;
                case EXPANDED:
                    break;
                case COLLAPSED:
                    break;
                case VIDEO_COMPLETED:
                    break;
                case ERROR:
                    break;
                default:
                    break;
            }
        }
    };


    private RewardListener rewardListener = new RewardListener() {
        @Override
        public void onRedeem(RedeemRewardData data) {
            int totalRewardAmount = 0;
            for (Reward reward : data.getRewards()) {
                totalRewardAmount += reward.getAmount();
            }
            requestForAddingBalance(String.valueOf(totalRewardAmount));
//            data.showAlert(getActivity());
        }
    };

    /**
     * SuperSonic ads
     */
    @Override
    public void onOfferwallInitSuccess() {

    }

    @Override
    public void onOfferwallInitFail(SupersonicError supersonicError) {

    }

    @Override
    public void onOfferwallOpened() {

    }

    @Override
    public void onOfferwallShowFail(SupersonicError supersonicError) {

    }

    @Override
    public boolean onOfferwallAdCredited(final int i, final int i1, boolean b) {
//        if(b)
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (i == i1 && Integer.parseInt(myPrefs.getSuperSonicTotalBalance()) == 555) {
                        myPrefs.setSuperSonicTotalBalance(String.valueOf(i1));
                    } else {
                        if (i > 0) {
                            requestForAddingBalance(String.valueOf(i));
                        }
                    }
                }
            });
        }
        return true;
    }

    @Override
    public void onGetOfferwallCreditsFail(SupersonicError supersonicError) {
    }

    @Override
    public void onOfferwallClosed() {
    }


    /**
     * adding balance
     */
    private void requestForAddingBalance(String money) {
        progressBar_woobi.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceid", AppConstants.getImeiNum(getActivity()));
        params.put("uniqueid", myPrefs.getUserKey());
        params.put("offerMoney", money);
        ApplicationFL.getInstance().addToRequestQueue(requestCustomAddMoney(new AppUrls(getActivity(), myPrefs).getHostName(8), params, money), TAG_INSERT_MONEY);
    }

    private CustomJsonObjectRequest requestCustomAddMoney(String url, Map<String, String> params, final String money) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("responseCode").equalsIgnoreCase("1")) {
                        myPrefs.setWalletBalance(String.valueOf(Float.valueOf(myPrefs.getWalletBalance()) + Float.valueOf(money)));

                            notifyUser("You got " + money + " Rs. to complete an offer.");
                        getActivity().invalidateOptionsMenu();
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar_woobi.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Error! try again later.", Toast.LENGTH_SHORT).show();
                    progressBar_woobi.setVisibility(View.GONE);
                }
            }
        });
        customJsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        return customJsonObjectRequest;
    }

    private void notifyUser(String msg) {
        NotificationAlert.sendNotification(getActivity(), msg);
        if (mpFr_database.getMessageCount() < 100) {
            mpFr_database.insertMessage(msg, System.currentTimeMillis(), "unread");
        } else {
            mpFr_database.updateMessage(msg, System.currentTimeMillis(), "unread");
        }
    }

    private CustomJsonArrayRequest requestExpletusOffer(String url) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceid", AppConstants.getImeiNum(getActivity()));
        CustomJsonArrayRequest customJsonArrayRequest = new CustomJsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (progressBar_woobi != null) {
                    progressBar_woobi.setVisibility(View.GONE);
                }
                if (myPrefs.getShowAdmob().equals("1")) {
                    cvAdmob.setVisibility(View.VISIBLE);
                } else {
                    cvAdmob.setVisibility(View.GONE);
                }
                expletusListview.setVisibility(View.VISIBLE);
                listOfOffersExpletus.clear();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject result = response.getJSONObject(i);
                            SurveyCampaignPojo pojo = new SurveyCampaignPojo();
                            pojo.setAppname(result.getString("surveyname"));
                            pojo.setImageurl(result.getString("imgurl"));
                            pojo.setDesciption(result.getString("description"));
                            if (result.getString("linkurl").contains("clicks.minimob.com")) {
                                pojo.setPayout(convertPayoutIntoCreditsMinimob(result.getString("payout").toString()));
                            } else {
                                pojo.setPayout(result.getString("payout"));
                            }
                            pojo.setPlayStoreLink(result.getString("linkurl"));
                            pojo.setCampaignType(result.getString("CampaignType"));
                            listOfOffersExpletus.add(pojo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    expletusListview.setAdapter(new ExpletusAdapter(getActivity(), listOfOffersExpletus, ApplicationFL.getInstance().getImageLoader(), OffersFragment.this));
                    ScrollingListView.setListViewHeightBasedOnChildren(expletusListview);
                } else {
                    Toast.makeText(getActivity(), "No Offers Available", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBar_woobi != null)
                    progressBar_woobi.setVisibility(View.GONE);
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

    private CustomJsonArrayRequest requestNativeAppsData(String url) {
        CustomJsonArrayRequest customJsonArrayRequest = new CustomJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (progressBar5 != null)
                    progressBar5.setVisibility(View.GONE);
                isNativeAppListShowing = true;
                nativeAppListView.setVisibility(View.VISIBLE);
                nativeAppPojoArrayList.clear();
                ivArrowNativeApps.setImageResource(R.mipmap.ic_down);
                myPrefs.setPackageName("");
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject result = response.getJSONObject(i);
                        NativeAppPojo pojo = new NativeAppPojo();
                        pojo.setAppPackageName(result.getString("packagename").toString());
                        pojo.setAppName(result.getString("appname").toString());
                        pojo.setAppPayout(result.getString("payout").toString());
                        pojo.setImageUrl(result.getString("iconurl").toString());
                        pojo.setAppUrl(result.getString("appurl").toString());
                        nativeAppPojoArrayList.add(pojo);
                        if (TextUtils.isEmpty(myPrefs.getPackageName())) {
                            myPrefs.setPackageName(result.getString("packagename").toString() + "payout" + result.getString("payout").toString() + "appName" + result.getString("appname").toString());
                        } else {
                            if (!myPrefs.getPackageName().contains(result.getString("packagename").toString())) {
                                myPrefs.setPackageName(myPrefs.getPackageName() + "," + result.getString("packagename").toString() + "payout" + result.getString("payout").toString() + "appName" + result.getString("appname").toString());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                nativeAppListView.setAdapter(new NativeAppAdapter(getActivity(), nativeAppPojoArrayList, ApplicationFL.getInstance().getImageLoader(), OffersFragment.this));
                ScrollingListView.setListViewHeightBasedOnChildren(nativeAppListView);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBar5 != null)
                    progressBar5.setVisibility(View.GONE);
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
    public void setOnItemButtonClickListnerNativeApps(int position, String playStoreLink) {
        Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
        startActivity(link);
    }

    @Override
    public void setOnItemButtonClickListenerExpletusOffers(int position, String playStoreLink) {
        if (playStoreLink.contains("clicks.minimob.com")) {
            offer_detail_dialog(position);
        } else {
            campaignAppsDialog(listOfOffersExpletus.get(position).getImageurl(), listOfOffersExpletus.get(position).getAppname(), listOfOffersExpletus.get(position).getDesciption(), listOfOffersExpletus.get(position).getPayout(), playStoreLink, listOfOffersExpletus.get(position).getCampaignType());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationFL.getInstance().cancelPendingRequests(TAG_JSON_MINIMOB);
        ApplicationFL.getInstance().cancelPendingRequests(TAG_INSERT_MONEY);
        ApplicationFL.getInstance().cancelPendingRequests(TAG_NATIVE);
    }

    class TaskGoogleAdId extends AsyncTask<Void, Void, String> {

        AdvertisingIdClient.Info adInfo = null;
        private Context context;

        TaskGoogleAdId(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return adInfo.getId();
        }

        @Override
        protected void onPostExecute(String adId) {
            super.onPostExecute(adId);
            Log.e("url", adId);
            myPrefs.setgAdId(adId);
        }
    }

    /*
    *   Campaign apps
    * */
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (myPrefs.isShowCampaign()) {
                        Dfragment dFragment = new Dfragment(myPrefs.getCampaignAppname(), myPrefs.getCampaignDescription(), myPrefs.getCampaignImgurl(), myPrefs.getCampaignPlaystoreLink(), myPrefs.getCampaignPayout(), myPrefs.getCampaignType());
                        dFragment.show(fm, "Dialog Fragment");
                        dFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
                    }
                }
            });
        }
    };

    private void campaignAppsDialog(String appIconUrl, final String appName, String appDescription, final String appPayout, final String playStoreLink, final String campaignType) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.offerdialog_expletus);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        NetworkImageView icon = (NetworkImageView) dialog.findViewById(R.id.app_icon_campaign);
        icon.setImageUrl(appIconUrl, ApplicationFL.getInstance().getImageLoader());
        TextView appname = (TextView) dialog.findViewById(R.id.campaign_app_name);
        appname.setText(appName);
        TextView description = (TextView) dialog.findViewById(R.id.campaign_app_description);
        description.setText(appDescription);
        TextView payout = (TextView) dialog.findViewById(R.id.campaign_app_payout);
        Button bt_install = (Button) dialog.findViewById(R.id.install_campaign_app_bt);
        payout.setText(appPayout);
        bt_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String payout = getFloatPayout(appPayout);
                long currentTimeMillis = System.currentTimeMillis();
                String clickid = String.valueOf(currentTimeMillis);
                String subid2 = AppConstants.getImeiNum(getActivity()) + "mtech" + String.valueOf(currentTimeMillis) + "Appname" + "mpa" + "Payout" + payout + "Campaigntype" + campaignType;
                makeRequestForCampaign(clickid, subid2, playStoreLink, payout, campaignType);

            }
        });
    }

    private void makeRequestForCampaign(final String clickid, final String subid2, final String playStoreLink, final String payout, final String campaignType) {
        String url = (new AppUrls(getActivity(), new MyPrefs(getActivity())).getHostName(18));
        url = url + "deviceid=" + AppConstants.getImeiNum(getActivity()) + "&appname=" + "mpa" + "Payout" + payout + "Campaigntype" + campaignType;

        System.out.println("kamalvermacamp 1 " + url);
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String link = playStoreLink + clickid + "/?sub_id2=" + subid2;
                System.out.println("kamalvermacamp " + link);
                Intent campaignApps = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(campaignApps);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error.Try again", Toast.LENGTH_SHORT).show();
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

    private String getFloatPayout(String value) {
        String str = value.replaceAll("[^0-9.]", "").substring(0, value.replaceAll("[^0-9.]", "").length() - 1);
        return str;
    }
}
