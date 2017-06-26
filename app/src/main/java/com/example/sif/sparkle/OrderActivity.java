package com.example.sif.sparkle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private Cart mCart;
    private SharedPreferences sharedPreferences;
    private TextView address,pname,pqty,pprice;
    private TextView icount,orderPrice,includeAmount;
    private Button changeAddressButton, orderButton;
    private View view;
    private double amount;
    private ArrayList<Product> cartProducts;
    private ScrollView scrollView;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent= getIntent();
        mCart=intent.getParcelableExtra("cart");
        cartProducts=mCart.getCartProduct();


        //Initializations
        sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
        address=(TextView)findViewById(R.id.tv_order_address);
        pname=(TextView)findViewById(R.id.tv_order_name);
        pqty=(TextView)findViewById(R.id.tv_order_quantity);
        pprice=(TextView)findViewById(R.id.tv_order_product_price);
        icount=(TextView)findViewById(R.id.tv_order_items_count);
        orderPrice=(TextView)findViewById(R.id.tv_order_price);
        changeAddressButton=(Button)findViewById(R.id.btn_order_chnage_add);
        view=findViewById(R.id.button_include);
        orderButton=(Button)view.findViewById(R.id.btn_cart_continue);
        includeAmount=(TextView) view.findViewById(R.id.tv_cart_amount);
        scrollView=(ScrollView)findViewById(R.id.scrollView);
        cardView=(CardView)findViewById(R.id.cv_order_success);

        hideOrderSuccess();

        String add=sharedPreferences.getString("add1","");
        add=add+"\n"+sharedPreferences.getString("add2","");
        add=add+"\n"+sharedPreferences.getString("city","");
        add=add+"\n"+sharedPreferences.getString("state","");
        add=add+"\n"+sharedPreferences.getString("pin","");

        address.setText(add);

        icount.setText("("+cartProducts.size()+" item)");
        setAmount(includeAmount);
        setAmount(orderPrice);

        orderButton.setText("Order Now");

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //empty cart
                requestOrder();
                showOrderSuccess();
            }
        });

        changeAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OrderActivity.this,ProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void showOrderSuccess(){
        cardView.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
    }

    private void hideOrderSuccess(){
        cardView.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
    }


    private void setAmount(TextView t){
        amount=0;
        for(int j=0;j<cartProducts.size();j++){
            Product p=cartProducts.get(j);
            double amt=p.getSellingPrice()*p.getQuantity();
            amount=amount+amt;
        }
        t.setText(String.format("Rs. %.2f",amount));
    }

    private void requestOrder(){


        StringRequest request = new StringRequest(Request.Method.POST, "http://link",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //response here
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderActivity.this,getString(R.string.err_connection),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String > params=new HashMap<>();
                for(int i=0;i<cartProducts.size();i++){
                Product p=cartProducts.get(i);
                    params.put("id[]",p.getId()+"");
                    params.put("qty[]",p.getQuantity()+"");
                }
                params.put("uid",sharedPreferences.getString("uid","-1"));
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
