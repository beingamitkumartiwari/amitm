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

import java.util.ArrayList;

/**
 * Created by DEVEN SINGH on 8/20/2015.
 */
public class NativeAppAdapter extends BaseAdapter {

    private ArrayList<NativeAppPojo> nativeAppPojos;
    private Context context;
    private ImageLoader imageLoader;
    private OnItemButtonClickListner onItemButtonClickListner;
    private CustomViewHolder customViewHolder;

    public NativeAppAdapter(Context context, ArrayList<NativeAppPojo> nativeAppPojos,ImageLoader imageLoader,OnItemButtonClickListner onItemButtonClickListner) {
        this.context = context;
        this.nativeAppPojos = nativeAppPojos;
        this.imageLoader=imageLoader;
        this.onItemButtonClickListner=onItemButtonClickListner;
    }

    @Override
    public int getCount() {
        return nativeAppPojos.size();
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
            convertView = mInflater.inflate(R.layout.native_app_item_layout, null);
            customViewHolder = new CustomViewHolder();
            customViewHolder.tv_appname = (TextView) convertView.findViewById(R.id.appname);
            customViewHolder.tv_payout = (TextView) convertView.findViewById(R.id.payout);
            customViewHolder.icon = (NetworkImageView) convertView.findViewById(R.id.app_icon);
            customViewHolder.bt_install = (Button) convertView.findViewById(R.id.button_install);

            convertView.setTag(customViewHolder);
        } else {
            customViewHolder = (CustomViewHolder) convertView.getTag();
        }
        final NativeAppPojo pojo = nativeAppPojos.get(position);
        customViewHolder.tv_appname.setText(pojo.getAppName());
        customViewHolder.tv_payout.setText(pojo.getAppPayout() + " Rs");
        customViewHolder.icon.setImageUrl(pojo.getImageUrl(),imageLoader);
        customViewHolder.bt_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemButtonClickListner.setOnItemButtonClickListnerNativeApps(position, pojo.getAppUrl());
            }
        });
        return convertView;
    }

    class CustomViewHolder {
        TextView tv_appname;
        TextView tv_payout;
        NetworkImageView icon;
        Button bt_install;
    }
}
