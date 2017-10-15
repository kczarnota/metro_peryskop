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

import static com.example.konrad.metroperyskop.ScanActivity.ADDRESS_KEY;
import static com.example.konrad.metroperyskop.ScanActivity.IS_WORKING_KEY;
import static com.example.konrad.metroperyskop.ScanActivity.URL_KEY;

public class StationMapActivity extends AppCompatActivity {
    public static final String IMG_KEY = "image.to.pass";
    public static final String TEXT_KEY = "text.to.pass";
    public static final int STATION_OFFSET = 1;
    public static final int POINT_OFFSET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_map);

        HTTPConnect.call(this, "/api/station/" + "1", new HttpResponseLamba() {
            @Override
            public void processRequest(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    View root = StationMapActivity.this.findViewById(R.id.station_background);
                    for (int index = 0; index < ((ViewGroup) root).getChildCount(); ++index) {
                        Button bt = (Button) ((ViewGroup) root).getChildAt(index);
                        String id = bt.getText().toString();
                        boolean isGood = true;
                        String type = null;
                        try {
                            JSONObject item = json.getJSONObject(id);
                            isGood = item.getBoolean("open");
                            type = item.getString("exitType");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (isGood && type.equals("ELEVATOR")) {
                            bt.setBackground(getDrawable(R.drawable.ic_elevator_green));
                        } else if (!isGood && type.equals("ELEVATOR")) {
                            bt.setBackground(getDrawable(R.drawable.ic_elevator_red));
                        } else if (isGood) {
                            bt.setBackground(getDrawable(R.drawable.ic_staris_green));
                        } else
                            bt.setBackground(getDrawable(R.drawable.ic_stairs_red));
                    }
                } catch (JSONException ex) {
                }
            }
        });
    }

    public void stationClicked(View view) {
        final String stationId = "1";
        final String placeId = ((Button) view).getText().toString();

        Toast.makeText(this, "Pobieranie zdjęcia wyjscia...", Toast.LENGTH_LONG).show();
        HTTPConnect.call(this, "/api/station/" + stationId + "/point/" + placeId, new HttpResponseLamba() {
            @Override
            public void processRequest(String response) {
                //TODO get image from http response
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    String b64_data = json.getString("image");
                    String text = json.getString("text");
                    String url = json.getString("url");
                    String address = json.getString("address");
                    Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                    ActivityResults.result = b64_data;
                    intent.putExtra(TEXT_KEY, text);
                    intent.putExtra(URL_KEY, url);
                    intent.putExtra(ADDRESS_KEY, address);
                    intent.putExtra("station", stationId);
                    intent.putExtra("point", placeId);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Toast.makeText(this, "Pobieranie zdjęcia wyjscia...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HTTPConnect.call(this, "/api/station/" + "1", new HttpResponseLamba() {
            @Override
            public void processRequest(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    View root = StationMapActivity.this.findViewById(R.id.station_background);
                    for (int index = 0; index < ((ViewGroup) root).getChildCount(); ++index) {
                        Button bt = (Button) ((ViewGroup) root).getChildAt(index);
                        String id = bt.getText().toString();
                        boolean isGood = true;
                        String type = null;
                        try {
                            JSONObject item = json.getJSONObject(id);
                            isGood = item.getBoolean("open");
                            type = item.getString("exitType");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (isGood && type.equals("ELEVATOR")) {
                            bt.setBackground(getDrawable(R.drawable.ic_elevator_green));
                        } else if (!isGood && type.equals("ELEVATOR")) {
                            bt.setBackground(getDrawable(R.drawable.ic_elevator_red));
                        } else if (isGood) {
                            bt.setBackground(getDrawable(R.drawable.ic_staris_green));
                        } else
                            bt.setBackground(getDrawable(R.drawable.ic_stairs_red));
                    }
                } catch (JSONException ex) {
                }
            }
        });

    }
}
