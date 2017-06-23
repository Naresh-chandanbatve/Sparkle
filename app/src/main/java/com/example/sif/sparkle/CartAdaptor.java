package com.example.sif.sparkle;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arch1 on 18-06-2017.
 */

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.CartViewHolder>{

    private ArrayList<Product> products;
    private Context ctx;

    public CartAdaptor(ArrayList<Product> products, Context ctx){ this.products=products; this.ctx=ctx;}

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.cart_layout,parent,false);
        CartViewHolder holder=new CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {
        final Product p= products.get(position);
        holder.name.setText(p.getProductName());
        holder.desc.setText(p.getShortDescription());
        holder.rating.setText(p.getRating()+"");

        if(p.getRating()>=3.5){
            holder.ratingView.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorGreen));
        }else if(p.getRating()>=2.5){
            holder.ratingView.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorOrange));
        }
        else {
            holder.ratingView.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorRed));
        }

        if(p.getProductImage()!=null)
            holder.image.setImageBitmap(p.getProductImage());
        else {
            //holder.image.setImageResource(R.drawable.break_profile);
            Picasso.with(ctx).invalidate(p.getProductImageUrl());
            Picasso.with(ctx)
                    .load(p.getProductImageUrl())
                    .placeholder(R.drawable.default_profile_pic) // optional
                    .error(R.drawable.break_profile)// optional
                    .into(holder.image);
        }

        holder.price.setText(String.format("Rs. %.2f",p.getSellingPrice()));

        if(p.getDicount()!=0){
            holder.cp.setVisibility(View.VISIBLE);
            holder.cp.setText(String.format("%.2f",p.getPrice()));
            holder.cp.setPaintFlags(holder.cp.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            holder.discount.setVisibility(View.VISIBLE);
            holder.discount.setText(p.getDicount()+"% off");
        }

        holder.quantity.setText(p.getQuantity()+"");

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ctx instanceof CartActivity){
                    ((CartActivity)ctx).removeFromCart(p,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        private TextView name,desc;
        private TextView price,rating;
        private ImageView image;
        private View ratingView;
        private TextView cp,discount;
        private TextView quantity;
        private Button removeButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            quantity=(TextView) itemView.findViewById(R.id.tv_cart_quantity);
            ratingView=itemView.findViewById(R.id.rating);
            name=(TextView)itemView.findViewById(R.id.tv_product_name);
            desc=(TextView)itemView.findViewById(R.id.tv_short_desc);
            price=(TextView)itemView.findViewById(R.id.tv_price);
            rating=(TextView)ratingView.findViewById(R.id.rating_text);
            image=(ImageView)itemView.findViewById(R.id.iv_product);
            cp=(TextView)itemView.findViewById(R.id.tv_costPrice);
            discount=(TextView)itemView.findViewById(R.id.tv_discount);
            removeButton=(Button)itemView.findViewById(R.id.btn_cart_remove);
        }
    }
}
