package com.getfreerecharge.mpaisafreerecharge.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.app_tour.AppTourPagerActivity;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppConstants;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;
import com.getfreerecharge.mpaisafreerecharge.volley_works.ConnectionCheck;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by DEVEN SINGH on 7/3/2015.
 */
public class WelcomeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private MyPrefs myPrefs;
    final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        fullScreencall();
        myPrefs = new MyPrefs(WelcomeActivity.this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_wel);
//        if (myPrefs.isRegistered()) {
//            progressBar.setVisibility(View.VISIBLE);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setVisibility(View.GONE);
//                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//                            finish();
//                        }
//                    });
//                }
//            }, 1500);
//        } else {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            permissionCheck();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            if (ConnectionCheck.isConnectionAvailable(this)) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceId", AppConstants.getImeiNum(this));
                ApplicationFL.getInstance().addToRequestQueue(requestCheckUserRegistration(new AppUrls(WelcomeActivity.this,myPrefs).getHostName(0), params));
            } else {
                progressBar.setVisibility(View.GONE);
                ConnectionCheck.alertDialog(this);
            }
        }



//        }
    }

    public void fullScreencall() {
        if (Build.VERSION.SDK_INT < 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void permissionCheck() {
        int readphoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        int audioRecord = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        int  writeExternal = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int contacts = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
        int location = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> permissions = new ArrayList<String>();
        if (readphoneState != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (audioRecord != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if (writeExternal != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (contacts != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.GET_ACCOUNTS);
        }
        if (location != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
        }else{
            progressBar.setVisibility(View.VISIBLE);
            if (ConnectionCheck.isConnectionAvailable(this)) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceId", AppConstants.getImeiNum(this));
                ApplicationFL.getInstance().addToRequestQueue(requestCheckUserRegistration(new AppUrls(WelcomeActivity.this,myPrefs).getHostName(0), params));
            } else {
                progressBar.setVisibility(View.GONE);
                ConnectionCheck.alertDialog(this);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                           if(i==permissions.length-1){
                               progressBar.setVisibility(View.VISIBLE);
                               if (ConnectionCheck.isConnectionAvailable(this)) {
                                   Map<String, String> params = new HashMap<String, String>();
                                   params.put("deviceId", AppConstants.getImeiNum(this));
                                   ApplicationFL.getInstance().addToRequestQueue(requestCheckUserRegistration(new AppUrls(WelcomeActivity.this,myPrefs).getHostName(0), params));
                               } else {
                                   progressBar.setVisibility(View.GONE);
                                   ConnectionCheck.alertDialog(this);
                               }
                           }
                        }

                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }



    private CustomJsonObjectRequest requestCheckUserRegistration(String url, Map<String, String> params) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.get("responseCode").toString().equals("0")) {
                        myPrefs.setUserId(response.getString("key"));
                        myPrefs.setUserName(response.getString("name"));
                        myPrefs.setUserEmail(response.getString("email"));
                        myPrefs.setMobileNumber(response.getString("mobile"));
                        myPrefs.setUserGender(response.getString("gender"));
                        myPrefs.setUserKey(AppConstants.getUserKey(WelcomeActivity.this, myPrefs));
                        myPrefs.setReferralCode(response.getString("refcode"));
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    } else if (response.get("responseCode").toString().equals("1")) {
                        Intent intent = new Intent(WelcomeActivity.this, AppTourPagerActivity.class);
                        intent.putExtra("tourKey", "Welcome");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "There is some error. Try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "There is some error. Try again.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(WelcomeActivity.this, "Server Error! please try again later.", Toast.LENGTH_SHORT).show();
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
