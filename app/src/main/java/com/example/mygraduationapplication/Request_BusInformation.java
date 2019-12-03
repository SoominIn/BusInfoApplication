package com.example.mygraduationapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Request_BusInformation extends AsyncTask<String, Void, String> {

    private String result_info;

    private static final String TAG = "Request_BusInformation";

    @Override
    protected String doInBackground(String... params) {

        String bus_name = (String) params[0];
        String xml_parsing = (String) params[1];

        String parsingURL = "http://openapi.gbis.go.kr/ws/rest/busrouteservice?"
                + "serviceKey=7GwSGhRq1iv50FQdSEcZ4LLVY6EV5z8m2iZnJ%2BnJzrl9sGt2tdY2nbvMKOQ%2BiuSQgpoZsolONKLzM8%2FIbrZ7%2BQ%3D%3D"
                + "&keyword=" + bus_name;

        StringBuffer buffer = new StringBuffer();

        String location = URLEncoder.encode(bus_name);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한

        try {
            URL url = new URL(parsingURL);//문자열로 된 요청 url을 URL 객체로 생성.

            //InputStream is = url.openStream(); //url위치로 입력스트림 연결
            URLConnection url_connection = url.openConnection();
            url_connection.setReadTimeout(3000);
            InputStream is = url_connection.getInputStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("start XML parsing...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    // 태그 이름 얻어오기

                        if (tag.equals("busRouteList")) ;// 첫번째 검색결과
                        else if (tag.equals(xml_parsing)) {
                            xpp.next();
                            buffer.append(xpp.getText()); //title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기

                        if (tag.equals("busRouteList")) buffer.append("\n"); // 첫번째 검색결과종료..줄바꿈
                        break;
                }

                eventType = xpp.next();
            }
        }catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
            Log.d(TAG, "InsertData: Error ", e);

            return new String("Error: " + e.getMessage());
        }

        buffer.append("파싱 끝\n");

        result_info = buffer.toString();

        return result_info;
    }
}
