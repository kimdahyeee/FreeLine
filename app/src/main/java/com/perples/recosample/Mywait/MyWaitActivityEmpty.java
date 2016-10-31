package com.perples.recosample.Mywait;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perples.recosample.R;

/**
 * Created by gwangyonglee on 2016. 10. 6..
 */

public class MyWaitActivityEmpty extends Fragment {
    public View view;
    public Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_my_wait_no, container, false);
        context = view.getContext();
        initView();
        return view;
    }
    private void initView() {
    }
}
