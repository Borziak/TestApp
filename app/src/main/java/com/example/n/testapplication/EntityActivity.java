package com.example.n.testapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EntityActivity extends AppCompatActivity {
    SharedPreferences spref;
    SharedPreferences mSettings;
    public static final String ADD_SETTINGS = "EntitySettings";
    public String LOG_TAG = "AppLogs";
    private String birthDate;
    private String deathDate;
    private String descriptionText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = getSharedPreferences(ADD_SETTINGS, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);
        TextView title = (TextView)findViewById(R.id.title);
        TextView birth = (TextView)findViewById(R.id.birthEntity);
        TextView death = (TextView)findViewById(R.id.deathEntity);
        TextView description = (TextView)findViewById(R.id.descriptionEntity);
        FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.edit);
        final String name = loadPref("name");
        fetchFromDB(name);
        title.setText(name);
        birth.setText(birthDate);
        death.setText(deathDate);
        description.setText(descriptionText);
        View.OnClickListener editListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreference("true", "edit");
                savePreference(name, "name");
                Intent edit = new Intent(getApplicationContext(), EditActivity.class);
                startActivity(edit);
            }
        };
        edit.setOnClickListener(editListener);
    }

    public String loadPref(String from) {//метод загружающий стрингу в преференсы
        spref = getPreferences(MODE_PRIVATE);
        String result = mSettings.getString(from, "Name Name");
        return result;
    }
    public void savePreference(String toSave, String name) {  //метод сохраняющий стрингу в преференсы
        spref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(name, toSave);
        Log.d(LOG_TAG, "Saved: " + toSave);
        editor.apply();
    }

    public void fetchFromDB(String name){
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase base = helper.getWritableDatabase();
        Cursor cursor = base.query("Painters", new String[]{"birthdate", "deathdate", "biography"}, "second_name=?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()){
            birthDate = cursor.getString(cursor.getColumnIndex("birthdate"));
            deathDate = cursor.getString(cursor.getColumnIndex("deathdate"));
            descriptionText = cursor.getString(cursor.getColumnIndex("biography"));
            Log.d(LOG_TAG,name+": "+birthDate+" - "+deathDate+"; ");
        } else {
            Log.d(LOG_TAG, "Unable to fetch");
        }
    }
}
