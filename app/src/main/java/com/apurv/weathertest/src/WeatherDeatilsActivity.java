package com.apurv.weathertest.src;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.apurv.weathertest.R;
import com.apurv.weathertest.pojo.WeatherDetails;
import com.apurv.weathertest.util.WeatherConstants;

public class WeatherDeatilsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_deatils);

        try {

            ActionBar mActionBar = getSupportActionBar();
            mActionBar.setDisplayHomeAsUpEnabled(true);


            //Getting weather details from previous intent using bundle and parcel
            Intent mCallingIntent = this.getIntent();
            WeatherDetails mWeatherDetails = mCallingIntent.getExtras().getParcelable(WeatherConstants.WEATHER_DETAILS_PARCEL_KEY);

            //Using text Views to populate weather data
            TextView mDayTextView = (TextView) findViewById(R.id.day1);
            TextView mHighTextView = (TextView) findViewById(R.id.high1);
            TextView mLowTextView = (TextView) findViewById(R.id.low1);
            TextView mConditionTextView = (TextView) findViewById(R.id.condition1);
            TextView mMaxWindTextView = (TextView) findViewById(R.id.maxWind);
            TextView mAvgWindTextView = (TextView) findViewById(R.id.avgWind);
            TextView mAvgHumidityTextView = (TextView) findViewById(R.id.avgHumidity);

            //Setting weather data to text views
            mDayTextView.setText(mWeatherDetails.getDay());
            mHighTextView.setText(mWeatherDetails.getHigh());
            mLowTextView.setText(mWeatherDetails.getLow());
            mConditionTextView.setText(mWeatherDetails.getCondition());
            mMaxWindTextView.setText(mWeatherDetails.getMaxWind() + WeatherConstants.MPHH);
            mAvgHumidityTextView.setText(mWeatherDetails.getAverageHumidity() + WeatherConstants.MPHH);
            mAvgWindTextView.setText(mWeatherDetails.getAverageWind() + WeatherConstants.MPHH);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
