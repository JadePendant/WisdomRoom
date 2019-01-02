package com.xx.jit;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.Timer;
import java.util.TimerTask;

public class AdvertisementTools extends AppCompatActivity {

    Timer timer;
    TimerTask task;
    View view;


    class mytimer extends TimerTask{
        int i = 0;
        public void run(){
            Message message = Message.obtain();
            message.what = i;
            i++;
            handler.sendMessage(message);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        public void handleMessage(Message message){
            super.handleMessage(message);
            if(message.what == 1){
                timer.cancel();
                task.cancel();
                Intent next = new Intent(AdvertisementTools.this,Animation.class);

                startActivity(next);

            }
        }
    };



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);
        timer = new Timer();
        task = new mytimer();
        timer.schedule(task, 10000,1);

    }


    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE: case MotionEvent.ACTION_DOWN:
                timer.cancel();
                task = new mytimer() ;
                timer = new Timer();
                timer.schedule(task, 10000, 1);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);

    }
    class HomeWatcherReceiver extends BroadcastReceiver {
        private static final String LOG_TAG = "HomeReceiver";
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(LOG_TAG, "onReceive: action: " + action);
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                // android.intent.action.CLOSE_SYSTEM_DIALOGS
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                Log.i(LOG_TAG, "reason: " + reason);

                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // 短按Home键
                    timer.cancel();
                    Log.i(LOG_TAG, "homekey");

                }
                else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                    // 长按Home键 或者 activity切换键
                    timer.cancel();
                    Log.i(LOG_TAG, "long press home key or activity switch");

                }
                else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                    // 锁屏
                    timer.cancel();
                    Log.i(LOG_TAG, "lock");
                }
                else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    // samsung 长按Home键
                    timer.cancel();
                    Log.i(LOG_TAG, "assist");
                }

            }
        }


        HomeWatcherReceiver mHomeKeyReceiver = null;

        private void registerHomeKeyReceiver(Context context) {
            Log.i(LOG_TAG, "registerHomeKeyReceiver");
            mHomeKeyReceiver = new HomeWatcherReceiver();
            final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

            context.registerReceiver(mHomeKeyReceiver, homeFilter);
        }

        public void unregisterHomeKeyReceiver(Context context) {
            Log.i(LOG_TAG, "unregisterHomeKeyReceiver");
            if (null != mHomeKeyReceiver) {
                context.unregisterReceiver(mHomeKeyReceiver);
            }
        }

    }

    @Override
    protected void onResume() {
        timer.cancel();
        task.cancel();
        super.onResume();
        //Toast.makeText(AdvertisementTools.this,"shuaxi",Toast.LENGTH_LONG).show();
        HomeWatcherReceiver homeWatcherReceiver = new HomeWatcherReceiver();
        homeWatcherReceiver.registerHomeKeyReceiver(this);

        timer = new Timer();

        task = new mytimer();
        timer.schedule(task, 10000, 1);


    }

    protected void onPause() {


        timer.cancel();
        task.cancel();
        HomeWatcherReceiver homeWatcherReceiver = new HomeWatcherReceiver();
         homeWatcherReceiver.unregisterHomeKeyReceiver(this);
        super.onPause();
    }

    protected void onStop(){
        timer.cancel();
        timer.cancel();
        super.onStop();
    }

}
