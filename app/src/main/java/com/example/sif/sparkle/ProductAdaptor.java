package com.example.sif.sparkle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by arch1 on 15-06-2017.
 */

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.ProductViewHolder>{

    private ArrayList<Product> list;
    private Context ctx;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Product product, int position);
    }

    public ProductAdaptor(ArrayList<Product> list ,Context ctx,OnItemClickListener listener){
        this.list=list;
        this.ctx=ctx;
        this.listener=listener;
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

        holder.price.setText("Rs. "+p.getSellingPrice());

        if(p.getDicount()!=0){
            holder.cp.setVisibility(View.VISIBLE);
            holder.cp.setText(p.getPrice()+"");
            holder.cp.setPaintFlags(holder.cp.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            holder.discount.setVisibility(View.VISIBLE);
            holder.discount.setText(p.getDicount()+"% off");
        }

        holder.bind(p,listener);
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
        private TextView cp,discount;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ratingView=itemView.findViewById(R.id.rating);
            name=(TextView)itemView.findViewById(R.id.tv_product_name);
            desc=(TextView)itemView.findViewById(R.id.tv_short_desc);
            price=(TextView)itemView.findViewById(R.id.tv_price);
            rating=(TextView)ratingView.findViewById(R.id.rating_text);
            image=(ImageView)itemView.findViewById(R.id.iv_product);
            cp=(TextView)itemView.findViewById(R.id.tv_costPrice);
            discount=(TextView)itemView.findViewById(R.id.tv_discount);
        }

        public void bind(final Product item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item,getAdapterPosition());
                }
            });
        }

    }
}
