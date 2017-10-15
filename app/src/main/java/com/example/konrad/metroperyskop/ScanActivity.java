package com.example.konrad.metroperyskop;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class ScanActivity extends AppCompatActivity {
    public static final String URL_KEY = "url.to.pass";
    public static final String ADDRESS_KEY = "address.to.pass";
    public static final String TEXT_KEY = "text.to.pass";
    public static final int STATION_OFFSET = 1;
    public static final int POINT_OFFSET = 2;

    private static final String TAG = ScanActivity.class.getSimpleName();
    public static final String IS_WORKING_KEY = "working.to.pass";

    @BindView(R.id.camera_view)
    SurfaceView mySurfaceView;
    private QREader qrEader;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QREader", "Value : " + data);
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        //text.setText(data);
                        v.vibrate(500);
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

    private void getData(String data) {

        final String[] splitted = data.split(":");
        HTTPConnect.call(this, "/api/station/" + splitted[STATION_OFFSET]
                + "/point/" + splitted[POINT_OFFSET], new HttpResponseLamba() {
            @Override
            public void processRequest(String response) {
                JSONObject json = null;
                Log.d(TAG, "onResponse: ");
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
                    intent.putExtra("station", splitted[STATION_OFFSET]);
                    intent.putExtra("point", splitted[POINT_OFFSET]);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrEader.initAndStart(mySurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrEader.stop();
        qrEader.releaseAndCleanup();
    }
}
