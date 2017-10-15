package com.example.konrad.metroperyskop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class ScanActivity extends AppCompatActivity
{
    public static final String IMG_KEY = "image.to.pass";
    public static final String TEXT_KEY = "text.to.pass";
    public static final int STATION_OFFSET = 1;
    public static final int POINT_OFFSET = 2;

    private static final String TAG = ScanActivity.class.getSimpleName();

    @BindView(R.id.camera_view) SurfaceView mySurfaceView;
    private QREader qrEader;
    @BindView(R.id.text) TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QREader", "Value : " + data);
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        //text.setText(data);
                        getData(data);
                        qrEader.stop();
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();
    }

    private void getData(String data)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String [] splitted =data.split(":");
        String url ="http://79.143.190.138:8081/api/station/" + splitted[STATION_OFFSET]
                + "/point/" + splitted[POINT_OFFSET];
        Log.d(TAG, "getData: " + url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //TODO get image from http response
                        JSONObject json = null;
                        Log.d(TAG, "onResponse: ");
                        try
                        {
                           json  = new JSONObject(response);
                           String b64_data = json.getString("image");
                           String text = json.getString("text");
                           Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                           ActivityResults.result = b64_data;
                           intent.putExtra(TEXT_KEY, text);
                           startActivity(intent);
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + "That didnt work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrEader.initAndStart(mySurfaceView);
        qrEader.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrEader.stop();
        qrEader.releaseAndCleanup();
    }
}
