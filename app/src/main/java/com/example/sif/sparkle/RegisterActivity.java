package com.example.sif.sparkle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private View reg_layout;
    private Button regButton;
    private TextInputLayout inputLayoutName,inputLayoutEmail,inputLayoutPass,inputLayoutCPass;
    private TextInputLayout inputLayoutContact, inputLayoutPin;
    private EditText inputName,inputEmail,inputPass,inputCPass,inputAdd1,inputAdd2,inputCity;
    private EditText inputState,inputPin,inputContact;
    private ProgressDialog pd;

    private String PIN_PATTERN="[0-9]{6}";
    private String CONTACT_PATTERN="[9,8,7]{1}[0-9]{9}";
    private Pattern pattern;
    private Matcher matcher;

    private String REGISTER_URL="https://techstart.000webhostapp.com/reg_user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialization
        reg_layout=findViewById(R.id.register_include);
        regButton=(Button)reg_layout.findViewById(R.id.btn_reg);
        inputLayoutName=(TextInputLayout)reg_layout.findViewById(R.id.nameWrapper);
        inputLayoutEmail=(TextInputLayout)reg_layout.findViewById(R.id.emailWrapper);
        inputLayoutPass=(TextInputLayout)reg_layout.findViewById(R.id.passWrapper);
        inputLayoutCPass=(TextInputLayout)reg_layout.findViewById(R.id.cpassWrapper);
        inputLayoutContact=(TextInputLayout)reg_layout.findViewById(R.id.contactWrapper);
        inputLayoutPin=(TextInputLayout)reg_layout.findViewById(R.id.pinWrapper);

        inputName=(EditText)reg_layout.findViewById(R.id.et_rg_name);
        inputEmail=(EditText)reg_layout.findViewById(R.id.et_rg_email);
        inputPass=(EditText)reg_layout.findViewById(R.id.et_rg_pass);
        inputCPass=(EditText)reg_layout.findViewById(R.id.et_rg_cpass);
        inputContact=(EditText)reg_layout.findViewById(R.id.et_rg_contact);
        inputAdd1=(EditText)reg_layout.findViewById(R.id.et_rg_add1);
        inputAdd2=(EditText)reg_layout.findViewById(R.id.et_rg_add2);
        inputCity=(EditText)reg_layout.findViewById(R.id.et_rg_city);
        inputState=(EditText)reg_layout.findViewById(R.id.et_rg_state);
        inputPin=(EditText)reg_layout.findViewById(R.id.et_rg_pin);

        pd=new ProgressDialog(this);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if(ifValidRegister()){
                    //register user
                    sendRequest();
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Please enter a valid input",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean ifValidRegister() {
        if(!validateName())
            return false;
        if(!validateEmail())
            return false;
        if(!validatePassword())
            return false;
        if(!validateCpass())
            return false;
        if(!validateContact())
            return false;
        if(!validatePin())
            return false;

        return true;
    }



    private boolean validatePin() {
        String pin=inputPin.getText().toString().trim();
        if(!isValidPin(pin)){
            inputLayoutPin.setError(getString(R.string.err_msg_pin));
            requestFocus(inputPin);
            return false;
        }
        else {
            inputLayoutPin.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidPin(String pin) {
        pattern=Pattern.compile(PIN_PATTERN);
        matcher=pattern.matcher(pin);
        return matcher.matches();
    }

    private boolean validateContact() {
        String contact=inputContact.getText().toString().trim();
        if(!isValidContact(contact)){
            inputLayoutContact.setError(getString(R.string.err_msg_contact));
            requestFocus(inputContact);
            return false;
        }
        else {
            inputLayoutContact.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidContact(String contact) {
        pattern=Pattern.compile(CONTACT_PATTERN);
        matcher=pattern.matcher(contact);
        return matcher.matches();
    }

    private boolean validateCpass() {
        String password = inputPass.getText().toString().trim();
        String cpassword=inputCPass.getText().toString().trim();

        if(!password.equals(cpassword)){
            inputLayoutCPass.setError(getString(R.string.err_msg_cpass));
            requestFocus(inputCPass);
            return false;
        }
        else {
            inputLayoutCPass.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        String name=inputName.getText().toString().trim();

        if(name.isEmpty()){
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
            }
            else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
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
        String password = inputPass.getText().toString().trim();

        if(password.isEmpty() || !isValidPassword(password)){
            inputLayoutPass.setError(getString(R.string.err_msg_pass));
            requestFocus(inputPass);
            return false;
        }
        else {
            inputLayoutPass.setErrorEnabled(false);
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

    void sendRequest(){

        pd.setMessage("Please wait...");
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, REGISTER_URL,
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

                                editor.putString("name",inputName.getText().toString().trim());
                                editor.putString("email",inputEmail.getText().toString().trim());
                                editor.putString("pass",inputPass.getText().toString().trim());
                                editor.putString("contact",inputContact.getText().toString().trim());
                                editor.putString("add1",inputAdd1.getText().toString().trim());
                                editor.putString("add2",inputAdd2.getText().toString().trim());
                                editor.putString("city",inputCity.getText().toString().trim());
                                editor.putString("state",inputState.getText().toString().trim());
                                editor.putString("pin",inputPin.getText().toString().trim());
                                editor.putString("url","0");
                                editor.putString("img_string","0");

                                pd.cancel();
                                Toast.makeText(RegisterActivity.this,"Register success",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(RegisterActivity.this,HomeActivity.class);
                                startActivity(i);
                                finishAffinity();
                            }
                            else {
                                editor.putString("uid",jsonObject.getString("uid"));
                                String errorMsg=jsonObject.getString("error");
                                pd.cancel();

                                final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(RegisterActivity.this);
                                dlgAlert.setMessage(errorMsg);
                                dlgAlert.setTitle("Register Error");
                                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
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
                Toast.makeText(RegisterActivity.this,getString(R.string.err_connection),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params=new HashMap<>();
                params.put("name",inputName.getText().toString().trim());
                params.put("email",inputEmail.getText().toString().trim());
                params.put("pass",inputPass.getText().toString().trim());
                params.put("mob",inputContact.getText().toString().trim());
                params.put("add1",inputAdd1.getText().toString().trim());
                params.put("add2",inputAdd2.getText().toString().trim());
                params.put("city",inputCity.getText().toString().trim());
                params.put("state",inputState.getText().toString().trim());
                params.put("pin",inputPin.getText().toString().trim());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
