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

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perples.recosample.MainActivity;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;

/*
 * RECOActivity 클래스는 RECOMonitoringActivity와 RECORangingActivity를 위한 기본 클래스 입니다.
 * Monitoring 이나 ranging을 단일 클래스로 구성하고 싶으시다면, 이 클래스를 삭제하시고 필요한 메소드와 RECOServiceConnectListener를 해당 클래스에서 구현하시기 바랍니다.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class RecoActivity extends Fragment implements RECOServiceConnectListener {
    protected RECOBeaconManager mRecoManager;
    protected ArrayList<RECOBeaconRegion> mRegions;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();

        /*
         * RECOBeaconManager 인스턴스틀 생성합니다. (스캔 대상 및 백그라운드 ranging timeout 설정)
         * RECO만을 스캔하고, 백그라운드 ranging timeout을 설정하고 싶지 않으시다면, 다음과 같이 생성하시기 바랍니다.
         * 		mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), true, false);
         * 주의: enableRangingTimeout을 false로 설정 시, 배터리 소모량이 증가합니다.
         */

        mRecoManager = RECOBeaconManager.getInstance(getActivity(), MainActivity.SCAN_RECO_ONLY, MainActivity.ENABLE_BACKGROUND_RANGING_TIMEOUT);
        mRegions = this.generateBeaconRegion();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private ArrayList<RECOBeaconRegion> generateBeaconRegion() {
        ArrayList<RECOBeaconRegion> regions = new ArrayList<RECOBeaconRegion>();

        RECOBeaconRegion recoRegion;
        recoRegion = new RECOBeaconRegion(MainActivity.RECO_UUID, "RECO Sample Region");
        regions.add(recoRegion);

        return regions;
    }

    protected abstract void start(ArrayList<RECOBeaconRegion> regions);
    protected abstract void stop(ArrayList<RECOBeaconRegion> regions);
}
