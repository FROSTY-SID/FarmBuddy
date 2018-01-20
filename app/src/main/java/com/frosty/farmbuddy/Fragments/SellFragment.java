package com.frosty.farmbuddy.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.frosty.farmbuddy.Adapters.ProductImageAdapter;
import com.frosty.farmbuddy.Objects.Product;
import com.frosty.farmbuddy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reginald.editspinner.EditSpinner;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public SellFragment() {
        // Required empty public constructor
    }

    public static SellFragment newInstance() {
        SellFragment fragment = new SellFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private EditSpinner mEditTextCategory;
    private EditSpinner mEditTextCommodity;
    private EditSpinner mEditTextVariety;
    private EditText mEditTextUnit;
    private EditText mEditTextDescription;
    private EditText mEditTextRate;
    private LinearLayout mLinearLayoutUploadPic;
    private Button mButtonSubmit;
    private View view;
    private DatabaseReference mDatabase;
    private String TAG ="SELL_FRAG";
    private DataSnapshot dataSnapshotCat;
    private RecyclerView mRecyclerViewProductPics;
    private ProductImageAdapter mProductImageAdapter;
    private int PICK_IMAGE_REQUEST = 2;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ArrayList<String> productPicUrls;
    private RoundCornerProgressBar mProgressBarProductPicUpload;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sell, container, false);
        mEditTextCategory = view.findViewById(R.id.et_sell_category);
        mEditTextCommodity = view.findViewById(R.id.et_sell_commodity);
        mEditTextVariety = view.findViewById(R.id.et_sell_variety);
        mEditTextUnit = view.findViewById(R.id.et_sell_unit);
        mEditTextDescription = view.findViewById(R.id.et_sell_description);
        mEditTextRate = view.findViewById(R.id.et_sell_rate);
        mButtonSubmit = view.findViewById(R.id.bt_sell_submit);
        mLinearLayoutUploadPic = view.findViewById(R.id.sell_upload_pic);
        mRecyclerViewProductPics = view.findViewById(R.id.recyclerView_product_pics);

        mProgressBarProductPicUpload = view.findViewById(R.id.roundCornerProgressBar);
        mProgressBarProductPicUpload.setBackgroundColor(getResources().getColor(R.color.backgroundLight_material_light));
        mProgressBarProductPicUpload.setProgressColor(getResources().getColor(R.color.accent_material_light));
        mProgressBarProductPicUpload.setMax(100);
        mProgressBarProductPicUpload.setVisibility(View.INVISIBLE);

        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        productPicUrls = new ArrayList<String>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        mRecyclerViewProductPics.setLayoutManager(mLayoutManager);
        mProductImageAdapter = new ProductImageAdapter(5);
        mRecyclerViewProductPics.setAdapter(mProductImageAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product p = new Product();
                p.name = mEditTextCommodity.getText().toString();
                p.description=mEditTextDescription.getText().toString();
                p.rate = mEditTextRate.getText().toString();
                p.unit = mEditTextUnit.getText().toString();
                p.varity = mEditTextVariety.getText().toString();
                if(!productPicUrls.isEmpty()){
                    p.productPicUrls = productPicUrls;
                }

                String pID = mDatabase.child("products").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().getKey();
                mDatabase.child("products").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(pID).setValue(p);
            }
        });



        mDatabase.child("cat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    Log.d(TAG,dataSnapshot.getValue().toString());
                    ArrayList<String> cat = new ArrayList<String>();
                    dataSnapshotCat=dataSnapshot;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.d(TAG,child.toString());
                        cat.add(child.getKey());
                    }

                    ArrayAdapter<String>  arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,cat);
                    mEditTextCategory.setAdapter(arrayAdapter);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mEditTextCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d(TAG,"beforeTextChanged :" +s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG,"onTextChanged :" +s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    Log.d(TAG,"afterTextChanged :" +dataSnapshotCat.child(s.toString()).toString());
                    if(!s.equals("")) {
                        ArrayList<String> commodity = new ArrayList<String>();
                        for (DataSnapshot child : dataSnapshotCat.child(s.toString()).getChildren()) {
                            Log.d(TAG, child.toString());
                            commodity.add(child.getKey());
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, commodity);
                        mEditTextCommodity.setAdapter(arrayAdapter);
                    }
                }catch (Exception e){
                    Log.d(TAG,"afterTextChanged :" +e.toString());
                }

            }
        });

        mEditTextCommodity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Log.d(TAG,"afterTextChanged :" +dataSnapshotCat
                            .child(mEditTextCategory.getText().toString())
                            .child(mEditTextCommodity.getText().toString()).toString());
                    if(!s.equals("")) {
                        ArrayList<String> variety = new ArrayList<String>();
                        for (DataSnapshot child : dataSnapshotCat
                                .child(mEditTextCategory.getText().toString())
                                .child(mEditTextCommodity.getText().toString()).child("variety").getChildren()) {
                            Log.d(TAG, child.toString());
                            variety.add((String) child.getValue());
                            //commodity.add(child.getKey());
                        }

                       ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,variety);
                        mEditTextVariety.setAdapter(arrayAdapter);
                    }
                }catch (Exception e){
                    Log.d(TAG,"afterTextChanged :" +e.toString());
                }
            }
        });

        mLinearLayoutUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
            }
        });
        //TODO : add auto complete Spinner
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Log.d(TAG,"FILE URI :"+uri.toString());

            //Uri file = Uri.fromFile(new File("path/to/mountains.jpg"));

            // Create the file metadata
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("uid",mUser.getUid())
                    .setContentType("image/jpeg")
                    .build();

            // Upload file and metadata to the path 'images/mountains.jpg'
            StorageReference storageRef  = storage.getReference();

            UploadTask uploadTask = storageRef.child("product_pics/sell_product_pics/"+uri.getLastPathSegment()).putFile(uri, metadata);
            mProgressBarProductPicUpload.setVisibility(View.VISIBLE);
            // Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    mProgressBarProductPicUpload.setProgress((float)progress);
                    Log.d(TAG,"Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    mLinearLayoutUploadPic.setVisibility(View.INVISIBLE);
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    mProgressBarProductPicUpload.setVisibility(View.INVISIBLE);
                    productPicUrls.add(taskSnapshot.getMetadata().getDownloadUrl().toString());
                    mProductImageAdapter = new ProductImageAdapter(productPicUrls,getContext());
                    mRecyclerViewProductPics.setAdapter(mProductImageAdapter);


                    /*// Handle successful uploads on complete
                    Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                    setProfileImage(downloadUrl.toString());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUrl)
                            .build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });*/

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
