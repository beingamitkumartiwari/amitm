package com.getfreerecharge.mpaisafreerecharge.app_tour;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.activities.RegistrationActivity;
import com.getfreerecharge.mpaisafreerecharge.utils.ViewPagerParallax;


public class AppTourPagerActivity extends ActionBarActivity {

    private TextView skipTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_pager);
        fullScreencall();
        skipTour = (TextView) findViewById(R.id.skip_join);
        if (getIntent().getExtras().getString("tourKey").equalsIgnoreCase("Welcome")) {
            skipTour.setText("Skip & Join");
        } else {
            skipTour.setText("Skip Tour");
        }
        ViewPagerParallax customViewpager = (ViewPagerParallax) findViewById(R.id.viewpager);
        customViewpager.set_max_pages(5);
        customViewpager.setBackgroundAsset(R.drawable.background_parallax);
        customViewpager.setPageTransformer(true, new CubeOutTransformer());
        FragmentIndicator customIndicator = (FragmentIndicator) findViewById(R.id.fragment_indicator);
        TourPagerAdapter customPagerAdapter = new TourPagerAdapter(getSupportFragmentManager());
        customViewpager.setAdapter(customPagerAdapter);
        customViewpager.setCurrentItem(0);
        customIndicator.setViewPager(customViewpager);
        customIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        skipTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (skipTour.getText().toString().equalsIgnoreCase("Skip Tour")) {
                    finish();
                } else {
                    startActivity(new Intent(AppTourPagerActivity.this, RegistrationActivity.class));
                    finish();
                }
            }
        });
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
}