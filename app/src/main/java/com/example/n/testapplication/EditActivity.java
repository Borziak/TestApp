package com.example.n.testapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import de.mxapplications.openfiledialog.OpenFileDialog;

public class EditActivity extends AppCompatActivity {
    SharedPreferences spref;
    SharedPreferences mSettings;
    public static final String ADD_SETTINGS = "EntitySettings";
    public String LOG_TAG = "AppLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = getSharedPreferences(ADD_SETTINGS, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        String edit = loadPref("edit");
        String namePref = loadPref("name");
        Button add = (Button)findViewById(R.id.add);
        final EditText name = (EditText)findViewById(R.id.name);
        if (!namePref.equals("false")){
            name.setText(namePref);
        }
        final EditText birth = (EditText)findViewById(R.id.birthEntity);
        final EditText death = (EditText)findViewById(R.id.death);
        final EditText description = (EditText)findViewById(R.id.descriptionEntity);
        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString();
                String birthText = birth.getText().toString();
                String deathText = death.getText().toString();
                String descriptionText = description.getText().toString();
                pushToDB(nameText, birthText, deathText, descriptionText);
                Toast.makeText(getApplicationContext(), "Painter added", Toast.LENGTH_LONG).show();
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            }
        };
        View.OnClickListener editListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString();
                String birthText = birth.getText().toString();
                String deathText = death.getText().toString();
                String descriptionText = description.getText().toString();
                editInDB(nameText, birthText, deathText, descriptionText);
                Toast.makeText(getApplicationContext(), "Painter added", Toast.LENGTH_LONG).show();
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            }
        };
        switch (edit){
            case "true":
                add.setText("edit");
                add.setOnClickListener(editListener);
                break;
            case "false":
                add.setText("add");
                add.setOnClickListener(addListener);
                break;
        }

    }

    public String loadPref(String from) {//метод загружающий стрингу в преференсы
        spref = getPreferences(MODE_PRIVATE);
        String result = mSettings.getString(from, "false");
        return result;
    }

    public void savePreference(String toSave, String name) {  //метод сохраняющий стрингу в преференсы
        spref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(name, toSave);
        Log.d(LOG_TAG, "Saved: " + toSave);
        editor.apply();
    }

    public void pushToDB(String name, String birth, String death, String desccription){
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase base = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("second_name", name);
        values.put("birthdate", birth);
        values.put("deathdate", death);
        values.put("biography", desccription);
        base.insert("Painters", null, values);
        Log.d(LOG_TAG, "Inserted painter "+name+": "+birth+" - "+death+" into DB");
    }

    public void editInDB(String name, String birth, String death, String desccription){
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase base = helper.getReadableDatabase();
        if (checkName(name)){
            ContentValues values = new ContentValues();
            values.put("second_name", name);
            values.put("birthdate", birth);
            values.put("deathdate", death);
            values.put("biography", desccription);
            base.update("Painters", values, "second_name=?", new String[]{name});
            Log.d(LOG_TAG, "Updated "+name);
        } else {
            Log.d(LOG_TAG, "NotFound");
            final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("CREATE A NEW ENTITY?");
            final AlertDialog dialog = builder.show();
            builder.setPositiveButton("Register", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    savePreference("false", "edit");
                    Intent newAct = new Intent(getApplicationContext(), EditActivity.class);
                    startActivity(newAct);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

    }
    public boolean checkName(String name){
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase base = helper.getReadableDatabase();
        Cursor cursor = base.query("Painters", new String[]{"second_name"}, "second_name=?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()){
            return true;
        } else {
            return false;
        }
    }
}
