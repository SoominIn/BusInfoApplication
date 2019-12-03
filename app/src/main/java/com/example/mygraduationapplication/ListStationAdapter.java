package com.example.mygraduationapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListStationAdapter extends BaseAdapter {

    private static final String TAG = "ListStationAdapter";

    private Context mContext;
    private int mNumber;
    public int mPosition = 0;
    private int mIndex = 0;

    private List<ListStationData> mItems = new ArrayList<ListStationData>();

    public ListStationAdapter(Context context, int number) {   // 0 : 정류장 지역 선택, 1 : 특정 정류장에 버스 목록 출력
        mContext = context;
        mNumber = number;
    }

    @Override
    public int getCount() { return mItems.size(); }

    @Override
    public Object getItem(int position) { return mItems.get(position); }

    @Override
    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ListStationView itemView = null;

        view = getSubCategoryView(position, convertView, itemView);
        return view;
    }

    public View getSubCategoryView(int position, View convertView, ListStationView itemView){
        View mItemView = null;

        if (mNumber == 0) {    // 정류장 지역 선택
            //listitem 레이아웃을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                itemView = new ListStationView(mContext,mItems.get(position), 0);
            } else {
                itemView = (ListStationView) convertView;
                itemView.setText(mItems.get(position).getData(0), mItems.get(position).getData(1));   // 정류장 이름 + 정류장 지역
            }
        } else {
            if (convertView == null) {
                itemView = new ListStationView(mContext,mItems.get(position), 1);
            } else {
                itemView = (ListStationView) convertView;
                itemView.setText(0, mItems.get(position).getData(2));   // 버스 이름
                itemView.setText(1, mItems.get(position).getData(3));   // 현재 위치1
                itemView.setText(2, mItems.get(position).getData(4));   // 현재 위치2
                itemView.setText(3, mItems.get(position).getData(5));   // 도착 시간1
                itemView.setText(4, mItems.get(position).getData(6));   // 도착 시간2
            }
        }

        mItemView = itemView;

        return mItemView;
    }

    public void clear() { mItems.clear(); }

    public int getPosition(){ return mPosition; }

    public void setListItems(List<ListStationData> lit) { mItems = lit; }

    public boolean areAllItemsSelectable() { return false; }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public void addItem(ListStationData it) { mItems.add(it); }
}

