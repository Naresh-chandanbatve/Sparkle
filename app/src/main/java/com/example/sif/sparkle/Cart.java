package com.example.sif.sparkle;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by arch1 on 19-06-2017.
 */

public class Cart implements Parcelable{

    private ArrayList<Product> cartProduct;
    private Product[] productArray;

    public Cart(){
        cartProduct=new ArrayList<>();
    }

    public Cart(Parcel in){
        cartProduct=new ArrayList<>();
        productArray=null;
        productArray=(Product[]) in.createTypedArray(Product.CREATOR);
        for(int i=0;i<productArray.length;i++){
            cartProduct.add(productArray[i]);
        }
    }

    public void addToCart(Product product){
        cartProduct.add(product);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<Product> getCartProduct(){
        return cartProduct;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        productArray=null;
        productArray=new Product[cartProduct.size()];
        for(int i=0;i<cartProduct.size();i++){
            productArray[i]=cartProduct.get(i);
        }

        dest.writeTypedArray(productArray,flags);
    }

    public Boolean isInCart(Product product){
        for(int i=0;i<cartProduct.size();i++){
            Product p=cartProduct.get(i);
            if(product.getId()==p.getId()){
                return true;
            }
        }
        return false;
    }

    public Boolean isEmpty(){
        if(cartProduct.size()==0)
        {
            return true;
        }
        else {
            return false;
        }
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    public int getItemNumber(){
        return cartProduct.size();
    }

    public void clear(){
        cartProduct.clear();
    }

    public void removeFromCart(Product product){
        cartProduct.remove(product);
    }
}
