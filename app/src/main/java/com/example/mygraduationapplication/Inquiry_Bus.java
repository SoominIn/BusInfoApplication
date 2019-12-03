package com.example.mygraduationapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Inquiry_Bus extends AppCompatActivity {    // 버스 혹은 정류장 검색하는 화면

    private static final String TAG = "Inquiry_Bus";

    private static String IP_ADDRESS = "172.20.10.9";

    private int category1;

    private ActionBar bar;
    private Intent intent;

    private EditText searchInfo;
    private Spinner search_spinner;
    private String str_search;

    public String busId;
    public String busRegion;
    public String busname;

    public String stationName;
    public String stationId;
    public String stationRegion;

    // station 출력할 때
    private ListStationAdapter station_adapter;
    private ListStationAdapter buslist_adapter;
    private ListView StaListView;

    // bus 출력할 때
    private ListBusAdapter bus_adapter;
    private ListView BusListView;

    private TextView textview;
    private String stationlist;
    private int count;

    private String add_station;
    private String[] station_name;
    private String add_stationid;
    private String[] station_id;

    private String add_buslocation;
    private String add_bustype;
    private String[] bus_location;
    private String[] bus_type;

    private int[] arrayint = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private int[] arr_locationnum;

    private int[] int_bus_location;

    private String add_buslist;

    private String[] routeId;
    private String[] routeName = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
            "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
            "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
            "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
    private String[] routelocation;

    private String[] locationNo1;
    private String[] locationNo2;
    private String[] lowPlate1;
    private String[] lowPlate2;
    private String[] predictTime1;
    private String[] predictTime2;

    private TextView print_stationName;
    private TextView print_stationRegion;

    private LinearLayout create_button;

    private String search_station;

    private String[] result_stationName;
    private String[] result_stationId;
    private String[] result_stationRegion;

    private ArrayList<String> stationIDArray = new ArrayList<String>();
    private ArrayList<String> stationRegionArray = new ArrayList<String>();

    private String[] point_id = new String[30];
    private String[] point_name= new String[30];
    private String[] point_region = new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        bar = getSupportActionBar();

        intent = getIntent();
        if (intent != null) {
            category1 = intent.getIntExtra("menu1_position", 0);   // 0 : 버스검색, 1 : 정류장검색
        }

        if(category1==0){
            bar.setTitle("버스 검색");
            setContentView(R.layout.search_info);
            bus_adapter  = new ListBusAdapter(this, 0);
            BusListView = (ListView) findViewById(R.id.ListView_ItemList);
            create_button = (LinearLayout)findViewById(R.id.create_button);
        } else {
            bar.setTitle("정류장 검색");
            setContentView(R.layout.search_info2);
            station_adapter  = new ListStationAdapter(this, 0);
            buslist_adapter = new ListStationAdapter(this, 1);
            StaListView = (ListView) findViewById(R.id.ListView_ItemList);
            create_button = (LinearLayout)findViewById(R.id.create_button);
        }

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

        print_stationName = (TextView)findViewById(R.id.busstatioin_name);
        print_stationRegion = (TextView)findViewById(R.id.busstatioin_region);

        searchInfo = (EditText) findViewById(R.id.searchInfo);

        search_spinner = (Spinner)findViewById(R.id.SearchSpinner);

        ArrayAdapter<CharSequence> sp_adapter = ArrayAdapter.createFromResource(this, R.array.search_array, android.R.layout.simple_spinner_item);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_spinner.setSelection(0);
        search_spinner.setAdapter(sp_adapter);

        search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                str_search=search_spinner.getSelectedItem().toString();

                Log.d("Selected item : ", str_search);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                str_search = "저상 버스";

                Log.d("No Selected item : ", str_search);
            }
        });

        // 버스 번호, 정류장 이름 검색
        searchInfo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){

                    // CASE 1 : 버스검색 -> 저상버스
                    if((category1==0) && (str_search.equals("저상 버스"))){

                        String search_bus = searchInfo.getText().toString();
                        int name_num = 0;

                        try {
                            busname = new Request_BusInformation().execute(search_bus, "routeName").get();
                        } catch (Exception e) {
                            MakeDialog();
                        }
                        Log.d(TAG,"###### 버스이름 호출" + busname);
                        String[] split_name = busname.split("\\n", -1);
                        int[] nameCOUNT = new int[20];

                        for (int i=0; i<split_name.length-2;i++){
                            if(split_name[i].equals(search_bus)){
                                nameCOUNT[name_num] = i;
                                name_num++;
                            }
                        }

                        try {
                            busRegion = new Request_BusInformation().execute(search_bus, "regionName").get();
                        } catch (Exception e) {
                            MakeDialog();
                        }
                        Log.d(TAG,"###### 버스지역 호출" + busRegion);

                        try {
                            busId = new Request_BusInformation().execute(search_bus, "routeId").get();
                        } catch (Exception e) {
                            MakeDialog();
                        }

                        Log.d(TAG,"###### 버스ID 호출" + busId);

                        String[] result_busId = busId.split("\\n", -1);
                        String[] temp_busId= new String[name_num];
                        for (int j = 0; j<name_num; j++){
                            temp_busId[j] = result_busId[nameCOUNT[j]];
                        }
                        String[] result_busRegion = busRegion.split("\\n", -1);
                        String[] temp_busRegion= new String[name_num];
                        for (int j = 0; j<name_num; j++){
                            temp_busRegion[j] = result_busRegion[nameCOUNT[j]];
                        }

                        if((result_busRegion.length)-2 == 0){     // 버스 번호가 존재하지 않는 경우
                            MakeDialog();
                        } else if ((result_busRegion.length)-2 == 1){    // 버스 번호가 하나만 존재하는 경우
                            printBusLowBus(result_busId[0], result_busRegion[0], search_bus);

                            // 최근 검색한 버스 정류장 목록 추가

                            String json = "";

                            try {
                                json = new Mysql_Service_CurrentBus().execute("http://" + IP_ADDRESS + "/recentbus.php", search_bus ,result_busRegion[0]).get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("update", json);

                        } else if ((result_busRegion.length)-2 > 1){     // 동일한 버스 번호가 여러개 존재하는 경우
                            printSelectbusEntireBus(temp_busId, temp_busRegion, search_bus, 0);
                        }

                    }

                    // CASE 2 : 버스검색 -> 전체버스
                    else if((category1==0) && (str_search.equals("모든 버스"))){

                        String search_bus = searchInfo.getText().toString();
                        int name_num = 0;

                        try {
                            busname = new Request_BusInformation().execute(search_bus, "routeName").get();
                        } catch (Exception e) {
                            MakeDialog();
                        }
                        Log.d(TAG,"###### 버스이름 호출" + busname);
                        String[] split_name = busname.split("\\n", -1);
                        int[] nameCOUNT = new int[20];

                        for (int i=0; i<split_name.length-2;i++){
                            if(split_name[i].equals(search_bus)){
                                nameCOUNT[name_num] = i;
                                name_num++;
                            }
                        }

                        try {
                            busRegion = new Request_BusInformation().execute(search_bus, "regionName").get();
                        } catch (Exception e) {
                            MakeDialog();
                        }

                        try {
                            busId = new Request_BusInformation().execute(search_bus, "routeId").get();
                        } catch (Exception e) {
                            MakeDialog();
                        }

                        Log.d(TAG,"###### 버스ID 호출" + busId);

                        String[] result_busId = busId.split("\\n", -1);
                        String[] temp_busId= new String[name_num];
                        for (int j = 0; j<name_num; j++){
                            temp_busId[j] = result_busId[nameCOUNT[j]];
                        }
                        String[] result_busRegion = busRegion.split("\\n", -1);
                        String[] temp_busRegion= new String[name_num];
                        for (int j = 0; j<name_num; j++){
                            temp_busRegion[j] = result_busRegion[nameCOUNT[j]];
                        }

                        if((result_busRegion.length)-2 == 0){     // 버스 번호가 존재하지 않는 경우
                            MakeDialog();
                        } else if ((result_busRegion.length)-2 == 1){    // 버스 번호가 하나만 존재하는 경우
                            printBusEntireBus(result_busId[0], result_busRegion[0], search_bus);

                            // 최근 검색한 버스 정류장 목록 추가

                            String json = "";

                            try {
                                json = new Mysql_Service_CurrentBus().execute("http://" + IP_ADDRESS + "/recentbus.php", search_bus ,result_busRegion[0]).get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("update", json);
                        } else if ((result_busRegion.length)-2 > 1){     // 동일한 버스 번호가 여러개 존재하는 경우
                            printSelectbusEntireBus(temp_busId, temp_busRegion, search_bus, 1);
                        }

                    }

                    // CASE 3 : 정류장검색 -> 저상버스
                    else if((category1==1) && (str_search.equals("저상 버스"))){

                        String search_station = searchInfo.getText().toString();

                        try {    // 정류장 이름 검색 결과
                            stationName = new Request_SearchSt().execute(search_station, "stationName").get();
                            Log.d(TAG,"###### CASE 3 : 정류장이름 호출" + stationName);
                        } catch (Exception e) {
                            MakeDialog();
                        }
                        result_stationName = stationName.split("\\n", -1);

                        if((result_stationName.length)-2 == 0){     // 정류장 이름이 존재하지 않는 경우
                            MakeDialog();
                        } else if ((result_stationName.length)-2 == 1){    // 정류장 이름이 하나만 존재하는 경우 + 정류장 결과가 검색한 값과 일치하지 않는 경우

                            // 정류장 결과가 검색한 값과 일치하는 경우
                            if (result_stationName[0].equals(search_station)){

                                try {    // 정류장Id 검색 결과
                                    stationId = new Request_SearchSt().execute(result_stationName[0], "stationId").get();
                                    Log.d(TAG,"###### CASE 3 : 정류장Id 호출" + stationId);
                                } catch (Exception e) {
                                    MakeDialog();
                                }
                                result_stationId = stationId.split("\\n", -1);

                                try {    // 정류장 지역 검색 결과
                                    stationRegion = new Request_SearchSt().execute(result_stationName[0], "regionName").get();
                                    Log.d(TAG,"###### CASE 3 : 정류장지역 호출" + stationRegion);
                                } catch (Exception e) {
                                    MakeDialog();
                                }
                                result_stationRegion = stationRegion.split("\\n", -1);

                                printStationLowBus(result_stationId[0], search_station, result_stationRegion[0]);

                                // 최근 검색한 버스 정류장 목록 추가

                                String json = "";

                                try {
                                    json = new Mysql_Service_CurrentStation().execute("http://" + IP_ADDRESS + "/recentstation.php", search_station ,result_stationRegion[0]).get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Log.e("update", json);
                            }

                            // 정류장 결과가 검색한 값과 일치하지 않는 경우
                            else {

                                try {    // 정류장Id 검색 결과
                                    stationIDArray.add(new Request_SearchSt().execute(result_stationName[0], "stationId").get());
                                    Log.d(TAG,"###### CASE 3 : 정류장Id 호출" + stationId);
                                } catch (Exception e) {
                                    MakeDialog();
                                }

                                try {    // 정류장 지역 검색 결과
                                    stationRegionArray.add(new Request_SearchSt().execute(result_stationName[0], "regionName").get());
                                    Log.d(TAG,"###### CASE 3 : 정류장지역 호출" + stationRegion);
                                } catch (Exception e) {
                                    MakeDialog();
                                }

                                String[] id = stationIDArray.get(0).split("\\n", -1);
                                String[] region = stationRegionArray.get(0).split("\\n", -1);

                                printStationLowBus(id[0], result_stationName[0], region[0]);

                                // 최근 검색한 버스 정류장 목록 추가

                                String json = "";

                                try {
                                    json = new Mysql_Service_CurrentStation().execute("http://" + IP_ADDRESS + "/recentstation.php", result_stationName[0] ,region[0]).get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Log.e("update", json);
                            }

                        } else if ((result_stationName.length)-2 > 1){     // 동일한 정류장 이름이 여러개 존재하는 경우
                            //MakeDialog();

                            for (int i = 0; i<result_stationName.length-2; i++){
                                try {    // 정류장Id 검색 결과
                                    stationIDArray.add(new Request_SearchSt().execute(result_stationName[i], "stationId").get());
                                    Log.d(TAG,"###### CASE 3 : 정류장Id 호출" + stationId);
                                } catch (Exception e) {
                                    MakeDialog();
                                }
                            }

                            for (int i = 0; i<result_stationName.length-2; i++){
                                try {    // 정류장 지역 검색 결과
                                    stationRegionArray.add(new Request_SearchSt().execute(result_stationName[0], "regionName").get());
                                    Log.d(TAG,"###### CASE 3 : 정류장지역 호출" + stationRegion);
                                } catch (Exception e) {
                                    MakeDialog();
                                }
                            }

                            printSelectStationEntireBus(stationIDArray, stationRegionArray, result_stationName, 0);
                        }

                    }

                    // CASE 4 : 정류장검색 -> 전체버스
                    else {

                        String search_station = searchInfo.getText().toString();

                        try {    // 정류장 이름 검색 결과
                            stationName = new Request_SearchSt().execute(search_station, "stationName").get();
                            Log.d(TAG,"###### CASE 4 : 정류장이름 호출" + stationName);
                        } catch (Exception e) {
                            MakeDialog();
                        }
                        result_stationName = stationName.split("\\n", -1);

                        if((result_stationName.length)-2 == 0){     // 정류장 이름이 존재하지 않는 경우
                            MakeDialog();
                        } else if ((result_stationName.length)-2 == 1){    // 정류장 이름이 하나만 존재하는 경우

                            // 검색한 값과 정류장 검색값이 같은 경우
                            if (result_stationName[0].equals(search_station)){

                                try {    // 정류장Id 검색 결과
                                    stationId = new Request_SearchSt().execute(result_stationName[0], "stationId").get();
                                    Log.d(TAG,"###### CASE 4 : 정류장Id 호출" + stationId);
                                } catch (Exception e) {
                                    MakeDialog();
                                }
                                result_stationId = stationId.split("\\n", -1);

                                try {    // 정류장 지역 검색 결과
                                    stationRegion = new Request_SearchSt().execute(result_stationName[0], "regionName").get();
                                    Log.d(TAG,"###### CASE 4 : 정류장지역 호출" + stationRegion);
                                } catch (Exception e) {
                                    MakeDialog();
                                }
                                result_stationRegion = stationRegion.split("\\n", -1);

                                printStationEntireBus(result_stationId[0], search_station, result_stationRegion[0]);

                                // 최근 검색한 버스 정류장 목록 추가

                                String json = "";

                                try {
                                    json = new Mysql_Service_CurrentStation().execute("http://" + IP_ADDRESS + "/recentstation.php", search_station ,result_stationRegion[0]).get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Log.e("update", json);
                            }

                            // 검색한 값과 정류장 검색값이 같지 않은 경우
                            else {

                                try {    // 정류장Id 검색 결과
                                    stationId = new Request_SearchSt().execute(result_stationName[0], "stationId").get();
                                    Log.d(TAG,"###### CASE 4 : 정류장Id 호출" + stationId);
                                } catch (Exception e) {
                                    MakeDialog();
                                }
                                result_stationId = stationId.split("\\n", -1);

                                try {    // 정류장 지역 검색 결과
                                    stationRegion = new Request_SearchSt().execute(result_stationName[0], "regionName").get();
                                    Log.d(TAG,"###### CASE 4 : 정류장지역 호출" + stationRegion);
                                } catch (Exception e) {
                                    MakeDialog();
                                }
                                result_stationRegion = stationRegion.split("\\n", -1);

                                printStationEntireBus(result_stationId[0], result_stationName[0], result_stationRegion[0]);

                                // 최근 검색한 버스 정류장 목록 추가

                                String json = "";

                                try {
                                    json = new Mysql_Service_CurrentStation().execute("http://" + IP_ADDRESS + "/recentstation.php", result_stationName[0] ,result_stationRegion[0]).get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Log.e("update", json);
                            }


                        } else if ((result_stationName.length)-2 > 1){     // 동일한 정류장 이름이 여러개 존재하는 경우
                            //MakeDialog();

                            for (int i = 0; i<result_stationName.length-2; i++){
                                try {    // 정류장Id 검색 결과
                                    stationIDArray.add(new Request_SearchSt().execute(result_stationName[i], "stationId").get());
                                    Log.d(TAG,"###### CASE 4 : 정류장Id 호출" + stationIDArray.get(i));
                                } catch (Exception e) {
                                    MakeDialog();
                                }

                                /*ex. '수일여중' -> '수일여중', '수일여중' ,'수일여중. 대우건설기술연구원..', '구일여중.한빛편요양병원,경기도융합...'
                                 *      그 때의 stationIDArray에 ID값 4개 들어감*/
                            }

                            for (int i = 0; i<result_stationName.length-2; i++){
                                try {    // 정류장 지역 검색 결과
                                    stationRegionArray.add(new Request_SearchSt().execute(result_stationName[i], "regionName").get());
                                    Log.d(TAG,"###### CASE 4 : 정류장지역 호출" + stationRegionArray.get(i));
                                } catch (Exception e) {
                                    MakeDialog();
                                }
                            }

                            printSelectStationEntireBus(stationIDArray, stationRegionArray, result_stationName, 1);
                        }

                    }
                    return true;
                } else { return false; }

            }
        });

    }

    // 버스 검색, 저상 버스 출력 (버스 번호가 하나만 존재하는 경우)
    public void printBusLowBus(final String bus_id, final String bus_region, final String bus_name){

        bus_adapter.clear();

        final Button button = new Button(this);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        create_button.setLayoutParams(param);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(400,130);

        button.setLayoutParams(lp);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf"); //asset > fonts 폴더 내 폰트파일 적용
        button.setTypeface(typeFace);
        button.setBackground(this.getResources().getDrawable(R.drawable.phpbutton_border));
        button.setText("데이터 웹페이지로 보내기");
        create_button.addView(button);

        textview = (TextView)findViewById(R.id.bus_location);

        class NewRunnable implements Runnable {

            @Override
            public void run() {
                stationlist = "<< 현재 운행 중인 저상 버스 위치 >>\n\n";

                try{   // 현재 버스 위치 정보 목록 조회
                    add_buslocation = new Request_BusLocation().execute(bus_id, "stationSeq").get();
                } catch (Exception e){
                    MakeDialog();
                }
                bus_location = add_buslocation.split("\\n", -1);

                int_bus_location = new int[bus_location.length-2];
                arr_locationnum = new int[int_bus_location.length];

                for (int i = 0; i<bus_location.length-2; i++){
                    int_bus_location[i] = Integer.parseInt(bus_location[i]);
                }

                int temp, count = 0;
                for (int i = 0; i < bus_location.length - 3; i++) {
                    count = 0;
                    count += i;
                    for (int j = i + 1; j < bus_location.length - 2; j++) {
                        if (int_bus_location[i] > int_bus_location[j]) {
                            temp = int_bus_location[i];
                            int_bus_location[i] = int_bus_location[j];
                            int_bus_location[j] = temp;

                            arr_locationnum[count] = i;
                            count++;
                        }
                    }
                }

                if(arr_locationnum[(bus_location.length - 3)] == 0){
                    arr_locationnum[(bus_location.length - 3)] = (bus_location.length - 3);
                }

                try{   // 버스가 저상버스인지 확인
                    add_bustype = new Request_BusLocation().execute(bus_id, "lowPlate").get();
                } catch (Exception e){
                    MakeDialog();
                }
                bus_type = add_bustype.split("\\n", -1);

                count = 0;

                for(int i=0; i<(bus_type.length-2); i++){
                    if(bus_type[i].equals("1")){
                        int location_num = arr_locationnum[i];
                        int location = Integer.parseInt(bus_location[location_num]);

                        arrayint[count] = location + 1;

                        count++;
                    }else{   }
                }

                try {   // 현재 버스가 경유하는 정류소명 출력
                    add_station = new Request_StInformation().execute(bus_id, "stationName").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                station_name = add_station.split("\\n", -1);

                button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO : click event

                        String json = "";

                        for (int i=0; i<(station_name.length-2); i++){
                            try {
                                json = new Mysql_Service().execute("http://" + IP_ADDRESS + "/and_con.php", bus_name ,bus_region, station_name[i]).get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("update", json);
                        }

                    }
                });

                if(arrayint[0]==0){
                    stationlist = "\n\n\n<< 현재 운행 중인 저상 버스 없음 >>\n\n\n\n";
                }else{
                    // 현재 버스 위치 (정류소) ID ->  정류소명으로 변경
                    for (int i = 0; i<count; i++){
                        int j = arrayint[i];
                        stationlist += (i+1) + "." + station_name[j] + "\n";
                    }
                }

                try {    // 현재 버스가 경유하는 정류소ID 출력  (버스 도착 정보 조회 서비스 -> 버스 도착 정보 항목 조회)
                    add_stationid = new Request_StInformation().execute(bus_id, "stationId").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                station_id = add_stationid.split("\\n", -1);
            }
        }

        NewRunnable nr = new NewRunnable() ;
        Thread t = new Thread(nr) ;
        t.start() ;

        try {
            t.join();
        } catch (InterruptedException e){
            Log.e(TAG, e.getMessage());
        }

        for (int i = 0; i<(station_name.length-2); i++){
            bus_adapter.addItem(new ListBusData(station_name[i]));
        }

        textview.setText(stationlist);
        print_stationName.setText(bus_name);
        print_stationRegion.setText(bus_region);

        BusListView.setAdapter(bus_adapter);

        listViewHeightSet(bus_adapter, BusListView);
    }

    // 버스 검색, 모든 버스 출력 (버스 번호가 하나만 존재하는 경우)
    public void printBusEntireBus(final String bus_id, final String bus_region, final String bus_name){

        bus_adapter.clear();

        final Button button = new Button(this);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        create_button.setLayoutParams(param);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(400,130);
        button.setLayoutParams(lp);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf"); //asset > fonts 폴더 내 폰트파일 적용
        button.setTypeface(typeFace);
        button.setBackground(this.getResources().getDrawable(R.drawable.phpbutton_border));
        button.setText("데이터 웹페이지로 보내기");
        create_button.addView(button);

        textview = (TextView)findViewById(R.id.bus_location);

        class NewRunnable implements Runnable {

            @Override
            public void run() {
                stationlist = "<< 현재 운행 중인 버스 위치 >>\n\n";

                try{   // 현재 버스 위치 정보 목록 조회
                    add_buslocation = new Request_BusLocation().execute(bus_id, "stationSeq").get();
                } catch (Exception e){
                    MakeDialog();
                }
                bus_location = add_buslocation.split("\\n", -1);

                try {   // 현재 버스가 경유하는 정류소명 출력
                    add_station = new Request_StInformation().execute(bus_id, "stationName").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                station_name = add_station.split("\\n", -1);

                button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO : click event

                        String json = "";

                        for (int i=0; i<(station_name.length-2); i++){
                            try {
                                json = new Mysql_Service().execute("http://" + IP_ADDRESS + "/and_con.php", bus_name, bus_region, station_name[i]).get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("update", json);
                        }

                    }
                });

                if(bus_location.length-2 == 0) {  // 현재 운행 중인 버스 X
                    stationlist = "\n\n\n<< 현재 운행 중인 버스 없음 >>\n\n\n\n";
                } else {
                    int_bus_location = new int[bus_location.length-2];
                    arr_locationnum = new int[int_bus_location.length];

                    for (int i = 0; i<int_bus_location.length; i++){
                        int_bus_location[i] = Integer.parseInt(bus_location[i]);
                    }

                    int temp, count = 0;
                    for (int i = 0; i < int_bus_location.length - 1; i++) {
                        count = 0;
                        count += i;
                        for (int j = i + 1; j < int_bus_location.length; j++) {
                            if (int_bus_location[i] > int_bus_location[j]) {
                                temp = int_bus_location[i];
                                int_bus_location[i] = int_bus_location[j];
                                int_bus_location[j] = temp;

                                arr_locationnum[count] = i;
                                count++;
                            }
                        }
                    }

                    if(arr_locationnum[(bus_location.length - 3)] == 0){
                        arr_locationnum[(bus_location.length - 3)] = (bus_location.length - 3);
                    }

                    count = 0;

                    for(int i=0; i<(bus_location.length-2); i++){
                        int location_num = arr_locationnum[i];
                        int location = Integer.parseInt(bus_location[location_num]);

                        arrayint[count] = location + 1;

                        count++;
                    }

                    // 현재 버스 위치 (정류소) ID ->  정류소명으로 변경
                    for (int i = 0; i<count; i++){
                        int j = arrayint[i];
                        stationlist += (i+1) + "." + station_name[j] + "\n";
                    }
                }

                try {    // 현재 버스가 경유하는 정류소ID 출력  (버스 도착 정보 조회 서비스 -> 버스 도착 정보 항목 조회)
                    add_stationid = new Request_StInformation().execute(bus_id, "stationId").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                station_id = add_stationid.split("\\n", -1);
            }
        }

        NewRunnable nr = new NewRunnable() ;
        Thread t = new Thread(nr) ;
        t.start() ;

        try{
            t.join();
        } catch (InterruptedException e){
            Log.e(TAG, e.getMessage());
        }

        for (int i = 0; i<(station_name.length-2); i++){
            bus_adapter.addItem(new ListBusData(station_name[i]));
        }

        textview.setText(stationlist);

        print_stationName.setText(bus_name);
        print_stationRegion.setText(bus_region);

        BusListView.setAdapter(bus_adapter);

        listViewHeightSet(bus_adapter, BusListView);
    }

    // 버스 검색 (동일한 버스 번호가 여러개 존재하는 경우)
    public void printSelectbusEntireBus(final String[] bus_id, final String[] bus_region, final String bus_name, final int num){

        print_stationName.setText("버스 지역 선택");

        bus_adapter.clear();

        for (int i = 0; i < bus_id.length; i++){
            bus_adapter.addItem(new ListBusData(bus_name+" : " + bus_region[i]));
        }

        BusListView.setAdapter(bus_adapter);

        listViewHeightSet(bus_adapter, BusListView);

        BusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d(TAG,"###### MainMenuList.setOnItemClickListener 호출");

                if (num==0){
                    printBusLowBus(bus_id[position], bus_region[position], bus_name);

                    // 최근 검색한 버스 정류장 목록 추가

                    String json = "";

                    try {
                        json = new Mysql_Service_CurrentBus().execute("http://" + IP_ADDRESS + "/recentbus.php", bus_name ,bus_region[position]).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("update", json);

                } else {
                    printBusEntireBus(bus_id[position], bus_region[position], bus_name);

                    // 최근 검색한 버스 정류장 목록 추가

                    String json = "";

                    try {
                        json = new Mysql_Service_CurrentBus().execute("http://" + IP_ADDRESS + "/recentbus.php", bus_name ,bus_region[position]).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("update", json);
                }

            }
        });
    }

    // 정류장 검색, 저상 버스 출력 (정류장 이름이 하나만 존재하는 경우)
    public void printStationLowBus(final String station_id, final String station_name, final String station_region){

        buslist_adapter.clear();

        final Button button = new Button(this);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        create_button.setLayoutParams(param);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(400,130);
        button.setLayoutParams(lp);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf"); //asset > fonts 폴더 내 폰트파일 적용
        button.setTypeface(typeFace);
        button.setBackground(this.getResources().getDrawable(R.drawable.phpbutton_border));
        button.setText("데이터 웹페이지로 보내기");
        create_button.addView(button);

        class NewRunnable implements Runnable {

            @Override
            public void run() {
                try {   // 버스 ID 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "routeId").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                routeId = add_buslist.split("\\n", -1);

                for (int i = 0; i < routeId.length - 2; i++) {
                    try {   // 버스 ID -> 버스 Name 변경
                        add_buslist = new Request_ChangeBusId().execute(routeId[i], "routeName").get();

                        String[] result = add_buslist.split("\\n", -1);

                        routeName[i] = result[0];
                    } catch (Exception e) {
                        MakeDialog();
                    }
                }

                try {   // 첫번째 차량 위치 정보
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "locationNo1").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                locationNo1 = add_buslist.split("\\n", -1);

                try {   // 두번째 차량 위치 정보
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "locationNo2").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                locationNo2 = add_buslist.split("\\n", -1);

                // 차량 위치 정보 (각 노선의 몇번째 정류장인지 출력해줌)
                // 각 차량의 전체 노선 parsing하여 입력해야함..
                for (int i = 0; i < routeId.length - 2; i++) {
                    try {
                        add_buslist = new Request_StInformation().execute(routeId[i], "stationName").get();
                    } catch (Exception e) {
                        MakeDialog();
                    }
                }
                routelocation = add_buslist.split("\\n", -1);

                try {   // 첫번째차량 저상버스여부 (0:일반버스, 1:저상버스)
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "lowPlate1").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                lowPlate1 = add_buslist.split("\\n", -1);

                try {   // 두번째차량 저상버스여부 (0:일반버스, 1:저상버스)
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "lowPlate2").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                lowPlate2 = add_buslist.split("\\n", -1);

                try {   // 첫번째차량 버스도착예정시간 (몇분후 도착예정)
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "predictTime1").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                predictTime1 = add_buslist.split("\\n", -1);

                try {   // 두번째차량 버스도착예정시간 (몇분후 도착예정)
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "predictTime2").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                predictTime2 = add_buslist.split("\\n", -1);

                button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO : click event

                        String json = "";

                        for (int i=0; i<(routeId.length-2); i++){
                            try {
                                json = new Mysql_Service().execute("http://" + IP_ADDRESS + "/and_con1.php", routeName[i] ,station_region, station_name).get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("update", json);
                        }

                    }
                });

                for (int i = 0; i < predictTime2.length - 2; i++) {
                    if (predictTime2[i].equals("null")) {
                        predictTime2[i] = "정보 없음";
                    }
                }
            }
        }

        NewRunnable nr = new NewRunnable() ;
        Thread t = new Thread(nr) ;
        t.start() ;

        try {
            t.join();
        } catch (InterruptedException e){
            Log.e(TAG, e.getMessage());
        }

        if (lowPlate1.length - 2 > 0) {
            for (int i = 0; i < lowPlate1.length - 2; i++) {
                int number1 = Integer.parseInt(lowPlate1[i]);
                int number2 = Integer.parseInt(lowPlate2[i]);
                if (locationNo1[i].equals("null") && locationNo2[i].equals("null")) {
                    String text = "현재, 해당 정류장에 " + routeName[i] + "번 저상 버스 도착 예정이 없습니다.";

                    buslist_adapter.addItem(new ListStationData(station_name, station_region, text, "0", "0", "0", "0"));
                } else if (locationNo2[i].equals("null") && (number1 == 1)) {
                    int location1 = Integer.parseInt(locationNo1[i]);

                    buslist_adapter.addItem(new ListStationData(station_name, station_region, routeName[i],routelocation[location1 + 1], "0", predictTime1[i], "0"));
                } else if ((number1 == 1) && (number2 == 1)) {
                    int location1 = Integer.parseInt(locationNo1[i]);
                    int location2 = Integer.parseInt(locationNo2[i]);

                    buslist_adapter.addItem(new ListStationData(station_name, station_region, routeName[i],routelocation[location1 + 1], routelocation[location2 + 1], predictTime1[i], predictTime2[i]));
                } else if ((number1 == 1) && (number2 == 0)) {
                    int location1 = Integer.parseInt(locationNo1[i]);

                    buslist_adapter.addItem(new ListStationData(station_name, station_region, routeName[i],routelocation[location1 + 1], "0", predictTime1[i], "0"));
                } else if ((number1 == 0) && (number2 == 1)) {
                    int location2 = Integer.parseInt(locationNo2[i]);

                    buslist_adapter.addItem(new ListStationData(station_name, station_region, routeName[i], "0", routelocation[location2 + 1], "0", predictTime2[i]));
                } else {
                }
            }
        } else {
            create_button.removeView(button);

            String text = "현재, 해당 정류장에 저상 버스 도착 예정이 없습니다.";

            buslist_adapter.addItem(new ListStationData(station_name, station_region, text, "0", "0", "0", "0"));
        }

        print_stationName.setText(station_name);
        print_stationRegion.setText(station_region);

        StaListView.setAdapter(buslist_adapter);

        listViewHeightSet(buslist_adapter, StaListView);
    }

    // 정류장 검색, 모든 버스 출력 (정류장 이름이 하나만 존재하는 경우)
    public void printStationEntireBus(final String station_id, final String station_name, final String station_region){

        buslist_adapter.clear();

        final Button button = new Button(this);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        create_button.setLayoutParams(param);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(400,130);
        button.setLayoutParams(lp);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf"); //asset > fonts 폴더 내 폰트파일 적용
        button.setTypeface(typeFace);
        button.setBackground(this.getResources().getDrawable(R.drawable.phpbutton_border));
        button.setText("데이터 웹페이지로 보내기");
        create_button.addView(button);

        class NewRunnable implements Runnable {

            @Override
            public void run() {
                try {   // 버스 ID 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "routeId").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                routeId = add_buslist.split("\\n", -1);

                for (int i = 0; i < routeId.length - 2; i++) {
                    try {   // 버스 ID -> 버스 Name 변경
                        add_buslist = new Request_ChangeBusId().execute(routeId[i], "routeName").get();

                        String[] result = add_buslist.split("\\n", -1);

                        routeName[i] = result[0];
                    } catch (Exception e) {
                        MakeDialog();
                    }
                }

                try {   // 첫번째 차량 위치 정보
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "locationNo1").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                locationNo1 = add_buslist.split("\\n", -1);

                try {   // 두번째 차량 위치 정보
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "locationNo2").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                locationNo2 = add_buslist.split("\\n", -1);

                // 차량 위치 정보 (각 노선의 몇번째 정류장인지 출력해줌)
                // 각 차량의 전체 노선 parsing하여 입력해야함..
                for (int i = 0; i < routeId.length - 2; i++) {
                    try {
                        add_buslist = new Request_StInformation().execute(routeId[i], "stationName").get();
                    } catch (Exception e) {
                        MakeDialog();
                    }
                }
                routelocation = add_buslist.split("\\n", -1);

                try {   // 첫번째차량 저상버스여부 (0:일반버스, 1:저상버스)
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "lowPlate1").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                lowPlate1 = add_buslist.split("\\n", -1);

                try {   // 두번째차량 저상버스여부 (0:일반버스, 1:저상버스)
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "lowPlate2").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                lowPlate2 = add_buslist.split("\\n", -1);

                try {   // 첫번째차량 버스도착예정시간 (몇분후 도착예정)
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "predictTime1").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                predictTime1 = add_buslist.split("\\n", -1);

                try {   // 두번째차량 버스도착예정시간 (몇분후 도착예정)
                    // 버스 도착 정보 조회 서비스 -> 버스 도착 정보 목록 조회
                    add_buslist = new Request_EntireBusList().execute(station_id, "predictTime2").get();
                } catch (Exception e) {
                    MakeDialog();
                }
                predictTime2 = add_buslist.split("\\n", -1);

                button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO : click event

                        String json = "";

                        for (int i=0; i<(routeId.length-2); i++){
                            try {
                                json = new Mysql_Service().execute("http://" + IP_ADDRESS + "/and_con1.php", routeName[i] ,station_region, station_name).get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("update", json);
                        }

                    }
                });

                for (int i = 0; i < predictTime2.length - 2; i++) {
                    if (predictTime2[i].equals("null")) {
                        predictTime2[i] = "정보 없음";
                    }
                }
            }
        }

        NewRunnable nr = new NewRunnable() ;
        Thread t = new Thread(nr) ;
        t.start() ;

        try {
            t.join();
        } catch (InterruptedException e){
            Log.e(TAG, e.getMessage());
        }

        if (routeId.length - 2 > 0) {
            for (int i = 0; i < lowPlate1.length - 2; i++) {
                int number1 = Integer.parseInt(lowPlate1[i]);
                int number2 = Integer.parseInt(lowPlate2[i]);
                if (locationNo1[i].equals("null") && locationNo2[i].equals("null")) {
                    String text = "현재, 해당 정류장에 " + routeName[i] + "번 버스 도착 예정이 없습니다.";
                    buslist_adapter.addItem(new ListStationData(station_name, station_region, text, "0", "0", "0", "0"));
                } else if (locationNo2[i].equals("null")) {
                    int location1 = Integer.parseInt(locationNo1[i]);
                    buslist_adapter.addItem(new ListStationData(station_name, station_region, routeName[i],routelocation[location1 + 1], "0", predictTime1[i], "0"));
                } else {
                    int location1 = Integer.parseInt(locationNo1[i]);
                    int location2 = Integer.parseInt(locationNo2[i]);

                    buslist_adapter.addItem(new ListStationData(station_name, station_region, routeName[i],routelocation[location1 + 1], routelocation[location2 + 1], predictTime1[i], predictTime2[i]));
                }
            }
        } else {
            create_button.removeView(button);
            String text = "현재, 해당 정류장에 버스 도착 예정이 없습니다.";
            buslist_adapter.addItem(new ListStationData(station_name, station_region, text, "0", "0", "0", "0"));
        }

        print_stationName.setText(station_name);
        print_stationRegion.setText(station_region);

        StaListView.setAdapter(buslist_adapter);

        listViewHeightSet(buslist_adapter, StaListView);
    }

    // 정류장 검색 (동일한 정류장 이름이 여러개 존재하는 경우)
    public void printSelectStationEntireBus(final ArrayList<String> stationIDArray, final ArrayList<String> stationRegionArray, final String[] result_stationName, final int num){

        print_stationName.setText("정류장 선택");

        station_adapter.clear();

        int count_number = 0;

        for (int i = 0; i<stationRegionArray.size(); i++){
            if (i>0){
                if (result_stationName[i].equals(result_stationName[i-1])){

                } else {
                    String[] splitRegion = stationRegionArray.get(i).split("\\n", -1);
                    String[] splitID = stationIDArray.get(i).split("\\n", -1);
                    station_adapter.addItem(new ListStationData(result_stationName[i], splitRegion[0]));
                    point_id[count_number] = splitID[0];
                    point_name[count_number] = result_stationName[i];
                    point_region[count_number] = splitRegion[0];

                    count_number++;
                }
            } else {
                String[] splitRegion = stationRegionArray.get(i).split("\\n", -1);
                String[] splitID = stationIDArray.get(i).split("\\n", -1);
                station_adapter.addItem(new ListStationData(result_stationName[i], splitRegion[0]));
                point_id[0] = splitID[0];
                point_name[0] = result_stationName[0];
                point_region[0] = splitRegion[0];

                count_number++;
            }
        }

        StaListView.setAdapter(station_adapter);

        listViewHeightSet(station_adapter, StaListView);

        StaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d(TAG,"###### MainMenuList.setOnItemClickListener 호출");

                if (num==0){
                    printStationLowBus(point_id[position], point_name[position], point_region[position]);

                    // 최근 검색한 버스 정류장 목록 추가

                    String json = "";

                    try {
                        json = new Mysql_Service_CurrentStation().execute("http://" + IP_ADDRESS + "/recentstation.php", point_name[position] ,point_region[position]).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("update", json);

                } else {
                    printStationEntireBus(point_id[position], point_name[position], point_region[position]);

                    // 최근 검색한 버스 정류장 목록 추가

                    String json = "";

                    try {
                        json = new Mysql_Service_CurrentStation().execute("http://" + IP_ADDRESS + "/recentstation.php", point_name[position] ,point_region[position]).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("update", json);
                }

            }
        });
    }

    // 옳지 않은 검색어 입력 시, Dialog 생성
    public void MakeDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("올바른 검색어를 입력해 주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).setTitle("안내");

        final AlertDialog alert = alt_bld.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });

        alert.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        Log.d(TAG, "######LOG####### onCreateOptionMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search_bus) {

            Intent intent = new Intent(getApplicationContext(), Show_Current_Bus.class);

            startActivity(intent);

            return true;
        } else if (id == R.id.action_search_station) {

            Intent intent = new Intent(getApplicationContext(), Show_Current_Station.class);

            startActivity(intent);

            return true;
        } else if (id == R.id.home){
            onBackPressed();
            return true;
        } else {
            Log.d(TAG, "###### WARNING ####### 선택한 메뉴와 맞는 id를 찾지 못했음");
        }

        return super.onOptionsItemSelected(item);
    }

}