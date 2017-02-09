package com.applicationlist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.applicationlist.R;
import com.applicationlist.db.DbHelper;

/**
 * Created by Vikrant Chauhan on 10/7/2016.
 */

public class ListAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    private Item item;
    private Boolean isFav;


    public ListAdapter(Context context, Cursor c, Boolean isFav) {
        super(context, c, 0);
        this.item = (Item) context;
        this.isFav = isFav;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_row, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView number = (TextView) view.findViewById(R.id.number);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.box);

        name.setText(cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME)));
        number.setText(cursor.getString(cursor.getColumnIndex(DbHelper.KEY_PHONE)));

        if (isFav) {
            checkBox.setVisibility(View.GONE);
        } else {
            if (cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_SELECTED)) == 1) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    item.addToFav((View) v.getParent());
                }else{
                    item.removeFromFav((View) v.getParent());
                }
            }
        });

    }

    public interface Item {
        public void addToFav(View v);

        public void removeFromFav(View v);
    }
}
