package com.frosty.farmbuddy.Objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by siddh on 18-01-2018.
 */

public class Product {
    public String id;
    public String cat;
    public String name;
    public String variety;
    public String unit;
    public String description;
    public String rate;
    public String sellerId;
    public ArrayList<String> productPicUrls;
    public String date;
    public Boolean instock;
    public int views;
    public boolean sanitize;

    public Product(){}

    public Product(String id, String cat, String name, String variety, String unit, String description, String rate, String sellerId, ArrayList<String> productPicUrls, String date, Boolean instock, int views, boolean sanitize) {
        this.id = id;
        this.cat = cat;
        this.name = name;
        this.variety = variety;
        this.unit = unit;
        this.description = description;
        this.rate = rate;
        this.sellerId = sellerId;
        this.productPicUrls = productPicUrls;
        this.date = date;
        this.instock = instock;
        this.views = views;
        this.sanitize = sanitize;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getInstock() {
        return instock;
    }

    public void setInstock(Boolean instock) {
        this.instock = instock;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setProductPicUrls(ArrayList<String> productPicUrls) {
        this.productPicUrls = productPicUrls;
    }

    public String getId() {
        return id;
    }

    public String getCat() {
        return cat;
    }

    public String getName() {
        return name;
    }

    public void setSanitize(boolean sanitize) {
        this.sanitize = sanitize;
    }

    public String getVariety() {
        return variety;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }

    public String getRate() {
        return rate;
    }

    public String getSellerId() {
        return sellerId;
    }

    public ArrayList<String> getProductPicUrls() {
        return productPicUrls;
    }


}
