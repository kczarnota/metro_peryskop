package com.example.konrad.metroperyskop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements View.OnTouchListener
{
    @BindView(R.id.stations_list) ListView stationList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

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
