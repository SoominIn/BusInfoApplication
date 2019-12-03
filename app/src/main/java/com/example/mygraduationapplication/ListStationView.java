package com.example.mygraduationapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListStationView extends LinearLayout {

    private static final String TAG = "ListStationView";

    private TextView bus_name;
    private TextView bus_location1;
    private TextView bus_time1;
    private TextView bus_location2;
    private TextView bus_time2;


    public ListStationView(Context context, ListStationData data, int num) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.list_station, this, true);

        bus_name = (TextView) findViewById(R.id.bus_name);
        bus_location1 = (TextView) findViewById(R.id.bus_location1);
        bus_time1 = (TextView) findViewById(R.id.bus_time1);
        bus_location2 = (TextView) findViewById(R.id.bus_location2);
        bus_time2 = (TextView) findViewById(R.id.bus_time2);

        /*
         * 정류장 이름
           정류장 지역
           버스 번호
           현재 위치1
           현재 위치2
           도착 시간1
         * 도착 시간2
         */

        if (num == 0) { // 동일한 정류장 이름이 여러개 존재하는 경우
            String str = "[" + data.getData(0) + "]\n" + "(" + data.getData(1) + ")";
            bus_name.setText(str);
        } else {        // 정류장에 지나가는 버스 목록 출력

            if(data.getData(3).equals("0") && data.getData(4).equals("0") && data.getData(5).equals("0") && data.getData(6).equals("0")){
                // 현재, 해당 정류장에 버스 도착 정보가 없습니다.
                bus_name.setText(data.getData(2));
            } else if (data.getData(4).equals("0") && data.getData(6).equals("0")){
                // 첫번째 버스 정보만 있는 경우
                bus_name.setText(data.getData(2));
                bus_location1.setText(data.getData(3));
                bus_time1.setText(data.getData(5) + " 분 후");
            } else if (data.getData(3).equals("0") && data.getData(5).equals("0")){
                // 두번째 버스 정보만 있는 경우
                bus_name.setText(data.getData(2));
                bus_location2.setText(data.getData(4));
                bus_time2.setText(data.getData(6) + " 분 후");
            } else {
                bus_name.setText(data.getData(2));
                bus_location1.setText(data.getData(3));
                bus_location2.setText(data.getData(4));
                bus_time1.setText(data.getData(5) + " 분 후");
                bus_time2.setText(data.getData(6) + " 분 후");
            }
        }

    }

    public void setText(int index, String data) {
        if (index == 0) {
            bus_name.setText(data);
        } else if (index == 1) {
            if(data.equals("0")){

            } else {
                bus_location1.setText(data);
            }
        } else if (index == 2) {
            if(data.equals("0")){

            } else {
                bus_location2.setText(data);
            }
        } else if (index == 3) {
            if(data.equals("0")){

            } else {
                bus_time1.setText(data + " 분 후");
            }
        } else if (index == 4) {
            if(data.equals("0")){

            } else {
                bus_time2.setText(data + " 분 후");
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setText(String data1, String data2) {
        String str = "[" + data1 + "]\n" + "(" + data2 + ")";
        bus_name.setText(str);
    }
}
