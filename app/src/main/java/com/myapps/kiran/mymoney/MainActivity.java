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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    Spinner spCategoryType;
    EditText etDesc;
    ImageButton ibSummaryPage;
    ImageButton ibSettingsPage ,ibExportCSV;

    Context context = this;
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "yyyy-MM-dd";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
    private int mYear, mMonth, mDay, mHour, mMinute;

    // spinner variables
    List<String> mSourceTypesList,mCategorysList;
    ArrayAdapter<String> adapterStype,adapterCat;
    // db related
    private DBHelper dbHelper;
    private SQLiteDatabase mDatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// Create table in Database ///
        dbHelper = new DBHelper(getApplicationContext());
        mDatabase = dbHelper.getReadableDatabase();
        // initialize the  fields
        tVTotBalnce = (TextView) findViewById(R.id.tvTotalBalance);
        etTotBalance = (TextView) findViewById(R.id.etTotalBalance);
        chkIncomeValue = (CheckBox) findViewById(R.id.chkIncome);
        chkIncomeValue.setChecked(false);
        chkExpenseValue = (CheckBox) findViewById(R.id.ChkExpense);
        chkExpenseValue.setChecked(true);
        ivIconAddDate = (ImageView) findViewById(R.id.ivDatePopup);
        editDate = (EditText) findViewById(R.id.etDate);
        ivIconAddTransaction = (ImageView) findViewById(R.id.ivAddTransaction);
        //////////////////       AmtSourceType - Spinner           //////////////////
        spAmtType = (Spinner) findViewById(R.id.spAmtSourceType);
        String[] mSourceTypeArray  = (String[]) getResources().getStringArray(R.array.amountType_array);
        mSourceTypesList = new ArrayList<>(Arrays.asList(mSourceTypeArray));
        adapterStype = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                mSourceTypesList);
        // Specify the layout to use when the list of choices appears
        adapterStype.setDropDownViewResource(android.R.layout.simple_spinner_item);
       // Apply the adapter to the spinner
        spAmtType.setAdapter(adapterStype);
        LoadSourceType4Db();
        adapterStype.notifyDataSetChanged();

        //////////////////       Category - Spinner           //////////////////
        spCategoryType = (Spinner) findViewById(R.id.spCategory);
        String[] mCatArray  = (String[]) getResources().getStringArray(R.array.categorytType_array);
        mCategorysList = new ArrayList<>(Arrays.asList(mCatArray));
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterCat = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                mCategorysList);
        // Specify the layout to use when the list of choices appears
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spCategoryType.setAdapter(adapterCat);
        LoadCategory4Db();
        adapterCat.notifyDataSetChanged();
        // description filed with Cap sentences
        etDesc = (EditText) findViewById(R.id.etDescription);
        etDesc.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        etTransAmount = (EditText) findViewById(R.id.etTransationAmount);
        etTransAmount.setText("0.00");

        // to close / hide the softkeyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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
        // to get today dat default
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = formatter.format(new Date());
        editDate.setText(stringDate.toString());
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick1DateSelect(v);
            }
        });

        // Add new transaction details
        ivIconAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickivAddTransaction(v);
            }
        });

        ibSummaryPage = (ImageButton) findViewById(R.id.ibsummary);
        ibSummaryPage.setOnClickListener  (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
                startActivity(intent);
                Toast.makeText(context.getApplicationContext(), "Natigated to the Summary Page !", Toast.LENGTH_SHORT).show();
            }
        });

        ibSettingsPage = (ImageButton) findViewById(R.id.ibsettings);
        ibSettingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                Toast.makeText(context.getApplicationContext(), "Natigated to the Settings Page !", Toast.LENGTH_SHORT).show();
            }
        });

        // export the data to aCSV file from DB SQLite
        ibExportCSV = (ImageButton) findViewById(R.id.ibexport);
        ibExportCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean exportFileStaus = dbHelper.exportDB2CSVFile();
                if (exportFileStaus) {
                    Toast.makeText(context.getApplicationContext(), "Exported To  Download Folder , FileName : ' MyMoney.csv ' !!", Toast.LENGTH_LONG).show();
                }
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
        adapterStype.notifyDataSetChanged();
        adapterCat.notifyDataSetChanged();

        etTotBalance.setText("RS: "+Integer.toString(GetTotalBalance()));
        etTransAmount.setText("");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = formatter.format(new Date());
        editDate.setText(stringDate.toString());
        etDesc.setText("");

           }

    public void onClick1DateSelect(View v) {
        try {
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
        catch (Exception e){}

    }

    // On click on the Save Transaction button
    public void OnClickivAddTransaction(View view) {
        try {
            Context context = getApplicationContext();
            String transactionamt = etTransAmount.getText().toString();
            CharSequence text = "Expense Amount added RS: " + transactionamt;
            //Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            Log.i("MainActivity", "Move to next");
            boolean chkExpValue = chkExpenseValue.isChecked();
            boolean chkIncmValue = chkIncomeValue.isChecked();
            String transType = "";
            if (chkExpValue) {
                transType = "expense";
            } else if (chkIncmValue) {
                transType = "income";
            }
            String transDate = editDate.getText().toString();
            String transAmount = etTransAmount.getText().toString();
            String amountSourceType = (String) spAmtType.getSelectedItem().toString();
            String categoryName = spCategoryType.getSelectedItem().toString();
            String desc = etDesc.getText().toString();
            // check if the transaction date , amount & type are not null
            if (!transDate.equals("") && !transAmount.equals("") && !amountSourceType.equals("") ) {
                // insert the data in DB table
                insertTransactionData(transType, amountSourceType, transDate, transAmount, categoryName, desc);
                Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Added the Amount "+  transAmount +" with Details !", Toast.LENGTH_LONG).show();
            }
            else
            {  Toast.makeText(MainActivity.this, "Enter the SourceType ,Date,Amount Details Of the Transaction !", Toast.LENGTH_LONG).show();}

        }catch(Exception e){e.printStackTrace();}

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void insertTransactionData(String transType,String amountSourceType,String  transDate,String  transAmount,String categoryName,String transdesc)
    {
         Log.i("insertData", "insertData: started");
        try {
            DBHelper dbHelper = new DBHelper(getApplicationContext());
            // Date dNow = new Timestamp( transDate.toString()) ;
            String table_name = dbHelper.getTable_name();  // "accountSummary";

            ContentValues values = new ContentValues();

            // values.put("id",1); // auto increment
            values.put(dbHelper.getColumn_amountsourcetype(), amountSourceType);
            values.put(dbHelper.getColumn_transType(), transType);
            values.put(dbHelper.getColumn_date(), transDate);
            values.put(dbHelper.getColumn_amount(), transAmount);
            values.put(dbHelper.getColumn_transCategory(), categoryName);
            values.put(dbHelper.getColumn_transDescription(), transdesc);

            Log.i("insertData", "insertData:values started");
            long rowId = 0;
            if (mDatabase != null) {
                rowId = mDatabase.insert(table_name, null, values);
                if (rowId != -1) {
                    Toast.makeText(MainActivity.this, "Inserted Successfully !!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error inserting !!!", Toast.LENGTH_SHORT).show();
                }
            } else

            {
                Toast.makeText(MainActivity.this, "Database is null!!!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){e.printStackTrace();}


    }

    private int GetTotalBalance()
    {
        int totBalance=0,totExp=0,totInc=0;
        Log.i("insertData", "insertData: started");
            try {
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                // Date dNow = new Timestamp( transDate.toString()) ;
                String table_name = dbHelper.getTable_name();  // "accountSummary";
                Cursor cursor = mDatabase.rawQuery("select SUM(amount) as 'totExp' from " + dbHelper.getTable_name() + "  Where transactiontype ='expense'  ;", null);
                System.out.println("GetTotalBalance - Totatl Expense:" + cursor.getCount());
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
                // get the totoal income balance
                Cursor cursor2 = mDatabase.rawQuery("select SUM(amount) as 'totInc' from " + dbHelper.getTable_name() + "  Where transactiontype ='income'  ;", null);
                System.out.println("GetTotalBalance - Totatl Income" + cursor2.getCount());
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
                totBalance = totInc - totExp;
            }
            catch (Exception e){e.printStackTrace();}
        return totBalance;
    }

    private void updateDate() {
        editDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void LoadSourceType4Db()
    {
        String _amountsourcetype;
        try {
            Cursor cursor = mDatabase.rawQuery("select  DISTINCT "+dbHelper.getStcolumn_sourcetype() +" from "+ dbHelper.getTable_SoucrTypename() +"   ;", null);
            System.out.println("MainActivity.onClick:"+ cursor.getCount());
            if (cursor != null) {
                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {
                        // Get version from Cursor
                         _amountsourcetype = cursor.getString(cursor.getColumnIndex(dbHelper.getStcolumn_sourcetype().toString()));
                        mSourceTypesList.add(_amountsourcetype.toString());
                        // move to next row
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void LoadCategory4Db()
    {
        String _categoryName;
        try {
            Cursor cursor = mDatabase.rawQuery("select  DISTINCT "+dbHelper.getCatcolumn_Category() +" from "+ dbHelper.getTable_Categoryname() +"   ;", null);
            System.out.println("MainActivity.onClick:"+ cursor.getCount());
            if (cursor != null) {
                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {
                        // Get version from Cursor
                        _categoryName = cursor.getString(cursor.getColumnIndex(dbHelper.getCatcolumn_Category().toString()));
                        mCategorysList.add(_categoryName.toString());
                        // move to next row
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}/////////////

