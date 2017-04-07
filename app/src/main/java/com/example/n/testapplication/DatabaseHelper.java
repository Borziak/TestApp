package com.example.n.testapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by N on 04.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        // конструктор суперкласса
        super(context, "TestBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MyLogs", "--- onCreate database ---");
        db.execSQL("create table Users ("
                +"login text,"
                + "password text,"
                + "email text);");
        Log.d("MyLogs", "Created database");
        db.execSQL("create table Painters ("
                +"name text,"
                +"second_name text,"
                +"birthdate text,"
                +"deathdate text,"
                +"biography text);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
