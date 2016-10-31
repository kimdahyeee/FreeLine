package com.perples.recosample;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;

import com.perples.recosample.AllStore.AllStoreMainActivity;
import com.perples.recosample.Http.HttpConnectionToURL;
import com.perples.recosample.Http.encode_string;
import com.perples.recosample.Http.value;
import com.perples.recosample.Mywait.MyWaitActivity;
import com.perples.recosample.Mywait.MyWaitActivityEmpty;
import com.perples.recosample.Reco.RecoRangingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by dahye on 2016-09-27.
 */
public class TabActivity extends AppCompatActivity {

    private final String TAG = "TabActivity_Log : ";
    private final String MyWait_url = value.MyWait_url;
    private final String NearStoreSelect_url = value.NearStoreSelect_url;
    public Context context;
    private String result_code = "2";
    private TabLayout tabLayout;
    View view1, view2, view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);

        tabLayout = (TabLayout)findViewById(R.id.tabs);

        view1 = getLayoutInflater().inflate(R.layout.customtab, null);
        view2 = getLayoutInflater().inflate(R.layout.customtab, null);
        view3 = getLayoutInflater().inflate(R.layout.customtab, null);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab1_select);
                        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab2);
                        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab3);
                        break;
                    case 1:
                        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab1);
                        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab2_select);
                        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab3);
                        break;
                    case 2:
                        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab1);
                        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab2);
                        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab3_select);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab1_select);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab2);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.tab3);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    private void setCurrentTabFragment(int tabPosition) {

        switch (tabPosition)
        {
            case 0:
                RecoRangingActivity tab1 = new RecoRangingActivity();
                replaceFragment(tab1);
                break;
            case 1:
                AllStoreMainActivity tab2 = new AllStoreMainActivity();
                replaceFragment(tab2);
                break;
            case 2:
                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);  //단말기 기본 서비스 호출
                final String usernum = tm.getDeviceId();
                encode_string es = new encode_string(usernum);
                String params = es.getParams();
                try {
                    String result = new HttpConnectionToURL().execute(NearStoreSelect_url, params).get();
                    JSONObject JSObj = new JSONObject(result);
                    final String resultCode_value = JSObj.getString("resultCode");
                    if(resultCode_value.equals(result_code)){
                        MyWaitActivity tab3 = new MyWaitActivity();
                        replaceFragment(tab3);
                        break;
                    } else {
                        MyWaitActivityEmpty tab3 = new MyWaitActivityEmpty();
                        replaceFragment(tab3);
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
