package com.apurv.weathertest.util;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apurv.weathertest.R;
import com.apurv.weathertest.src.MainActivity;

/**
 * Created by akamalapuri on 7/29/2015.
 * Displays alert information in a dialog box
 */
public class AlertDialog extends DialogFragment {

    /**
     * lifecycle method to display information fetched from bundle passed from calling activity.
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        android.app.AlertDialog.Builder mDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();


        View mPageView = mLayoutInflater.inflate(R.layout.dialog_alert, null);
        mDialogBuilder.setView(mPageView);

        Bundle mMessageBundle = this.getArguments();
        final String mAlertText = mMessageBundle.getString(WeatherConstants.ALERT_TEXT);
        TextView mMessageTextView = (TextView) mPageView.findViewById(R.id.text);
        mMessageTextView.setText(mAlertText);
        setCancelable(false);


        Button mOkButton = (Button) mPageView.findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mParentActivity;
                mParentActivity = (MainActivity) getActivity();

                mParentActivity.alertCallback();
                dismiss();
            }
        });


        // Create the AlertDialog object and return it
        return mDialogBuilder.create();
    }


}
