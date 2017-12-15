package com.mda.school.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mda.school.activities.R;

public class CurrentPositionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView mTvCurrentPosition;
    private Button mBtnSavePos;
    private Button mBtnRefreshPos;

    public CurrentPositionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_position, container, false);
        mTvCurrentPosition = (TextView)v.findViewById(R.id.tv_current_pos);
        mTvCurrentPosition.setText(getString(R.string.tv_empty_pos));
        mBtnSavePos = (Button)v.findViewById(R.id.btn_save_pos);
        mBtnSavePos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSaveButtonClicked();
                }
            }
        });
        mBtnRefreshPos = (Button)v.findViewById(R.id.btn_refresh_pos);
        mBtnRefreshPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRefreshLocationClicked();
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

    public TextView getTvCurrentPosition() {
        return mTvCurrentPosition;
    }

    public interface OnFragmentInteractionListener {
        void onSaveButtonClicked();
        void onRefreshLocationClicked();
    }
}
