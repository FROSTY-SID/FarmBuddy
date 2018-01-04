package com.frosty.farmbuddy.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frosty.farmbuddy.MainActivity;
import com.frosty.farmbuddy.Objects.Location_fb;
import com.frosty.farmbuddy.Objects.User;
import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.Converter;
import com.frosty.farmbuddy.Utility.FarmBuddyValues;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.frosty.farmbuddy.Activities.UserRegistrationActivity;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText mMobileNumber;
    private EditText mVerificationCode;
    private Button mLogin;
    private Button mVerify;
    private TextView mUserRegistration;
    private TextView mResendOtp;
    private LinearLayout mLinearLayLogin;
    private LinearLayout mLinearLayLoginVerify;
    private ProgressBar mProgressBar;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String PHONE_VER_FLAG_TAG = "PHONE_VER_FLAG";
    private boolean FLAG_PHONEVERFIY = false;
    private String LOG_TAG = "LOGINACTIVITY";
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Intent intentForMainActivity ;
    private final int ACTIVITY_CONSTANT = 1;
    private Intent dataFromReg;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intentForMainActivity = new Intent(this, MainActivity.class);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(intentForMainActivity);
            finish();
        }

        mLogin = (Button) findViewById(R.id.bt_Login);
        mVerify = (Button) findViewById(R.id.bt_verify);
        mMobileNumber = (EditText) findViewById(R.id.et_phone);
        mVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        mUserRegistration =(TextView) findViewById(R.id.tv_user_reg);
        mUserRegistration.setText(Html.fromHtml(getString(R.string.text_user_reg)+" "+"<font color='#18ecb7'><b>"+getString(R.string.text_register_here)+"</b></font>"));
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mLinearLayLogin = (LinearLayout) findViewById(R.id.lineraLay_login_input);
        mLinearLayLoginVerify = (LinearLayout) findViewById(R.id.LinearLay_LoginVerify);
        mResendOtp = (TextView) findViewById(R.id.tv_resend_OTP);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if(FLAG_PHONEVERFIY){
            mLinearLayLoginVerify.setVisibility(View.VISIBLE);
            mLinearLayLogin.setVisibility(View.GONE);
        }else{
            mLinearLayLogin.setVisibility(View.VISIBLE);
            mLinearLayLoginVerify.setVisibility(View.GONE);
        }

        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //Check for valid mobile number
                if(mMobileNumber.getText().toString().length()!=10){

                    Toast.makeText(getApplicationContext(),
                            "Incorrect Mobile Number!!",
                            Toast.LENGTH_LONG).show();

                }
                //Verify mobile Number
                else{
                    mDatabase.child("users").orderByChild("phone").equalTo(mMobileNumber.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()==null){
                                Log.d("TEST","onDataChange=>"+dataSnapshot.toString());
                                startActivityForResult(new Intent(getApplicationContext(),UserRegistrationActivity.class),ACTIVITY_CONSTANT);
                            }else{
                                verifyPhoneNumber(mMobileNumber.getText().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mVerificationCode.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });

        mResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mLinearLayLogin.setVisibility(View.GONE);
                mLinearLayLoginVerify.setVisibility(View.GONE);
                verifyPhoneNumber(mMobileNumber.getText().toString());

            }
        });

        mUserRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),UserRegistrationActivity.class),ACTIVITY_CONSTANT);
            }
        });

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
       if(credential!=null){
           mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FLAG_PHONEVERFIY = false;
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            //Result from UserRegistration Activity is not NULL i.e New User has registered
                            if(dataFromReg!=null){
                                if(dataFromReg.hasExtra(UserRegistrationActivity.FIRST_NAME_KEY)&&
                                        dataFromReg.hasExtra(UserRegistrationActivity.LAST_NAME_KEY)){

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(dataFromReg.getStringExtra(UserRegistrationActivity.FIRST_NAME_KEY)+
                                                    " "+
                                                    dataFromReg.getStringExtra(UserRegistrationActivity.LAST_NAME_KEY))
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(LOG_TAG, "User profile updated.");
                                                    }
                                                }
                                            });
                                    if(!dataFromReg.getStringExtra(UserRegistrationActivity.EMAIL_KEY).equals(" ")){
                                        user.updateEmail(dataFromReg.getStringExtra(UserRegistrationActivity.EMAIL_KEY))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(LOG_TAG, "User email address updated.");
                                                        }
                                                    }
                                                });
                                    }
                                }
                                User fb_user = new User(
                                        dataFromReg.getStringExtra(UserRegistrationActivity.FIRST_NAME_KEY),
                                        dataFromReg.getStringExtra(UserRegistrationActivity.LAST_NAME_KEY),
                                        dataFromReg.getStringExtra(UserRegistrationActivity.EMAIL_KEY),
                                                new Location_fb(
                                                        dataFromReg.getStringExtra(UserRegistrationActivity.STATE_KEY),
                                                        dataFromReg.getStringExtra(UserRegistrationActivity.DISTRICT_KEY),
                                                        dataFromReg.getStringExtra(UserRegistrationActivity.TALUKA_KEY),
                                                        dataFromReg.getStringExtra(UserRegistrationActivity.PINCODE_KEY)),
                                        dataFromReg.getStringExtra(UserRegistrationActivity.MOBILE_KEY));

                                mDatabase.child("users").child(user.getUid()).setValue(fb_user);
                                //TODO:Add Same to FireStore if required

                            }



                            Log.d(LOG_TAG,user.getPhoneNumber().toString());
                            //TODO: Open The MainActivity and Send User object to That Activity
                            startActivity(intentForMainActivity);
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(),"Sign In Faild",Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                            mLinearLayLogin.setVisibility(View.VISIBLE);
                            mLinearLayLoginVerify.setVisibility(View.GONE);
                            Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(),"verification code entered was invalid",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        }
    }


    private void verifyPhoneNumber(String phoneNumber){

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(LOG_TAG, "onVerificationCompleted:" + credential);


                mVerificationCode.setText(credential.getSmsCode());

                signInWithPhoneAuthCredential(credential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(LOG_TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(getApplicationContext(),R.string.text_invalid_Req,Toast.LENGTH_LONG);
                    mProgressBar.setVisibility(View.GONE);
                    mLinearLayLogin.setVisibility(View.VISIBLE);
                    mLinearLayLoginVerify.setVisibility(View.GONE);
                    //TODO:Toast for Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    //TODO:Create a Dialog to Notify that service is unavailable

                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setNegativeButton(R.string.text_close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setMessage(R.string.dialog_message_service_unavailable)
                            .setTitle(R.string.dialog_title_service_unavailable);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(LOG_TAG, "onCodeSent:" + verificationId);
                FLAG_PHONEVERFIY =true;
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                SharedPreferences.Editor editor = getSharedPreferences(FarmBuddyValues.SHARED_PRE_NAME, MODE_PRIVATE).edit();
                editor.putString(FarmBuddyValues.VERIFICATION_ID_KEY,mVerificationId);
                editor.apply();
                Converter.saveObjectToSharedPreference(getBaseContext(),FarmBuddyValues.SHARED_PRE_NAME,FarmBuddyValues.RESEND_TOKEN_KEY,mResendToken);
                mProgressBar.setVisibility(View.GONE);
                mLinearLayLogin.setVisibility(View.GONE);
                mLinearLayLoginVerify.setVisibility(View.VISIBLE);
                // ...
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(getBaseContext(),R.string.text_Enter_Manually,Toast.LENGTH_LONG);


            }
        };
        mProgressBar.setVisibility(View.VISIBLE);
        mLinearLayLogin.setVisibility(View.GONE);
        mLinearLayLoginVerify.setVisibility(View.GONE);


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks );        // OnVerificationStateChangedCallbacks


    }

    public void setDataFromReg(Intent dataFromReg) {
        this.dataFromReg = dataFromReg;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ACTIVITY_CONSTANT) {
            if (data.hasExtra(UserRegistrationActivity.MOBILE_KEY)&& data.hasExtra(UserRegistrationActivity.FIRST_NAME_KEY)) {
                setDataFromReg(data);
                verifyPhoneNumber(data.getStringExtra(UserRegistrationActivity.MOBILE_KEY));

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: check if verification is already in progress, and if so, call verifyPhoneNumber again. Be sure to clear the flag when verification completes or fails
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(PHONE_VER_FLAG_TAG,FLAG_PHONEVERFIY);
        outState.putString(FarmBuddyValues.VERIFICATION_ID_KEY,mVerificationId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

         FLAG_PHONEVERFIY = (boolean) savedInstanceState.get(PHONE_VER_FLAG_TAG);
         mVerificationId = (String) savedInstanceState.get(FarmBuddyValues.VERIFICATION_ID_KEY);
    }
}
