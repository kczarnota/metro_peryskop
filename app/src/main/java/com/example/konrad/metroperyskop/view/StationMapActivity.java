package com.example.konrad.metroperyskop.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.konrad.metroperyskop.net.ApiCaller;
import com.example.konrad.metroperyskop.utils.Constants;
import com.example.konrad.metroperyskop.R;
import com.example.konrad.metroperyskop.model.Exit;
import com.example.konrad.metroperyskop.model.ExitDetails;
import com.example.konrad.metroperyskop.model.Station;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationMapActivity extends AppCompatActivity
{
    private String pointId;
    private String stationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_map);

        ApiCaller.makeStationApiCall("1", stationCallback);
    }

    public void stationClicked(View view) {
        stationId = "1";
        pointId = ((Button) view).getText().toString();

        Toast.makeText(this, "Pobieranie zdjęcia wyjscia...", Toast.LENGTH_SHORT).show();
        ApiCaller.makeExitApiCall(stationId, pointId, exitDetailsCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApiCaller.makeStationApiCall("1", stationCallback);
    }

    private Callback<Station> stationCallback = new Callback<Station>()
    {
        @Override
        public void onResponse(Call<Station> call, Response<Station> response)
        {
            View root = StationMapActivity.this.findViewById(R.id.station_background);
            Station station = response.body();
            for (int index = 0; index < ((ViewGroup) root).getChildCount(); ++index)
            {
                Button bt = (Button) ((ViewGroup) root).getChildAt(index);
                String id = bt.getText().toString();
                Exit exit = station.findPoint(id);

                if (exit.open && exit.exitType.equals("ELEVATOR"))
                {
                    bt.setBackground(getDrawable(R.drawable.ic_elevator_green));
                } else if (!exit.open && exit.exitType.equals("ELEVATOR"))
                {
                    bt.setBackground(getDrawable(R.drawable.ic_elevator_red));
                } else if (exit.open)
                {
                    bt.setBackground(getDrawable(R.drawable.ic_staris_green));
                } else
                    bt.setBackground(getDrawable(R.drawable.ic_stairs_red));
            }
        }

        @Override
        public void onFailure(Call<Station> call, Throwable t)
        {
            t.printStackTrace();
        }
    };

    private Callback<ExitDetails> exitDetailsCallback = new Callback<ExitDetails>()
    {
        @Override
        public void onResponse(Call<ExitDetails> call, Response<ExitDetails> response)
        {
            ExitDetails exitDetails = response.body();
            Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
            ActivityResults.result = exitDetails.image;
            intent.putExtra(Constants.TEXT_KEY, exitDetails.text);
            intent.putExtra(Constants.URL_KEY, exitDetails.url);
            intent.putExtra(Constants.ADDRESS_KEY, exitDetails.address);
            intent.putExtra("station", stationId);
            intent.putExtra("point", pointId);
            startActivity(intent);
        }

        @Override
        public void onFailure(Call<ExitDetails> call, Throwable t)
        {
            t.printStackTrace();
        }
    };
}
