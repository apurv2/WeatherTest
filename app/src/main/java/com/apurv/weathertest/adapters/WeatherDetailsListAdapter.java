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

    public WeatherDetailsListAdapter(Context context, List<WeatherDetails> data, RecyclerTouchInterface parentActivity) {

        mLayoutInflator = LayoutInflater.from(context);
        this.mWeatherDataList = data;
        mVolleySingleton = VolleySingleton.getNetworkInstnace();
        mImageLoader = mVolleySingleton.getmImageLoader();
        this.mRecyclerInterface = parentActivity;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View mRowView = mLayoutInflator.inflate(R.layout.weather_list_items, viewGroup, false);
        MyViewHolder mViewHolder = new MyViewHolder(mRowView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherDetailsListAdapter.MyViewHolder myViewHolder, int mPosition) {


        WeatherDetails mWeatherDetail = mWeatherDataList.get(mPosition);
        myViewHolder.day.setText(mWeatherDetail.getDay());
        myViewHolder.high.setText("H:"+mWeatherDetail.getHigh());
        myViewHolder.low.setText("L:"+mWeatherDetail.getLow());
        myViewHolder.condition.setText(mWeatherDetail.getCondition());


        loadImages(myViewHolder, mWeatherDetail.getImageUrl(), mPosition);

    }

    private void loadImages(final WeatherDetailsListAdapter.MyViewHolder mViewHolder, String url, final int position) {


        mImageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {


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


    public void addAll(List<WeatherDetails> advertisements) {


        for (int i = 0; i < advertisements.size(); i++) {
            mWeatherDataList.add(advertisements.get(i));
            notifyItemInserted(mWeatherDataList.size());
        }
    }

    public void clear() {


        for (int i = mWeatherDataList.size() - 1; i >= 0; i--) {
            mWeatherDataList.remove(mWeatherDataList.get(i));
            notifyItemRemoved(mWeatherDataList.size());
        }


    }

    @Override
    public int getItemCount() {

        return mWeatherDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView day;
        TextView high;
        TextView low;
        TextView condition;
        ImageView weatherImage;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            day = (TextView) itemView.findViewById(R.id.day);
            high = (TextView) itemView.findViewById(R.id.high);
            low = (TextView) itemView.findViewById(R.id.low);
            condition = (TextView) itemView.findViewById(R.id.condition);
            weatherImage = (ImageView) itemView.findViewById(R.id.weatherImage);


        }

        @Override
        public void onClick(View v) {

            mRecyclerInterface.onTouch(getAdapterPosition());

        }
    }


}
