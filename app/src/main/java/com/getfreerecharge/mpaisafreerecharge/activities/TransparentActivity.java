package com.getfreerecharge.mpaisafreerecharge.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.storage.MpFr_Database;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.NotificationAlert;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppConstants;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by KAMAL VERMA on 3/25/2016.
 */
public class TransparentActivity extends Activity implements View.OnClickListener {

    MyPrefs myPrefs;
    MpFr_Database mpFr_database;
    ImageButton btClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        setContentView(R.layout.transparent);
        mpFr_database = new MpFr_Database(this);
        btClose= (ImageButton) findViewById(R.id.imagebuttonclose);
        myPrefs=new MyPrefs(this);
        myPrefs.setTransparentClosed(false);
        btClose.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myPrefs.setTransparentClosed(true);
        Intent goToA = new Intent(this,MainActivity.class);
        goToA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToA);
    }


    @Override
    public void onClick(View v) {
        myPrefs.setTransparentClosed(true);
        Intent goToA = new Intent(this,MainActivity.class);
        goToA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToA);
    }
}
