package com.example.sif.sparkle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sif.sparkle.Fragments.MenFragment;
import com.example.sif.sparkle.Fragments.WomenFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce = false;
    private CoordinatorLayout content;
    private View header;
    private TextView hName,hEmail;
    private RoundedImageView profilePic,profilePicWrapper;
    private Cart mCart;
    private String GET_CART_URL="https://techstart.000webhostapp.com/get_cart.php";
    private String GET_USER_RATING="https://techstart.000webhostapp.com/get_user_rating.php";
    private ProgressDialog pd;
    private ArrayList<Product> products;
    private String response;
    private NavigationView navigationView;
    private FloatingActionButton cartFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        //Initialization
        header=navigationView.getHeaderView(0);
        hName=(TextView) header.findViewById(R.id.header_name);
        hEmail=(TextView)header.findViewById(R.id.header_email);
        profilePic=(RoundedImageView)header.findViewById(R.id.header_profile);
        profilePicWrapper=(RoundedImageView)header.findViewById(R.id.header_profile_wrapper);
        content = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mCart=new Cart();
        pd = new ProgressDialog(this);
        pd.setTitle("Loading cart");
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        products=new ArrayList<>();
        cartFab=(FloatingActionButton)findViewById(R.id.fab_cart);

        SharedPreferences sharedPreferences=getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
        hName.setText(sharedPreferences.getString("name","ABC"));
        hEmail.setText(sharedPreferences.getString("email","abc@xyz.com"));
        profilePicWrapper.setImageResource(R.drawable.white);

        if(!sharedPreferences.getString("url","0").equals("0")) {
            String url=sharedPreferences.getString("url","0");
                Picasso.with(this).invalidate(url);
                Picasso.with(this)
                        .load(url)
                        .placeholder(R.drawable.default_profile_pic) // optional
                        .error(R.drawable.break_profile)// optional
                        .into(profilePic);

                profilePic.setImageResource(R.drawable.default_profile_pic);

        }
        else {
            profilePic.setImageResource(R.drawable.default_profile_pic);
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        cartFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCartActivity();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestUserRating();
        Toast.makeText(this,"HomeActivity: "+mCart.getItemNumber(),Toast.LENGTH_SHORT).show();
        navigationView.setCheckedItem(R.id.nav_men);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment=new MenFragment();
        fragmentTransaction.replace(R.id.fl_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            finishAffinity();
            return;
        }

        doubleBackToExitPressedOnce = true;

        Snackbar.make(content, "Click Back again to Exit", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences = getApplicationContext().
                    getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uid", "-1");
            editor.commit();
            Intent i = new Intent(HomeActivity.this, UserLogin.class);
            startActivity(i);
            finishAffinity();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_men) {
            Fragment fragment=new MenFragment();
            fragmentTransaction.replace(R.id.fl_container,fragment);
        } else if (id == R.id.nav_women) {
            Fragment fragment=new WomenFragment();
            fragmentTransaction.replace(R.id.fl_container,fragment);
        } else if (id == R.id.nav_cart) {
            callCartActivity();
        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {
            Intent i=new Intent(HomeActivity.this,AboutUsActivity.class);
            startActivity(i);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void callCartActivity(){
        Intent intent=new Intent(HomeActivity.this,CartActivity.class);
        intent.putExtra("cart",mCart);
        startActivity(intent);
    }

    private void getCart() {

        pd.show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, GET_CART_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("status").equals("ok")){
                                int count=Integer.parseInt(jsonObject.getString("count"));
                                mCart.clear();
                                for(int i=0;i<count;i++){
                                    JSONObject object=jsonObject.getJSONObject(i+"");
                                    Product p=getProduct(Integer.parseInt(object.getString("pid")),Integer.parseInt(object.getString("quantity")));
                                    if(p!=null){
                                        mCart.addToCart(p);
                                    }
                                }
                                Toast.makeText(HomeActivity.this,"Cart: success",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mCart.clear();
                                Toast.makeText(HomeActivity.this,"Cart: "+jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this,"Cart: exception",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this,"Cart: "+getString(R.string.err_connection),Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                SharedPreferences sharedPreferences=getApplicationContext()
                        .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
                params.put("uid",sharedPreferences.getString("uid","-1"));
                return params;
            }
        };

        RequestQueue request= Volley.newRequestQueue(this);
        request.add(stringRequest);

    }

    @Nullable
    private Product getProduct(int pid, int quantity){
        for(int i=0;i<products.size();i++){
            Product p=products.get(i);
            if(pid==p.getId()){
                p.setQuantity(quantity);
                return p;
            }
        }
        return null;
    }


    private void parseResponseFromCache() throws JSONException {
        SharedPreferences sharedPreferences=getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
        response=sharedPreferences.getString("response","");

        if(!response.equals("")) {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("ok")) {
                int count = Integer.parseInt(jsonObject.getString("count"));
                for (int i = 0; i < count; i++) {
                    JSONObject object = jsonObject.getJSONObject(i + "");
                        Product p = new Product(this);
                        p.setId(Integer.parseInt(object.getString("pid")));
                        p.setProductName(object.getString("pname"));
                        p.setShortDescription(object.getString("sdesc"));
                        p.setLongDescription(object.getString("ldesc"));
                        p.setRating(Double.parseDouble(object.getString("rating")));
                        p.setPrice(Double.parseDouble(object.getString("price")));
                        p.setDicount(Double.parseDouble(object.getString("discount")));
                        p.setSuitedFor(object.getString("gender"));
                        p.setProductImageUrl(object.getString("purl"));
                        p.setProduct();
                        products.add(p);
                }

            } else {
                String errorString = jsonObject.getString("error");
                Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"No response in cart",Toast.LENGTH_SHORT).show();
        }
    }

    public void parseResponse(){
        try {
            parseResponseFromCache();
            getCart();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void addToCart(Product product){
        mCart.addToCart(product);
    }

    public Boolean isInCart(Product product){
        if(mCart.isInCart(product)){
            return true;
        }
        return false;
    }

    public Cart getmCart(){return mCart;}

//    private void requestUserRaing(){
//
//        StringRequest request=new StringRequest(Request.Method.POST, GET_USER_RATING, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                SharedPreferences sharedPreferences = getApplicationContext()
//                        .getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//                editor.putString("userRating",response);
//                editor.commit();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params=new HashMap<>();
//                SharedPreferences sharedPreferences = getApplicationContext()
//                        .getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
//                params.put("uid",sharedPreferences.getString("uid","-1"));
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue=Volley.newRequestQueue(this);
//        requestQueue.add(request);
//    }

    private void requestUserRating(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, GET_USER_RATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences sharedPreferences = getApplicationContext()
                        .getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("userRating",response);
                editor.commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                SharedPreferences sharedPreferences = getApplicationContext()
                        .getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE);
                params.put("uid",sharedPreferences.getString("uid","-1"));
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
