package com.applicationlist.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applicationlist.R;
import com.applicationlist.activities.FileData;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private TextView tv;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(v==null) {
            v = inflater.inflate(R.layout.fragment_my, container, false);
            tv = (TextView) v.findViewById(R.id.fragment_tv);
            new MyTask().execute("Start time" + SystemClock.currentThreadTimeMillis());
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }

    private class MyTask extends AsyncTask<String,Long,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //if(tv!=null)
            tv.setText("Current time is: "+ SystemClock.currentThreadTimeMillis());
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
