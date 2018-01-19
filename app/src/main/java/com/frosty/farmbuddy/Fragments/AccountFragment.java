package com.frosty.farmbuddy.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.frosty.farmbuddy.Objects.Location_fb;
import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.FarmBuddyValues;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import static android.app.Activity.RESULT_OK;


public class AccountFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private TextView mTextViewEmail;
    private TextView mTextViewName;
    private TextView mTextViewAddress;
    private CircularImageView mImageViewAccount;
    private TextView mTextViewPhone;
    private TextView mTextViewUpdateInfo;
    private static String NO_TEXT = ".....";
    private FirebaseAuth auth;
    private FirebaseUser user;
    private final static String TAG ="ACC_FRAG";
    private View view ;
    private FirebaseStorage storage;
    private LinearLayout mLinearLayoutProfilePicUpload;
    private int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabase;
    private Location_fb mUserLocation;
    private LinearLayout mLinearLayoutSell;
    private FragmentManager mFragmentManager;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view  = inflater.inflate(R.layout.fragment_account, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFragmentManager = getFragmentManager();

        mTextViewEmail = (TextView) view.findViewById(R.id.tv_account_email) ;
        mTextViewName = (TextView)view.findViewById(R.id.tv_account_name);
        mTextViewAddress = (TextView)view.findViewById(R.id.tv_account_address);
        mTextViewPhone = (TextView)view.findViewById(R.id.tv_account_phone);
        mImageViewAccount =  view.findViewById(R.id.im_account_pic);
        mLinearLayoutProfilePicUpload = view.findViewById(R.id.linear_layout_upload_profile_pic);
        mLinearLayoutSell = view.findViewById(R.id.linearLayout_sell);

        mTextViewEmail.setText(user!=null?user.getEmail():"...");
        mTextViewName.setText(user!=null?user.getDisplayName():"...");
        mTextViewPhone.setText(user!=null?user.getPhoneNumber():"...");

        mDatabase.child("users").child(user.getUid()).child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserLocation = dataSnapshot.getValue(Location_fb.class);
                Log.d(TAG, "Location : " + dataSnapshot.toString());
                if (mUserLocation != null) {
                    mTextViewAddress.setText(mUserLocation.taluka + ", " + mUserLocation.district + ", " + mUserLocation.state + ".");

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(user.getPhotoUrl()!=null) {
            Log.d(TAG,"Photo URL :"+user.getPhotoUrl().toString());
            StorageReference gsReference = storage.getReferenceFromUrl(user.getPhotoUrl().toString());
            Glide.with(this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(gsReference)
                    .into(mImageViewAccount);
        }



        mLinearLayoutProfilePicUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
            }
        });

        mLinearLayoutSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.replace(R.id.empty_activity_layout_for_fragment,new SellFragment(), FarmBuddyValues.FRAGMENT_SELL_TAG);
                ft.commit();
            }
        });

        return view;
    }

    void setProfileImage(String url){

        StorageReference gsReference = storage.getReferenceFromUrl(url);
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .into(mImageViewAccount);

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
                    .setContentType("image/jpeg")
                    .build();

        // Upload file and metadata to the path 'images/mountains.jpg'
            StorageReference storageRef  = storage.getReference();

            UploadTask uploadTask = storageRef.child("profile_pics/"+uri.getLastPathSegment()).putFile(uri, metadata);

        // Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
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
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Handle successful uploads on complete
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
                            });

                }
            });
        }
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
