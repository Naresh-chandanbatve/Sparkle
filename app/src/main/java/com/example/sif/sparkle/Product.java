package com.example.sif.sparkle;

import android.graphics.Bitmap;

/**
 * Created by arch1 on 15-06-2017.
 */

public class Product {

    private int id;
    private String productName;
    private String shortDescription;
    private String LongDescription;
    private double rating,price,dicount,sellingPrice;
    private Bitmap productImage;
    private int categoryId,suitedFor;

    public Product(){}

    public Product(int id, String productName, float price) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        shortDescription="abc";
        LongDescription="ABC";
        rating=0.0;
        dicount=0.0;
        productImage=null;
        setDiscountedPrice();
    }

    public Product(int id, String productName, String shortDescription, String longDescription, double rating, double price, Bitmap productImage, double dicount) {
        this.id = id;
        this.productName = productName;
        this.shortDescription = shortDescription;
        LongDescription = longDescription;
        this.rating = rating;
        this.price = price;
        this.productImage = productImage;
        this.dicount=dicount;
        setDiscountedPrice();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSuitedFor() {
        return suitedFor;
    }

    public void setSuitedFor(int suitedFor) {
        this.suitedFor = suitedFor;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDicount() {
        return dicount;
    }

    public void setDicount(double dicount) {
        this.dicount = dicount;
    }

    private void setDiscountedPrice(){
        sellingPrice=((100-dicount)/100)*price;
    }

    public double getSellingPrice() {
        return sellingPrice;
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
