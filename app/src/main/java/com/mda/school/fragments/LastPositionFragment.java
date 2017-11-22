package com.mda.school.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mda.school.activities.R;
import com.mda.school.model.Car;

public class LastPositionFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView mTvLastPosition;
    private Button mBtnSharePos;
    private Button mBtnNavigate;

    public LastPositionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_last_position, container, false);
        mTvLastPosition = (TextView)v.findViewById(R.id.tv_last_pos);
        mTvLastPosition.setText(getString(R.string.tv_empty_pos));
        mBtnSharePos = (Button)v.findViewById(R.id.btn_share_pos);
        mBtnSharePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Btn share clicked");
            }
        });
        mBtnNavigate = (Button)v.findViewById(R.id.btn_navigate);
        mBtnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    Car c = mListener.getLastKnowCar();
                    double lat = c.getLocation().getLatitude();
                    double lng = c.getLocation().getLongitude();
                    String mTitle = "My car";
                    String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + mTitle + ")";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(geoUri));
                    startActivity(intent);
                }
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public TextView getTvLastPosition() {
        return mTvLastPosition;
    }

    public interface OnFragmentInteractionListener {
        Car getLastKnowCar();
    }
}
