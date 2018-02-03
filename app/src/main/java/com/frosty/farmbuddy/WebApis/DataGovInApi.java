package com.frosty.farmbuddy.WebApis;


public class DataGovInApi {
    //Request Values
    public static final String APIKEY="579b464db66ec23bdd000001e36e755de2d944225b6db5f2bac4a0cb";
    public static final String APIKEY_TAG="api-key=";
    public static final String FORMAT_TAG= "format=";
    public static final String FILTERS_TAG ="filters";
    public static final String FIELDS_TAG ="fields=";
    public static final String SORT_TAG ="sort";
    public static final String LIMIT_TAG="limit=";
    public static final String OFFSET_TAG="offset=";


    public static final String ORDER_ASC ="asc";
    public static final String ORDER_DESC ="desc";

    public static final String FORMAT_XML ="xml";
    public static final String FORMAT_JSON ="json";

    public String FILTERS ="";
    public String FIELDS ="";
    public String SORT  ="";
    public String LIMIT ="";
    public String OFFSET ="";


    //Respond Values



    DataGovInApi(){

    }

    public String setFilters(String attribute,String value){
        FILTERS +=FILTERS_TAG+"["+attribute+"]="+value;
        return FILTERS;
    }

    public String setSORT(String attribute,String order){
        SORT += SORT_TAG+"["+attribute+"]="+order;
        return SORT;
    }
}
