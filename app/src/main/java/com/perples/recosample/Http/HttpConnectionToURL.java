package com.perples.recosample.Http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gwangyonglee on 16. 9. 19..
 */
public class HttpConnectionToURL extends AsyncTask<String,Void,String> {
    private static final String TAG = "FreeLine_log";
    //params[0] = url
    //params[1] = StringBuffer;

    @Override
    protected String doInBackground(String... params) {
        URL url;
        String response = "";



        try{
            Log.d(TAG," param[0] : "+params[0]+ " param[1] : "+params[1]);

            Log.d(TAG,"URL : "+params[0]);
            url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDefaultUseCaches(false);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //서버에 값 전송
            Log.d(TAG,"params[1] : "+params[1]);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            writer.write(params[1]);
            writer.flush();



            //결과 값 받아오기
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String newStr;
            while((newStr = rd.readLine()) != null){
                newStr = newStr.replaceAll("<br />","");
                response += newStr+"\n";
            }

            writer.close();
            rd.close();
        }catch (Exception e){
            Log.d(TAG,"URL error");
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "result : " + result);
    }
}