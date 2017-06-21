package com.getfreerecharge.mpaisafreerecharge.utils.app_constants;

import android.content.Context;

import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;

/**
 * Created by DEVEN SINGH on 7/20/2015.
 */
public class AppUrls {

    private Context context;
    private MyPrefs myPrefs;
    public AppUrls(Context context,MyPrefs myPrefs){
        this.context=context;
        this.myPrefs=myPrefs;
    }

    public String getHostName(int v){
        String hostName="";
        switch (v){
            case 0:
                hostName=myPrefs.getHostPort() + context.getResources().getString(R.string.check_registration);
                break;
            case 1:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.register);
                break;
            case 2:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.gcm_reg_id);
                break;
            case 3:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.total_money);
                break;
            case 4:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.minimob_conversion_rate);
                break;
            case 5:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.minimob_offers);
                break;
            case 6:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.insert_clicks);
                break;
            case 7:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.redeem_balance);
                break;
            case 8:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.insert_money);
                break;
            case 9:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.update_profile);
                break;
            case 10:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.user_profile);
                break;
            case 11:
                hostName = myPrefs.getHostPort() + context.getResources().getString(R.string.resend_mail);
                break;
            case 12:
                hostName = context.getResources().getString(R.string.privacy_link);
                break;
            case 13:
                hostName= context.getResources().getString(R.string.faq);
                break;
            case 14:
                hostName= context.getResources().getString(R.string.native_app);
                break;
            case 15:
                hostName= context.getResources().getString(R.string.campaign_insert_click);
                break;
            case 16:
                hostName= context.getResources().getString(R.string.request_campaign_ads);
                break;
            case 17:
                hostName= context.getResources().getString(R.string.lucky_draw_link);
                break;
            case 18:
                hostName=context.getResources().getString(R.string.setids_forcampaign);
                break;
            case 19:
                hostName=context.getResources().getString(R.string.offers_surveycampaign);
                break;

        }
        return hostName;
    }
}
