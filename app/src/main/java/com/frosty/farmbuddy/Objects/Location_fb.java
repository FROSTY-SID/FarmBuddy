package com.frosty.farmbuddy.Objects;

/**
 * Created by siddh on 31-12-2017.
 */

public class Location_fb {
    public String state;
    public String district;
    public String taluka;
    public String pincode;
    public String latitude;
    public String longitude;

    public Location_fb(){

    }

    public Location_fb(String state,String district,String taluka){
        this.state = state;
        this.district = district;
        this.taluka = taluka;
    }

    public Location_fb(String state,String district,String taluka,String pincode){
        this.state = state;
        this.district = district;
        this.taluka = taluka;
        this.pincode = pincode;
    }

    public Location_fb(String state,String district,String taluka,String latitude,String pincode,String longitude){
        this.state = state;
        this.district = district;
        this.taluka = taluka;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location_fb(String latitude,String longitude){
        this.longitude = longitude;
        this.latitude = latitude;

    }
}
