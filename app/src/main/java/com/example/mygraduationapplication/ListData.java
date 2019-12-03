package com.example.mygraduationapplication;

public class ListData {

    private static final String TAG = "ListData";

    public int mId;
    private int input_index;

    public String[] mData;
    private boolean mSelectable = true;

    public ListData() {
        mId=0;
        mData = new String[2];
    }

    public ListData(String str) {
        mData = new String[1];
        mData[0] = str;
    }

    public ListData(String[] obj) {
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
