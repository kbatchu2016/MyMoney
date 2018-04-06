package com.myapps.kiran.mymoney;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tVTotBalnce;
    TextView etTotBalance;
    CheckBox chkIncomeValue;
    CheckBox chkExpenseValue;
    EditText etTransAmount;
    ImageView ivIconAddDate;
    ImageView ivIconAddTransaction;
    EditText editDate;
    Spinner spAmtType;

    Context context = this;
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "yyyy-mm-dd";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);

    private int mYear, mMonth, mDay, mHour, mMinute;

    // json file name
    String accDetailsJsonDataFileName ="accountSummaryInfo.json";

    private DBHelper dbHelper;

    private SQLiteDatabase mDatabase ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

/// Create table in Database ///
        dbHelper = new DBHelper(getApplicationContext());
        mDatabase = dbHelper.getReadableDatabase();

        tVTotBalnce = (TextView) findViewById(R.id.tvTotalBalance);
        etTotBalance = (TextView) findViewById(R.id.etTotalBalance);
        chkIncomeValue = (CheckBox) findViewById(R.id.chkIncome);
        chkIncomeValue.setChecked(false);
        chkExpenseValue = (CheckBox) findViewById(R.id.ChkExpense);
        chkExpenseValue.setChecked(true);
        ivIconAddDate = (ImageView) findViewById(R.id.ivDatePopup);
        editDate = (EditText) findViewById(R.id.etDate);
        ivIconAddTransaction = (ImageView) findViewById(R.id.ivAddTransaction);
        spAmtType = (Spinner) findViewById(R.id.spAmtSourceType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.amountType_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spAmtType.setAdapter(adapter);

        etTransAmount = (EditText) findViewById(R.id.etTransationAmount);
        etTransAmount.setText("0.00");

        // On  Income checkbox click
        chkIncomeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkIncomeValue.isChecked()) {
                    chkExpenseValue.setChecked(false);
                } else {
                    chkExpenseValue.setChecked(true);
                    chkIncomeValue.setChecked(false);
                }
            }
        });
        // On  expense checkbox click
        chkExpenseValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkExpenseValue.isChecked()) {
                    chkIncomeValue.setChecked(false);
                } else {
                    chkExpenseValue.setChecked(false);
                    chkIncomeValue.setChecked(true);
                }
            }
        });


        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String stringDate = formatter.format(new Date());
        editDate.setText(stringDate.toString());
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick1DateSelect(v);
            }
        });


        ivIconAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickivAddTransaction(v);
            }
        });


        //TODO Category like movie, others, home  &    //TODO description of the trassaction
        //https://android--code.blogspot.in/2015/08/android-spinner-add-item-dynamically.html


        //TODO MONTHLYWise report
        //TODO PayBills / Category


    } //oncreate

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
        etTotBalance.setText("RS: "+Integer.toString(GetTotalBalance()));
       etTransAmount.setText("");

        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String stringDate = formatter.format(new Date());
        editDate.setText(stringDate.toString());
    }

    public void onClick1DateSelect(View v) {

        if (v == editDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
             mMonth = c.get(Calendar.MONTH);
             mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            editDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                           // editDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }



    // On click on the Save Transaction button
    public void OnClickivAddTransaction(View view) {
        Context context = getApplicationContext();
        String transactionamt = etTransAmount.getText().toString();
        CharSequence text = "Expense Amount added RS: " + transactionamt;
        //Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        Log.i("MainActivity","Move to next");

        boolean chkExpValue=  chkExpenseValue.isChecked();
        boolean chkIncmValue= chkIncomeValue.isChecked();
        String transType=  "";
        if(chkExpValue  ){ transType="expense"; }
        else  if ( chkIncmValue){ transType="income";}
        String transDate = editDate.getText().toString();
        String transAmount =  etTransAmount.getText().toString();
        String amountSourceType= (String) spAmtType.getSelectedItem().toString();
        // insert the data in DB table
        insertData(transType,amountSourceType,transDate,transAmount);
        Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
        startActivity(intent);

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-mm-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void insertData(String transType,String amountSourceType,String  transDate,String  transAmount)
    {
         Log.i("insertData", "insertData: started");

        DBHelper dbHelper =new DBHelper(getApplicationContext());
        // Date dNow = new Timestamp( transDate.toString()) ;
        String table_name =  dbHelper.getTable_name();  // "accountSummary";

        ContentValues values = new ContentValues();

        // values.put("id",1); // auto increment
        values.put(dbHelper.getColumn_amountsourcetype(),amountSourceType);
        values.put(dbHelper.getColumn_transType(),transType);
        values.put(dbHelper.getColumn_date(),transDate);
        values.put(dbHelper.getColumn_amount(),transAmount);
        Log.i("insertData", "insertData:values started");
        long rowId = 0 ;
        if(mDatabase!=null)        {
            rowId = mDatabase.insert(table_name, null, values);
            if (rowId != -1)            {
                Toast.makeText(MainActivity.this, "Inserted Successfully !!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Error inserting !!!", Toast.LENGTH_SHORT).show();
            }
        }
        else

        {
            Toast.makeText(MainActivity.this, "Database is null!!!", Toast.LENGTH_SHORT).show();
        }


    }

    private int GetTotalBalance()
    {
        int totBalance=0,totExp=0,totInc=0;
        Log.i("insertData", "insertData: started");

        DBHelper dbHelper =new DBHelper(getApplicationContext());
        // Date dNow = new Timestamp( transDate.toString()) ;
        String table_name =  dbHelper.getTable_name();  // "accountSummary";
        Cursor cursor = mDatabase.rawQuery("select SUM(amount) as 'totExp' from "+ dbHelper.getTable_name() +"  Where transactiontype ='expense'  ;", null);
        System.out.println("GetTotalBalance - Totatl Expense:"+ cursor.getCount());
        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    // Get version from Cursor
                    totExp = cursor.getInt(cursor.getColumnIndex("totExp"));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        Cursor cursor2 = mDatabase.rawQuery("select SUM(amount) as 'totInc' from "+ dbHelper.getTable_name() +"  Where transactiontype ='income'  ;", null);
        System.out.println("GetTotalBalance - Totatl Income"+ cursor2.getCount());
        if (cursor2 != null) {
            // move cursor to first row
            if (cursor2.moveToFirst()) {
                do {
                    // Get version from Cursor
                    totInc = cursor2.getInt(cursor2.getColumnIndex("totInc"));
                } while (cursor2.moveToNext());
            }
            cursor2.close();
        }
        totBalance= totInc-totExp;
        return totBalance;
    }

    private void updateDate() {
        editDate.setText(sdf.format(myCalendar.getTime()));
    }


}/////////////

