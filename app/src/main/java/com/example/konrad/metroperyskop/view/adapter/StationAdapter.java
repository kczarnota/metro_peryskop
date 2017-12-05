package com.example.konrad.metroperyskop.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konrad.metroperyskop.R;

import java.util.ArrayList;

public class StationAdapter extends ArrayAdapter<String>
{
    ArrayList<String> stationList = new ArrayList<>();
    int positionChange;

    public StationAdapter(Context context, int textViewResourceId,
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
        v = inflater.inflate(R.layout.list_item, null);
        TextView textView = (TextView) v.findViewById(R.id.text_item);
        ImageView imageView = (ImageView) v.findViewById(R.id.image_item);
        textView.setText(stationList.get(position));
        if(position >= positionChange)
            imageView.setBackground(this.getContext().getDrawable(R.drawable.ic_stacja_lista_red2_01));
        else
            imageView.setBackground(this.getContext().getDrawable(R.drawable.ic_stacja_lista_blue_2_01));
        return v;
    }
}
