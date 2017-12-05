package com.example.konrad.metroperyskop.view;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.konrad.metroperyskop.net.ApiCaller;
import com.example.konrad.metroperyskop.utils.Constants;
import com.example.konrad.metroperyskop.R;
import com.example.konrad.metroperyskop.view.adapter.StationAdapter;
import com.example.konrad.metroperyskop.utils.Utils;
import com.example.konrad.metroperyskop.model.ExitDetails;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationListActivity extends AppCompatActivity implements Callback<ExitDetails>
{
    private static final String TAG = Utils.getTag(StationListActivity.class);

    public static final int STATION_OFFSET = 1;
    public static final int POINT_OFFSET = 2;

    @BindView(R.id.stations_list)
    public ListView mStationList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_list);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        if(action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
            sendStationExitRequest(parseNfcMessage(intent));

        prepareDummyStationsList();

        FloatingActionButton myFab = StationListActivity.this.findViewById(R.id.scan_qr);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openQrScanActivity();
            }
        });
    }

    private String parseNfcMessage(Intent intent)
    {
        Parcelable[] rawMessages =
                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage message = (NdefMessage) rawMessages[0];
        NdefRecord record = message.getRecords()[0];
        String msg = parseNfcRecord(record);
        Log.d(TAG, "onNewIntent: " + msg);
        return msg;
    }

    private String parseNfcRecord(NdefRecord record)
    {
        byte [] bytes = record.getPayload();
        return new String(bytes, Charset.forName("UTF-8"));
    }

    private void prepareDummyStationsList()
    {
        String[] stations1 = getResources().getStringArray(R.array.stations_array_first);
        String[] stations2 = getResources().getStringArray(R.array.stations_array_second);
        ArrayList<String> stat1 = new ArrayList<>(Arrays.asList(stations1));
        ArrayList<String> stat2 = new ArrayList<>(Arrays.asList(stations2));
        stat1.addAll(stat2);
        mStationList.setAdapter(new StationAdapter(this,
                R.layout.list_item, stat1, stations1.length));
        mStationList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(getApplicationContext(), StationMapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendStationExitRequest(String data)
    {
        String[] stationInfo = data.split(":");
        ApiCaller.makeExitApiCall(stationInfo[STATION_OFFSET], stationInfo[POINT_OFFSET], this);
    }

    private void openQrScanActivity() {
        Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponse(Call<ExitDetails> call, Response<ExitDetails> response)
    {
        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
        ExitDetails station = response.body();
        ActivityResults.result = station.image;
        intent.putExtra(Constants.TEXT_KEY, station.text);
        intent.putExtra(Constants.URL_KEY, station.url);
        startActivity(intent);
    }

    @Override
    public void onFailure(Call<ExitDetails> call, Throwable t)
    {
        t.printStackTrace();
    }
}