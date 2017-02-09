package com.applicationlist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.applicationlist.db.DbHelper;
import com.applicationlist.adapter.ListAdapter;
import com.applicationlist.R;

public class Favorites extends AppCompatActivity implements ListAdapter.Item {

    private ListView lv;
    private ListAdapter adapter;
    private DbHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        lv = (ListView) findViewById(R.id.fav_list);

        helper = new DbHelper(Favorites.this);

        adapter = new ListAdapter(Favorites.this,helper.getFavCursor(),true);

        lv.setAdapter(adapter);
    }

    @Override
    public void addToFav(View v) {

    }

    @Override
    public void removeFromFav(View v) {

    }
}
