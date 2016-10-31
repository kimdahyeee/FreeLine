package com.perples.recosample.Near;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.perples.recosample.Http.value;
import com.perples.recosample.R;

import java.util.List;

public class NearStoreReservation extends AppCompatActivity {

    private final String TAG = "FreeLine_Log";
    private final String NearStoreReservation_URL = value.NearStoreReservation_url;
    TextView storeName, waitNum, storeIntro;
    ImageView storeImage;
    Button reservationBtn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_store_reservation);

        Intent intent = new Intent(this.getIntent());
        final String beaconID_value = intent.getStringExtra("beaconID");
        String storeName_value = intent.getStringExtra("storeName");
        String storeImage_value = intent.getStringExtra("storeImage");
        String storeIntro_value = intent.getStringExtra("storeIntroduction");
        String totalWaitingCount_value = intent.getStringExtra("totalWaitingCount");

        storeName = (TextView)findViewById(R.id.rb_storeName);
        storeName.setText(storeName_value);
        waitNum = (TextView)findViewById(R.id.rb_waitCount);
        waitNum.setText(totalWaitingCount_value);
        storeIntro = (TextView)findViewById(R.id.rb_storeIntro);
        storeIntro.setText(storeIntro_value);

        storeImage = (ImageView)findViewById(R.id.rb_storeImage);
        String storeImage_value_url = "http://cslab2.kku.ac.kr/~201341307/images/" + storeImage_value;
        Glide.with(this).load(storeImage_value_url).into(storeImage);

        reservationBtn = (Button)findViewById(R.id.rb_btn);
        reservationBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NearStoreReservation.this, NearStoreReservation2.class);
                intent.putExtra("beaconID", beaconID_value);
                startActivity(intent);
            }
        });
    }

}
