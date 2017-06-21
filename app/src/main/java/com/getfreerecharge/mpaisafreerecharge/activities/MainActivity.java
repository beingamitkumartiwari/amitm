package com.getfreerecharge.mpaisafreerecharge.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.fragments.HomeFragment;
import com.getfreerecharge.mpaisafreerecharge.fragments.NotificationFragment;
import com.getfreerecharge.mpaisafreerecharge.fragments.ProfileFragment;
import com.getfreerecharge.mpaisafreerecharge.fragments.SettingFragment;
import com.getfreerecharge.mpaisafreerecharge.fragments.WalletFragment;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.UserAvatar;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;


public class MainActivity extends AppCompatActivity {

    private MyPrefs myPrefs;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    String tagFragment = "HomeFragment";
    String[] title = {"Home", "Wallet", "Notification", "Profile", "Privacy Policy", "Setting"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        setupNavigationView();
        setupToolbar();
        if (savedInstanceState == null) {
            displayFragment(0);
        }
        if (!myPrefs.isRegistered()) {
            drawerLayout.openDrawer(GravityCompat.START);
            myPrefs.setRegistered(true);
            alertVerifyEmailId();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void alertVerifyEmailId() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Alert!");
        alertDialogBuilder
                .setMessage("We sent a verification link to your email address. Click that link to verify your Email address. Please check spam folder if you do not get email. Even then if problem persist, You can ask for link again from Edit Profile page.\n\n\nNote: Only verified users can redeem their amount.")
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
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

    private void initComponents() {
        myPrefs = new MyPrefs(this);
    }

    private void setupNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        ((TextView) findViewById(R.id.pUserName)).setText(myPrefs.getUserName());
        ((TextView) findViewById(R.id.pUserEmail)).setText(myPrefs.getUserEmail());
        if (myPrefs.isVerifiedUser()) {
            ((ImageView) findViewById(R.id.verificationUser)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_verified));
        } else {
            ((ImageView) findViewById(R.id.verificationUser)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_not_verified));
        }
        if (myPrefs.getUserGender().equalsIgnoreCase("Male")) {
            ((UserAvatar) findViewById(R.id.pUserAvatar)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_user_male_light));
        } else {
            ((UserAvatar) findViewById(R.id.pUserAvatar)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_user_female_light));
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        displayFragment(0);
                        return true;
                    case R.id.navigation_item_2:
                        displayFragment(1);
                        return true;
                    case R.id.navigation_item_3:
                        displayFragment(2);
                        return true;
                    case R.id.navigation_item_4:
                        displayFragment(3);
                        return true;
                    case R.id.navigation_item_5:
                        drawerLayout.closeDrawer(navigationView);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(new AppUrls(MainActivity.this,myPrefs).getHostName(12))));
                        return true;
                    case R.id.navigation_item_6:
                        displayFragment(5);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                tagFragment = "HomeFragment";
                break;
            case 1:
                fragment = new WalletFragment();
                tagFragment = "WalletFragment";
                break;
            case 2:
                fragment = new NotificationFragment();
                tagFragment = "NotificationFragment";
                break;
            case 3:
                fragment = new ProfileFragment();
                tagFragment = "ProfileFragment";
                break;
            case 5:
                fragment = new SettingFragment();
                tagFragment = "SettingFragment";
                break;
        }
        if (fragment != null) {
            toolbar.setTitle(title[position]);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainContainer, fragment, tagFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            drawerLayout.closeDrawer(navigationView);
        }
    }

    @Override
    public void onBackPressed() {
        HomeFragment myFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
        if (myFragment != null && myFragment.isVisible()) {
            super.onBackPressed();
        } else {
            displayFragment(0);
        }
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            HomeFragment myFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
            if (myFragment != null && myFragment.isVisible()) {
            } else {
                displayFragment(0);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
