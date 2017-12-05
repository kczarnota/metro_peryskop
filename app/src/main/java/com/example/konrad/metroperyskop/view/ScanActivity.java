package com.example.konrad.metroperyskop.view;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.konrad.metroperyskop.net.ApiCaller;
import com.example.konrad.metroperyskop.utils.Constants;
import com.example.konrad.metroperyskop.R;
import com.example.konrad.metroperyskop.utils.Utils;
import com.example.konrad.metroperyskop.model.ExitDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity implements Callback<ExitDetails>
{
    private static final String TAG = Utils.getTag(ScanActivity.class);

    @BindView(R.id.camera_view)
    public SurfaceView mySurfaceView;
    @BindView(R.id.text)
    public TextView text;

    private QREader qrEader;
    private String stationId;
    private String pointId;

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
        stationId = splitted[Constants.STATION_OFFSET];
        pointId = splitted[Constants.POINT_OFFSET];
        ApiCaller.makeExitApiCall(stationId, pointId, this);
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

    @Override
    public void onResponse(Call<ExitDetails> call, Response<ExitDetails> response)
    {
        ExitDetails station = response.body();
        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
        ActivityResults.result = station.image;
        intent.putExtra(Constants.TEXT_KEY, station.text);
        intent.putExtra(Constants.URL_KEY, station.url);
        intent.putExtra(Constants.ADDRESS_KEY, station.address);
        intent.putExtra("station", stationId);
        intent.putExtra("point", pointId);
        startActivity(intent);
    }

    @Override
    public void onFailure(Call<ExitDetails> call, Throwable t)
    {
        t.printStackTrace();
    }
}
