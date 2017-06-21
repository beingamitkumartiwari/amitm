package com.getfreerecharge.mpaisafreerecharge.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.getfreerecharge.mpaisafreerecharge.fragments.LuckyDrawWinner;
import com.getfreerecharge.mpaisafreerecharge.fragments.OffersFragment;
import com.getfreerecharge.mpaisafreerecharge.fragments.ReferralsFragment;


/**
 * Created by DEVEN SINGH on 7/1/2015.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Offers","Referrals","Lucky Draw"};

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            OffersFragment offersFragment = new OffersFragment();
            return offersFragment;
        } else if(position==1){
            ReferralsFragment referralsFragment = new ReferralsFragment();
            return referralsFragment;
        }else {
            LuckyDrawWinner luckyDrawWinner = new LuckyDrawWinner();
            return luckyDrawWinner;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
