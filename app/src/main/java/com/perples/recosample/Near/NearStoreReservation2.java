package com.perples.recosample.Near;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.perples.recosample.AllStore.AllStoreMainActivity;
import com.perples.recosample.Mywait.MyWaitActivity;
import com.perples.recosample.Http.HttpConnectionToURL;
import com.perples.recosample.Http.encode_string;
import com.perples.recosample.Http.value;
import com.perples.recosample.R;
import com.perples.recosample.Reco.RecoRangingActivity;
import com.perples.recosample.TabActivity;

import org.json.JSONObject;

public class NearStoreReservation2 extends AppCompatActivity {
    private final String TAG = "Reservation2_Log : ";
    private final String NearStoreReservation2_URL = value.NearStoreReservation2_url;
    EditText waitName, waitTel, waitPeople;
    Button reservationBtn2;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_store_reservation2);

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  //단말기 기본 서비스 호출
        final String usernum = tm.getDeviceId();
        Log.d(TAG, "usernum : " + usernum);

        //NearStoreReservation beaconID 값 받아오기
        Intent intent = new Intent(this.getIntent());
        final String beaconID_value = intent.getStringExtra("beaconID");
        Log.d(" beaconID : ", String.valueOf(beaconID_value));

        waitName = (EditText)findViewById(R.id.rb2_waitName);
        waitTel = (EditText)findViewById(R.id.rb2_waitTel);
        waitPeople = (EditText)findViewById(R.id.rb2_peopleNum);
        reservationBtn2 = (Button)findViewById(R.id.rb2_btn);

        reservationBtn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String wait_Name = waitName.getText().toString();
                String wait_Tel = waitTel.getText().toString();
                String wait_People = waitPeople.getText().toString();

                try{
                    encode_string es = new encode_string(beaconID_value, wait_Name, wait_Tel, usernum, wait_People);
                    String params = es.getParams();
                    String result = new HttpConnectionToURL().execute(NearStoreReservation2_URL, params).get();
                    JSONObject JSObj = new JSONObject(result);
                    String resultCode_value = JSObj.getString("resultCode");
                    String waitNum_value = JSObj.getString("waitNum");
                    String preWaitingCount_value = JSObj.getString("preWaitingCount");
                    Log.d(TAG, "resultCode: " + resultCode_value);
                    Log.d(TAG, "waitNum " + waitNum_value);
                    Log.d(TAG, "preWaitingCount: " + preWaitingCount_value);
                    Intent intent = new Intent(NearStoreReservation2.this, RecoRangingActivity.class);
                    //Intent intent = new Intent(context, NearStoreReservation.class);
                    intent.putExtra("beaconID", beaconID_value);
                    intent.putExtra("waitNum", waitNum_value);
                    intent.putExtra("userNum", usernum);
                    //context.startActivity(intent);
                    startActivity(intent);
                } catch (Exception e){
                    Log.d(TAG, "beaconID trans error");
                }
                Toast.makeText(view.getContext(), "대기열에 등록되셨습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NearStoreReservation2.this, TabActivity.class);
                startActivity(intent);
            }
        });
    }
}
