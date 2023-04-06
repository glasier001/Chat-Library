package com.commonlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.commonlib.utils.NetworkUtils.isNetworkAvailable;


/**
 * Created by mauliksantoki on 30/5/17.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String LCN_NETWORKCHANGE = "lcn_networkchange";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(LCN_NETWORKCHANGE));
        }
    }
}
