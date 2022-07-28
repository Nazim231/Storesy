package com.naztec.storesy.Models;

public class CategoryModel {

    String categoryName, iconUrl;

    public CategoryModel() {
        // Required Empty Constructor
    }

    public CategoryModel(String categoryName, String iconUrl) {
        this.categoryName = categoryName;
        this.iconUrl = iconUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

}
