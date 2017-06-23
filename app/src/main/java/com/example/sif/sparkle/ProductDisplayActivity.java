package com.example.sif.sparkle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDisplayActivity extends AppCompatActivity {

    private TextView productName,productDescription,costPrice,sellingPrice,discount;
    private ImageView productImage;
    private RatingBar ratingBar;
    private View ratingView;
    private TextView rating;
    private Button addToCartButton;
    private Product product;
    private Spinner spinner;
    private Boolean isInCart;
    private ProgressDialog pd;
    private Cart mCart;
    private boolean isRated;
    private static final String ADD_TO_CART_URL="https://techstart.000webhostapp.com/add_to_cart.php";
    private static final String UPDATE_USER_RATING_URL="https://techstart.000webhostapp.com/update_user_rating.php";
    private static final String INSERT_USER_RATING_URL="https://techstart.000webhostapp.com/new_user_rating.php";

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
        addToCartButton=(Button)findViewById(R.id.btn_add_to_cart);
        spinner=(Spinner)findViewById(R.id.spinner);
        pd=new ProgressDialog(this);

        Intent intent=getIntent();
        product= intent.getExtras().getParcelable("product");
        isInCart=intent.getExtras().getBoolean("isInCart");
        mCart=intent.getParcelableExtra("cart");

        try {
            parseUserRating();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(isInCart){
            addToCartButton.setText("Go to Cart");
        }

        if(product.getUserRating() == 0){
            isRated=false;
        }
        else {
            isRated =true;
        }

        if(product.getDicount()!=0){
            costPrice.setVisibility(View.VISIBLE);
            costPrice.setText(product.getPrice()+"");
            costPrice.setPaintFlags(costPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            discount.setVisibility(View.VISIBLE);
            discount.setText(product.getDicount()+"% off");
        }

        productName.setText(product.getProductName());
        productDescription.setText(product.getLongDescription());
        sellingPrice.setText("Rs. "+product.getSellingPrice());

        rating.setText(String.format("%.1f",product.getRating()));

        ratingBar.setRating((float) product.getUserRating());

        if(product.getRating()>=3.5){
            ratingView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorGreen));
        }else if(product.getRating()>=2.5){
            ratingView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorOrange));
        }
        else {
            ratingView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorRed));
        }

        if(product.getProductImage()!=null)
            productImage.setImageBitmap(product.getProductImage());
        else {
            //holder.image.setImageResource(R.drawable.break_profile);
            Picasso.with(this).invalidate(product.getProductImageUrl());
            Picasso.with(this)
                    .load(product.getProductImageUrl())
                    .placeholder(R.drawable.default_profile_pic) // optional
                    .error(R.drawable.break_profile)// optional
                    .into(productImage);
        }

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isInCart) {
                    String quantity = spinner.getSelectedItem().toString().trim();
                    product.setQuantity(Integer.parseInt(quantity));
                    addToCart(product.getId() + "", quantity);
//                    Intent intent = new Intent();
//                    intent.putExtra("addProduct", product);
//                    setResult(RESULT_OK, intent);
                    isInCart=true;
                    mCart.addToCart(product);
                    addToCartButton.setText("Go to Cart");
                }
                else {
                    Toast.makeText(ProductDisplayActivity.this,"To Cart Activity",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ProductDisplayActivity.this,CartActivity.class);
                    intent.putExtra("cart",mCart);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                    if(isRated)
                        updateUserRating(rating);
                    else
                        insertUserRating(rating);
                }
            }
        });

    }

    private void addToCart(final String pid, final String quantity){

        pd.setTitle("Adding to Cart");
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);

        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ADD_TO_CART_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("status").equals("ok")){
                                Toast.makeText(ProductDisplayActivity.this,"Added to Cart",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ProductDisplayActivity.this,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                               }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductDisplayActivity.this,"JSON Exception occurred",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(ProductDisplayActivity.this,getString(R.string.err_connection),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                SharedPreferences sharedPreferences=getApplicationContext()
                        .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
                params.put("uid",sharedPreferences.getString("uid","-1"));
                params.put("pid", pid);
                params.put("quantity", quantity);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void parseUserRating() throws JSONException {

        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
        String response=sharedPreferences.getString("userRating","");

        JSONObject jsonObject=new JSONObject(response);
        if(jsonObject.getString("status").equals("ok")){
            if(jsonObject.has(product.getId()+"")) {
                String userRating = jsonObject.getString(product.getId() + "");
                product.setUserRating(Double.parseDouble(userRating));
            }
            else {
                product.setUserRating(0.0);
            }
        }
    }

    private void updateUserRating(final float rating){

        pd.setTitle("Updating");
        pd.setMessage("Please wait...");
        pd.show();
        final String pid=product.getId()+"";
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
        final String uid=sharedPreferences.getString("uid","-1");

        StringRequest request =new StringRequest(Request.Method.POST, UPDATE_USER_RATING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String > params=new HashMap<>();
                params.put("uid",uid);
                params.put("pid",pid);
                params.put("rating",String.valueOf(rating));
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    private void insertUserRating(final float rating){

        pd.setTitle("Updating");
        pd.setMessage("Please wait...");
        pd.show();
        final String pid=product.getId()+"";
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
        final String uid=sharedPreferences.getString("uid","-1");

        StringRequest request =new StringRequest(Request.Method.POST, INSERT_USER_RATING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String > params=new HashMap<>();
                params.put("uid",uid);
                params.put("pid",pid);
                params.put("rating",String.valueOf(rating));
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
