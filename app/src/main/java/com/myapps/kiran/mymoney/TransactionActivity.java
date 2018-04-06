package com.myapps.kiran.mymoney;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    // ArrayList for sourcetype,TransType,Amount,Date
    ArrayList<String> _arrTransID = new ArrayList<>();
    ArrayList<String> _arrAmtSourceType = new ArrayList<>();
    ArrayList<String> _arrTransType = new ArrayList<>();
    ArrayList<String> _arrAmount = new ArrayList<>();
    ArrayList<String> _arrTransDate = new ArrayList<>();

    RecyclerView recyclerView;
    private DBHelper dbHelper;
    private SQLiteDatabase mDatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        // get the reference of RecyclerView
         recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.d("in transcationActivy", "onCreate: layoutmanage done");


    }

    @Override
    protected void onStart() {

        super.onStart();

        try {

            dbHelper = new DBHelper(getApplicationContext());
            mDatabase = dbHelper.getReadableDatabase();

            Cursor cursor = mDatabase.rawQuery("select * from "+ dbHelper.getTable_name() +"   ;", null);
            System.out.println("MainActivity.onClick:"+ cursor.getCount());
            if (cursor != null) {
                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {
                        // Get version from Cursor
                        int _id = cursor.getInt(cursor.getColumnIndex("id"));
                        String _amountsourcetype = cursor.getString(cursor.getColumnIndex("amountsourcetype"));
                        String _transType = cursor.getString(cursor.getColumnIndex("transactiontype"));
                        String _dateoftrans = cursor.getString(cursor.getColumnIndex("dateoftrans"));
                        int _transAmount =cursor.getInt(cursor.getColumnIndex("amount"));

                        _arrTransID.add(Integer.toString(_id));
                        _arrAmtSourceType.add(_amountsourcetype);
                        // fetch mobile number and store it in arraylist
                        _arrTransType.add(_transType);
                        // fetch mobile number and store it in arraylist
                        _arrAmount.add(Integer.toString( _transAmount));
                        // fetch mobile number and store it in arraylist
                        _arrTransDate.add(_dateoftrans);


                        // move to next row
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        CustomAdapter customAdapter;
        customAdapter = new CustomAdapter(TransactionActivity.this, _arrTransID,_arrAmtSourceType, _arrTransType, _arrAmount,_arrTransDate);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

    }


////////////////////////////////////
}