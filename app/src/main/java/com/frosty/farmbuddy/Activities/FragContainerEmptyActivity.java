package com.frosty.farmbuddy.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.frosty.farmbuddy.Fragments.AccountFragment;
import com.frosty.farmbuddy.Fragments.UpdateAccountFragment;
import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.FarmBuddyValues;

public class FragContainerEmptyActivity extends AppCompatActivity
        implements
        AccountFragment.OnFragmentInteractionListener,UpdateAccountFragment.OnFragmentInteractionListener{

    // Selected  Randomly  no logic
    private static final int UPDATE_ACCOUNT_INFO = 8647;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_container_empty);

        /*
        * The Fragment ID is sent from MainActivity  Using Put Extra
        * It is retrieved using getExtra and passed to the loadTheFragment
        * Which loads the Fragment inside the EmptyActivity
        * */

        Intent intentForThisActivity = getIntent();
        int fragment_id = intentForThisActivity.getIntExtra(FarmBuddyValues.FRAGMENT_TO_START,0);
        loadTheFragment(fragment_id);

        //TODO: Remove and replace it with proper UI element


    }

    @Override
    public void onFragmentInteraction(Uri uri) {



    }

    @Override
    protected void onStart() {
        super.onStart();

      /*  Fragment f = getFragmentManager().findFragmentByTag(FarmBuddyValues.FRAGMENT_ACCOUNT_TAG);

        if(f!=null ){
            TextView mTextViewUpdateInfo = f.getView().findViewById(R.id.tv_update_accinfo);
            mTextViewUpdateInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loadTheFragment(UPDATE_ACCOUNT_INFO);

                }
            });
        }*/

    }

    private void loadTheFragment(int id){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_my_account) {

            ft.replace(R.id.empty_activity_layout_for_fragment, new AccountFragment(),FarmBuddyValues.FRAGMENT_ACCOUNT_TAG);
            //ft.addToBackStack(null);

        } else if (id == R.id.nav_sell) {

        } else if (id == R.id.nav_category) {

        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if(id==UPDATE_ACCOUNT_INFO){

            ft.replace(R.id.empty_activity_layout_for_fragment, new UpdateAccountFragment());
        }

        ft.commit();

    }
}
