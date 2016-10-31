package com.perples.recosample.PushNotification;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by dahye on 2016-10-05.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIDService";
    //기기별 토큰을 생성하는 클래스
    //나중에 push알림을 특정 타겟에게 보낼 때 사용되는 고유 키 값
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + token);

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  //단말기 기본 서비스 호출
        //final String usernum = tm.getLine1Number();
        String usernum = tm.getDeviceId();
        Log.d(TAG, "usernum : " + usernum);

        //서버로 토큰 전달
        sendingRegistrationToServer(token, usernum);
    }

    private void sendingRegistrationToServer(String token, String userNum){


/*
        try {
            encode_string es = new encode_string(token, userNum);
            String params = es.getParams();
            Log.d(TAG, "params : " + params);
            new HttpConnectionToURL().execute("http://cslab2.kku.ac.kr/~201341307/pushNotificationRegister.php", params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .add("UserNum", userNum)
                .build();

        //요청
        Request request = new Request.Builder()
                .url("http://cslab2.kku.ac.kr/~201341307/pushNotificationRegister.php")
                .post(body)
                .build();

        try{
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
