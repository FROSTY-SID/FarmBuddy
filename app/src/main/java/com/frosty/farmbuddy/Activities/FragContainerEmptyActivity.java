package com.frosty.farmbuddy.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.frosty.farmbuddy.Fragments.AccountFragment;
import com.frosty.farmbuddy.Fragments.MyShopFragment;
import com.frosty.farmbuddy.Fragments.SellFragment;
import com.frosty.farmbuddy.Fragments.UpdateAccountFragment;
import com.frosty.farmbuddy.Fragments.ProductUpdateFragment;
import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.FarmBuddyValues;

public class FragContainerEmptyActivity extends AppCompatActivity
        implements
        AccountFragment.OnFragmentInteractionListener,
        UpdateAccountFragment.OnFragmentInteractionListener ,
        SellFragment.OnFragmentInteractionListener,
        MyShopFragment.OnFragmentInteractionListener,
        ProductUpdateFragment.OnFragmentInteractionListener{

    // Selected  Randomly  no logic
    private static final int UPDATE_ACCOUNT_INFO = 8647;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_container_empty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent intentForThisActivity = getIntent();
        int fragment_id = intentForThisActivity.getIntExtra(FarmBuddyValues.FRAGMENT_TO_START,0);
        loadTheFragment(fragment_id);

        //TODO: Remove and replace it with proper UI element


    }

    @Override
    public void onFragmentInteraction(Uri uri) {



    }


    private void loadTheFragment(int id){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_my_account) {

            ft.replace(R.id.empty_activity_layout_for_fragment,AccountFragment.getInstance(),FarmBuddyValues.FRAGMENT_ACCOUNT_TAG);
            //ft.addToBackStack(null);

        } else if (id == R.id.nav_sell) {

        } else if (id == R.id.nav_category) {

        } else if (id == R.id.nav_info) {

        }else if(id==UPDATE_ACCOUNT_INFO){

            ft.replace(R.id.empty_activity_layout_for_fragment, new UpdateAccountFragment());
        }

        ft.commit();

    }
}
