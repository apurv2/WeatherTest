package com.apurv.weathertest.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apurv.weathertest.pojo.WeatherDetails;
import com.apurv.weathertest.util.L;
import com.apurv.weathertest.util.LoadingDialog;
import com.apurv.weathertest.util.WeatherConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apurv on 6/3/15.
 * Business Object to fetch and receive weather information using weatherAPI
 */
public class WeatherBO {


    // Business Object constructor the fetch WeatherAPI data using URL
    public WeatherBO(String mUrl, final LoadingDialog mLoadingDialog) {

        //Forming a RequestQueue object to store network requests
        RequestQueue mRequestQueue = VolleySingleton.getNetworkInstnace().getRequestQueue();

        //Creating a JsonObject instance to fetch JSON object data from Server
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {


            /**
             * Response from GET method to fetch Weather Data
             * @param mResponse
             */
            @Override
            public void onResponse(JSONObject mResponse) {
                //ArrayList that stores weather data after the JSON has been parsed.
                ArrayList<WeatherDetails> mWeatherDetails = new ArrayList<WeatherDetails>();
                try {

                    // code for parsing weather API JSON data.
                    JSONObject mForecastObject = mResponse.getJSONObject(WeatherConstants.FORECAST);
                    JSONArray mForecastDayArray = mForecastObject.getJSONObject(WeatherConstants.SIMPLE_FORECAST).getJSONArray(WeatherConstants.FORECAST_DAY);

                    // Looping through JSON object to parse them
                    for (int mForecastDayCounter = 0; mForecastDayCounter < mForecastDayArray.length(); mForecastDayCounter++) {

                        L.m("adding");

                        JSONObject mForecastDay = mForecastDayArray.getJSONObject(mForecastDayCounter);

                        JSONObject mDate = mForecastDay.getJSONObject(WeatherConstants.DATE);
                        String day = mDate.getString(WeatherConstants.WEEKDAY);

                        JSONObject mHighObj = mForecastDay.getJSONObject(WeatherConstants.HIGH);
                        String high = mHighObj.getString(WeatherConstants.CELSIUS);

                        JSONObject mLowObj = mForecastDay.getJSONObject(WeatherConstants.LOW);
                        String low = mLowObj.getString(WeatherConstants.CELSIUS);

                        JSONObject mMaxWindObj = mForecastDay.getJSONObject(WeatherConstants.MAX_WIND);
                        int maxWind = Integer.parseInt(mMaxWindObj.getString(WeatherConstants.MPH));

                        JSONObject mAvgWindObj = mForecastDay.getJSONObject(WeatherConstants.AVE_WIND);
                        int aveWind = Integer.parseInt(mAvgWindObj.getString(WeatherConstants.MPH));

                        String mConditions = mForecastDay.getString(WeatherConstants.CONDITIONS);
                        String mWeatherUrl = mForecastDay.getString(WeatherConstants.ICON_URL);

                        int mAveHumidity = Integer.parseInt(mForecastDay.getString(WeatherConstants.AVE_HUMIDITY));

                        // adding parsed data to WeatherDetails class and then to ArrayList
                        mWeatherDetails.add(new WeatherDetails(day, high, low, mConditions, maxWind, aveWind, mAveHumidity, mWeatherUrl));

                    }

                    //Returning the weather data to the calling activity
                    mLoadingDialog.weatherBI.onWeatherInfoReady(mWeatherDetails, 0);
                    //closing the loading dialog as the data has been fetched.
                    mLoadingDialog.dismiss();

                } catch (JSONException e) {
                    // Handling JSON parser exception
                    e.printStackTrace();
                    mLoadingDialog.weatherBI.onWeatherInfoReady(null, 1);
                    mLoadingDialog.dismiss();

                } catch (Exception e) {

                    // Handling all other exceptions
                    e.printStackTrace();
                    mLoadingDialog.weatherBI.onWeatherInfoReady(null, 1);
                    mLoadingDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error thrown by VolleyRequest especially for network problems or bad requests
                mLoadingDialog.weatherBI.onWeatherInfoReady(new ArrayList<WeatherDetails>(), 2);
                mLoadingDialog.dismiss();
            }
        });

        mRequestQueue.add(mJsonObjectRequest);

    }
}
