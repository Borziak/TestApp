package com.example.n.testapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity  {
    public String glob_login = "Korvin";
    public String glob_password = "123456";
    public String LOG_TAG = "AppLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText login = (EditText)findViewById(R.id.phoneentry);
        final EditText pass = (EditText)findViewById(R.id.password);
        Button enter = (Button) findViewById(R.id.enter);
        Button register = (Button) findViewById(R.id.registration);
        TextView forgot = (TextView) findViewById(R.id.pass_forgot);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            String postal = "";
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] choosed = getResources().getStringArray(R.array.countryList);
                postal = choosed[position];
                switch (postal){
                    case "Ukraine":
                        login.setHint("+380(12)3456789");
                        break;
                    case "USA":
                        login.setHint("+1 234 567 89");
                        break;
                    case "England":
                        login.setHint("+44 787 123 45 67");
                        break;
                    case "Russia":
                        login.setHint("+7 (123) 456-7890");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String path = getApplicationInfo().dataDir;
        Log.d(LOG_TAG, path);
        View.OnClickListener enterListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginText = login.getText().toString();
                String passwordText = pass.getText().toString();
                authentication(loginText, passwordText);
            }
        };
        View.OnClickListener registerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(registerIntent);
            }
        };
        register.setOnClickListener(registerListener);
        enter.setOnClickListener(enterListner);
    }


    public void authentication(String login, String password){
        boolean result = false;
        String[] tableColumns = new String[]{"login", "password"};
        String[] loginForQuery = new String[]{login};
        String[] passwordForQuery = new String[]{password};
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase base = helper.getReadableDatabase();
        Cursor cursor = base.query("Users", tableColumns, "login=?", loginForQuery, null, null, null);
        if (cursor.moveToFirst()){
            Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex("login")));
            String checkPass = cursor.getString(cursor.getColumnIndex("password"));
            if (password.equals(checkPass)){
                Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex("password")));
                Toast.makeText(getApplicationContext(), "Hello Again", Toast.LENGTH_LONG).show();
                cursor.close();
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            } else {
                EditText passField = (EditText)findViewById(R.id.password);
                passField.setText("");
                passField.setHighlightColor(14423100);
                Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(LOG_TAG, "NotFound");
            final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("YOU HAVE TO REGISTER FIRST");
            final AlertDialog dialog = builder.show();
            builder.setPositiveButton("Register", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                   Intent register = new Intent(getApplicationContext(), RegistrationActivity.class);
                    startActivity(register);
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

}
