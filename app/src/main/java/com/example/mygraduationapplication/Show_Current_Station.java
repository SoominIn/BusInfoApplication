package com.example.mygraduationapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Show_Current_Station extends AppCompatActivity {

    private static final String TAG = "Show_Current_Station";

    private static String IP_ADDRESS = "172.20.10.9";

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_STATION = "st_name";
    private static final String TAG_REGION = "bus_region";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_layout);

        list = (ListView)findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        getData("http://" + IP_ADDRESS + "/jsonrecentstation.php");
    }

    protected void showList(){
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray((TAG_RESULTS));

            for (int i = 0; i<peoples.length(); i++){
                JSONObject c = peoples.getJSONObject(i);

                String station = c.getString(TAG_STATION);
                String region = c.getString(TAG_REGION);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_STATION, station);
                persons.put(TAG_REGION, region);

                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    Show_Current_Station.this, personList, R.layout.station_list,
                    new String[]{TAG_REGION, TAG_STATION},
                    new int[]{R.id.station, R.id.region}
            );

            list.setAdapter(adapter);

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {

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