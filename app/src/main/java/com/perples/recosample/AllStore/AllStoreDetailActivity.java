package com.perples.recosample.AllStore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.perples.recosample.R;

/**
 * Created by dahye on 2016-09-27.
 */
public class AllStoreDetailActivity extends AppCompatActivity {

    ImageView storeImage;
    TextView storeName, storeTel, storeIntroduction, storeLocation, storeCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_store_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = new Intent(this.getIntent());
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(intent.getStringExtra("storeName"));


        String storeName_value = intent.getStringExtra("storeName");
        String storeImage_value = intent.getStringExtra("storeImage");
        String storeTel_value = intent.getStringExtra("storeTel");
        String storeIntroduction_value = intent.getStringExtra("storeIntroduction");
        String storeLocation_value = intent.getStringExtra("storeLocation");
        String storeCategory_value = intent.getStringExtra("storeCategory");
        storeImage = (ImageView)findViewById(R.id.store_image);
        storeName = (TextView)findViewById(R.id.store_name);
        storeTel = (TextView)findViewById(R.id.store_tel);
        storeIntroduction = (TextView)findViewById(R.id.store_Introduction);
        storeLocation = (TextView)findViewById(R.id.store_location);
        storeCategory = (TextView)findViewById(R.id.store_category);

        Glide.with(this).load(storeImage_value).into(storeImage);
        storeName.setText(storeName_value);
        storeTel.setText(storeTel_value);
        storeIntroduction.setText(storeIntroduction_value);
        storeLocation.setText(storeLocation_value);
        storeCategory.setText(storeCategory_value);
    }
}

