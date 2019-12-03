package com.example.mygraduationapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Show_Emergency extends AppCompatActivity{

    private static final String TAG = "Show_Emergency";

    private static String IP_ADDRESS = "172.20.10.9";

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "region";
    private static final String TAG_REG = "name";
    private static final String TAG_ADD = "address";
    private static final String TAG_PHONE = "phone";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_layout);

        list = (ListView)findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        getData("http://" + IP_ADDRESS + "/jsonemergency.php");
    }

    protected void showList(){
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray((TAG_RESULTS));

            for (int i = 0; i<peoples.length(); i++){
                JSONObject c = peoples.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String region = c.getString(TAG_REG);
                String address = c.getString(TAG_ADD);
                String phone = c.getString(TAG_PHONE);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_NAME, name);
                persons.put(TAG_REG, region);
                persons.put(TAG_ADD, address);
                persons.put(TAG_PHONE, phone);

                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    Show_Emergency.this, personList, R.layout.emergency_list,
                    new String[]{TAG_NAME, TAG_REG, TAG_ADD, TAG_PHONE},
                    new int[]{R.id.name, R.id.region, R.id.address, R.id.phone}
            );

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    Log.d(TAG,"###### EmergencyList.setOnItemClickListener 호출");

                    String callnumber = "";

                    try {
                        callnumber = peoples.getJSONObject(position).getString(TAG_PHONE).replaceAll("-","");
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    // 전화 권한 확인
                    int permissionCheck = ContextCompat.checkSelfPermission(Show_Emergency.this, Manifest.permission.CALL_PHONE);

                    if (permissionCheck== PackageManager.PERMISSION_DENIED){
                        // 권한 없음
                        ActivityCompat.requestPermissions(Show_Emergency.this, new String[]{Manifest.permission.CALL_PHONE},0);
                    } else {
                        // 권한 있음
                        Intent intent = new Intent (Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + callnumber));

                        try{
                            startActivity(intent);
                        } catch (SecurityException e){
                            e.printStackTrace();
                        }
                    }


                }
            });

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params){

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json=bufferedReader.readLine()) != null){
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
                myJSON = result;
                showList();
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}