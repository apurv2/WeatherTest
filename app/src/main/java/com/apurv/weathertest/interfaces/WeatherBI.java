package com.apurv.weathertest.interfaces;

import com.apurv.weathertest.pojo.WeatherDetails;

import java.util.ArrayList;

/**
 * Created by akamalapuri on 7/7/2016.
 * Callback interface when weatherInfo is fetched from weatherAPI
 */
public interface WeatherBI {


    /**
     * callback method from weatherAPI
     * @param weatherDetails
     * contains list of weatherInformation     */
    public void onWeatherInfoReady(ArrayList<WeatherDetails> weatherDetails,int mErrorCode);


}
