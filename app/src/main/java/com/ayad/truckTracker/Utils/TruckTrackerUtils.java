package com.ayad.truckTracker.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TruckTrackerUtils {
    public static String formatDate(Date Date) {
        Log.e(NetworkUtils.TAG, "formatDate--->" + Date);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        String formattedDate = sdfDate.format(Date);
        Log.e(NetworkUtils.TAG, "formattedDate--->" + formattedDate);
        return formattedDate;
    }
}
