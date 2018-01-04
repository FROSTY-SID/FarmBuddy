package com.frosty.farmbuddy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.VolleyNetwork;
import com.frosty.farmbuddy.WebApis.PincodeApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class UserRegistrationActivity extends AppCompatActivity {
    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextEmail;
    private EditText mEditTextMobileNo;
    private EditText mEditTextPincode;
    private ProgressBar mProgressBarPincode;
    private Spinner  mSpinnerState;
    private Spinner  mSpinnerDistrict;
    private Spinner  mSpinnerTaluka;
    private TextView mTextViewRegister;
    private final String LOG_TAG = "USER_REG";
    private Intent data;
    private String state="";
    private String district="";
    private String taluka="";
    private int textColor;
    private int textHintColor;
    private static final Pattern rfc2822 = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    );
    private boolean allInputsAreOkay;


    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    public static final String PINCODE_KEY = "pincode";
    public static final String MOBILE_KEY = "mobile";
    public static final String STATE_KEY = "State";
    public static final String DISTRICT_KEY = "district";
    public static final String TALUKA_KEY = "taluka";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mEditTextFirstName = findViewById(R.id.et_reg_first_name);
        mEditTextLastName= findViewById(R.id.et_reg_last_name);
        mEditTextEmail= findViewById(R.id.et_rg_email);
        mEditTextMobileNo= findViewById(R.id.et_reg_phone);
        mEditTextPincode= findViewById(R.id.et_reg_pincode);
        mSpinnerState= findViewById(R.id.sp_reg_state);
        mSpinnerDistrict= findViewById(R.id.sp_reg_district);
        mSpinnerTaluka= findViewById(R.id.sp_reg_taluka);
        mTextViewRegister= findViewById(R.id.tv_reg_register);
        mTextViewRegister.setClickable(false);
        mProgressBarPincode = findViewById(R.id.pb_pincode);
        mProgressBarPincode.setVisibility(View.GONE);
        mTextViewRegister.setBackgroundColor(getResources().getColor(R.color.colorSecondaryGray));
        data = new Intent();
        textColor = mEditTextFirstName.getCurrentTextColor();
        textHintColor = mEditTextFirstName.getCurrentHintTextColor();

        mEditTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!rfc2822.matcher(s.toString()).matches() && s.length()>0 ){
                    Log.d(LOG_TAG,"LENGTH = "+Integer.toString(s.length()) );
                    mEditTextEmail.setHintTextColor(getResources().getColor(R.color.colorError));
                    mEditTextEmail.setTextColor(getResources().getColor(R.color.colorError));
                    allInputsAreOkay= allInputsAreOkay && false;
                }else{
                    mEditTextEmail.setHintTextColor(textHintColor);
                    mEditTextEmail.setTextColor(textColor);
                    allInputsAreOkay= allInputsAreOkay || true;
                }

            }
        });

        mEditTextMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()<10 && s.length()>0 ){
                    Log.d(LOG_TAG,"LENGTH = "+Integer.toString(s.length()) );
                    mEditTextMobileNo.setHintTextColor(getResources().getColor(R.color.colorError));
                    mEditTextMobileNo.setTextColor(getResources().getColor(R.color.colorError));
                    allInputsAreOkay= allInputsAreOkay && false;
                }else{
                    mEditTextMobileNo.setHintTextColor(textHintColor);
                    mEditTextMobileNo.setTextColor(textColor);
                    allInputsAreOkay= allInputsAreOkay || true;
                }
            }
        });

        mEditTextPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<6){
                    mTextViewRegister.setClickable(false);
                    mTextViewRegister.setBackgroundColor(getResources().getColor(R.color.colorSecondaryGray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mEditTextPincode.getText().toString().length()==6){
                    mProgressBarPincode.setVisibility(View.VISIBLE);
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET,
                                    new PincodeApi().generateUrl(mEditTextPincode.getText().toString()),
                                    null,
                                    new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {

                                            Log.d(LOG_TAG,"Response: " + response.toString());
                                            JSONObject location = null;
                                            try {
                                                location = response.getJSONArray("records").getJSONObject(0);
                                                state = location.getString("statename");
                                                district= location.getString("districtname");
                                                taluka = location.getString("taluk");
                                                mProgressBarPincode.setVisibility(View.GONE);
                                                mTextViewRegister.setClickable(true);
                                                mTextViewRegister.setBackgroundColor(getResources().getColor(R.color.colorSecondary));

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // TODO Auto-generated method stub

                                        }
                                    });
                    VolleyNetwork.getInstance(getApplicationContext())
                            .addToRequestQueue(jsObjRequest);
                }else{
                    mProgressBarPincode.setVisibility(View.GONE);
                }

            }
        });

        //TODO:Check if All fields are filled
        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditTextFirstName.getText().toString().trim().length()<2 || mEditTextFirstName.getText().toString().trim()==""){

                    mEditTextFirstName.setHintTextColor(getResources().getColor(R.color.colorError));
                    allInputsAreOkay= allInputsAreOkay && false;
                }else{
                    mEditTextFirstName.setHintTextColor(textColor);
                    allInputsAreOkay= allInputsAreOkay || true;
                }

                if(mEditTextLastName.getText().toString().trim().length()<2 || mEditTextFirstName.getText().toString().trim()==""){

                    mEditTextLastName.setHintTextColor(getResources().getColor(R.color.colorError));
                    allInputsAreOkay= allInputsAreOkay && false;
                }else{
                    mEditTextLastName.setHintTextColor(textColor);
                    allInputsAreOkay= allInputsAreOkay || true;
                }

        if(allInputsAreOkay){

                    data.putExtra(FIRST_NAME_KEY,mEditTextFirstName.getText().toString());
                    data.putExtra(LAST_NAME_KEY,mEditTextLastName.getText().toString());
                    data.putExtra(PINCODE_KEY,mEditTextPincode.getText().toString());
                    data.putExtra(EMAIL_KEY,!mEditTextEmail.getText().toString().isEmpty()?mEditTextEmail.getText().toString():" ");
                    data.putExtra(MOBILE_KEY,mEditTextMobileNo.getText().toString());
                    data.putExtra(STATE_KEY,state);
                    data.putExtra(DISTRICT_KEY,district);
                    data.putExtra(TALUKA_KEY,taluka);
                    setResult(RESULT_OK,data);
                    finish();
                }else{
            Toast.makeText(getApplicationContext(),"problem with some inputs",Toast.LENGTH_SHORT).show();
        }
            }
        });


    }
}
