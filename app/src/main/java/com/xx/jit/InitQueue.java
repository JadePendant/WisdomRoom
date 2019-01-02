package com.xx.jit;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by 炊烟 on 2018/11/8.
 */

public class InitQueue extends Application {
    public static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(getApplicationContext());
    }

    public static com.android.volley.RequestQueue getHttpQueue()
    {
        return queue;
    }

}
