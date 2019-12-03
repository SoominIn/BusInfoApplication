package com.example.mygraduationapplication;

public class ListStationData {

    private static final String TAG = "ListStationData";

    private int input_index;

    public String[] mData;
    private boolean mSelectable = true;

    public ListStationData() { mData = new String[6]; }

    public ListStationData(String str) {
        mData = new String[1];
        mData[0] = str;
    }

    // 동일한 정류장 이름이 여러개 존재하는 경우
    public ListStationData(String Data_Col0, String Data_Col1) {
        mData = new String[2];
        mData[0] = Data_Col0;  // 정류장 이름
        mData[1] = Data_Col1;  // 정류장 지역
    }


    // 정류장에 지나가는 버스 목록 출력
    public ListStationData(String Data_Col0, String Data_Col1, String Data_Col2, String Data_Col3, String Data_Col4, String Data_Col5, String Data_Col6) {
        mData = new String[7];
        mData[0] = Data_Col0;  // 정류장 이름
        mData[1] = Data_Col1;  // 정류장 지역
        mData[2] = Data_Col2;  // 버스 번호
        mData[3] = Data_Col3;  // 현재 위치1
        mData[4] = Data_Col4;  // 현재 위치2
        mData[5] = Data_Col5;  // 도착 시간1
        mData[6] = Data_Col6;  // 도착 시간2
    }

    public ListStationData(String[] obj) {
        mData = obj;
    }

    public boolean isSelectable() {
        return mSelectable;
    }

    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

    public String getData() {
        if (mData == null) {
            return null;
        }
        return mData[0];
    }

    public String getData(int index) {
        if (mData == null || index >= mData.length) {
            return null;
        }

        return mData[index];
    }

    public void setData(String[] obj) {
        mData = obj;
    }

    public void setIndex(int index) { input_index = index; }

    public int getIndex(){ return this.input_index; }

}
