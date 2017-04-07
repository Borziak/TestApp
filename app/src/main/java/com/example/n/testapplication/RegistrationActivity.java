package com.example.n.testapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    public String LOG_TAG = "AppLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText login = (EditText)findViewById(R.id.reg_login);
        final EditText mail = (EditText)findViewById(R.id.reg_mail);
        final EditText password = (EditText)findViewById(R.id.reg_pass);
        final EditText confirmPassword = (EditText)findViewById(R.id.reg_pass_confirm);
        Spinner spinner = (Spinner)findViewById(R.id.postalcode);
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
        String code = spinner.getSelectedItem().toString();


        Button register = (Button) findViewById(R.id.registration_confirm);
        View.OnClickListener registrationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regLogin = login.getText().toString();
                String regMail = mail.getText().toString();
                String regPass = password.getText().toString();
                String regPassConfirm = confirmPassword.getText().toString();
                if (!regPass.equals(regPassConfirm)){
                    password.setText("");
                    confirmPassword.setText("");
                    password.setHighlightColor(14423100);
                    confirmPassword.setHighlightColor(14423100);
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                } else {
                    if (registration(regLogin, regMail, regPass)){
                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        register.setOnClickListener(registrationListener);
    }

    public boolean registration (String login, String email, String password){
        boolean result = false;
        String[] tableColumn = new String[]{"login"};
        String[] loginForQuery = new String[]{login};
        String[] emailForQuery = new String[]{email};
        String[] passwordForQuery = new String[]{password};
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase base = helper.getReadableDatabase();
        Cursor cursor = base.query("Users", tableColumn, "login=?", loginForQuery, null, null, null);
        if (cursor.moveToFirst()){
            Log.d(LOG_TAG, cursor.getString(cursor.getColumnIndex("login")));
            final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("LOGIN ALREADY REGISTERED");
            final AlertDialog dialog = builder.show();
            builder.setPositiveButton("Register", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(login);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            Log.d(LOG_TAG, "NotFound");
            newUser(login, email, password);
            Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        return result;
    }
    public void newUser(String login, String email, String password){
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase base = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("login", login);
        values.put("email", email);
        values.put("password", password);
        base.insert("Users", null, values);
        Log.d(LOG_TAG, "New user inserted "+login+"; "+email+"; "+password);
    }
}
