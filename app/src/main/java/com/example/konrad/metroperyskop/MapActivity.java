package com.example.konrad.metroperyskop;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements View.OnTouchListener
{
    public static final String URL_KEY = "url.to.pass";
    public static final String TEXT_KEY = "text.to.pass";
    public static final int STATION_OFFSET = 1;
    public static final int POINT_OFFSET = 2;
    private static final String TAG = MapActivity.class.getSimpleName();
    @BindView(R.id.stations_list) ListView stationList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String action = intent.getAction();
        if(action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
        {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage message = (NdefMessage) rawMessages[0];
            NdefRecord record = message.getRecords()[0];
            //TODO parse record
            String msg = parseRecord(record);
            Log.d(TAG, "onNewIntent: " + msg);
            //TODO open photo page
            sendData(msg);

        }

        String[] stations1 = getResources().getStringArray(R.array.stations_array_first);
        String[] stations2 = getResources().getStringArray(R.array.stations_array_second);
        //Row layout defined by Android: android.R.layout.simple_list_item_1
        //stationList.setAdapter(new ArrayAdapter<String>(this,
        ArrayList<String> stat1 = new ArrayList<>(Arrays.asList(stations1));
        ArrayList<String> stat2 = new ArrayList<>(Arrays.asList(stations2));
        stat1.addAll(stat2);
        stationList.setAdapter(new StationAdapter(this,
                R.layout.list_item, stat1, stations1.length));
        stationList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(getApplicationContext(), StationMapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendData(String data) {

        String[] splitted = data.split(":");
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
                    Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                    ActivityResults.result = b64_data;
                    intent.putExtra(TEXT_KEY, text);
                    intent.putExtra(URL_KEY, url);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String parseRecord(NdefRecord record)
    {
        byte [] bytes = record.getPayload();
        return new String(bytes, Charset.forName("UTF-8"));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.scan_qr:
                Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
