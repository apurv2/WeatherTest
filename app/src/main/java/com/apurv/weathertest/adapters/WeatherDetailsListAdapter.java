package com.apurv.weathertest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.apurv.weathertest.R;
import com.apurv.weathertest.interfaces.RecyclerTouchInterface;
import com.apurv.weathertest.network.VolleySingleton;
import com.apurv.weathertest.pojo.WeatherDetails;

import java.util.Collections;
import java.util.List;

/**
 * Created by apurv on 7/5/06.
 * RecyclerView adapter that contains weather information. It has methods to add all elements
 * from an arrayList using a ViewHolder. It fetches Images from the URLs using Volley library.
 */
public class WeatherDetailsListAdapter extends RecyclerView.Adapter<WeatherDetailsListAdapter.MyViewHolder> {


    private LayoutInflater mLayoutInflator;

    List<WeatherDetails> mWeatherDataList = Collections.emptyList();

    private ImageLoader mImageLoader;
    private VolleySingleton mVolleySingleton;
    RecyclerTouchInterface mRecyclerInterface;

    /**
     * constructor to populate adapter with weather details and interface for Recycler view touch callback
     *
     * @param context        - contains activity context
     * @param data           - weather data
     * @param parentActivity - Inteface for touch callback
     */
    public WeatherDetailsListAdapter(Context context, List<WeatherDetails> data, RecyclerTouchInterface parentActivity) {

        mLayoutInflator = LayoutInflater.from(context);
        this.mWeatherDataList = data;
        mVolleySingleton = VolleySingleton.getNetworkInstnace();
        //Image loader to load images
        mImageLoader = mVolleySingleton.getmImageLoader();
        this.mRecyclerInterface = parentActivity;
    }


    /**
     * Creating view holder to contain the weather details single row
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View mRowView = mLayoutInflator.inflate(R.layout.weather_list_items, viewGroup, false);
        MyViewHolder mViewHolder = new MyViewHolder(mRowView);
        return mViewHolder;
    }

    /**
     * To bind weatherData from API to adapter's viewHolder
     *
     * @param myViewHolder
     * @param mPosition
     */
    @Override
    public void onBindViewHolder(WeatherDetailsListAdapter.MyViewHolder myViewHolder, int mPosition) {

        //binding weather data to adapter
        WeatherDetails mWeatherDetail = mWeatherDataList.get(mPosition);
        myViewHolder.day.setText(mWeatherDetail.getDay());
        myViewHolder.high.setText("H:" + mWeatherDetail.getHigh());
        myViewHolder.low.setText("L:" + mWeatherDetail.getLow());
        myViewHolder.condition.setText(mWeatherDetail.getCondition());


        loadImages(myViewHolder, mWeatherDetail.getImageUrl(), mPosition);

    }

    /**
     * load images from URL parsed from JSON
     *
     * @param mViewHolder
     * @param url
     * @param position
     */
    private void loadImages(final WeatherDetailsListAdapter.MyViewHolder mViewHolder, String url, final int position) {


        mImageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                // Loading Images requested using image URL
                Bitmap mPhoto = response.getBitmap();
                if (mPhoto != null) {
                    mViewHolder.weatherImage.setImageBitmap(mPhoto);
                }

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }

    /**
     * adding all weather data using custom method
     *
     * @param weatherDetails
     */
    public void addAll(List<WeatherDetails> weatherDetails) {

        //Looping through data structure to populate weather data at once
        for (int counter = 0; counter < weatherDetails.size(); counter++) {
            mWeatherDataList.add(weatherDetails.get(counter));
            notifyItemInserted(mWeatherDataList.size());
        }
    }

    //clears the weather data adapter/ recycler view
    public void clear() {
        for (int counter = mWeatherDataList.size() - 1; counter >= 0; counter--) {
            mWeatherDataList.remove(mWeatherDataList.get(counter));
            notifyItemRemoved(mWeatherDataList.size());
        }


    }

    /**
     * returns the size of adapter
     *
     * @return
     */
    @Override
    public int getItemCount() {

        return mWeatherDataList.size();
    }

    /**
     * Inner class to contain ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView day;
        TextView high;
        TextView low;
        TextView condition;
        ImageView weatherImage;

        //Constructor to populate all child views
        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            day = (TextView) itemView.findViewById(R.id.day);
            high = (TextView) itemView.findViewById(R.id.high);
            low = (TextView) itemView.findViewById(R.id.low);
            condition = (TextView) itemView.findViewById(R.id.condition);
            weatherImage = (ImageView) itemView.findViewById(R.id.weatherImage);


        }

        //Touch interface method called when an item is clicked on the recycler view
        @Override
        public void onClick(View v) {

            mRecyclerInterface.onTouch(getAdapterPosition());

        }
    }


}
