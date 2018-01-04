package com.frosty.farmbuddy.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.frosty.farmbuddy.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestActivity extends AppCompatActivity {
    private Button button ;
    private TextView resultTv;
    private DatabaseReference mDatabase;
    private EditText et_para;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = findViewById(R.id.button2);
        resultTv= findViewById(R.id.tv_result);
        et_para = findViewById(R.id.et_query_para);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").orderByChild("phone").equalTo(et_para.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        resultTv.setText(dataSnapshot.toString());
                        Log.d("TEST","onDataChange=>"+dataSnapshot.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("TEST","onCancelled=>"+databaseError.toString());
                    }
                });
                /*.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        resultTv.setText(dataSnapshot.toString()+" "+s);
                        Log.d("TEST","onChildAdded=>"+dataSnapshot.toString()+" "+s);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("TEST","onChildChanged=>"+dataSnapshot.toString()+" "+s);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("TEST","onChildRemoved=>"+dataSnapshot.toString());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d("TEST","onChildMoved=>"+dataSnapshot.toString()+" "+s);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("TEST","onCancelled=>"+databaseError.toString());
                    }
                });*/
            }
        });
    }

}
