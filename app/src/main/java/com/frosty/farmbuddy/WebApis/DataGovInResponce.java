package com.frosty.farmbuddy.WebApis;

public class DataGovInResponce <T>{
   public String index_name;
   public String title;
   public String desc;
   public String created;
   public String updated;
   public String visualized;
   public String source;
   public String org_type;
   public String [] org;
   public String [] sector;
   public String status;
   public String message;
   public String count;
   public String limit;
   public String offset;
   public DataGovInField [] fields;
   public T[] records;
}
