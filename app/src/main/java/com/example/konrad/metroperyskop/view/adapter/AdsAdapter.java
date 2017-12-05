package com.example.konrad.metroperyskop.view.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konrad.metroperyskop.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AdsAdapter extends ArrayAdapter<String> {
    private ArrayList<String> stationList = new ArrayList<>();

    public AdsAdapter(Context context, int textViewResourceId,
                      ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        stationList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(v == null)
            v = inflater.inflate(R.layout.adw, null);
        TextView textView = v.findViewById(R.id.text_item);
        ImageView imageView = v.findViewById(R.id.image_item);
        textView.setText(stationList.get(position));

        imageView.setImageBitmap(decodeAdImage(position));
        return v;
    }

    private Bitmap decodeAdImage(int position)
    {
        int resourceID;
        if(position == 0)
            resourceID = R.raw.ad1;
        else
            resourceID = R.raw.ad2;

        InputStream is = getContext().getResources().openRawResource(resourceID);
        byte[] b = new byte[0];
        try
        {
            b = new byte[is.available()];
            is.read(b);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        String imageAsString = new String(b);
        byte[] imageBytes = Base64.decode(imageAsString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        return decodedImage;
    }
}