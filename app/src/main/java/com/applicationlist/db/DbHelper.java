package com.applicationlist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.applicationlist.pojo.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikrant Chauhan on 10/7/2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "contacts_manager";
    public static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_FAV = "favorites";

    public static final String KEY_ID = "_id";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_NAME = "name";
    public static final String KEY_NOTE = "note";
    public static final String KEY_SELECTED = "selected";

    private static DbHelper sHelper;

    public static DbHelper getHelper(Context context){
        if(sHelper == null){
            sHelper = new DbHelper(context);
        }
        return sHelper;
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String contacts_table = "CREATE TABLE "+TABLE_CONTACTS+"( "+KEY_ID+" INTEGER PRIMARY KEY"+","+
                KEY_NAME+" TEXT"+", "+KEY_PHONE+" TEXT"+", "+KEY_NOTE+" TEXT" +", "+KEY_SELECTED + " INTEGER DEFAULT 0"+ " )";

        String fav_table = "CREATE TABLE "+TABLE_FAV+"( "+KEY_ID+" INTEGER PRIMARY KEY"+","+
                KEY_NAME+" TEXT"+", "+KEY_PHONE+" TEXT"+", "+KEY_NOTE+" TEXT" + " )";

        db.execSQL(contacts_table);
        db.execSQL(fav_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContactToFav(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_NAME,contact.getName());
        v.put(KEY_PHONE,contact.getNumber());
        v.put(KEY_NOTE,contact.getNote());
        db.insert(TABLE_FAV,null,v);
    }

    public void addContact(String name, int number,String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_NAME,name);
        v.put(KEY_PHONE,number);
        v.put(KEY_NOTE,note);
        db.insert(TABLE_CONTACTS,null,v);
    }


    public void deleteContact(String number){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAV,KEY_PHONE+" =? ",new String[]{String.valueOf(number)});
    }

    public Contact getContact(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_CONTACTS,null,KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null);

        if(c!=null){
            c.moveToFirst();
        }

        return new Contact(c.getString(c.getColumnIndex(KEY_NAME)),
                Integer.parseInt(c.getString(c.getColumnIndex(KEY_PHONE))),
                c.getString(c.getColumnIndex(KEY_NOTE)),
                c.getInt(c.getColumnIndex(KEY_SELECTED)));
    }

    public List<Contact> getAllContacts(){
        List<Contact> list = new ArrayList<>();

        String query = "SELECT * FROM "+TABLE_CONTACTS;
        Cursor c = this.getReadableDatabase().rawQuery(query,null);

        while(c.moveToNext()){
            list.add(new Contact(c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getInt(c.getColumnIndex(KEY_PHONE)),
                    c.getString(c.getColumnIndex(KEY_NOTE)),
                    c.getInt(c.getColumnIndex(KEY_SELECTED))));
        }

        return list;
    }

    public Cursor getFavCursor(){

        String query = "SELECT * FROM "+TABLE_FAV;
        return this.getReadableDatabase().rawQuery(query,null);
    }

    public Cursor getCursor(){

        String query = "SELECT * FROM "+TABLE_CONTACTS;
        return this.getReadableDatabase().rawQuery(query,null);
    }

    public Cursor getFilteredContacts(String s){

        if(s.length()>0) {

            String[] selectionArgs = {"%" + s + "%"};
            //String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + KEY_NAME + "=?";
            return this.getReadableDatabase().
                    query(TABLE_CONTACTS,null,KEY_NAME+" like?",selectionArgs,null,null,null);
        }
        else
            return getCursor();
    }

    public Boolean updateContactSelectState(int id,int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_SELECTED,value);
        return db.update(TABLE_CONTACTS,v,KEY_ID+"=?",new String[]{String.valueOf(id)})==1;
    }

    public Boolean updateContact(String old_name,String new_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_NAME,new_name);
        return db.update(TABLE_CONTACTS,v,KEY_NAME+"=?",new String[]{String.valueOf(old_name)})==1;
    }

}
