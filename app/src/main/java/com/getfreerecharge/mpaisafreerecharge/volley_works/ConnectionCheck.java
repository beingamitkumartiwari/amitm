package com.getfreerecharge.mpaisafreerecharge.volley_works;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.Window;

import com.getfreerecharge.mpaisafreerecharge.R;

/**
 * Created by DEVEN SINGH on 7/3/2015.
 */
public class ConnectionCheck {

    public static boolean isConnectionAvailable(Context context)
    {
        ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         if ((localConnectivityManager.getActiveNetworkInfo() == null) || (!localConnectivityManager.getActiveNetworkInfo().isAvailable()) || (!localConnectivityManager.getActiveNetworkInfo().isConnected()))
            return false;
        else
        return true;
    }

    public static void alertDialog(Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Internet unavailable");
        alertDialogBuilder
                .setMessage("Check your Internet connection and try again.")
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
}
