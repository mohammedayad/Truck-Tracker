package com.ayad.truckTracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ayad.truckTracker.Utils.NetworkUtils;
import com.ayad.truckTracker.Utils.TruckTrackerUtils;
import com.ayad.truckTracker.Utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TruckDetailsActivity extends AppCompatActivity {
    @BindView(R.id.input_meterRead)
    EditText meterRead;
    @BindView(R.id.input_truckNumber)
    EditText truckNumber;
    @BindView(R.id.input_routeNumber)
    EditText routeNumber;
    @BindView(R.id.btn_truckDetailsSubmit)
    AppCompatButton truckDetailsSubmitBtn;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_details);
        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        handleTruckDetailesSubmition();
    }

    private void handleTruckDetailesSubmition() {
        truckDetailsSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
                    String mRead = meterRead.getText().toString();
                    String tNumber = truckNumber.getText().toString();
                    String rNumber = routeNumber.getText().toString();
                    if (mRead.equals("") || tNumber.equals("") || rNumber.equals("")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.insert_all_fields), Toast.LENGTH_LONG).show();

                    } else {
                        Map<String, String> truckData = new HashMap<>(3);
                        truckData.put("RoadNumber", mRead);
                        truckData.put("CarNumber", tNumber);
                        truckData.put("StartCarMeter", rNumber);
                        String startDate = TruckTrackerUtils.formatDate(new Date());
                        truckData.put("StartDateTime", startDate);
                        truckDataValidation(truckData);
                        truckData.clear();
                        truckData = null;


                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void truckDataValidation(Map<String, String> truckData) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        JSONObject truckDataJson = new JSONObject(truckData);
        Log.e(NetworkUtils.TAG, "++++truckDataJson++++\n" + truckDataJson);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, NetworkUtils.TRUCK_DELIVERY_DETAILS_VALIDATION, truckDataJson, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(NetworkUtils.TAG, "++++onResponse++++");

                        Toast.makeText(getApplicationContext(), getString(R.string.login_successfully), Toast.LENGTH_LONG).show();
                        callMainActivityPage();

//                        if (response != null) {
//                            Toast.makeText(getApplicationContext(), getString(R.string.login_successfully), Toast.LENGTH_LONG).show();
//                            try {
//
//                            callMainActivityPage();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Toast.makeText(getApplicationContext(), getString(R.string.data_validation_error), Toast.LENGTH_LONG).show();
//
//
//                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e(NetworkUtils.TAG, "++++onErrorResponse++++");
                        error.printStackTrace();
                        //   Handle Error
//                        Log.d(NetworkUtils.TAG, "Error: " + error
//                                + "\nStatus Code " + error.networkResponse.statusCode
//                                + "\nResponse Data " + error.networkResponse.data.toString()
//                                + "\nResponse headers " + error.networkResponse.headers
//                                + "\nCause " + error.getCause()
//                                + "\nmessage " + error.getMessage());
                        Log.e(NetworkUtils.TAG, "error happen in truckDataValidation " + error.getMessage());
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), getString(R.string.data_validation_error), Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.e(NetworkUtils.TAG, "++++parseNetworkResponse++++");
                int statusCode = response.statusCode;
                Log.e(NetworkUtils.TAG, "statusCode " + statusCode);
                Map<String, String> responseHeaders = response.headers;
                Log.e(NetworkUtils.TAG, "responseHeaders " + responseHeaders);
                String jsonResponse="{\"status\":\"failed\"}";
                if (statusCode==200) {
                    jsonResponse="{\"status\":\"success\"}";
                }
                Log.e(NetworkUtils.TAG, "jsonResponse--->"+jsonResponse);
                response = new NetworkResponse(jsonResponse.getBytes());
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.e(NetworkUtils.TAG, "++++getHeaders++++");
                Map<String, String> params = new HashMap<String, String>();
                String userToken = sharedPreferences.getString("userAccessToken", "");
                Log.d("+++userToken+++", userToken);
                params.put("Authorization", "Bearer " + userToken);
//                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };
// Access the RequestQueue through your singleton class.
        jsObjRequest.setTag(NetworkUtils.TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    private void callMainActivityPage() {
        Log.d(NetworkUtils.TAG, "Called Main Activity");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
