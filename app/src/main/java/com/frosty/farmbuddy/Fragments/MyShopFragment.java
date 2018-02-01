package com.frosty.farmbuddy.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frosty.farmbuddy.Adapters.FireBaseMyShopViewHolder;
import com.frosty.farmbuddy.Objects.Product;
import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.FirebaseDatabaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyShopFragment extends Fragment {



    private OnFragmentInteractionListener mListener;
    private static MyShopFragment myShopFragment;

    public MyShopFragment() {
    }

    public static MyShopFragment getInstant(){
        if(myShopFragment==null){
            myShopFragment = new MyShopFragment();
        }

        return myShopFragment;
    }

    // TODO: Rename and change types and number of parameters
    public static MyShopFragment newInstance(String param1, String param2) {
        MyShopFragment fragment = new MyShopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private View view;
    private RecyclerView mRecyclerViewForProducts;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsersProductsRef;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private static final String TAG = "MYSHOPFRAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_my_shop, container, false);

        mDatabase = FirebaseDatabaseUtil.getFirebaseDatabaseInstance().getReference();
        mUsersProductsRef = mDatabase.child("products").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mRecyclerViewForProducts = view.findViewById(R.id.recyclerView_myshop_products);
        Log.d(TAG,view.toString());
        setUpFirebaseAdapter();
        return view;
    }

    private void setUpFirebaseAdapter() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayout.VERTICAL);

        mRecyclerViewForProducts.setLayoutManager(mLinearLayoutManager);

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(mUsersProductsRef.limitToFirst(50),Product.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Product, FireBaseMyShopViewHolder>(options) {
            @Override
            public FireBaseMyShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_product_recycler_view, parent, false);
                return new FireBaseMyShopViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FireBaseMyShopViewHolder holder, int position, @NonNull Product model) {
                Log.d(TAG,model.id);
                holder.bindProducts(model,getFragmentManager());

            }
        };

       // mRecyclerViewForProducts.setHasFixedSize(true);
        mFirebaseAdapter.notifyDataSetChanged();
        mRecyclerViewForProducts.setAdapter(mFirebaseAdapter);
        Log.d(TAG,"Adapter "+mFirebaseAdapter.toString());
        mFirebaseAdapter.notifyDataSetChanged();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
