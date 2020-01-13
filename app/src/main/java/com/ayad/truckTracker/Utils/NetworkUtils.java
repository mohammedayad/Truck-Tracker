package com.ayad.truckTracker.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by mohammed.ayad on 3/20/2017.
 */

public class NetworkUtils {
    public static final String TAG = "Truck-Tracker";
    public static final String DOMAIN_URL = "http://s802614865.online.de";
    //    public static final String DOMAIN_URL="http://192.168.1.3:2063";
//    public static final String GC_URL="http://196.205.93.189:15761/api/Governorates";
    public static final String GC_GOVERNORATES = DOMAIN_URL + "/api/Governorates";
    public static final String GC_CITIES = DOMAIN_URL + "/api/cities/CitiesbyGov";
    public static final String GC_FAMILY_RANGES = DOMAIN_URL + "/api/Services/Ranges";
    public static final String SUPPLIER_NAME = DOMAIN_URL + "/api/Services/suppliersbycity";
    public static final String SIGN_UP_USER = DOMAIN_URL + "/api/Users";
    public static final String UPDATE_USER_PROFILE = DOMAIN_URL + "/api/Users/PutUser";
    public static final String LOGIN_USER = DOMAIN_URL + "/token";
    public static final String TRUCK_DELIVERY_DETAILS_VALIDATION = DOMAIN_URL + "/api/DeliveryDetail/InsertDeliveryDetail";
    public static final String REQUEST_ANBOBA = DOMAIN_URL + "/api/userrequests";
    public static final String LAST_REQUEST = DOMAIN_URL + "/api/userrequests/LastRequest";
    public static final String USER_TOKEN = DOMAIN_URL + "/api/users/token";
    public static final String QUERY_PARAM = "id";
    public static final String SUPPLIER_QUERY_PARAM = "supplierId";
    public static final String SUPPLIER_GC_NUM_QUERY_PARAM = "numOfGc";
    public static final String QUERY_PARAM_SUPPLIER = "cid";
    public static final String QUERY_PARAM_USER_NAME = "userName";
    public static final String QUERY_PARAM_PASSWORD = "password";
    public static final String QUERY_PARAM_REQUEST_ANBOBA = "userId";
    public static final String GC_ALL_RQUESTS = DOMAIN_URL + "/sup/AllRequests";
    public static final String GC_DELIVERED_USERS = DOMAIN_URL + "/sup/delivered";
    // Splash screen timer
    public static final int SPLASH_TIME_OUT = 3000;
    public static final int MY_SOCKET_TIMEOUT_MS = 9000;


    public static String buildUrl(String locationUrl, String queryParam, String QueryParamValue) {
        Uri builtUri = Uri.parse(locationUrl).buildUpon()
                .appendQueryParameter(queryParam, QueryParamValue)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("++++++++++", "Built URI " + url);

        return url.toString();
    }

    public static String buildLoginUrl(String locationUrl, String userName, String password) {
        Uri builtUri = Uri.parse(locationUrl).buildUpon()
                .appendQueryParameter(QUERY_PARAM_USER_NAME, userName)
                .appendQueryParameter(QUERY_PARAM_PASSWORD, password)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("++++++++++", "Built URI " + url);

        return url.toString();
    }

    public static boolean isNetworkConnected(Context context) {


        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();


    }

    public static void showActionBar(AppCompatActivity context, Toolbar mToolbar) {
//        getSupportActionBar().show();

        context.setSupportActionBar(mToolbar);


    }
}
