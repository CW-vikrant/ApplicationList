package com.applicationlist.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

import com.applicationlist.pojo.Contact;
import com.applicationlist.db.DbHelper;
import com.applicationlist.utility.FileHelper;
import com.applicationlist.adapter.ListAdapter;
import com.applicationlist.R;
import com.google.gson.Gson;

public class Home extends AppCompatActivity implements ListAdapter.Item {

    private ListView lv;
    private ListAdapter adapter;
    private DbHelper helper;
    EditText tv;
    public static final int PERMISSION_WRITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,Favorites.class);
                startActivity(intent);
            }
        });

        lv = (ListView) findViewById(R.id.lv);
        tv = (EditText) findViewById(R.id.tv);
        helper = new DbHelper(Home.this);

        if(!getDatabasePath(DbHelper.DATABASE_NAME).exists())
            insertContacts(helper);

        adapter = new ListAdapter(Home.this,helper.getCursor(),false);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return helper.getFilteredContacts(constraint.toString());
            }
        });
        lv.setAdapter(adapter);

        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = adapter.getCursor();
                c.moveToPosition(position);
                Intent intent = new Intent(Home.this,Detail.class);
                intent.putExtra("contact",
                        helper.getContact(c.getInt(c.getColumnIndex(DbHelper.KEY_ID))));
                startActivity(intent);
            }
        });

        addContacts();
        //saveContacts();
        updateContact();

        findViewById(R.id.open_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,FileData.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.save_file_dir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContactsToDirectory();
            }
        });
        //bindService();
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Home.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Home.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(Home.this, new String[]{permission}, requestCode);
            }
        } else {
            saveContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    saveContacts();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void saveContacts() {
        Gson gson = new Gson();
        // write to external location
        FileHelper.writeToExternalStorage(gson.toJson(helper.getAllContacts()),FileHelper.FILE_NAME_EXTERNAL);
        Toast.makeText(this, "file saved", Toast.LENGTH_SHORT).show();
    }

    private void saveContactsToDirectory() {
        Gson gson = new Gson();

        //write to default internal storage
        //FileHelper.writeToFile(gson.toJson(helper.getAllContacts()),Home.this,FileHelper.FILE_NAME);

        //write to default cache directory
        FileHelper.writeToDirectory(getCacheDir(),gson.toJson(helper.getAllContacts()),FileHelper.FILE_NAME_DIRECTORY);
        Toast.makeText(this, "file saved", Toast.LENGTH_SHORT).show();
    }

    public void updateContact(){
        final EditText name_old = (EditText) findViewById(R.id.name_old);
        final EditText name_new = (EditText) findViewById(R.id.name_new);

        Button b = (Button) findViewById(R.id.update);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helper.updateContact(name_old.getText().toString(),
                        name_new.getText().toString()))
                    Toast.makeText(Home.this,"Contact Updated",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Home.this,"Operation Failed",Toast.LENGTH_SHORT).show();

                name_old.setText("");
                name_new.setText("");

                adapter.changeCursor(helper.getFilteredContacts(tv.getText().toString()));
//                adapter.notifyDataSetChanged();
            }
        });
    }

    public void addContacts(){
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText number = (EditText) findViewById(R.id.number);
        final EditText note = (EditText) findViewById(R.id.note);

        Button b = (Button) findViewById(R.id.add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.addContact(name.getText().toString(),
                        Integer.parseInt(number.getText().toString()),
                        note.getText().toString());

                name.setText("");
                name.setText("");
                number.setText("");
                note.setText("");

                adapter.changeCursor(helper.getFilteredContacts(tv.getText().toString()));
                //adapter.notifyDataSetChanged();
            }
        });
    }

    public void insertContacts(DbHelper helper){
        helper.addContact("hello",34455,"contact hello");
        helper.addContact("world",90355,"contact world");
        helper.addContact("abcld",56455,"contact abcd");
        helper.addContact("pqrshe",18855,"contact pqrs");


        helper.addContact("bid",34455,"contact hello");
        helper.addContact("sid",90355,"contact world");
        helper.addContact("rdks",56455,"contact abcd");
        helper.addContact("ddd",18855,"contact pqrs");


        helper.addContact("dedef",34455,"contact hello");
        helper.addContact("rrw",90355,"contact world");
        helper.addContact("ttyl",56455,"contact abcd");
        helper.addContact("aeff",18855,"contact pqrs");
    }

    public void addToFav(View v){

        Cursor c = adapter.getCursor();
        c.moveToPosition(lv.getPositionForView(v));
        helper.addContactToFav(new Contact(c.getString(c.getColumnIndex(DbHelper.KEY_NAME)),
                c.getInt(c.getColumnIndex(DbHelper.KEY_PHONE)), c.getString(c.getColumnIndex(DbHelper.KEY_NOTE)),
                c.getInt(c.getColumnIndex(DbHelper.KEY_SELECTED))));
        helper.updateContactSelectState(c.getInt(c.getColumnIndex(DbHelper.KEY_ID)),1);
        adapter.changeCursor(helper.getFilteredContacts(tv.getText().toString()));

    }

    public void removeFromFav(View v){

        Cursor c = adapter.getCursor();
        c.moveToPosition(lv.getPositionForView(v));
        helper.deleteContact(c.getString(c.getColumnIndex(DbHelper.KEY_PHONE)));
        helper.updateContactSelectState(c.getInt(c.getColumnIndex(DbHelper.KEY_ID)),0);
        adapter.changeCursor(helper.getFilteredContacts(tv.getText().toString()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_retrofit:
                // About option clicked.
                Intent intent = new Intent(Home.this,SampleRetroFit.class);
                startActivity(intent);
                return true;
            case R.id.action_service:
                // About option clicked.
                Intent i = new Intent(Home.this,BoundServiceActivity.class);
                startActivity(i);
                return true;
            case R.id.action_save:
                // About option clicked.
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,PERMISSION_WRITE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
