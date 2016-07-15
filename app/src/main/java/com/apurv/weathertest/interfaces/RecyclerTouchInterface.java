package com.apurv.weathertest.interfaces;

/**
 * Created by akamalapuri on 6/7/2016.
 * RecyclerView Interface for onTouch callback
 */
public interface RecyclerTouchInterface {

    /**
     * OnTouch for RecyclerView onTouchItem
     * @param position where the user touched
     */
    public void onTouch(int position);
}
