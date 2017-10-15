package com.example.konrad.metroperyskop;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdsAdapter extends ArrayAdapter<String> {
    ArrayList<String> stationList = new ArrayList<>();
    int positionChange;

    public AdsAdapter(Context context, int textViewResourceId,
                      ArrayList<String> objects, int pos) {
        super(context, textViewResourceId, objects);
        stationList = objects;
        positionChange = pos;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.adw, null);
        TextView textView = (TextView) v.findViewById(R.id.text_item);
        ImageView imageView = (ImageView) v.findViewById(R.id.image_item);
        textView.setText(stationList.get(position));
        imageView.setBackground(this.getContext().getDrawable(R.drawable.ic_r));
        return v;
    }
}