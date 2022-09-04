package com.naztec.storesy.Models;

public class ViewSectionItemsModel {

    String prodName;
    String prodPrice;
    String prodMRP;

    public ViewSectionItemsModel() {

    }

    public ViewSectionItemsModel(String prodName, String prodPrice, String prodMrp) {
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.prodMRP = prodMrp;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public String getProdMRP() {
        return prodMRP;
    }
}
