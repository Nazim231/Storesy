package com.naztec.storesy.Models;

public class MultiLayoutModel {

    String layoutTitle;
    int layoutID;

    public MultiLayoutModel(int layoutID, String layoutTitle) {
        this.layoutID = layoutID;
        this.layoutTitle = layoutTitle;
    }

    public int getLayoutID() {
        return layoutID;
    }

    public String getLayoutTitle() {
        return layoutTitle;
    }

}
