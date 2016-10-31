/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Perples, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.perples.recosample.Reco;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.perples.recosample.Http.HttpConnectionToURL;
import com.perples.recosample.Http.encode_string;
import com.perples.recosample.Http.value;
import com.perples.recosample.Near.NearStoreReservation;
import com.perples.recosample.Near.NearStoreReservation2;
import com.perples.recosample.R;
import com.perples.recosdk.RECOBeacon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class RecoRangingListAdapter extends BaseAdapter {
    private final String TAG = "FreeLine_Log";
    private ArrayList<RECOBeacon> mRangedBeacons;
    private LayoutInflater mLayoutInflater;
    private final String restaurantList_url = value.restaurantList_url;
    private final String NearStoreSelect_url = value.NearStoreSelect_url;
    private Context context;
    private String result_code = "1";

    public RecoRangingListAdapter(Context context) {
        super();
        mRangedBeacons = new ArrayList<RECOBeacon>();
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void updateBeacon(RECOBeacon beacon) {
        synchronized (mRangedBeacons) {
            if(mRangedBeacons.contains(beacon)) {
                mRangedBeacons.remove(beacon);
            }
            mRangedBeacons.add(beacon);
        }
    }

    public void updateAllBeacons(Collection<RECOBeacon> beacons) {
        synchronized (beacons) {
            mRangedBeacons = new ArrayList<RECOBeacon>(beacons);
        }
    }

    public void clear() {
        mRangedBeacons.clear();
    }

    @Override
    public int getCount() {
        return mRangedBeacons.size();
    }

    @Override
    public Object getItem(int position) {
        return mRangedBeacons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)   {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_ranging_beacon, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.recoAccuracy = (TextView)convertView.findViewById(R.id.recoAccuracy);
            viewHolder.storeName = (TextView)convertView.findViewById(R.id.storeName);
            viewHolder.storeIntro = (TextView)convertView.findViewById(R.id.storeIntro);
            viewHolder.storeWaitNum = (TextView)convertView.findViewById(R.id.storeWaitNum);
            viewHolder.storeImg = (ImageView)convertView.findViewById(R.id.storeImg);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RECOBeacon recoBeacon = mRangedBeacons.get(position);

        viewHolder.recoAccuracy.setText(String.format("%.2f", recoBeacon.getAccuracy()));

        try {
            final String Minor_value = Integer.toString(recoBeacon.getMinor());
            encode_string es = new encode_string(Minor_value);
            String params = es.getParams();
            String result = new HttpConnectionToURL().execute(restaurantList_url, params).get();
            JSONObject obj = new JSONObject(result);
            final String beaconID_value = obj.getString("beaconID");
            final String storeName_value = obj.getString("storeName");
            final String storeImage_value = obj.getString("storeImage");
            final String storeIntroduction_value = obj.getString("storeIntroduction");
            final String resultCode_value = obj.getString("resultCode");
            final String totalWaitingCount_value = obj.getString("totalWaitingCount");

            String storeImage_value_url = "http://cslab2.kku.ac.kr/~201341307/images/" + storeImage_value;
            Glide.with(context).load(storeImage_value_url).into(viewHolder.storeImg);

            viewHolder.storeName.setText(storeName_value);
            viewHolder.storeIntro.setText(storeIntroduction_value);
            viewHolder.storeWaitNum.setText(totalWaitingCount_value);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                        final String usernum = tm.getDeviceId();
                        encode_string es = new encode_string(usernum);
                        String params = es.getParams();
                        String result = new HttpConnectionToURL().execute(NearStoreSelect_url, params).get();
                        JSONObject JSObj = new JSONObject(result);
                        final String resultCode_value = JSObj.getString("resultCode");
                        Log.d(TAG, "gwangd" + resultCode_value);
                        if(resultCode_value.equals(result_code)){
                            Intent intent = new Intent(context, NearStoreReservation.class);
                            intent.putExtra("beaconID", beaconID_value);
                            intent.putExtra("storeName", storeName_value);
                            intent.putExtra("storeImage", storeImage_value);
                            intent.putExtra("storeIntroduction", storeIntroduction_value);
                            intent.putExtra("totalWaitingCount", totalWaitingCount_value);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(view.getContext(), "이미 뽑으셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        TextView recoAccuracy;
        TextView storeName;
        TextView storeIntro;
        TextView storeWaitNum;
        ImageView storeImg;
    }
}