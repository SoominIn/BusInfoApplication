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

public class Show_Notice extends AppCompatActivity {

    private static String IP_ADDRESS = "172.20.10.9";

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NUMBER = "number";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_ID = "id";
    private static final String TAG_DATE = "date";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_layout);

        list = (ListView)findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        getData("http://" + IP_ADDRESS + "/jsonnotice.php");
    }

    protected void showList(){
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray((TAG_RESULTS));

            for (int i = 0; i<peoples.length(); i++){
                JSONObject c = peoples.getJSONObject(i);

                String number = c.getString(TAG_NUMBER);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String id = c.getString(TAG_ID);
                String date = c.getString(TAG_DATE);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_NUMBER, number);
                persons.put(TAG_TITLE, title);
                persons.put(TAG_CONTENT, content);
                persons.put(TAG_ID, id);
                persons.put(TAG_DATE, date);

                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    Show_Notice.this, personList, R.layout.notice_list,
                    new String[]{TAG_NUMBER, TAG_TITLE, TAG_CONTENT, TAG_ID, TAG_DATE},
                    new int[]{R.id.number, R.id.title, R.id.content, R.id.id, R.id.date}
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
                myJSON = result.replaceAll("<br>", "");
                myJSON = myJSON.replaceAll("&ensp;", "");
                showList();
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}