package com.opensooq.weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class NoInternetConnectionFragment extends Fragment {
private ImageView imageViewSad;
    private Button retryButton;
    private onRefreshButtonListener mListener;

    public NoInternetConnectionFragment() {
        // Required empty public constructor
    }

    public static NoInternetConnectionFragment newInstance() {
        NoInternetConnectionFragment fragment = new NoInternetConnectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_internet_connection, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        imageViewSad = (ImageView) view.findViewById(R.id.sad_img);
        retryButton = (Button) view.findViewById(R.id.retry);
        Picasso.with(getContext()).load(R.drawable.ic_android_sad).centerCrop().fit().into(imageViewSad);
         retryButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 onButtonPressed();
             }
         });
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onRefresh();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onRefreshButtonListener) {
            mListener = (onRefreshButtonListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onRefreshButtonListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onRefreshButtonListener {
        // TODO: Update argument type and name
        void onRefresh();
    }
}
