package com.getfreerecharge.mpaisafreerecharge.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.pojos.SurveyCampaignPojo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DEVEN SINGH on 11/16/2015.
 */
public class ExpletusAdapter extends BaseAdapter {

    private ArrayList<SurveyCampaignPojo> listOfOffersExpletus;
    private Context context;
    private ImageLoader imageLoader;
    private OnItemButtonClickListner onItemButtonClickListner;
    private CustomViewHolderExpletus customViewHolderExpletus;

    public ExpletusAdapter(Context context, ArrayList<SurveyCampaignPojo> listOfOffersExpletus,ImageLoader imageLoader,OnItemButtonClickListner onItemButtonClickListner) {
        this.context = context;
        this.listOfOffersExpletus = listOfOffersExpletus;
        this.imageLoader=imageLoader;
        this.onItemButtonClickListner=onItemButtonClickListner;
    }

    @Override
    public int getCount() {
        return listOfOffersExpletus.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.expletus_offer_layout, null);
            customViewHolderExpletus = new CustomViewHolderExpletus();
            customViewHolderExpletus.tv_appname = (TextView) convertView.findViewById(R.id.appname);
            customViewHolderExpletus.tv_payout = (TextView) convertView.findViewById(R.id.payout);
            customViewHolderExpletus.icon = (NetworkImageView) convertView.findViewById(R.id.app_icon);
            customViewHolderExpletus.bt_install = (Button) convertView.findViewById(R.id.button_install);

            convertView.setTag(customViewHolderExpletus);
        } else {
            customViewHolderExpletus = (CustomViewHolderExpletus) convertView.getTag();
        }
        final SurveyCampaignPojo pojo = listOfOffersExpletus.get(position);
        customViewHolderExpletus.tv_appname.setText(pojo.getAppname());
        if (pojo.getPlayStoreLink().contains("clicks.minimob.com")) {
            customViewHolderExpletus.tv_payout.setText(pojo.getPayout()+" Rs");
        } else {
            customViewHolderExpletus.tv_payout.setText(pojo.getPayout().replaceAll("[^0-9.]", "").substring(0, pojo.getPayout().replaceAll("[^0-9.]", "").length() - 1) + " Rs");

        }
         customViewHolderExpletus.icon.setImageUrl(pojo.getImageurl(),imageLoader);
        customViewHolderExpletus.bt_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemButtonClickListner.setOnItemButtonClickListenerExpletusOffers(position, pojo.getPlayStoreLink());
            }
        });
        return convertView;
    }

    class CustomViewHolderExpletus {
        TextView tv_appname;
        TextView tv_payout;
        NetworkImageView icon;
        Button bt_install;
    }

}
