package com.example.sif.sparkle;

import android.graphics.Bitmap;

/**
 * Created by arch1 on 15-06-2017.
 */

public class Product {

    private int id;
    private String productName,shortDescription,LongDescription;
    private double rating,price;
    private Bitmap productImage;

    public Product(int id, String productName, float price) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        shortDescription="abc";
        LongDescription="ABC";
        rating=0.0;
        productImage=null;
    }

    public Product(int id, String productName, String shortDescription, String longDescription, double rating, double price, Bitmap productImage) {
        this.id = id;
        this.productName = productName;
        this.shortDescription = shortDescription;
        LongDescription = longDescription;
        this.rating = rating;
        this.price = price;
        this.productImage = productImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Bitmap getProductImage() {
        return productImage;
    }

    public void setProductImage(Bitmap productImage) {
        this.productImage = productImage;
    }
}
