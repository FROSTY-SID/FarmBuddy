package com.frosty.farmbuddy.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.frosty.farmbuddy.R;


public class MarketPriceFilterFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;

    public MarketPriceFilterFragment() {
        // Required empty public constructor
    }
    private View view;
    private final static String TAG= "MarPriFil";
    private Spinner mSpPriceArrivals;
    private Spinner mSpCommodity;
    private Spinner mSpState;
    private Spinner mSpDistrict;
    private Spinner mSpMarket;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_market_price_filter, container, false);
        view.findViewById(R.id.spinner_commodity);

        mSpPriceArrivals  = view.findViewById(R.id.spinner_price_arrivals);;
        mSpCommodity = view.findViewById(R.id.spinner_commodity);
        mSpState = view.findViewById(R.id.spinner_state);
        mSpDistrict = view.findViewById(R.id.spinner_district);
        mSpMarket = view.findViewById(R.id.spinner_market);
        mSpPriceArrivals.setAdapter(createSpinnerAdapter(R.array.priceArrival));
        mSpCommodity.setAdapter(createSpinnerAdapter(R.array.commodity));
        mSpState.setAdapter(createSpinnerAdapter(R.array.state));

        //mSpState.onSele

        return view;
    }

    private ArrayAdapter<CharSequence> createSpinnerAdapter(int resId){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                resId,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_state:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
