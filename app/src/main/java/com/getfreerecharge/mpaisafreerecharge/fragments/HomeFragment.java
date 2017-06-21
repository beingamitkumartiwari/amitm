package com.getfreerecharge.mpaisafreerecharge.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.adapters.MyViewPagerAdapter;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppConstants;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 7/16/2015.
 */
public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    private MyPrefs myPrefs;
    GoogleCloudMessaging gcmObj;
    String regId = "";
    String TAG_JSON_REG = "requestReg";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myPrefs = new MyPrefs(getActivity());
        progressDialog = new ProgressDialog(getActivity(), R.style.DevenDialogTheme);
        progressDialog.setMessage("Please wait.....");
        setupTablayout(view);
        initGcmReq(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (myPrefs != null) {
            setActionText(myPrefs.getWalletBalance(), menu);
        }
       super.onPrepareOptionsMenu(menu);
    }

    private void setActionText(String balance, Menu mMenu) {
        MenuItem item = mMenu.findItem(R.id.wallet_balance);

        if (mMenu != null) {
              item.setTitle(balance+" Rs.");
        }
    }


    private void initGcmReq(View view) {
        if (!TextUtils.isEmpty(myPrefs.getGcmRegId())) {
            if (myPrefs.isGcmRegIdSavedInServer()) {

            } else {
                if (progressDialog != null)
                    progressDialog.show();
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceId", AppConstants.getImeiNum(getActivity()));
                params.put("uniqueId", myPrefs.getUserKey());
                params.put("registrationId", myPrefs.getGcmRegId());
                ApplicationFL.getInstance().addToRequestQueue(requestCustomGcmReg(new AppUrls(getActivity(),myPrefs).getHostName(2), params, view), TAG_JSON_REG);
            }
        } else {
            if (checkPlayServices()) {
                registerInBackground(view);
            }
        }
    }

    private void registerInBackground(final View view) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(getActivity());
                    }
                    regId = gcmObj
                            .register(AppConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    myPrefs.setGcmRegId(regId);
                    if (progressDialog != null)
                        progressDialog.show();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("deviceId", AppConstants.getImeiNum(getActivity()));
                    params.put("uniqueId", myPrefs.getUserKey());
                    params.put("registrationId", myPrefs.getGcmRegId());
                    ApplicationFL.getInstance().addToRequestQueue(requestCustomGcmReg(new AppUrls(getActivity(),myPrefs).getHostName(2), params, view), TAG_JSON_REG);
                } else {
                }
            }
        }.execute(null, null, null);
    }

    private CustomJsonObjectRequest requestCustomGcmReg(String url, Map<String, String> params, final View view) {
        CustomJsonObjectRequest customJsonObjectRequest=new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                myPrefs.setGcmRegIdSavedInServer(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                Toast.makeText(
                        getActivity(),
                        "Unexpected Error occcured! [Most common Error: Device might "
                                + "not be connected to Internet or remote server is not up and running], check for other errors as well",
                        Toast.LENGTH_LONG).show();


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

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        getActivity(),
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        } else {
        }
        return true;
    }

    private void setupTablayout(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        MyViewPagerAdapter customPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(customPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }
}
