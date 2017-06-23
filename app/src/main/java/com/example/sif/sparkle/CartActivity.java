package com.example.sif.sparkle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {


    private ArrayList<Product> cartProducts;
    private Cart mCart;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CartAdaptor mAdaptor;
    private ScrollView scrollView;
    private View view;
    private CardView noProductView;
    private double amount=0;
    private TextView cartAmount;
    private Button continueButton;
    private ProgressDialog pd;
    private String REMOVE_FROM_CART_URL="https://techstart.000webhostapp.com/remove_from_cart.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent i=getIntent();
        mCart=i.getParcelableExtra("cart");
        cartProducts=mCart.getCartProduct();

        //Initializations
        mRecyclerView=(RecyclerView)findViewById(R.id.rv_cart);
        mLayoutManager=new LinearLayoutManager(this);
        mAdaptor=new CartAdaptor(cartProducts,CartActivity.this);
        scrollView=(ScrollView)findViewById(R.id.sv_cart);
        view=findViewById(R.id.button_include);
        noProductView=(CardView)findViewById(R.id.cv_noProduct);
        cartAmount=(TextView)view.findViewById(R.id.tv_cart_amount);
        continueButton=(Button)view.findViewById(R.id.btn_cart_continue);
        pd=new ProgressDialog(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdaptor);
        pd.setTitle("Removing");
        pd.setMessage("Please wait...");

        if(cartProducts.size()==0){
            hideCart();
        }
        else {
            showCart();
        }

        setAmount();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this,"Continue button clicked",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAmount(){
        amount=0;
        for(int j=0;j<cartProducts.size();j++){
            Product p=cartProducts.get(j);
            double amt=p.getSellingPrice()*p.getQuantity();
            amount=amount+amt;
        }
        cartAmount.setText(String.format("Rs. %.2f",amount));
    }


    public void hideCart(){
        scrollView.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        noProductView.setVisibility(View.VISIBLE);
    }

    public void showCart(){
        scrollView.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
        noProductView.setVisibility(View.GONE);
    }

    public void removeFromCart(Product p, int position){
        mCart.removeFromCart(p);
        cartProducts=mCart.getCartProduct();
        mRecyclerView.removeViewAt(position);
        mAdaptor.notifyItemRemoved(position);
        mAdaptor.notifyItemRangeChanged(position, cartProducts.size());
        removeFromCartRequest(p.getId());
        setAmount();
        if(cartProducts.size()==0){
            hideCart();
        }
    }

    private void removeFromCartRequest(final int pid){
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, REMOVE_FROM_CART_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("status").equals("ok")){
                                Toast.makeText(CartActivity.this,"Item Removed",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(CartActivity.this,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CartActivity.this,"Exception occured",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(CartActivity.this,getString(R.string.err_connection),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                SharedPreferences sharedPreferences=getApplicationContext()
                        .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
                params.put("uid",sharedPreferences.getString("uid","-1"));
                params.put("pid",pid+"");
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
