package com.example.mygraduationapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static String IP_ADDRESS = "172.20.10.9";

    private ListDataAdapter adapter = new ListDataAdapter(this, 0);
    private ListView MainMenuList;

    public String[] menu_Data = {"버스 검색", "정류장 검색"};

    private ImageButton webpageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // PUSH 알림 (FCM)
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

        webpageButton = (ImageButton) findViewById(R.id.webpageButton);
        webpageButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://" + IP_ADDRESS));

                try {
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        MainMenuList = (ListView) findViewById(R.id.main_listview);

        adapter.clear();

        adapter.addItem(new ListData(menu_Data[0]));
        adapter.addItem(new ListData(menu_Data[1]));
        adapter.addItem(new ListData("응급시설"));
        adapter.addItem(new ListData("공지사항"));


        MainMenuList.setAdapter(adapter);

        listViewHeightSet(adapter, MainMenuList);

        // 메뉴 클릭했을 때
        MainMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d(TAG,"###### MainMenuList.setOnItemClickListener 호출");

                if (position == 0 || position == 1){
                    Intent intent = new Intent(getApplicationContext(), Inquiry_Bus.class);
                    ListData menu_data = (ListData) adapter.getItem(position);

                    intent.putExtra("menu1_position", position);

                    startActivity(intent);
                } else if (position == 2){
                    Intent intent = new Intent(getApplicationContext(), Show_Emergency.class);

                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Show_Notice.class);

                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);  //your class
        startActivity(intent);
        finish();
    }

    private static void listViewHeightSet(BaseAdapter listAdapter, ListView listView){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}