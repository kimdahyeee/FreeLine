package com.perples.recosample.AllStore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perples.recosample.R;

import java.util.ArrayList;

/**
 * Created by dahye on 2016-09-27.
 */
public class AllStoreMainActivity extends Fragment {

    public View view;
    public Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_all_store_main, container, false);
        context = view.getContext();

        initView();
        return view;
    }

    private void initView() {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<AllStoreListObject> allStore = new ArrayList<>();
        AllStoreServerRequest allStoreListServerRequests = new AllStoreServerRequest();
        allStoreListServerRequests.fetchAllStoresListInBackground(new GetAllStoreCallback() {
            @Override
            public void done(ArrayList<AllStoreConstructor> returnedAllStores) {
                for (int i = 0; i < returnedAllStores.size(); i++) {
                    AllStoreListObject storeListObject = new AllStoreListObject();
                    storeListObject.setName(returnedAllStores.get(i).StoreName);
                    storeListObject.setImage(returnedAllStores.get(i).StoreImage);
                    storeListObject.setCategory(returnedAllStores.get(i).StoreCategory);
                    storeListObject.setIntroduction(returnedAllStores.get(i).StoreIntroduction);
                    storeListObject.setLocation(returnedAllStores.get(i).StoreLocation);
                    storeListObject.setTel(returnedAllStores.get(i).StoreTel);
                    allStore.add(storeListObject);
                }

                AllStoreListAdapter recyclerViewAdapter = new AllStoreListAdapter(context.getApplicationContext(), allStore);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
        });
    }
}

