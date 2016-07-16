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

        //getting the root view of the page and inflating it to a Java object
        View mPageView = mLayoutInflater.inflate(R.layout.dialog_alert, null);
        mDialogBuilder.setView(mPageView);

        //Getting message sent via bundle from calling activity
        Bundle mMessageBundle = this.getArguments();
        final String mAlertText = mMessageBundle.getString(WeatherConstants.ALERT_TEXT);

        //populating message into text view inside Alert Dialog
        TextView mMessageTextView = (TextView) mPageView.findViewById(R.id.text);
        mMessageTextView.setText(mAlertText);
        setCancelable(false);

        //Initializing ok button
        Button mOkButton = (Button) mPageView.findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mParentActivity;
                mParentActivity = (MainActivity) getActivity();

                //calling the callback method to the activity
                mParentActivity.alertCallback();
                //dismiss the alert dialog
                dismiss();
            }
        });


        // Create the AlertDialog object and return it
        return mDialogBuilder.create();
    }


}
