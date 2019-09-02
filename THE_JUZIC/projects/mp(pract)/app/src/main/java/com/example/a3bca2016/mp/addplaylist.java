package com.example.a3bca2016.mp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.a3bca2016.mp.R.id.btnplstadd;
import static com.example.a3bca2016.mp.R.id.plstname;

public class addplaylist extends AppCompatActivity {

    public databasehelper mydb;
    private EditText name;
    private Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplaylist);
        mydb= new databasehelper(this);
        name = (EditText) findViewById(R.id.plstname);
        add = (Button) findViewById(R.id.btnplstadd);
        addlist();
    }

    public void addlist() {
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mydb.addlist(name.getText().toString())) {
                            Toast.makeText(addplaylist.this, "Playlist added", Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    addplaylist.this.finish();
                                }
                            }, 3000);
                        }
                        else{
                            Toast.makeText(addplaylist.this, "Playlist not added", Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    addplaylist.this.finish();
                                }
                            }, 3000);


                        }


                    }
                }
        );
    }
}