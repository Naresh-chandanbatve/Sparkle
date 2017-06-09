package com.example.sif.sparkle;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private RoundedImageView profileWrapper,profile;
    private TextView name,pass,mob,email,add1,add2,city,state,pin;
    private View layout_profile;
    private RelativeLayout passLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initialization
        profile=(RoundedImageView)findViewById(R.id.circleView);
        profileWrapper=(RoundedImageView)findViewById(R.id.circleViewOverlay);
        layout_profile=findViewById(R.id.layout_profile);
        name=(TextView)layout_profile.findViewById(R.id.tv_name);
        email=(TextView)layout_profile.findViewById(R.id.tv_email);
        pass=(TextView)layout_profile.findViewById(R.id.tv_changePassword);
        mob=(TextView)layout_profile.findViewById(R.id.tv_phone);
        add1=(TextView)layout_profile.findViewById(R.id.tv_add1);
        add2=(TextView)layout_profile.findViewById(R.id.tv_add2);
        city=(TextView)layout_profile.findViewById(R.id.tv_city);
        state=(TextView)layout_profile.findViewById(R.id.tv_state);
        pin=(TextView)layout_profile.findViewById(R.id.tv_pin);
        passLayout=(RelativeLayout)layout_profile.findViewById(R.id.rl_passLayout);


        SharedPreferences sharedPreferences=getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);

        name.setText(sharedPreferences.getString("name","ABC"));
        email.setText(sharedPreferences.getString("email","abc@xyz.com"));
        mob.setText(sharedPreferences.getString("contact","0000000000"));
        add1.setText(sharedPreferences.getString("add1","Add1"));
        add2.setText(sharedPreferences.getString("add2","Add2"));
        city.setText(sharedPreferences.getString("city","city"));
        state.setText(sharedPreferences.getString("state","state"));
        pin.setText(sharedPreferences.getString("pin","pin"));

        passLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this,"Change password Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        profileWrapper.setImageResource(R.drawable.white);
        profile.setImageResource(R.drawable.default_profile_pic);
    }
}
