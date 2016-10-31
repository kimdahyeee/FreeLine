package com.perples.recosample.AllStore;

/**
 * Created by dahye on 2016-09-27.
 */
public class AllStoreConstructor {
    public String BeaconID, StoreName, StoreTel, StoreLocation, StoreIntroduction, StoreImage, StoreCategory;

    public AllStoreConstructor(String beaconID, String storeName, String storeTel, String storeLocation, String storeIntroduction, String storeImage, String storeCategory){
        this.BeaconID=beaconID;
        this.StoreName = storeName;
        this.StoreTel = storeTel;
        this.StoreLocation = storeLocation;
        this.StoreIntroduction = storeIntroduction;
        this.StoreImage = storeImage;
        this.StoreCategory = storeCategory;
    }
}

