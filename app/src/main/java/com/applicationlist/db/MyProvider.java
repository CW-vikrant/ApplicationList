package com.applicationlist.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MyProvider extends ContentProvider {

    private DbHelper dbHelper;
    private static final String AUTHORITY =
            "com.applicationlist.db.MyProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DbHelper.TABLE_CONTACTS);
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static final int uriCode = 1;

    static {
        uriMatcher.addURI(AUTHORITY, DbHelper.TABLE_CONTACTS, uriCode);
        uriMatcher.addURI(AUTHORITY, DbHelper.TABLE_CONTACTS + "/*", uriCode);
    }

    public MyProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = db.delete(dbHelper.TABLE_CONTACTS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int uriType = uriMatcher.match(uri);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = 0;

        switch (uriType) {
            case uriCode:
                id = db.insert(DbHelper.TABLE_CONTACTS, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(DbHelper.TABLE_CONTACTS + "/" + id);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DbHelper.TABLE_CONTACTS);

        int uriType = uriMatcher.match(uri);

        switch (uriType) {
            case uriCode:
                //builder.appendWhere(DbHelper.KEY_ID+"="+uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown uri "+uri);
        }

        Cursor cursor = builder.query(dbHelper.getReadableDatabase(),projection,
                selection,selectionArgs,null,null,sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode:
                return "vnd.android.cursor.dir/"+DbHelper.TABLE_CONTACTS;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = db.update(dbHelper.TABLE_CONTACTS, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
