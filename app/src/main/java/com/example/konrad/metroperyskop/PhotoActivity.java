package com.example.konrad.metroperyskop;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity {

    public String streetViewUrl = null;

    @BindView(R.id.image)
    ImageView view;
    @BindView(R.id.photo_name)
    TextView name;
    @BindView(R.id.photo_address)
    TextView address;
    @BindView(R.id.ads)
    ListView addList;
    @BindView(R.id.photo_checkbox)
    CheckBox checkBox;
    private String stationId;
    private String pointId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String img = ActivityResults.result;
        String text = intent.getStringExtra(ScanActivity.TEXT_KEY);
        this.streetViewUrl = intent.getStringExtra(ScanActivity.URL_KEY);
        String adr = intent.getStringExtra(ScanActivity.ADDRESS_KEY);
        this.stationId = intent.getStringExtra("station");
        this.pointId = intent.getStringExtra("point");
        boolean isWorking = intent.getBooleanExtra(ScanActivity.IS_WORKING_KEY, true);

        checkBox.setChecked(isWorking);

        name.setText(text);
        address.setText(adr);

        byte[] imageBytes = Base64.decode(img, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        view.setImageBitmap(decodedImage);

        String[] adds = getResources().getStringArray(R.array.ads_array);
        ArrayList<String> ads2 = new ArrayList<>(Arrays.asList(adds));
        addList.setAdapter(new AdsAdapter(this, R.layout.list_item, ads2, adds.length));

        HTTPConnect.call(this, "/api/station/" + this.stationId, new HttpResponseLamba() {
            @Override
            public void processRequest(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    JSONObject item = json.getJSONObject(pointId);
                    boolean isGood = item.getBoolean("open");
                    checkBox.setChecked(isGood);
                } catch (JSONException ex) {
                }
            }
        });
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
        String url = "/api/change/station/" + this.stationId + "/point/" + this.pointId + "/state/" + newState;


        HTTPConnect.call(this, url, new HttpResponseLamba() {
            @Override
            public void processRequest(String response) {
            }
        });
    }
}
