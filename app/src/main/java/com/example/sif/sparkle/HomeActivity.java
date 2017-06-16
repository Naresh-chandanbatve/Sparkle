package com.example.sif.sparkle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce=false;
    private CoordinatorLayout content;
    private RecyclerView recyclerView;
    private ProductAdaptor adaptor;
    private LinearLayoutManager layoutManager;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Initialization
        content=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        recyclerView=(RecyclerView)findViewById(R.id.rv_product);
        layoutManager=new LinearLayoutManager(HomeActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        Bitmap icon= BitmapFactory.decodeResource(getResources(), R.drawable.login);

        Product one =new Product(1,"IceCream","Amul IceCream","Amul IceCream with chocolate syrup",4.2,50,icon,20);
        Product two =new Product(2,"IceCream","Vadilal IceCream","Vadilal IceCream with chocolate syrup",2.5,50,null,0);
        Product three =new Product(3,"Laptop","Acer Laptop","Acer Laptop with High end specifications",5.0,50000,null,25);
        Product four =new Product(4,"Milk","Amul milk","Amul milk with cream",3.5,26,null,0);
        Product five =new Product(5,"Curd","Amul Dahi","Amul IceCream with chocolate syrup",3.6,10,null,0);
        Product six =new Product(8,"Soap","Dettol Soap","Amul IceCream with chocolate syrup",4.8,20,null,0);
        Product seven =new Product(7,"HandWash","Dettol HandWash","Amul IceCream with chocolate syrup",2.2,60,null,0);
        Product eight =new Product(8,"Ghee","Amul Ghee","Amul IceCream with chocolate syrup",2.6,200,null,0);

        ArrayList<Product> products=new ArrayList<>();
        products.add(one);
        products.add(two);
        products.add(three);
        products.add(four);
        products.add(five);
        products.add(six);
        products.add(seven);
        products.add(eight);
        products.add(one);
        products.add(one);
        products.add(one);
        products.add(one);
        products.add(one);
        products.add(one);

        adaptor=new ProductAdaptor(products, HomeActivity.this, new ProductAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(Product product,int position) {
                Toast.makeText(HomeActivity.this,"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adaptor);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else  if (doubleBackToExitPressedOnce) {
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
                doubleBackToExitPressedOnce=false;
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
            Intent i= new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(i);
        }
        if (id== R.id.action_logout){
            SharedPreferences sharedPreferences=getApplicationContext().
                    getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("uid","-1");
            editor.commit();
            Intent i = new Intent(HomeActivity.this,UserLogin.class);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
