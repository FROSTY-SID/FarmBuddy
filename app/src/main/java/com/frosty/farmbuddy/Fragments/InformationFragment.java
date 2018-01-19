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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.FarmBuddyValues;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment implements AdapterView.OnItemSelectedListener,MarketPriceFilterFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String  TAG ="InfoFrag";

    private OnFragmentInteractionListener mListener;

    public InformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationFragment newInstance(String param1, String param2) {
        InformationFragment fragment = new InformationFragment();
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

    private Spinner mSpinnerInfoSystem;
    private FrameLayout mFrameLayoutFilterContainer;
    private LayoutInflater inflater;
    private View view;
    private WebView mWebView_Info;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view =  inflater.inflate(R.layout.fragment_information, container, false);
       this.inflater =inflater;
       mFrameLayoutFilterContainer  = view.findViewById(R.id.fl_info_system_filter_container);
       mSpinnerInfoSystem = view.findViewById(R.id.sp_info_system) ;
       mWebView_Info = view.findViewById(R.id.WebView_information);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.informationApis,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerInfoSystem.setAdapter(adapter);
        mSpinnerInfoSystem.setOnItemSelectedListener(this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG,"parent : "+parent.toString()+" \n view : "+ view.toString()+"\n position : "+ position+"\n id : "+id);
        switch (position){
            case 0:
                mFrameLayoutFilterContainer.removeAllViewsInLayout();

                break;
            case 1:
                mFrameLayoutFilterContainer.removeAllViewsInLayout();
                mWebView_Info.getSettings().setJavaScriptEnabled(true);
                mWebView_Info.loadUrl("http://agmarknet.gov.in/");
                //mWebView_Info.setOverScrollMode();
                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fl_info_system_filter_container, new MarketPriceFilterFragment(), FarmBuddyValues.FRAGMENT_MARKET_PRICE_FILTER_TAG);
                ft.commit();*/
                break;
        }
        if(position==1){

        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
