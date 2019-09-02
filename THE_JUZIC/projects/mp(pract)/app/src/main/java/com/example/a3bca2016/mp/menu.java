package com.example.a3bca2016.mp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by S on 04-10-2016.
 */

public class menu extends Activity{
    databasehelper mydb;
    ListView newlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb=new databasehelper(this);
        setContentView(R.layout.createdpl);
        newlist = (ListView) findViewById(R.id.listView3);
        String[] values=mydb.loadnames();

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.txtcreatedpl,R.id.textView, values);


        // Assign adapter to ListView
        newlist.setAdapter(adapter);
        // ListView Item Click Listener
        newlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                // ListView Clicked item value
                String  itemValue    = (String) newlist.getItemAtPosition(position);

                    mydb.updateplaylist(itemValue);

                // Show Alert
                Toast.makeText(getApplicationContext(),"Playlist Updated", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        menu.this.finish();
                    }
                }, 3000);

            }

        });



}}

