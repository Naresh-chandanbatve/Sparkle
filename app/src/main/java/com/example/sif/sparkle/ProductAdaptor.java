package com.example.sif.sparkle;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arch1 on 15-06-2017.
 */

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.ProductViewHolder>{

    private ArrayList<Product> list;
    private Context ctx;

    ProductAdaptor(ArrayList<Product> list ,Context ctx){
        this.list=list;
        this.ctx=ctx;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.product_display_layout,parent,false);
        ProductViewHolder holder=new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product p=list.get(position);
        holder.name.setText(p.getProductName());
        holder.desc.setText(p.getShortDescription());

        holder.rating.setText(p.getRating()+"");

        if(p.getRating()>=3.5){
            holder.ratingView.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorGreen));
        }else if(p.getRating()>2.5){
            holder.ratingView.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorOrange));
        }
        else {
            holder.ratingView.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorRed));
        }

        if(p.getProductImage()!=null)
            holder.image.setImageBitmap(p.getProductImage());
        else
            holder.image.setImageResource(R.mipmap.ic_launcher);

        holder.price.setText("Rs. "+p.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        private TextView name,desc;
        private TextView price,rating;
        private ImageView image;
        private View ratingView;
        private LinearLayout ratingBackground;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ratingView=itemView.findViewById(R.id.rating);
            name=(TextView)itemView.findViewById(R.id.tv_product_name);
            desc=(TextView)itemView.findViewById(R.id.tv_short_desc);
            price=(TextView)itemView.findViewById(R.id.tv_price);
            rating=(TextView)ratingView.findViewById(R.id.rating_text);
            image=(ImageView)itemView.findViewById(R.id.iv_product);
            //ratingBackground=(LinearLayout)ratingView.findViewById(R.id.rating_background);
        }
    }
}
