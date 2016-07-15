package com.apurv.weathertest.util;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * Created by apurv on 7/12/2016.
 * Created to contain customized methods to show hide views, show dialogs i.e to reduce redundant code.
 */
public class Utilities {

    /**
     * shows loading dialog when the server call is made
     *
     * @param mLoaderText      contains text to be displayed on the loading dialogue
     * @param mFragmentManager instance of the FragmentManager sent from the calling activity.
     * @return loading dialog instance after launching it
     */
    public static LoadingDialog showLoadingDialog(String mLoaderText, FragmentManager mFragmentManager) {

        // creating instance of dialog fragment and setting the message bundle.
        LoadingDialog mLoadingDialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        mLoadingDialog.setArguments(bundle);
        // launching the dialog fragment
        mLoadingDialog.show(mFragmentManager, mLoaderText);

        return mLoadingDialog;
    }

    /**
     * Utility method to display an alert dialog with a custom message
     * @param mLoaderText - custom message to be shown on the alert dialog
     * @param mFragmentManager - instance of the FragmentManager sent from the calling activity.
     * @return  AlertDialog instance after launching it
     */
    public static AlertDialog showALertDialog(String mLoaderText, FragmentManager mFragmentManager) {

        //setting a bundle with message parameter to send to Dialog fragment
        Bundle mMessageBundle = new Bundle();
        mMessageBundle.putString(WeatherConstants.ALERT_TEXT, mLoaderText);

        // creating instance of dialog fragment and setting the message bundle.
        AlertDialog mErrorDialog = new AlertDialog();
        mErrorDialog.setArguments(mMessageBundle);

        // launching the dialog fragment
        mErrorDialog.show(mFragmentManager, "");


        return mErrorDialog;
    }


}
