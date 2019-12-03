package com.example.mygraduationapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuListDataView extends LinearLayout {

    private static final String TAG = "MenuListDataView";

    private TextView textdata;

    public MenuListDataView(Context context, ListData data, int number) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.list_menu, this, true);
        textdata = (TextView) findViewById(R.id.MainItem);

        textdata.setText(data.getData());
    }

    public void setText(String data) { textdata.setText(data); }
}
