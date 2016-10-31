package com.perples.recosample.AllStore;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dahye on 2016-09-27.
 */
public class AllStoreServerRequest {

    private String TAGLOG = "로그: AllStoresServerRequests: ";
    private static String SERVER_URL = "http://cslab2.kku.ac.kr/~201341307/allRestaurantList.php";
    HttpURLConnection conn = null;
    String result="";
    public String stores[];
    private String strCookie;


    public void fetchAllStoresListInBackground(GetAllStoreCallback getAllStoreCallback){
        new LoadAllStoreListServerRequests(getAllStoreCallback).execute();
    }
    class LoadAllStoreListServerRequests extends AsyncTask<Void, Void, ArrayList<AllStoreConstructor>> {

        GetAllStoreCallback getAllStoreCallback;

        public LoadAllStoreListServerRequests(GetAllStoreCallback getAllStoreCallback){
            this.getAllStoreCallback = getAllStoreCallback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<AllStoreConstructor> doInBackground(Void... params) {
            ArrayList<AllStoreConstructor> returnedAllStores = new ArrayList<>();
            try {
                URL url = new URL(SERVER_URL); //요청 url 입력
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); //post방식으로 요청

                conn.setDoInput(true);//input 사용하도록 설정
                conn.setDoOutput(true); //output 사용하도록 설정 , 서버에서 값 받을 수 있도록 설정
                conn.setUseCaches(false);     // 캐싱데이터를 받을지 안받을지, httpClient는 쿠키 알아서 보관, 이건 알아서보관못하므로 필요할땐 임의로 쿠키값 저장해야함
                conn.setDefaultUseCaches(false);
                conn.setInstanceFollowRedirects(false);
                conn.setReadTimeout(10000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setConnectTimeout(90000); //timeout시간설정 default=무한대기
                conn.connect();
                strCookie = conn.getHeaderField("Set-Cookie");
                InputStream is = conn.getInputStream();
                StringBuilder sb = new StringBuilder(); //문자열 담기위한 객체

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //캐릭터셋 설정
                String line;

                //reader.readLine() =>json문자열출력됨!
                while((line=reader.readLine())!=null){
                    sb.append(line + "\n"); //append =>문자열 끝에 문자 추가
                }

                result = sb.toString();
                //Log.e(TAGLOG, "json result : " + result);
                JSONObject JSObj =new JSONObject(result);
                stores = new String[JSObj.length()];

                if(JSObj.length()!=0){
                    String resultCode = JSObj.getString("resultCode");
                    //Log.e(TAGLOG, "resultCode : " + resultCode);
                    for(int i=0; i<JSObj.length()-1; i++){
                        stores[i] = JSObj.getString(String.valueOf(i));
                        //Log.e(TAGLOG, "stores[" + i + "] : " + stores[i]);
                        JSONObject storeObject = new JSONObject(stores[i]);
                        String beaconID = storeObject.getString("beaconID");
                        String storeName = storeObject.getString("storeName");
                        String storeTel = storeObject.getString("storeTel");
                        String storeLocation = storeObject.getString("storeLocation");
                        String storeIntroduction = storeObject.getString("storeIntroduction");
                        String storeImage = storeObject.getString("storeImage");
                        String storeCategory = storeObject.getString("storeCategory");
                        AllStoreConstructor storeConstructor = new AllStoreConstructor(beaconID, storeName, storeTel, storeLocation, storeIntroduction, storeImage, storeCategory);

                        returnedAllStores.add(storeConstructor);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnedAllStores;
        }

        @Override
        protected void onPostExecute(ArrayList<AllStoreConstructor> storeConstructors) {
            super.onPostExecute(storeConstructors);
            getAllStoreCallback.done(storeConstructors);
        }
    }
}
