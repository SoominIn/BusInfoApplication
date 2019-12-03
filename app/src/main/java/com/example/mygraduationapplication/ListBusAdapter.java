package com.example.mygraduationapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListBusAdapter extends BaseAdapter {

    private static final String TAG = "ListDataAdapter";

    private Context mContext;
    private int mNumber;
    public int mPosition = 0;
    private int mIndex = 0;

    private List<ListBusData> mItems = new ArrayList<ListBusData>();

    public ListBusAdapter(Context context, int number) {
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
        ListBusView itemView = null;

        view = getSubCategoryView(position, convertView, itemView);
        return view;
    }

    public View getSubCategoryView(int position, View convertView, ListBusView itemView){
        View mItemView = null;

        //listitem 레이아웃을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            itemView = new ListBusView(mContext,mItems.get(position));
        } else {
            itemView = (ListBusView) convertView;
            itemView.setText(mItems.get(position).getData());
        }
        mItemView = itemView;

        return mItemView;
    }

    public void clear() { mItems.clear(); }

    public int getPosition(){ return mPosition; }

    public void setListItems(List<ListBusData> lit) { mItems = lit; }

    public boolean areAllItemsSelectable() { return false; }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public void addItem(ListBusData it) { mItems.add(it); }
}
