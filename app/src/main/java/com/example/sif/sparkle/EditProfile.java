package com.example.sif.sparkle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sif on 6/13/2017.
 */

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private EditText email,mob,add1,add2,city,state,pin;
    private Button cancelButton, submitButton;
    private SharedPreferences sharedPreferences;
    private ProgressDialog pd;
    private TextInputLayout emailWrapper,mobWrapper,add1Wrapper,add2Wrapper,cityWrapper,stateWrapper,pinWrapper;
    private String PIN_PATTERN="[0-9]{6}";
    private String CONTACT_PATTERN="[9,8,7]{1}[0-9]{9}";
    private Pattern pattern;
    private Matcher matcher;
    private String EDIT_PROFILE_URL="https://techstart.000webhostapp.com/edit_profile.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);

        //Initializations
        email=(EditText)findViewById(R.id.et_editemail);
        mob=(EditText)findViewById(R.id.et_editmob);
        add1=(EditText)findViewById(R.id.et_edit_add1);
        add2=(EditText)findViewById(R.id.et_edit_add2);
        city=(EditText)findViewById(R.id.et_edit_city);
        state=(EditText)findViewById(R.id.et_edit_state);
        pin=(EditText)findViewById(R.id.et_edit_pin);
        cancelButton=(Button)findViewById(R.id.btn_cancel);
        submitButton=(Button)findViewById(R.id.btn_edit_profile);
        pd=new ProgressDialog(this);
        sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.shared_pref),MODE_PRIVATE);
        emailWrapper=(TextInputLayout)findViewById(R.id.emailWrapper);
        mobWrapper=(TextInputLayout)findViewById(R.id.mobWrapper);
        add1Wrapper=(TextInputLayout)findViewById(R.id.add1Wrapper);
        add2Wrapper=(TextInputLayout)findViewById(R.id.add2Wrapper);
        cityWrapper=(TextInputLayout)findViewById(R.id.cityWrapper);
        stateWrapper=(TextInputLayout)findViewById(R.id.stateWrapper);
        pinWrapper=(TextInputLayout)findViewById(R.id.pinWrapper);


        setValues();

        hideKeyboard();
        cancelButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        pd.setMessage("Please wait...");
    }

    private void setValues() {
        email.setText(sharedPreferences.getString("email",""));
        mob.setText(sharedPreferences.getString("contact",""));
        add1.setText(sharedPreferences.getString("add1",""));
        add2.setText(sharedPreferences.getString("add2",""));
        city.setText(sharedPreferences.getString("city",""));
        state.setText(sharedPreferences.getString("state",""));
        pin.setText(sharedPreferences.getString("pin",""));
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch (id){
            case R.id.btn_cancel:
                onBackPressed();
                break;
            case R.id.btn_edit_profile:
                if(ifValidRegister()){
                    //edit Profile
                    sendRequest();
                }
                else {
                    Toast.makeText(EditProfile.this,"Please enter a valid input",Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
    }

    private boolean ifValidRegister() {
        if(!validateEmail())
            return false;
        if(!validateContact())
            return false;
        if(!validatePin())
            return false;

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail() {
        String inputEmail = email.getText().toString().trim();

        if (inputEmail.isEmpty() || !isValidEmail(inputEmail)) {
            emailWrapper.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            emailWrapper.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePin() {
        String inputPin= pin.getText().toString().trim();
        if(!isValidPin(inputPin)){
            pinWrapper.setError(getString(R.string.err_msg_pin));
            requestFocus(pin);
            return false;
        }
        else {
            pinWrapper.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidPin(String pin) {
        pattern=Pattern.compile(PIN_PATTERN);
        matcher=pattern.matcher(pin);
        return matcher.matches();
    }

    private boolean validateContact() {
        String contact=mob.getText().toString().trim();
        if(!isValidContact(contact)){
            mobWrapper.setError(getString(R.string.err_msg_contact));
            requestFocus(mob);
            return false;
        }
        else {
            mobWrapper.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidContact(String contact) {
        pattern=Pattern.compile(CONTACT_PATTERN);
        matcher=pattern.matcher(contact);
        return matcher.matches();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void sendRequest() {

        final String inputEmail,inputMob,inputAdd1,inputAdd2,inputCity,inputState,inputPin;

        inputEmail=email.getText().toString().trim();
        inputMob=mob.getText().toString().trim();
        inputAdd1=add1.getText().toString().trim();
        inputAdd2=add2.getText().toString().trim();
        inputCity=city.getText().toString().trim();
        inputState=state.getText().toString().trim();
        inputPin=pin.getText().toString().trim();

        pd.show();
        StringRequest req=new StringRequest(Request.Method.POST, EDIT_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("status").equals("ok")){
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("email",inputEmail);
                                editor.putString("contact",inputMob);
                                editor.putString("add1",inputAdd1);
                                editor.putString("add2",inputAdd2);
                                editor.putString("city",inputCity);
                                editor.putString("state",inputState);
                                editor.putString("pin",inputPin);
                                editor.commit();
                                pd.dismiss();
                                Toast.makeText(EditProfile.this,"Profile updated!",Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                            else {
                                pd.dismiss();
                                Toast.makeText(EditProfile.this,"Some error occurred. Please try again after sometime",
                                        Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }

                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(EditProfile.this,getString(R.string.err_connection),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("uid",sharedPreferences.getString("uid","-1"));
                params.put("email",inputEmail);
                params.put("mob",inputMob);
                params.put("add1",inputAdd1);
                params.put("add2",inputAdd2);
                params.put("city",inputCity);
                params.put("state",inputState);
                params.put("pin",inputPin);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(req);
    }
}
