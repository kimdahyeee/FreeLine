package com.perples.recosample.Mywait;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.perples.recosample.Http.HttpConnectionToURL;
import com.perples.recosample.Http.encode_string;
import com.perples.recosample.Http.value;
import com.perples.recosample.R;
import com.perples.recosample.TabActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MyWaitActivity extends Fragment {

    public View view, view2;
    public Context context, context2;
    private final String TAG = "MyWait_Log : ";
    private final String MyWait_url = value.MyWait_url;
    private final String DeleteWaiting_url = value.DeleteWaiting_url;
    TextView myNum, waitNum;
    Button cancelBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_my_wait, container, false);
        context = view.getContext();
        initView();
        return view;
    }

    private void initView() {
        TelephonyManager tm  =  (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE );
        final String usernum = tm.getDeviceId();
        Log.d(TAG, "usernum : " + usernum);
        myNum = (TextView)view.findViewById(R.id.mw_myNum);
        waitNum = (TextView)view.findViewById(R.id.mw_waitNum);

        try {
            encode_string es = new encode_string(usernum);
            String params = es.getParams();
            Log.d(TAG , "params : " + params);

            String result = new HttpConnectionToURL().execute(MyWait_url, params).get();
            JSONObject JSObj = new JSONObject(result);
            String resultCode_value = JSObj.getString("resultCode");
            String preWaitingCount_value = JSObj.getString("preWaitingCount");
            String waitNum_value = JSObj.getString("waitNum");
            Log.d(TAG , "resultCode_value : " + resultCode_value);
            Log.d(TAG , "preWaitingCount_value : " + preWaitingCount_value);
            Log.d(TAG , "waitNum_value : " + waitNum_value);

            waitNum.setText(preWaitingCount_value);
            myNum.setText(waitNum_value);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cancelBtn = (Button)view.findViewById(R.id.mw_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try{
                    encode_string es = new encode_string(usernum);
                    String params = es.getParams();
                    String result = new HttpConnectionToURL().execute(DeleteWaiting_url, params).get();
                    JSONObject JSObj = new JSONObject(result);
                    String resultCode_value = JSObj.getString("resultCode");


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TabActivity.class);
                startActivity(intent);
            }
        });
    }
}