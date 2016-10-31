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

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perples.recosample.Http.HttpConnectionToURL;
import com.perples.recosample.Http.encode_string;
import com.perples.recosample.Http.value;
import com.perples.recosample.MainActivity;
import com.perples.recosample.R;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/*
 * RECORangingActivity 클래스는 foreground 상태에서 ranging을 수행합니다.
 */
@SuppressLint("ValidFragment")
public class RecoRangingActivity extends RecoActivity implements RECORangingListener, OnMapReadyCallback {


    private RecoRangingListAdapter mRangingListAdapter;
    private ListView mRegionListView;
    private ArrayList<RECOBeacon> mRangedBeacons;
    MapView mapView;
    GoogleMap map;
    Marker marker;
    public View view;
    public Context context;
    private final String StoreLocation_url = value.StoreLocation_url;
    private final String StoreWaitNum_url = value.StoreWaitNum_url;


    @Override
    public void onStart() {
        super.onStart();

        //mRecoManager will be created here. (Refer to the RECOActivity.onCreate())
        //mRecoManager 인스턴스는 여기서 생성됩니다. RECOActivity.onCreate() 메소드를 참고하세요.

        //Set RECORangingListener (Required)
        //RECORangingListener 를 설정합니다. (필수)
        mRecoManager.setRangingListener(this);

        /**
         * Bind RECOBeaconManager with RECOServiceConnectListener, which is implemented in RECOActivity
         * You SHOULD call this method to use monitoring/ranging methods successfully.
         * After binding, onServiceConenct() callback method is called.
         * So, please start monitoring/ranging AFTER the CALLBACK is called.
         *
         * RECOServiceConnectListener와 함께 RECOBeaconManager를 bind 합니다. RECOServiceConnectListener는 RECOActivity에 구현되어 있습니다.
         * monitoring 및 ranging 기능을 사용하기 위해서는, 이 메소드가 "반드시" 호출되어야 합니다.
         * bind후에, onServiceConnect() 콜백 메소드가 호출됩니다. 콜백 메소드 호출 이후 monitoring / ranging 작업을 수행하시기 바랍니다.
         */
        mRecoManager.bind(this);

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        //List<NearStoreListObject> rowListItem = getNearStoreList();
        mRangingListAdapter = new RecoRangingListAdapter(getActivity());
        mRegionListView = (ListView) getView().findViewById(R.id.list_ranging);
        mRegionListView.setAdapter(mRangingListAdapter);
        //mRegionListView.setOnItemClickListener(new ItemList());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.activity_reco_ranging, container, false);
        View v = inflater.inflate(R.layout.activity_reco_ranging, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);


        //map.getUiSettings().setMyLocationButtonEnabled(true);


        MapsInitializer.initialize(this.getActivity());

        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        //map.animateCamera(cameraUpdate);


        return v;


    }


    /*
        class ItemList implements AdapterView.OnItemClickListener{


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("Minor_value", String.valueOf(recoBeacon.getMinor()));
                ViewGroup vg = (ViewGroup)view;
                Intent intent = new Intent(RecoRangingActivity.this, NearStoreReservation.class);
                startActivity(intent);
            }
        }*/
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        this.stop(mRegions);
        this.unbind();
    }

    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void unbind() {
        try {
            mRecoManager.unbind();
        } catch (RemoteException e) {
            Log.i("RECORangingActivity", "Remote Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnect() {
        Log.i("RECORangingActivity", "onServiceConnect()");
        mRecoManager.setDiscontinuousScan(MainActivity.DISCONTINUOUS_SCAN);
        this.start(mRegions);
        //Write the code when RECOBeaconManager is bound to RECOBeaconService
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoRegion) {
        Log.i("RECORangingActivity", "didRangeBeaconsInRegion() region: " + recoRegion.getUniqueIdentifier() + ", number of beacons ranged: " + recoBeacons.size());
        mRangingListAdapter.updateAllBeacons(recoBeacons);
        mRangingListAdapter.notifyDataSetChanged();
        //Write the code when the beacons in the region is received
    }

    @Override
    protected void start(ArrayList<RECOBeaconRegion> regions) {

        /**
         * There is a known android bug that some android devices scan BLE devices only once. (link: http://code.google.com/p/android/issues/detail?id=65863)
         * To resolve the bug in our SDK, you can use setDiscontinuousScan() method of the RECOBeaconManager.
         * This method is to set whether the device scans BLE devices continuously or discontinuously.
         * The default is set as FALSE. Please set TRUE only for specific devices.
         *
         * mRecoManager.setDiscontinuousScan(true);
         */

        for (RECOBeaconRegion region : regions) {
            try {
                mRecoManager.startRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void stop(ArrayList<RECOBeaconRegion> regions) {
        for (RECOBeaconRegion region : regions) {
            try {
                mRecoManager.stopRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onServiceFail(RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed.
        //See the RECOErrorCode in the documents.
        return;
    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {
        Log.i("RECORangingActivity", "error code = " + errorCode);
        //Write the code when the RECOBeaconService is failed to range beacons in the region.
        //See the RECOErrorCode in the documents.
        return;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

        try {
            // 전 식당 주소 마커 찍기
            encode_string es = new encode_string("1");
            String params = es.getParams();
            String result = new HttpConnectionToURL().execute(StoreLocation_url, params).get();
            JSONArray JSArr = new JSONArray(result);
            for(int i=0; i < JSArr.length(); i++){
                JSONObject JSObj = JSArr.getJSONObject(i);
                String beaconID_value = JSObj.getString("beaconID");
                String storeName_value = JSObj.getString("storeName");
                String storeLocation_value = JSObj.getString("storeLocation");
                Geocoder gc = new Geocoder(getContext());
                List<android.location.Address> list = null;
                try {
                    list = gc.getFromLocationName(storeLocation_value , 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                android.location.Address address = list.get(0);
                //String locality = address.getLocality();

                double lat = address.getLatitude();
                double lng = address.getLongitude();
                goToLocationZoom(lat, lng, 15);

                // 총 대기 수 받아 오기
                encode_string es2 = new encode_string(beaconID_value);
                String params2 = es2.getParams();
                String result2 = new HttpConnectionToURL().execute(StoreWaitNum_url, params2).get();
                JSONObject JSObj2 = new JSONObject(result2);
                String TotalWaitNum_value = JSObj2.getString("TotalWaitNum");

                setMarker(storeName_value, lat, lng, TotalWaitNum_value);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }




        if(map != null){

            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);

                    TextView stLocality = (TextView)v.findViewById(R.id.store_locality);
                    TextView stwainNum = (TextView)v.findViewById(R.id.store_waitNum);

                    //LatLng ll = marker.getPosition();
                    stLocality.setText(marker.getTitle());
                    stwainNum.setText(marker.getSnippet());
                    return v;
                }
            });
        }
        /*Geocoder gc = new Geocoder(getContext());
        List<android.location.Address> list = null;


        try {
            list = gc.getFromLocationName("범계역" , 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        android.location.Address address = list.get(0);
        //String locality = address.getLocality();

        double lat = address.getLatitude();
        double lng = address.getLongitude();
        goToLocationZoom(lat, lng, 15);

        setMarker("범계역", lat, lng);*/

    }

    private void setMarker(String locality, double lat, double lng, String waitnum) {
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .position(new LatLng(lat, lng))
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.page1_logo_white))
                .snippet(waitnum);
        marker = map.addMarker(options);


    }

    private void goToLocationZoom(double lat, double lng, float zoom){
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        map.moveCamera(update);
    }


}
