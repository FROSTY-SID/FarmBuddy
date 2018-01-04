package com.frosty.farmbuddy.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.FarmBuddyValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mTextViewEmail;
    private TextView mTextViewName;
    private TextView mTextViewAddress;
    private TextView mTextViewPhone;
    private TextView mTextViewUpdateInfo;
    private static String NO_TEXT = ".....";
    private FirebaseAuth auth;
    private FirebaseUser user;
    private View view ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
          view  = inflater.inflate(R.layout.fragment_account,container);

        // Inflate the layout for this fragment
        mTextViewEmail = (TextView) view.findViewById(R.id.tv_account_email) ;
        mTextViewName = (TextView)view.findViewById(R.id.tv_account_name);
        mTextViewAddress = (TextView)view.findViewById(R.id.tv_account_address);
        mTextViewPhone = (TextView)view.findViewById(R.id.tv_account_phone);
        mTextViewUpdateInfo = (TextView)view.findViewById(R.id.tv_update_accinfo);

        if(mTextViewEmail!=null) {
            Log.d(FarmBuddyValues.LOG_ACCOUNT_FRAGMENT_TAG,"GOT TEXT VIEW" );
        }else{
            Log.d(FarmBuddyValues.LOG_ACCOUNT_FRAGMENT_TAG,"DIDN'T GET TEXT VIEW" );
        }

        //TODO: Get Address form database or Firestore using query (user.uid as parameter)
        mTextViewEmail.setText(user!=null?user.getEmail():"...");
        mTextViewName.setText(user!=null?user.getDisplayName():"...");
        mTextViewPhone.setText(user!=null?user.getPhoneNumber():"...");

       /* mTextViewUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                container.removeAllViews();
                ft.remove(getFragmentManager().findFragmentById(R.id.empty_activity_layout_for_fragment))
                        .addToBackStack(null)
                        .replace(R.id.empty_activity_layout_for_fragment, new UpdateAccountFragment())
                        .commit();

            }
        });*/

       //Temporary
        mTextViewUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });

        return inflater.inflate(R.layout.fragment_account, container, false);
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
