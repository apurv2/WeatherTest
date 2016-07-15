package com.apurv.weathertest.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.apurv.weathertest.R;
import com.apurv.weathertest.interfaces.WeatherBI;


/**
 * Created by akamalapuri on 11/5/2015.
 */
public class LoadingDialog extends DialogFragment {

    public WeatherBI weatherBI;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        setRetainInstance(true);
        setCancelable(false);


        final View pageView = inflater.inflate(R.layout.loading_dialog, null);
        builder.setView(pageView);


        Bundle bundle = this.getArguments();
        final String text = bundle.getString(WeatherConstants.LOADER_TEXT);


        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        weatherBI = (WeatherBI) activity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        L.m("sve instance state called");
    }
}
