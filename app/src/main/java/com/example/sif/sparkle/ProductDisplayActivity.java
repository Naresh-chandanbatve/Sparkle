package com.example.sif.sparkle;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDisplayActivity extends AppCompatActivity {

    private TextView productName,productDescription,costPrice,sellingPrice,discount;
    private ImageView productImage;
    private RatingBar ratingBar;
    private View ratingView;
    private TextView rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        //Initializations
        productName=(TextView)findViewById(R.id.tv_detail_product_name);
        productDescription=(TextView)findViewById(R.id.tv_detail_product_desc);
        costPrice=(TextView)findViewById(R.id.tv_detail_costprice);
        sellingPrice=(TextView)findViewById(R.id.tv_detail_product_price);
        discount=(TextView)findViewById(R.id.tv_detail_discount);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        ratingView=findViewById(R.id.rating);
        rating=(TextView)ratingView.findViewById(R.id.rating_text);
        productImage=(ImageView)findViewById(R.id.iv_detail_product_image);

        Intent intent=getIntent();
        Product p= intent.getExtras().getParcelable("product");

        if(p.getDicount()!=0){
            costPrice.setVisibility(View.VISIBLE);
            costPrice.setText(p.getPrice()+"");
            costPrice.setPaintFlags(costPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            discount.setVisibility(View.VISIBLE);
            discount.setText(p.getDicount()+"% off");
        }

        productName.setText(p.getProductName());
        productDescription.setText(p.getLongDescription());
        sellingPrice.setText("Rs. "+p.getSellingPrice());

        rating.setText(p.getRating()+"");

        if(p.getRating()>=3.5){
            ratingView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorGreen));
        }else if(p.getRating()>=2.5){
            ratingView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorOrange));
        }
        else {
            ratingView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorRed));
        }

        if(p.getProductImage()!=null)
            productImage.setImageBitmap(p.getProductImage());
        else {
            //holder.image.setImageResource(R.drawable.break_profile);
            Picasso.with(this).invalidate(p.getProductImageUrl());
            Picasso.with(this)
                    .load(p.getProductImageUrl())
                    .placeholder(R.drawable.default_profile_pic) // optional
                    .error(R.drawable.break_profile)// optional
                    .into(productImage);
        }
    }
}
