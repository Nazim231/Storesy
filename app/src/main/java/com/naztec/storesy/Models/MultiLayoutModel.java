package com.naztec.storesy.Models;

import java.util.ArrayList;

public class MultiLayoutModel {

    int layoutID;
    String layoutTitle;
    ArrayList<String> productIds;

    public MultiLayoutModel(int layoutID, String layoutTitle, ArrayList<String> productIds) {
        this.layoutID = layoutID;
        this.layoutTitle = layoutTitle;
        this.productIds = productIds;
    }

    public int getLayoutID() {
        return layoutID;
    }

    public String getLayoutTitle() {
        return layoutTitle;
    }

    public ArrayList<String> getProductIds() {
        return productIds;
    }

}
