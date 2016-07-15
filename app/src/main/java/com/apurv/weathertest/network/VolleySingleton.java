package com.apurv.weathertest.network;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.apurv.weathertest.util.GlobalApplicationContext;

/**
 * Created by apurv on 7/8/16.
 * Singleton class to handle Volley Requests
 */
public class VolleySingleton {

    public static VolleySingleton sSingleton = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    /**
     * Constructor to create Request queue
     */
    private VolleySingleton() {

        mRequestQueue = Volley.newRequestQueue(GlobalApplicationContext.getAppContext());

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            private LruCache<String, Bitmap> cache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024) / 8);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });


    }

    /**
     * returns VolleySingleton instance
     *
     * @return
     */
    public static VolleySingleton getNetworkInstnace() {
        if (sSingleton == null) {
            sSingleton = new VolleySingleton();
        }
        return sSingleton;
    }

    /**
     * returns requestQuest object
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * returns ImageLoader object
     *
     * @return
     */
    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }

}
