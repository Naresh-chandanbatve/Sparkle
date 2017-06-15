package com.example.sif.sparkle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class UserLogin extends AppCompatActivity {

    Button login_button, register_button;
    ImageButton facebook_button, google_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        login_button = (Button) findViewById(R.id.btn_login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        ifLoggedin();
    }

    private void ifLoggedin(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
        int uid = Integer.parseInt(sharedPreferences.getString("uid","-1"));

        if(uid!=-1) {
            Intent i = new Intent(UserLogin.this,HomeActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }

}