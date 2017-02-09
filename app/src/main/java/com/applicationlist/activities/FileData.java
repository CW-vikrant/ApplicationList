package com.applicationlist.activities;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;

import com.applicationlist.fragments.MyFragment;
import com.applicationlist.pojo.Contact;
import com.applicationlist.adapter.ContactsAdapter;
import com.applicationlist.utility.FileHelper;
import com.applicationlist.adapter.ListItemTouchHelper;
import com.applicationlist.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FileData extends AppCompatActivity implements MyFragment.OnFragmentInteractionListener{

    RecyclerView recyclerView;
    ContactsAdapter adapter;
    private TextView tv;
    ArrayList<Contact> list;

    private static final String TAG_MY_FRAGMENT = "myFragment";
    private MyFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_data);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        tv = (TextView) findViewById(R.id.tv);

        //read from default cache dir
        //String s = FileHelper.readFromDirectory(getCacheDir(),FileHelper.FILE_NAME_DIRECTORY);

        //read from specified external storage
        //String s = FileHelper.readFromExternalStorage(FileHelper.FILE_NAME_EXTERNAL);

        //read from default internal storage
        String s = FileHelper.readFromFile(FileData.this, FileHelper.FILE_NAME);
        Gson gson = new Gson();

        if(savedInstanceState!=null){
            mFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag(TAG_MY_FRAGMENT);
            list = (ArrayList<Contact>) savedInstanceState.getSerializable("myKey");
        }else{
            insertFragment();
            list = gson.fromJson(s,new TypeToken<ArrayList<Contact>>(){}.getType());
        }
        adapter = new ContactsAdapter(list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        ItemTouchHelper.Callback callback =
                new ListItemTouchHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
        new MyTask().execute("Start time"+SystemClock.currentThreadTimeMillis());


    }

    public void insertFragment(){
        mFragment = new MyFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame,mFragment,TAG_MY_FRAGMENT)
                .commit();
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        savedState.putSerializable("myKey",list );

    }

    @Override
    public void onFragmentInteraction() {

    }

    private class MyTask extends AsyncTask<String,Long,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv.setText("Current time is: "+SystemClock.currentThreadTimeMillis());
        }

        @Override
        protected String doInBackground(String... params) {
            final String s = params[0];
            new Timer().scheduleAtFixedRate(new TimerTask() {
                long x = SystemClock.currentThreadTimeMillis();
                @Override
                public void run() {
                    if(SystemClock.currentThreadTimeMillis() - x > 10){
                        cancel();
                        onPostExecute(s);
                    }else {
                        onProgressUpdate(SystemClock.currentThreadTimeMillis());
                    }
                }
            },0,100);
            return s;
        }

        @Override
        protected void onProgressUpdate(final Long... i){
            tv.post(new Runnable() {
                @Override
                public void run() {
                    tv.setText(Long.toString(i[0]));
                }
            });

        }

        @Override
        protected void onPostExecute(final String params){

            tv.post(new Runnable() {
                @Override
                public void run() {
                    tv.setText(params+" End Time: "+SystemClock.currentThreadTimeMillis());
                }
            });

        }
    }
}
