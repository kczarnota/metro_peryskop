package com.example.konrad.metroperyskop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StationMapActivity extends AppCompatActivity
{
    public static final String IMG_KEY = "image.to.pass";
    public static final String TEXT_KEY = "text.to.pass";
    public static final int STATION_OFFSET = 1;
    public static final int POINT_OFFSET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_map);

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject json = null;

        String url = "http://79.143.190.138:8081/api/station/" + "1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        //TODO get image from http response
                        JSONObject json = null;
                        try
                        {
                            json = new JSONObject(response);
                            View root = StationMapActivity.this.findViewById(R.id.station_background);
                            for(int index=0; index<((ViewGroup)root).getChildCount(); ++index) {
                                Button bt = (Button)((ViewGroup)root).getChildAt(index);
                                String id = bt.getText().toString();
                                boolean isGood = true;
                                try
                                {
                                    isGood = json.getBoolean(id);
                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                                if (isGood)
                                {
                                    bt.setBackground(getDrawable(R.drawable.ic_staris_green));
                                }
                                else {
                                    bt.setBackground(getDrawable(R.drawable.ic_stairs_red));
                                }
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void stationClicked(View view)
    {
        String stationId = "1";
        String placeId = ((Button) view).getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://79.143.190.138:8081/api/station/" + stationId
                + "/point/" + placeId;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        //TODO get image from http response
                        JSONObject json = null;
                        try
                        {
                            json = new JSONObject(response);
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
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
            }
        });
        // Add the request to the RequestQueue.
        Toast.makeText(this, "Pobieranie zdjęcia wyjscia...",Toast.LENGTH_LONG).show();
        queue.add(stringRequest);


    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }
}
