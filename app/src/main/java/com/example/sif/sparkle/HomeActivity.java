package com.example.sif.sparkle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce = false;
    private CoordinatorLayout content;
    private View header;
    private TextView hName,hEmail;
    private RoundedImageView profilePic,profilePicWrapper;

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

        navigationView.setCheckedItem(R.id.nav_men);
        
        //Initialization
        header=navigationView.getHeaderView(0);
        hName=(TextView) header.findViewById(R.id.header_name);
        hEmail=(TextView)header.findViewById(R.id.header_email);
        profilePic=(RoundedImageView)header.findViewById(R.id.header_profile);
        profilePicWrapper=(RoundedImageView)header.findViewById(R.id.header_profile_wrapper);
        content = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment=new MenFragment();
        fragmentTransaction.replace(R.id.fl_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
