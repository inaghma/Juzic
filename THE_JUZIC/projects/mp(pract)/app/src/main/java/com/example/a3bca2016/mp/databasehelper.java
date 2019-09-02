package com.example.a3bca2016.mp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.name;

/**
 * Created by 3bca2016 on 9/22/2016.
 */

public class databasehelper extends SQLiteOpenHelper {
    public static final String db_name = "Jusic.db";

    public static final String tb1_name = "tracks";
    public static final String tb1_c1 = "track_id";
    public static final String tb1_c2 = "name";
    public static final String tb1_c3 = "album_id";
    public static final String tb1_c4 = "artist_id";
    public static final String tb1_c5 = "size";
    public static final String tb1_c6 = "duration";
    public static final String tb1_c7 = "playlist_id";
    public static final String tb1_c8 = "count";
    public static final String tb1_c9 = "last_play_time";

    public static final String tb2_name = "playlist";
    public static final String tb2_c1 = "playlist_id";
    public static final String tb2_c2 = "playlist_name";
    public static final String tb2_c3 = "date_added";

    public static final String tb3_name = "artist";
    public static final String tb3_c1 = "artist_id";
    public static final String tb3_c2 = "artist_name";

    public static final String tb4_name = "album";
    public static final String tb4_c1 = "album_id";
    public static final String tb4_c3 = "artist_id";
    public static final String tb4_c2 = "album_name";


    public databasehelper(Context context) {
        super(context, db_name, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + tb3_name + "(" + tb3_c1 + " integer primary key autoincrement," + tb3_c2 + " text)");
        db.execSQL("create table " + tb2_name + "(" + tb2_c1 + " integer primary key autoincrement," + tb2_c2 + " text," + tb2_c3 + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("create table " + tb4_name + "(" + tb4_c1 + " integer primary key autoincrement," + tb4_c2 + " text," + tb4_c3 + " integer,foreign key(" + tb4_c3 + ") references " + tb3_name + "(" + tb3_c1 + "))");
        db.execSQL("create table " + tb1_name + "(" + tb1_c1 + " integer primary key autoincrement," + tb1_c2 + " text," + tb1_c3 + " integer," + tb1_c4 + " integer," + tb1_c5 + " integer," + tb1_c6 + " integer," + tb1_c7 + " integer default 0," + tb1_c8 + " integer default 0," + tb1_c9 + " text,foreign key (" + tb1_c3 + ") references " + tb4_name + "(" + tb4_c1 + "),foreign key (" + tb1_c4 + ") references " + tb3_name + "(" + tb3_c1 + "),foreign key (" + tb1_c7 + ") references " + tb2_name + "(" + tb2_c1 + "))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + tb3_name);
        db.execSQL("drop table if exists " + tb2_name);
        db.execSQL("drop table if exists " + tb4_name);
        db.execSQL("drop table if exists " + tb1_name);
        onCreate(db);
    }
    public boolean loaddatabase(String name, float fdur, String albname, String artname, String fsize) {
        long result=1,result1=1,result2=1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues loadartist = new ContentValues();
        loadartist.put(tb3_c2, artname);
        Cursor cursorartist = db.rawQuery("SELECT artist_name FROM artist; ",null);
        boolean artistPresent = false;
        while(cursorartist.moveToNext()){
            String recorded_artistname=cursorartist.getString(cursorartist.getColumnIndex("artist_name"));
            if(recorded_artistname.equals(artname)){
                artistPresent = true;
                break;
            }
        }
        if(!artistPresent) {
            result = db.insert(tb3_name,null,loadartist);
        }

        ContentValues loadalbum = new ContentValues();
        loadalbum.put(tb4_c2, albname);

        Cursor cursoralbum = db.rawQuery("SELECT album_name FROM album; ",null);
        boolean albumPresent = false;
        while(cursoralbum.moveToNext()){
            String recorded_albumname=cursoralbum.getString(cursoralbum.getColumnIndex("album_name"));
            if(recorded_albumname.equals(albname)){
                albumPresent = true;
                break;
            }
        }
        if(!albumPresent) {
            result1 = db.insert(tb4_name, null, loadalbum);
            // long result1 = db.insert(tb4_name, null, loadalbum);
        }

        ContentValues loadtracks = new ContentValues();
        loadtracks.put(tb1_c2, name);
        loadtracks.put(tb1_c5, fsize);
        loadtracks.put(tb1_c6, fdur);
        Cursor cursortracks = db.rawQuery("SELECT name FROM tracks; ",null);
        boolean trackPresent = false;
        while(cursortracks.moveToNext()){
            String recorded_trackname=cursortracks.getString(cursortracks.getColumnIndex("name"));
            if(recorded_trackname.equals(name)){
                trackPresent = true;
                break;
            }
        }
        if(!trackPresent) {
            result2 = db.insert(tb1_name,null,loadtracks);
            update();
        }




        // long result2 = db.insert(tb1_name, null, loadtracks);

        if (result==-1 && result1==-1 && result2==-1)
            return false;
        else
            return true;
    }
    public void update()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update album set artist_id = (select artist_id from artist where artist_id= album.album_id);");
        db.execSQL("update tracks set artist_id = (select artist_id from artist where artist_id= tracks.track_id);");
        db.execSQL("update tracks set album_id = (select album_id from album where album_id= tracks.track_id);");

    }
    public void incrementcount(String tname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorincrement = db.rawQuery("SELECT name from tracks",null);
        boolean incrementpresent = false;
        while(cursorincrement.moveToNext()){
            String songname=cursorincrement.getString(cursorincrement.getColumnIndex("name"));
            if(songname.equals(tname)){
                incrementpresent = true;
                break;
            }
        }
        if(!incrementpresent)
        {
            db.execSQL("update tracks set count= count+1,last_play_time="+getDateTime()+"where name=tname ");
        }

    }
    public String getDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    public boolean addlist(String name) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(tb2_c2, name);
        values.put(tb2_c3, getDateTime());
        // insert the row
        long result = db.insert(tb2_name, null, values);



        if (result == -1)
            return false;
        else return true;
    }




    public String[] loadnames()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select "+tb2_c2+" from "+tb2_name+"",null);
        String[] array = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()){
            String uname = c.getString(c.getColumnIndex(tb2_c2));
            array[i] = uname;
            i++;
        }
        return array;

    }
    public void updateplaylist(String plname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update tracks set playlist_id = (select playlist_id from playlist where playlist_name = '"+plname+"')");
    }
    public void removesngplaylist(String plname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from playlist where playlist_name = "+plname+"");
    }
}

