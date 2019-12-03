package com.example.mygraduationapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Mysql_Service extends AsyncTask<String, Void, String> {    // 버스 이름, 버스가 경유하는 정류장 목록

    private static final String TAG = "Mysql1";

    @Override
    protected String doInBackground(String... params) {

        String bus_number = (String)params[1];
        String bus_region = (String)params[2];
        String st_name = (String)params[3];

        String serverURL = (String)params[0];

        String postParameters = "bus_number=" + bus_number + "&bus_region=" + bus_region+ "&st_name=" + st_name;

        try {
            URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외 발생

            httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외 발생

            httpURLConnection.setRequestMethod("POST"); //요청 방식을 : POST
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                // 정상적인 응답 데이터
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                // 에러
                inputStream = httpURLConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            bufferedReader.close();

            return sb.toString();
        } catch (Exception e) {
            Log.d(TAG, "InsertData: Error ", e);

            return new String("Error: " + e.getMessage());
        }

    }
}
