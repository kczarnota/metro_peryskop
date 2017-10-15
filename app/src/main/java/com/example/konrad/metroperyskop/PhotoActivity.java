package com.example.konrad.metroperyskop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity {

    public String streetViewUrl = null;

    @BindView(R.id.image)
    ImageView view;
    @BindView(R.id.photo_text)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String img = ActivityResults.result;
        String text = intent.getStringExtra(ScanActivity.TEXT_KEY);
        this.streetViewUrl = intent.getStringExtra(ScanActivity.URL_KEY);

        textView.setText(text);

        byte[] imageBytes = Base64.decode(img, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        view.setImageBitmap(decodedImage);
    }

    public void streetView(View view) {
        Uri uri = Uri.parse(this.streetViewUrl);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
