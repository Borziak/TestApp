package com.example.n.testapplication;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends ListActivity implements AdapterView.OnItemLongClickListener {
    public static final String ADD_SETTINGS = "EntitySettings";
    public final String entityName = "Entity";
    private ArrayAdapter<String> adapter;
    public String LOG_TAG = "AppLogs";
    SharedPreferences spref;
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FloatingActionButton add = (FloatingActionButton)findViewById(R.id.add_entity);
        mSettings = getSharedPreferences(ADD_SETTINGS, Context.MODE_PRIVATE);
        ArrayList<String> names = fetchFromDB();
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        setListAdapter(adapter);
        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreference("false", "edit");
                Intent addIntent = new Intent(getApplicationContext(), EditActivity.class);
                startActivity(addIntent);
            }
        };
        add.setOnClickListener(addListener);
        getListView().setOnItemLongClickListener(this);
    }

    private ArrayList<String> fetchFromDB(){
        ArrayList<String> result = new ArrayList<>();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase base = helper.getReadableDatabase();
        Cursor cursor = base.query("Painters", new String[]{"second_name"}, null, null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(cursor.getColumnIndex("second_name")));
            } while (cursor.moveToNext());
        }
        return result;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        savePreference(l.getItemAtPosition(position).toString(), "name");
        Intent entity = new Intent(getApplicationContext(), EntityActivity.class);
        startActivity(entity);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final String selectedItem = parent.getItemAtPosition(position).toString();
        View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.remove(selectedItem);
                adapter.notifyDataSetChanged();
                deleteFromDB(selectedItem);
                Toast.makeText(getApplicationContext(),
                        selectedItem + " удалён.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        Snackbar mSnackbar = Snackbar.make(view, "Confirm delete", Snackbar.LENGTH_INDEFINITE)
                .setAction("Yes", snackbarOnClickListener).setDuration(3000);
        mSnackbar.show();
        return true;
    }
    private void deleteFromDB(String name){
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase base = helper.getWritableDatabase();
        int i = base.delete("Painters", "second_name=?", new String[]{name});
        Log.d(LOG_TAG, "Deleted "+name+" from DB");
    }
    public void savePreference(String toSave, String name) {  //метод сохраняющий стрингу в преференсы
        spref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(name, toSave);
        Log.d(LOG_TAG, "Saved: " + toSave);
        editor.apply();
    }
}
