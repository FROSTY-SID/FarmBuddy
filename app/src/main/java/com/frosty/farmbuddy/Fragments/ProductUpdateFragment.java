package com.frosty.farmbuddy.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.frosty.farmbuddy.Objects.Product;
import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.FarmBuddyValues;
import com.frosty.farmbuddy.Utility.FirebaseDatabaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reginald.editspinner.EditSpinner;


public class ProductUpdateFragment extends Fragment {



    private OnFragmentInteractionListener mListener;
    private View view;
    private static ProductUpdateFragment productUpdateFragment;
    private String productId;
    public ProductUpdateFragment() {
        // Required empty public constructor
    }


    public static ProductUpdateFragment newInstance() {
        ProductUpdateFragment fragment = new ProductUpdateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductUpdateFragment getInstance(String productId){
        if(productUpdateFragment==null){
            productUpdateFragment = new ProductUpdateFragment();
        }
        productUpdateFragment.productId=productId;
        return productUpdateFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private TextView mTextViewName;
    private TextView mTextViewCat;
    private TextView mTextViewVariety;
    private EditSpinner mEditSpinnerUnit;
    private EditText mEditTextDescription;
    private EditText mEditTextRate;
    private Button mButtonSubmit;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_update, container, false);

        mTextViewName = view.findViewById(R.id.tv_product_update_name);
        mTextViewCat= view.findViewById(R.id.tv_product_update_cat);
        mTextViewVariety= view.findViewById(R.id.tv_product_update_varity);
        mEditSpinnerUnit = view.findViewById(R.id.et_product_update_unit);
        mEditTextDescription = view.findViewById(R.id.et_product_update_description);
        mEditTextRate = view.findViewById(R.id.et_product_update_rate);
        mButtonSubmit = view.findViewById(R.id.bt_product_update_submit);
        mDatabase = FirebaseDatabaseUtil.getFirebaseDatabaseInstance().getReference();

        if(productId!=null) {
            Log.d("UpdateFrag",productId);
            mDatabase.child("products").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(productId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("UpdateFrag",dataSnapshot.toString());
                    final Product product = dataSnapshot.getValue(Product.class);
                    mTextViewCat.setText(product.cat);
                    mEditSpinnerUnit.setText(product.unit);
                    mTextViewName.setText(product.name);
                    mTextViewVariety.setText(product.variety);
                    mEditTextDescription.setText(product.description);
                    mEditTextRate.setText(product.rate);

                    mButtonSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            product.rate = mEditTextRate.getText().toString();
                            product.unit = mEditSpinnerUnit.getText().toString();
                            product.description = mEditTextDescription.getText().toString();
                            mDatabase.child("products").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(productId).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    getFragmentManager().popBackStack(FarmBuddyValues.FRAGMENT_PRODUCT_UPDATE_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                }
                            });


                        }

                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
