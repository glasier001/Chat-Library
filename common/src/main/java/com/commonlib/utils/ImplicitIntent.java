package com.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;


/**
 * Created by sagar on 15/6/17.
 */

public final class ImplicitIntent {

    private ImplicitIntent() {
    }

    /*check sim card available*/
    public boolean isSimSupport(Activity activity) {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }

    private void onCall(final Activity activity, String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            //callIntent.setPackage("com.android.dialer");
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // sagar : 3/4/19 3:39 PM According to google guideline, we will open dial pad only and not perform calling
            /*if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissionshere to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                return;
            }*/

            activity.startActivity(callIntent);
            //dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
