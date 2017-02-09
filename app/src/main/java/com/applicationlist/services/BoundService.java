package com.applicationlist.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Vikrant Chauhan on 10/20/2016.
 */

public class BoundService extends Service {

    /* Binder object returned to onServiceConnected*/
    private final IBinder myLocalBinder = new MyLocalBinder();
    private CallBack mCallBack;
    private Timer timer;

    /**
     * schedule timer at fixed rate
     * posts updates to mCallBack
     */
    public void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            long x = 0;
            @Override
            public void run() {
                if (SystemClock.currentThreadTimeMillis() - x > 10) {
                    cancel();
                    if (mCallBack != null)
                        mCallBack.taskCompleteAuto();
                } else if (mCallBack != null) {
                    mCallBack.updateActivityTime(SystemClock.currentThreadTimeMillis());
                }
            }
        }, 0, 100);

    }

    /**
     * stop timer
     * post result to mCallBack
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            mCallBack.taskCompleteManual();
        }
    }

    /**
     * custom class for binder
     */
    public class MyLocalBinder extends Binder {

        /**
         * @return BoundService instance
         */
        public BoundService getService() {
            return BoundService.this;
        }

        /**
         *
         * @param callBack
         */
        /*
        *Any component which binds to service may register itself
        *to receive callbacks
        */
        public void registerCallBack(CallBack callBack) {
            mCallBack = callBack;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myLocalBinder;
    }

    /**
     * interface between component and service
     */
    public interface CallBack {
        public void updateActivityTime(long t);

        public void taskCompleteAuto();

        public void taskCompleteManual();
    }
}
