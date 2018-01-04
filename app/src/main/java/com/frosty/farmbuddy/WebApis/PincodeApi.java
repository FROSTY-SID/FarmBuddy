package com.frosty.farmbuddy.WebApis;

import android.util.Log;

import com.frosty.farmbuddy.Activities.UserRegistrationActivity;

/**
 * Created by siddh on 30-12-2017.
 */

public class PincodeApi extends DataGovInApi{

    private static final String PINCODE_API_URL = " https://api.data.gov.in/resource/04cbe4b1-2f2b-4c39-a1d5-1c2e28bc0e32";
    public static final String FIELD_officename="officename";
    public static final String FIELD_pincode="pincode";
    public static final String FIELD_officetype="officetype";
    public static final String FIELD_deliverystatus="deliverystatus";
    public static final String FIELD_divisionname="divisionname";
    public static final String FIELD_regionname="regionname";
    public static final String FIELD_circlename="circlename";
    public static final String FIELD_taluk="taluk";
    public static final String FIELD_districtname="districtname";
    public static final String FIELD_statename="statename";
    public static final String FIELD_telephone="telephone";
    public static final String FIELD_related_suboffice="related_suboffice";
    public static final String FIELD_related_headoffice="related_headoffice";
    public static final String FIELD_longitude="longitude";
    public static final String FIELD_latitude="latitude";

    public String generateUrl(String pincode){
        String result=null;
        super.FIELDS = FIELD_statename+","+FIELD_districtname+","+FIELD_taluk;
        super.LIMIT ="1";
        result = PINCODE_API_URL+"?"+DataGovInApi.FORMAT_TAG+
                DataGovInApi.FORMAT_JSON+"&"+
                DataGovInApi.APIKEY_TAG+DataGovInApi.APIKEY+"&"+
                super.setFilters(FIELD_pincode,pincode)+"&"+
                DataGovInApi.FIELDS_TAG+super.FIELDS+"&"+DataGovInApi.LIMIT_TAG+LIMIT;
        Log.d("UESR_REG",result);
        return result;

    }

}
