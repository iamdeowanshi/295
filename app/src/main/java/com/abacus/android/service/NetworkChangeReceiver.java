package com.abacus.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.abacus.android.util.DialogUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final boolean activeNetwork = isOnline(context);

        if (! activeNetwork ) {
            final DialogUtil dialog = new DialogUtil();
            dialog.setMessage("Check Internet Connectivity and Try again Later !");
            dialog.setTitle("Connection Error");
            dialog.setNegativeButtonText("Retry");
            dialog.setDialogClickListener(new DialogUtil.DialogClickListener() {
                @Override
                public void onPositiveClick() {

                }

                @Override
                public void onNegativeClick() {
                    if (!activeNetwork) {
                        onReceive(context, intent);
                    }
                }
            });
            dialog.show(context);
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }
}
