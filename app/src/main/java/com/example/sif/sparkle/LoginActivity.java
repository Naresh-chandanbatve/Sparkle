package com.example.sif.sparkle;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView reg;
    private View login_layout;
    private Button loginButton;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private EditText inputEmail,inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialization
        login_layout=findViewById(R.id.login_include);
        reg=(TextView)login_layout.findViewById(R.id.tv_register);
        loginButton=(Button)login_layout.findViewById(R.id.button);
        inputLayoutEmail=(TextInputLayout) login_layout.findViewById(R.id.etEmailLayout);
        inputLayoutPassword=(TextInputLayout) login_layout.findViewById(R.id.etPasswordLayout);
        inputEmail=(EditText)login_layout.findViewById(R.id.et_Email);
        inputPassword=(EditText)login_layout.findViewById(R.id.et_Password);

        //Register OnClick
        reg.setOnClickListener(this);

        //Login OnClick
        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.button:
                hideKeyboard();
                if(isValidLogin()){
                   //login user
                }
                break;
            case R.id.tv_register:
                Intent i=new Intent(this,RegisterActivity.class);
                startActivity(i);
                break;
            default:break;
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        String password = inputPassword.getText().toString().trim();

        if(password.isEmpty() || !isValidPassword(password)){
            inputLayoutPassword.setError(getString(R.string.err_msg_pass));
            requestFocus(inputPassword);
            return false;
        }
        else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isValidPassword(String password) {
        return password.length()>=8;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidLogin(){
        if(!validateEmail())
            return false;
        if(!validatePassword())
            return false;
        return true;
    }
}
