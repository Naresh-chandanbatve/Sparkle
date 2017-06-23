package com.example.sif.sparkle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;

/**
 * Created by arch1 on 15-06-2017.
 */

public class Product implements Parcelable{

    private int id;
    private String productName;
    private String shortDescription;
    private String LongDescription;
    private double rating,price,dicount,sellingPrice;
    private Bitmap productImage;
    private String suitedFor;
    private String productImageUrl;
    private Target loadtarget;
    private Context ctx;
    private int quantity;
    private double userRating;
    private double totalRating;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(productName);
        dest.writeString(shortDescription);
        dest.writeString(LongDescription);
        dest.writeDouble(rating);
        dest.writeDouble(price);
        dest.writeDouble(dicount);
        dest.writeDouble(sellingPrice);
        dest.writeString(suitedFor);
        dest.writeString(productImageUrl);
        dest.writeInt(quantity);
        dest.writeDouble(userRating);
        dest.writeDouble(totalRating);
    }

    public Product(Parcel in) {
        id=in.readInt();
        productName = in.readString();
        shortDescription = in.readString();
        LongDescription = in.readString();
        rating=in.readDouble();
        price=in.readDouble();
        dicount=in.readDouble();
        sellingPrice=in.readDouble();
        suitedFor= in.readString();
        productImageUrl = in.readString();
        quantity=in.readInt();
        userRating=in.readDouble();
        totalRating=in.readDouble();
        setDiscountedPrice();
    }

    public Product(Context ctx){
        this.ctx=ctx;
    }

    public Product(int id, String productName, float price) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        shortDescription="abc";
        LongDescription="ABC";
        rating=0.0;
        dicount=0.0;
        productImage=null;
        userRating=0;
        totalRating=rating;
        setDiscountedPrice();
    }

    public Product(int id, String productName, String shortDescription, String longDescription, double rating, double price, String productImageUrl, double dicount) {
        this.id = id;
        this.productName = productName;
        this.shortDescription = shortDescription;
        LongDescription = longDescription;
        this.rating = rating;
        this.price = price;
        this.productImageUrl=productImageUrl;
        this.dicount=dicount;
        userRating=0;
        totalRating=rating;
        setDiscountedPrice();
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public void setProduct(){
        setDiscountedPrice();
        loadProductImage();
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSuitedFor() {
        return suitedFor;
    }

    public void setSuitedFor(String suitedFor) {
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
        setDiscountedPrice();
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

    private void loadProductImage(){
        loadBitmap(productImageUrl);
    }

    private void loadBitmap(String url) {

        if (loadtarget == null)
            loadtarget=new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    handleLoadedBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };

        Picasso.with(ctx).load(url)
                .placeholder(R.drawable.default_profile_pic) // optional
                .error(R.drawable.break_profile)// optional
                .into(loadtarget);
    }

    public void handleLoadedBitmap(Bitmap b) {
        // do something here
        productImage=b;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
