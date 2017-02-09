package com.applicationlist.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applicationlist.R;
import com.applicationlist.services.BoundService;

public class BoundServiceActivity extends AppCompatActivity implements BoundService.CallBack {

    private BoundService mService;
    private boolean isBound = false;
    private boolean isStarted;
    private TextView tv;
    private Button start,stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_service);
        bindService();
        initViews();
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        bindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
      unbindService();
    }

    /**
     * initialize views and click listeners
     */
    public void initViews(){
        tv = (TextView) findViewById(R.id.tv);
        start = (Button) findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStarted) {
                    isStarted = true;
                    mService.startTimer();
                }
            }
        });
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mService!=null){
                    mService.stopTimer();
                }
            }
        });
    }

    /**
     * bind service to activity
     */
    public void bindService(){
        Intent intent = new Intent(this, BoundService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * unbind service from activity
     */
    public void unbindService(){
        if (isBound && mServiceConnection!=null && mService!=null) {
            mService.stopTimer();
            unbindService(mServiceConnection);
            isBound = false;
            stopService(new Intent(this, BoundService.class));
        }
    }

    /**
     * service connection object
     * used as parameter to bind service with activity
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.MyLocalBinder binder = (BoundService.MyLocalBinder) service;
            binder.registerCallBack(BoundServiceActivity.this);
            mService = binder.getService();
            isBound = true;
            Toast.makeText(BoundServiceActivity.this,"Service Connected",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Toast.makeText(BoundServiceActivity.this,"Service Disconnected",Toast.LENGTH_LONG).show();
        }
    };

    /**
     * callback method from service
     * called to update activity at regular intervals
     */
    @Override
    public void updateActivityTime(final long t) {
        tv.post(new Runnable() {
            @Override
            public void run() {
                tv.setText(Long.toString(t));
            }
        });
    }

    /**
     * callback method from service
     * called when timer stops itself
     */
    @Override
    public void taskCompleteAuto() {
        unbindService();
        isStarted = false;
        tv.post(new Runnable() {
            @Override
            public void run() {
                tv.setText("Auto Stop");
            }
        });
    }

    /**
     * callback method from service
     * called when timer is stopped manually
     */
    @Override
    public void taskCompleteManual() {
        isStarted = false;
        tv.post(new Runnable() {
            @Override
            public void run() {
                tv.setText("Manual Stop");
            }
        });
    }
}
