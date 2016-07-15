package com.apurv.weathertest.src;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.apurv.weathertest.R;
import com.apurv.weathertest.adapters.WeatherDetailsListAdapter;
import com.apurv.weathertest.interfaces.RecyclerTouchInterface;
import com.apurv.weathertest.interfaces.WeatherBI;
import com.apurv.weathertest.network.WeatherBO;
import com.apurv.weathertest.pojo.WeatherDetails;
import com.apurv.weathertest.util.GPSTracker;
import com.apurv.weathertest.util.L;
import com.apurv.weathertest.util.LoadingDialog;
import com.apurv.weathertest.util.Utilities;
import com.apurv.weathertest.util.WeatherConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * MainActivity class fetches weather data from the weather API -
 * Asks the user to grant GPS permission(Marshmallow) and turn on GPS when location is not provided.
 * It populates the ListView from callback methods from API
 * Stores the list in android parcel so that it can fetch it when the device configuration changes
 */
public class MainActivity extends AppCompatActivity implements
        RecyclerTouchInterface, WeatherBI {

    private WeatherDetailsListAdapter mWeatherDetailsAdapter;
    private boolean mFetchWeatherFlag = false;
    private boolean mAskGpsPermissionFlag = false;


    ArrayList<WeatherDetails> weatherDetails = new ArrayList<WeatherDetails>();

    /**
     * Lifecycle method that initializes RecyclerViews and adapters, fetches WeatherInfo and manages GPS permissions.
     *
     * @param mSavedInstanceState - contains saved instance state (weather data mainly)
     */
    @Override
    protected void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.activity_main);


        // Setting up Recycler View and adapter to it.
        mWeatherDetailsAdapter = new WeatherDetailsListAdapter(getApplicationContext(), new ArrayList<WeatherDetails>(), this);
        RecyclerView mWeatherRecyclerView = (RecyclerView) findViewById(R.id.weatherList);
        mWeatherRecyclerView.setAdapter(mWeatherDetailsAdapter);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mWeatherRecyclerView.setLayoutManager(mLayoutManager);


        //Setting up actionbar and setting a custom view to accept zip code.
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.search_bar);

        // Checking for app permissions on Android Marshmallow for GPS permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // If the permission is not already granted, open dialog box to ask for permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
                //if the activity instance has been previously saved
            if (mSavedInstanceState == null) {

                // if the permissions have been granted get Weather Information using the current location of the user.
                getFromServer(getCurrentZipCode());
            } else {

                // Populate recycler view with the data fetched from a saved activity instance
                populateRecyclerView(mSavedInstanceState.<WeatherDetails>getParcelableArrayList(WeatherConstants.STATE_CHANGED));
            }
        }


    }

    /**
     * Asynchronous HTTP GET method to API to fetch weather information
     */
    private void getFromServer(String mZip5) {
        MainActivity mActivity = this;

        try {
            //checking for valid zip code
            if (mZip5.length() == 5) {

                // setting the zip code text field with
                EditText mZip5EditText = (EditText) findViewById(R.id.zip);
                mZip5EditText.setText(mZip5);

                //showing the loading dialog while the weather information is being fetched
                final LoadingDialog loadingDialog = Utilities.showLoadingDialog(WeatherConstants.LOADING, mActivity.getSupportFragmentManager());
                String url = WeatherConstants.URL + mZip5 + WeatherConstants.REQUEST_TYPE; // url for API
                L.m("url==" + url);

                // call to WeatherBO - business object to make a HTTP GET request using android volley library.
                new WeatherBO(url, loadingDialog);


            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    /**
     * Fetches the current zip code based on GPS location.
     *
     * @return String contains Zip code
     */
    private String getCurrentZipCode() {

        String mZip5 = "";

        try {
            //Initializing GeoCoder service for GPS tracking
            Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());
            GPSTracker mGpsTracker = new GPSTracker();

            //method to get location
            Location mLocation = mGpsTracker.getLocation(getSupportFragmentManager(), this);

            if (mLocation != null) {
                //geoCoder API to fetch postal code from latitude and longitude
                List<Address> address = mGeocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                mZip5 = address.get(0).getPostalCode();
            }

        } catch (IOException e) {
            e.printStackTrace();

            L.m(e.getCause() + "");
            Utilities.showALertDialog(WeatherConstants.GPS_ERROR, getSupportFragmentManager());


        } catch (NullPointerException e) {
            e.printStackTrace();
            Utilities.showALertDialog(WeatherConstants.GPS_ERROR, getSupportFragmentManager());

        } catch (Exception e) {
            e.printStackTrace();
            Utilities.showALertDialog(WeatherConstants.GPS_ERROR, getSupportFragmentManager());

        }
        return mZip5;
    }

    /**
     * Callback method from DialogFragment that alerts user that GPS permission(Marshmallow) is required for
     * functioning of the app.
     */
    public void alertCallback() {

        try {
            //Check for GPS app permissions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.
                    checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // If the permission is not already granted, open dialog box to ask for permissions
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Callback method after the user interacts with the Permissions popup
     *
     * @param requestCode  contains the request code for the permission
     * @param permissions  array that contains all permissions
     * @param grantResults array containing code for granted and not granted.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //flag to fetch weather data when onResumeActivity method is called after the user grants permission
                    mFetchWeatherFlag = true;


                } else {

                    //flag to track the user permission for GPS
                    mAskGpsPermissionFlag = true;
                }
                return;
            }


        }


    }

    /**
     * callback method for RecyclerView touch intreface
     *
     * @param position contains position of item touched on the RecyclerView
     */
    @Override
    public void onTouch(int position) {

        // Intent to launch Weather Details activity
        Intent details = new Intent(this, WeatherDeatilsActivity.class);
        details.putExtra(WeatherConstants.WEATHER_DETAILS_PARCEL_KEY, weatherDetails.get(position));
        startActivity(details);


    }

    /**
     * callback method from the weather API
     *
     * @param details      - contains data fetched from Weather API
     * @param mWeatherCode - contains code error code for API call.
     *                     0- no error
     *                     1- Error with input zip code or API response
     *                     2- network connectivity error
     */
    @Override
    public void onWeatherInfoReady(ArrayList<WeatherDetails> details, int mWeatherCode) {

        switch (mWeatherCode) {

            case 0:
                populateRecyclerView(details);
                break;
            case 1:
                Utilities.showALertDialog(WeatherConstants.API_ERROR, getSupportFragmentManager());
                break;
            case 2:
                Utilities.showALertDialog(WeatherConstants.CONNECTIVITY_ERROR, getSupportFragmentManager());
                break;
        }


    }

    /**
     * Populates the RecyclerView with weather data
     *
     * @param details contains data containing weather information
     */
    private void populateRecyclerView(ArrayList<WeatherDetails> details) {

        try {
            if (mWeatherDetailsAdapter != null) {

                //populating the recycler View
                mWeatherDetailsAdapter.clear();
                mWeatherDetailsAdapter.addAll(details);
                mWeatherDetailsAdapter.notifyDataSetChanged();

                //populating the data structure that maintains weather Details to send to WeatherDetailsActivity when
                // a list item is clicked on the recycler view.
                weatherDetails.clear();
                weatherDetails.addAll(details);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * callback method when a menu option is selected
     *
     * @param menuItem menu item that is touched
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        try {

            // closing keyboard when search button is clicked
            if (menuItem.getItemId() == R.id.search) {
                View mView = this.getCurrentFocus();
                if (mView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                }

                // fetching the value of zip from the text view
                EditText mZip5EditText = (EditText) findViewById(R.id.zip);
                String mZip5 = String.valueOf(mZip5EditText.getText());

                // when the user does not enter a zip code
                if (mZip5.length() == 0) {

                    // checking for GPS permission when the user does not enter any zip code
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.
                            checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    } else {

                        getFromServer(getCurrentZipCode());
                    }


                } else if (mZip5.length() != 5) {
                    // if the user has entered invalid zip code show alert
                    Utilities.showALertDialog(WeatherConstants.INVALID_INPUT, getSupportFragmentManager());
                } else {
                    // fetch the weather data from WeatherAPI for the zip code entered by the user.
                    getFromServer(mZip5);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(menuItem);

    }

    /**
     * activity life cycle method that inflates the xml menu to actionbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }


    /**
     * Activity life cycle method that called when the activity resumes.
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();


        try {
            // check for the GPS permission flag. If the permission has not been granted, ask for it again.
            if (mAskGpsPermissionFlag && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.
                    checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //show alert dialog saying that the user needs to provide that permission
                Utilities.showALertDialog(WeatherConstants.NEED_PERMISSION, getSupportFragmentManager());

            } else {
                // if the user has provided GPS permission and has requested for weather data to be fetched for his current location.
                if (mAskGpsPermissionFlag || mFetchWeatherFlag) {
                    mFetchWeatherFlag = false;
                    getFromServer(getCurrentZipCode());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Activity life cycle method called to store the instance of the activity  before its destroyed
     *
     * @param outState Bundle to store instance objects or values
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // saving weather data into parcel and then to bundle before destroying the activity.
        outState.putParcelableArrayList(WeatherConstants.STATE_CHANGED, weatherDetails);


    }
}
