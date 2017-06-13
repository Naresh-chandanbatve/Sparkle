package com.example.sif.sparkle;

import android.content.Intent;
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

        login_button = (Button) findViewById(R.id.login_button);
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
        facebook_button = (ImageButton) findViewById(R.id.facebook_button);

        google_button = (ImageButton) findViewById(R.id.google_button);

    }
}
