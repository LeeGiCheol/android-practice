package com.example.listview;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.listview.model.AddressInfo;
import com.example.listview.model.DBHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this, "addressDB", null, 1);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        dbHelper.onUpgrade(sqLiteDatabase, 1, 1);
        dbHelper.onCreate(sqLiteDatabase);

        setUpCreateAddress(sqLiteDatabase, 3);

        showListView(sqLiteDatabase);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddressInsertActivity.class), 1);
            }
        });

    }

    private void showListView(SQLiteDatabase sqLiteDatabase) {
        String sql = "select * from address;";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        List<String> phoneNumber = new ArrayList<>();

        while (cursor.moveToNext()) {
            phoneNumber.add(cursor.getString(cursor.getColumnIndex("phoneNumber")));
        }

        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, phoneNumber);

        listView = findViewById(R.id.listView);
        listView.setAdapter(listAdapter);

        if (listView.getFooterViewsCount() < 1) {
            View footer = getLayoutInflater().inflate(R.layout.listview_footer, null, false);
            listView.addFooterView(footer);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != 1) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String newPhoneNumber = data.getExtras().getString("newPhoneNumber");

        Toast.makeText(MainActivity.this, newPhoneNumber + " 전화번호가 등록되었습니다.", Toast.LENGTH_SHORT).show();


        insertAddress(sqLiteDatabase, newPhoneNumber);

        showListView(sqLiteDatabase);

    }

    private void insertAddress(SQLiteDatabase sqLiteDatabase, String phoneNumber) {
        ContentValues addressInfo = new ContentValues();

        addressInfo.put("phoneNumber", phoneNumber);

        sqLiteDatabase.insert("address", null, addressInfo);
    }

    private void setUpCreateAddress(SQLiteDatabase sqLiteDatabase, int idx) {
        ContentValues addressInfo = new ContentValues();

        for (int i = 0; i < idx; i++) {
            addressInfo.put("phoneNumber", "01076587523");

            sqLiteDatabase.insert("address", null, addressInfo);
        }
    }


}