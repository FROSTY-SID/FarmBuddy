package com.frosty.farmbuddy.WebApis;

import android.net.Uri;

/**
 * Created by siddh on 15-01-2018.
 */

public class MarketPriceApi extends DataGovInApi {
    public static final String api = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070";

   public MarketPriceApi(){}

   public class MarketPriceResponce{
       public String timeStamp;
       public String state;
       public String district;
       public String market;
       public String commodity;
       public String variety;
       public String arrival_date;
       public String min_price;
       public String max_price;
       public String modal_price;

   }
   public class MarketPriceRequest{

       public static final String timeStamp="timeStamp;";
       public static final String state="state;";
       public static final String district="district;";
       public static final String market="market;";
       public static final String commodity="commodity;";
       public static final String variety="variety;";
       public static final String arrival_date="arrival_date;";
       public static final String min_price="min_price;";
       public static final String max_price="max_price;";
       public static final String modal_price="modal_price;";

       public String generateGetRequest(String filterAttribute,String value){
           Uri builder = Uri.parse(api).buildUpon()
                   .appendQueryParameter(APIKEY_TAG,APIKEY)
                   .appendQueryParameter(FORMAT_TAG,FORMAT_JSON)
                   .appendQueryParameter(FILTERS_TAG+"["+filterAttribute+"]",value)
                   .build();
           return builder.toString();
       }
   }

}
