package com.example.konrad.metroperyskop.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.konrad.metroperyskop.view.adapter.AdsAdapter;
import com.example.konrad.metroperyskop.net.ApiCaller;
import com.example.konrad.metroperyskop.utils.Constants;
import com.example.konrad.metroperyskop.R;
import com.example.konrad.metroperyskop.model.Station;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoActivity extends AppCompatActivity implements Callback<Station>
{

    public String streetViewUrl = null;

    @BindView(R.id.image)
    public ImageView view;
    @BindView(R.id.photo_name)
    public TextView name;
    @BindView(R.id.photo_address)
    public TextView address;
    @BindView(R.id.ads)
    public ListView addList;
    @BindView(R.id.photo_checkbox)
    public CheckBox checkBox;
    private String stationId;
    private String pointId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String img = ActivityResults.result;
        String text = intent.getStringExtra(Constants.TEXT_KEY);
        this.streetViewUrl = intent.getStringExtra(Constants.URL_KEY);
        String adr = intent.getStringExtra(Constants.ADDRESS_KEY);
        this.stationId = intent.getStringExtra("station");
        this.pointId = intent.getStringExtra("point");
        boolean isWorking = intent.getBooleanExtra(Constants.IS_WORKING_KEY, true);

        checkBox.setChecked(isWorking);

        name.setText(text);
        address.setText(adr);

        byte[] imageBytes = Base64.decode(img, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        view.setImageBitmap(decodedImage);

        String[] adds = getResources().getStringArray(R.array.ads_array);
        ArrayList<String> ads2 = new ArrayList<>(Arrays.asList(adds));
        addList.setAdapter(new AdsAdapter(this, R.layout.list_item, ads2));

        ApiCaller.makeStationApiCall(stationId, this);
    }

    public void streetView(View view) {
        Uri uri = Uri.parse(this.streetViewUrl);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void setStatus(View view) {
        String newState = "true";

        CheckBox checkBox = (CheckBox) view;
        boolean isChecked = checkBox.isChecked();
        if (isChecked) {
            newState = "true";
        } else {
            newState = "false";

        }
        ApiCaller.makeStationChangeApiCall(stationId, pointId, newState, this);
    }

    @Override
    public void onResponse(Call<Station> call, Response<Station> response)
    {
        Station station = response.body();
        checkBox.setChecked(station.findPoint(pointId).open);
    }

    @Override
    public void onFailure(Call<Station> call, Throwable t)
    {
        t.printStackTrace();
    }
}
