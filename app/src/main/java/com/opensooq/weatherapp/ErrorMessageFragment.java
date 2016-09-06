package com.opensooq.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErrorMessageFragment extends Fragment {
    private static final String MESSAGE = "message";
    @BindView(R.id.sad_img)
    ImageView imageViewSad;
    @BindView(R.id.retry)
    Button retryButton;
    @BindView(R.id.error_txt)
    TextView errorTextView;
    private String message;

    /*

    imageViewSad = (ImageView) view.findViewById(R.id.sad_img);
    retryButton = (Button) view.findViewById(R.id.retry);*/
    private onRefreshButtonListener mListener;

    public ErrorMessageFragment() {
        // Required empty public constructor
    }

    public static ErrorMessageFragment newInstance(String message) {
        ErrorMessageFragment fragment = new ErrorMessageFragment();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.message = getArguments().getString(MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_error_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Picasso.with(getContext()).load(R.drawable.ic_android_sad).centerCrop().fit().into(imageViewSad);
         retryButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 onButtonPressed();
             }
         });
        errorTextView.setText(message);
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
