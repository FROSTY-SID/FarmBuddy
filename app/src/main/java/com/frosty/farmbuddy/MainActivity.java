package com.frosty.farmbuddy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frosty.farmbuddy.Activities.FragContainerEmptyActivity;
import com.frosty.farmbuddy.Activities.LoginActivity;
import com.frosty.farmbuddy.Activities.UserRegistrationActivity;
import com.frosty.farmbuddy.Fragments.AccountFragment;
import com.frosty.farmbuddy.Fragments.HomeFragment;
import com.frosty.farmbuddy.Fragments.InformationFragment;
import com.frosty.farmbuddy.Fragments.MarketPriceFilterFragment;
import com.frosty.farmbuddy.Utility.FarmBuddyValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        AccountFragment.OnFragmentInteractionListener ,
        InformationFragment.OnFragmentInteractionListener,
        MarketPriceFilterFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener
       {
    private LinearLayout mNavHeaderUserInfo;
    private LinearLayout  mNavHeaderNoUser;
    private FirebaseUser user;
    private MainActivity ActivityObjectContext ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FrameLayout mFrameLayoutNavHeaderLayout  = (FrameLayout) navigationView.getHeaderView(0);
        mNavHeaderNoUser  = mFrameLayoutNavHeaderLayout.findViewById(R.id.nav_header_no_user_signed_in);
        mNavHeaderUserInfo = mFrameLayoutNavHeaderLayout.findViewById(R.id.nav_header_sign_in_info);
        TextView mTextViewNavHeaderDisplayName = mNavHeaderUserInfo.findViewById(R.id.nav_header_user_display_name);
        TextView mTextViewNavHeaderMobile = mNavHeaderUserInfo.findViewById(R.id.nav_header_user_mobile);

        Button mButtonNavHeaderSignIn = mNavHeaderNoUser.findViewById(R.id.nav_header_sign_in);
        Button mButtonNavHeaderRegister = mNavHeaderNoUser.findViewById(R.id.nav_header_register);
        ActivityObjectContext = this;
        mButtonNavHeaderSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityObjectContext, LoginActivity.class));
            }
        });

        mButtonNavHeaderRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityObjectContext, UserRegistrationActivity.class));
            }
        });
        if(user!=null){
                       mNavHeaderUserInfo.setVisibility(View.VISIBLE);
                       mTextViewNavHeaderDisplayName.setText(user.getDisplayName());
                       mTextViewNavHeaderMobile.setText(user.getPhoneNumber());
        }else{
            mNavHeaderUserInfo.setVisibility(View.GONE);
            mNavHeaderNoUser.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            ft.replace(R.id.scrollView_main_activity, new HomeFragment(),FarmBuddyValues.FRAGMENT_INFROMATION_TAG);
            ft.commit();

        } else if (id == R.id.nav_my_account && user!=null) {
            startActivity(new Intent(this,
                    FragContainerEmptyActivity.class)
                    .putExtra(FarmBuddyValues.FRAGMENT_TO_START,R.id.nav_my_account));

        } else if (id == R.id.nav_sell) {
            Toast.makeText(this," " +isNetworkConnected(),Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_category) {

        } else if (id == R.id.nav_info) {

            ft.replace(R.id.fl_main_activity_empty, new InformationFragment(),FarmBuddyValues.FRAGMENT_INFROMATION_TAG);
            ft.commit();

        }else if (id == R.id.nav_sign_out) {
            FirebaseAuth.getInstance().signOut();
            this.recreate();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private  boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.d("INTERNET",""+isConnected+" " + activeNetwork.isConnected()+" "+activeNetwork.getState());
        return isConnected;
    }
}
