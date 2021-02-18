



package com.example.practice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.database.Cursor;
import android.os.Bundle;
import android.database.sqlite.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private final String dbName = "testdb";


    String phoneNumber = "";
    String password = "";


    SQLiteDatabase sqLiteDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        try {

            sqLiteDatabase = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

            sqLiteDatabase.execSQL("CREATE TABLE USER (PHONE VARCHAR(20) PRIMARY KEY, PASSWORD VARCHAR(20))");


        } catch (Exception e) {
            e.printStackTrace();
        }

        Button button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText phoneNumberId = findViewById(R.id.phoneNumber);
                phoneNumber = phoneNumberId.getText().toString();

                EditText passwordId = findViewById(R.id.password);
                password = passwordId.getText().toString();


                String insertUser = "INSERT INTO USER VALUES('" + phoneNumber + "', '" + password + "')";

                try {
                    sqLiteDatabase.execSQL(insertUser);

                    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USER", null);

                    while (cursor.moveToNext()) {
                        String phone = cursor.getString(0);
                        String password = cursor.getString(1);

                        Log.d("test", "phone = " + phone + " password = " + password);
                    }

                } catch (Exception e) {
                    Log.d("test", "전화번호 중복");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("전화번호 중복!").setMessage("전화번호 중복입니다!");

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sqLiteDatabase.execSQL("DROP TABLE USER");
    }
}
