package com.example.sif.sparkle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView reg;
    private View login_layout;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialization
        login_layout=(View)findViewById(R.id.login_include);
        reg=(TextView)login_layout.findViewById(R.id.tv_register);
        loginButton=(Button)login_layout.findViewById(R.id.button);


        //Register OnClick
        reg.setOnClickListener(this);

        //Login OnClick
        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.button: Toast.makeText(LoginActivity.this,"Login",
                    Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_register: Toast.makeText(LoginActivity.this,"Register",
                    Toast.LENGTH_SHORT).show();
                break;
            default:break;
        }
    }
}
