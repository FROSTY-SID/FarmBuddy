package com.frosty.farmbuddy.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.FirebaseDatabaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    private TableLayout mTableLayoutCat;
    private DatabaseReference mDatabase;
    private String TAG = "HOMEFRAG";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_home, container, false);
        mTableLayoutCat = view.findViewById(R.id.home_catagories_table_layout);
        mDatabase  = FirebaseDatabaseUtil.getFirebaseDatabaseInstance().getReference();
        if(mTableLayoutCat.getChildCount()>1){ mTableLayoutCat.removeViews(1,3);}
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        
        if(mTableLayoutCat.getChildCount()==1){
            mDatabase.child("cat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,dataSnapshot.toString());
                try {
                    //if(mTableLayoutCat.getChildCount()>1){ mTableLayoutCat.removeViews(1,3);}
                    final Iterator<DataSnapshot> catkeys =  dataSnapshot.getChildren().iterator();
                    TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.home_cat_row,null);
                    mTableLayoutCat.addView(row);
                    LinearLayout cat=null;
                    TextView textView = null;

                    //Log.d(TAG," key : "+catkeys.next().getKey());
                    while(catkeys.hasNext()){
                        if(mTableLayoutCat.getChildCount()<=4 ){
                            /*if(mTableLayoutCat.getChildCount() == 4){
                                TableRow tempRow = (TableRow) mTableLayoutCat.getChildAt(3);
                                if(tempRow.getChildCount()==3){
                                    lastItemSaver=false;
                                }
                            }*/
                            if(mTableLayoutCat.getChildCount()==4 && row.getChildCount()==3){
                                row = (TableRow) getLayoutInflater().inflate(R.layout.home_cat_row,null);
                                mTableLayoutCat.addView(row);
                            }
                            else{
                                if(row.getChildCount()==3){
                                    row = (TableRow) getLayoutInflater().inflate(R.layout.home_cat_row,null);
                                    mTableLayoutCat.addView(row);
                                }

                                cat = (LinearLayout) getLayoutInflater().inflate(R.layout.home_cat_view,null);
                                textView  = cat.findViewById(R.id.home_categories_table_layout_item_text_view);
                                textView.setText(catkeys.next().getKey());
                                row.addView(cat);

                            }
                        }else{
                            row = (TableRow) getLayoutInflater().inflate(R.layout.home_cat_row,null);
                            TextView tv = new TextView(getContext());
                            tv.setText(R.string.more);
                            tv.setGravity(Gravity.CENTER_HORIZONTAL);
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mTableLayoutCat.removeViewAt(mTableLayoutCat.getChildCount()-1);
                                    TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.home_cat_row,null);
                                    mTableLayoutCat.addView(row);
                                    LinearLayout cat=null;
                                    TextView textView = null;
                                    while(catkeys.hasNext()){
                                        if(row.getChildCount()==3){
                                            row = (TableRow) getLayoutInflater().inflate(R.layout.home_cat_row,null);
                                            mTableLayoutCat.addView(row);
                                        }
                                        cat = (LinearLayout) getLayoutInflater().inflate(R.layout.home_cat_view,null);
                                        textView  = cat.findViewById(R.id.home_categories_table_layout_item_text_view);
                                        textView.setText(catkeys.next().getKey());
                                        row.addView(cat);
                                    }
                                }
                            });
                            row.addView(tv);
                            row.setGravity(Gravity.CENTER_HORIZONTAL);
                            mTableLayoutCat.addView(row);
                            break;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
