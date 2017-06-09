package com.example.sif.sparkle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView reg,errText;
    private View login_layout;
    private Button loginButton;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private EditText inputEmail,inputPassword;
    private ProgressDialog pd;

    private String LOGIN_URL="https://techstart.000webhostapp.com/login.php";

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
        errText=(TextView)login_layout.findViewById(R.id.tv_err_login);
        pd=new ProgressDialog(this);

        ifLoggedin();

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
                    loginUser();
                }
                break;
            case R.id.tv_register:
                Intent i=new Intent(this,RegisterActivity.class);
                startActivity(i);
                break;
            default:break;
        }
    }

    private void loginUser() {

        pd.setMessage("Please wait...");
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            SharedPreferences sharedPreferences = getApplicationContext()
                                    .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if(jsonObject.getString("status").equals("ok")) {
                                editor.putString("uid", jsonObject.getString("uid"));
                                pd.cancel();
                                Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(i);
                                finishAffinity();
                            }
                            else {
                                editor.putString("uid",jsonObject.getString("uid"));
                                pd.cancel();
                                errText.setVisibility(View.VISIBLE);
                            }

                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Toast.makeText(LoginActivity.this,getString(R.string.err_connection),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("email",inputEmail.getText().toString().trim());
                params.put("pass",inputPassword.getText().toString().trim());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

    private void ifLoggedin(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
        int uid = Integer.parseInt(sharedPreferences.getString("uid","-1"));

        if(uid!=-1) {
            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }
}
