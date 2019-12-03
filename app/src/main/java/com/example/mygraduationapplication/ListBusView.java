package com.example.mygraduationapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListBusView extends LinearLayout {

    private static final String TAG = "ListBusView";

    private TextView textdata;

    public ListBusView(Context context, ListBusData data) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.list_bus, this, true);
        textdata = (TextView) findViewById(R.id.station_name);

        textdata.setText(data.getData());
    }

    public void setText(String data) { textdata.setText(data); }
}