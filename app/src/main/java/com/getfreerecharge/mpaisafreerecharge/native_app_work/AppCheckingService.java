package com.getfreerecharge.mpaisafreerecharge.native_app_work;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppConstants;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 8/12/2015.
 */
public class AppCheckingService extends IntentService {

    private MyPrefs myPrefs;
    String TAG = "AppCheckingService";
    String TAG_APPFRSCAMPAIGN = "AppCheckingServiceCampaign";

    public AppCheckingService(){
        super("AppCheckingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        myPrefs = new MyPrefs(getApplicationContext());
        String packageName = intent.getStringExtra("packageName");
        if(!myPrefs.isParticipated()&&packageName.contains(myPrefs.getLuckyDrawAppPackage())){
            myPrefs.setIsParticipated(true);
        }
        String[] packageNamePayout=myPrefs.getPackageName().split(",");
        String payout=null;
        String appName=null;
        for(String appPackage:packageNamePayout){
            if(appPackage.contains(packageName)){
                payout=appPackage.substring(appPackage.indexOf("payout") + 6, appPackage.indexOf("appName"));
                appName=appPackage.substring(appPackage.indexOf("appName")+7,appPackage.length());
            }
        }
        Log.e("payout and appName ",payout+"  "+appName);
        if (myPrefs.getPackageName().contains(packageName)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceId", AppConstants.getImeiNum(getApplicationContext()));
            params.put("uniqueId", AppConstants.getUserKey(getApplicationContext(),myPrefs));
            params.put("Payout", payout);
            params.put("packageName", packageName);
            params.put("appName", appName);
            ApplicationFL.getInstance().addToRequestQueue(requestWithPackageName("http://www.mpaisa.info/MtechAppsPromotion.asmx/installApps", params), TAG);
        }

        if (myPrefs.getCampaignPackagename().contains(packageName)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceId", AppConstants.getImeiNum(getApplicationContext()));
            params.put("uniqueId", myPrefs.getUserKey());
            params.put("Payout",myPrefs.getCampaignPayout().replaceAll("\\D+",""));
            params.put("packageName", packageName);
            params.put("appName","mpaCampaigntype"+myPrefs.getCampaignType());
            ApplicationFL.getInstance().addToRequestQueue(requestWithPackageNameCampaign("http://www.mpaisa.info/MtechAppsPromotion.asmx/installAppsFrsCampaign", params), TAG_APPFRSCAMPAIGN);
        }
    }

    private CustomJsonObjectRequest requestWithPackageName(String url, Map<String, String> params) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response packagename", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        customJsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        return customJsonObjectRequest;
    }

    private CustomJsonObjectRequest requestWithPackageNameCampaign(String url, Map<String, String> params) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response campaign ", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response campaign error", error.toString());
            }
        });
        customJsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        return customJsonObjectRequest;
    }
}
