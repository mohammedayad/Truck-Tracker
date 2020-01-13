package com.ayad.truckTracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ayad.truckTracker.Utils.NetworkUtils;
import com.ayad.truckTracker.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    //    @BindView(R.id.link_signup)
//    TextView signUpLink;
    @BindView(R.id.input_uName)
    EditText userName;
    @BindView(R.id.input_password)
    EditText userPassword;
    @BindView(R.id.btn_login)
    AppCompatButton loginBtn;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    //    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);//user
        boolean isSupplierLoggedIn = sharedPreferences.getBoolean("isSupplierLoggedIn", false);//supplier
        if (isLoggedIn) {//he is logged in before
            callTruckDetailsActivityPage();
        } else {
            ButterKnife.bind(this);
//            NetworkUtils.showActionBar(this, mToolbar);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
                        String uName = userName.getText().toString();
                        String password = userPassword.getText().toString();
                        if (uName.equals("") || password.equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.insert_all_fields), Toast.LENGTH_LONG).show();

                        } else {
                            checkUserCredentials(uName, password);


                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();

                    }
                }
            });
//            signUpLink.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            });
        }

    }

    private void checkUserCredentials(String userName, String password) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, NetworkUtils.LOGIN_USER, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        if (response != null) {
                            Toast.makeText(getApplicationContext(), getString(R.string.login_successfully), Toast.LENGTH_LONG).show();
                            try {
                                String userAccessToken = response.getString("access_token");
                                Log.d(NetworkUtils.TAG, userAccessToken);
                                createUserSession(userAccessToken);
                                callTruckDetailsActivityPage();
//                                createUserNotificationToken();
//                                    callMainActivityPage();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //check on statues that returnned from the server don't forget
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.unSuccessfully_login), Toast.LENGTH_LONG).show();


                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        error.printStackTrace();
                        Log.e(NetworkUtils.TAG, "error happen in checkUserCredentials " + error.getMessage());
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), getString(R.string.unSuccessfully_login), Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            public byte[] getBody() {
                String str = "grant_type=password&username=" + userName + "&password=" + password + "";
                return str.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
// Access the RequestQueue through your singleton class.
        jsObjRequest.setTag(NetworkUtils.TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    private void createUserNotificationToken() {
        String userNotificationToken = sharedPreferences.getString("userToken", "");
        String userId = sharedPreferences.getString("userId", "");
//        Toast.makeText(getApplicationContext(), "User Token "+userNotificationToken, Toast.LENGTH_LONG).show();
        if (!userNotificationToken.equalsIgnoreCase("") && !userId.equalsIgnoreCase("")) {
//            Toast.makeText(getApplicationContext(), "User Token id "+userId, Toast.LENGTH_LONG).show();
            Map<String, Object> userToken = new LinkedHashMap<String, Object>();
            userToken.put("User_id", Integer.parseInt(userId));
            userToken.put("token", userNotificationToken);
            sendUserTokenToServer(userToken);

        } else {
//            callMainActivityPage();

        }

    }


    private void sendUserTokenToServer(Map<String, Object> userData) {
//        mLoadingIndicator.setVisibility(View.VISIBLE);
        Log.d(NetworkUtils.TAG, userData.toString());
        Log.d(NetworkUtils.TAG, new JSONObject(userData).toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                NetworkUtils.USER_TOKEN, new JSONObject(userData),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        Log.d(NetworkUtils.TAG, response.toString());
//                        callMainActivityPage();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                mLoadingIndicator.setVisibility(View.INVISIBLE);
                VolleyLog.d(NetworkUtils.TAG, "Error: " + error.getMessage());


            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };
// Access the RequestQueue through your singleton class.
        jsonObjReq.setTag(NetworkUtils.TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjReq);


    }

    private void createUserSession(String userAccessToken) {
        Log.d(NetworkUtils.TAG, "create User Session");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userAccessToken", userAccessToken);
        editor.apply();


    }


    private void callTruckDetailsActivityPage() {
        Log.d(NetworkUtils.TAG, "Called Main Activity");
//        Toast.makeText(getApplicationContext(),getString(R.string.login_successfully), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), TruckDetailsActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(NetworkUtils.TAG);
    }
}
