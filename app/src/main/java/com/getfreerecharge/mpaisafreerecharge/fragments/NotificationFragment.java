package com.getfreerecharge.mpaisafreerecharge.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.adapters.NotificationAdapter;
import com.getfreerecharge.mpaisafreerecharge.storage.MpFr_Database;

/**
 * Created by DEVEN SINGH on 7/16/2015.
 */
public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MpFr_Database mpFr_database;
    private LinearLayout noNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
    }

    private void initComponents(View view) {
        mpFr_database = new MpFr_Database(getActivity());
        noNotification= (LinearLayout) view.findViewById(R.id.noNotification);
        recyclerView = (RecyclerView) view.findViewById(R.id.notification_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        notificationAdapter = new NotificationAdapter(getActivity(), mpFr_database.getNotificationData() );
        recyclerView.setAdapter(notificationAdapter);
        mpFr_database.updateMsgReadStatus();
        if(mpFr_database.getMessageCount()>0){
            noNotification.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            noNotification.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_notification, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear) {
            deleteAllNotification();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotification() {
        if(mpFr_database.deleteAllNotification()){
            noNotification.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}
