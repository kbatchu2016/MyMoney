//summary page in the application - recycle view
// detailed transactions

package com.myapps.kiran.mymoney;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

public class TransactionActivity extends AppCompatActivity {

    // ArrayList for sourcetype,TransType,Amount,Date
    ArrayList<String> _arrTransID = new ArrayList<>();
    ArrayList<String> _arrAmtSourceType = new ArrayList<>();
    ArrayList<String> _arrTransType = new ArrayList<>();
    ArrayList<String> _arrAmount = new ArrayList<>();
    ArrayList<String> _arrTransDate = new ArrayList<>();
    ArrayList<String> _arrCategory = new ArrayList<>();
    ArrayList<String> _arrDesc = new ArrayList<>();

    RecyclerView recyclerView;
    private DBHelper dbHelper;
    private SQLiteDatabase mDatabase;
    CustomAdapter customAdapter;
    EditText editTextSearch;
    Spinner spmonthyear,spFilterType;
    // spinner variables
    List<String> mMonthYearList;
    ArrayAdapter<String> adapterMonthYear;
    LinearLayout rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        rv = (LinearLayout) findViewById(R.id.lrlist) ;
        // get the reference of RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        editTextSearch = (EditText) findViewById(R.id.mSearch);
        editTextSearch.clearFocus();
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.d("in transcationActivy", "onCreate: layoutmanage done");

        FloatingActionButton backtoHomePage = (FloatingActionButton) findViewById(R.id.tbacktoHomePage);
        backtoHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //////////////////       AmtSourceType - Spinner           //////////////////

        spmonthyear = (Spinner) findViewById(R.id.spmonthyear);
        spFilterType= (Spinner) findViewById(R.id.spFilterType);

        String[] mMMYYArray = (String[]) getResources().getStringArray(R.array.MonthYear_array);
        mMonthYearList = new ArrayList<>(Arrays.asList(mMMYYArray));
        adapterMonthYear = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                mMonthYearList);
        // Specify the layout to use when the list of choices appears
        adapterMonthYear.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spmonthyear.setAdapter(adapterMonthYear);
        LoadMonthYear4Db();
        adapterMonthYear.notifyDataSetChanged();

        spmonthyear.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        //showToast("Spinner2: position=" + position + " id=" + id);
                        //if(position>0)
                        editTextSearch.setText("");
                       displayRecyclerViewList();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                       // showToast("Spinner2: unselected");
                    }
                });


        spFilterType.setOnItemSelectedListener(       new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> parent, View view, int position, long id) {
                //showToast("Spinner2: position=" + position + " id=" + id);
                //if(position>0)
                 editTextSearch.setText("");
                displayRecyclerViewList();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // showToast("Spinner2: unselected");
            }
        });
        //adding a TextChangedListener
        //to call a method whenever there is some change on the EditText
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

           @Override
           public void afterTextChanged(Editable editable) {
             //  displayRecyclerViewList();
                //after the change calling the method and passing the search input
  /*
                // switch statement
                switch(spFilterType.getSelectedItem().toString().trim())
                {
                    // case statements
                    // values must be of same type of expression
                    case "Source Type" :
                       // filter_arrAmtSourceType(editable.toString());
                        filter_SearchArray(editable.toString(),_arrAmtSourceType,"Source Type");
                        break; // break is optional

                    case "Category" :
                        filter_SearchArray(editable.toString(),_arrCategory,"Category");
                        break; // break is optional
                    case "Transaction Type" :
                        //filter_arrTransType(editable.toString());
                        filter_SearchArray(editable.toString(),_arrTransType,"Transaction Type");
                        break; // break is optional
                    case "Description" :
                        filter_SearchArray(editable.toString(),_arrDesc,"Description");

                        break; // break is optional

                    // No break is needed in the default case.
                    default :
                        // Statements
                } */

            }
        });



    }

    @Override
    protected void onStart() {

        super.onStart();


        displayRecyclerViewList();

    }


    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        customAdapter=null;

    }

private void displayRecyclerViewList()
{
    dbHelper = new DBHelper(getApplicationContext());
    mDatabase = dbHelper.getReadableDatabase();
    Cursor cursor=null;
   // customAdapter=null;


    recyclerView.setAdapter(customAdapter);
    recyclerView.refreshDrawableState();
   // if(customAdapter==null) {
        try {

            String mmyyFilter = (String) spmonthyear.getSelectedItem().toString().trim();
            String ftyFilter = (String) spFilterType.getSelectedItem().toString().trim();
            String searchFilter = (String) editTextSearch.getText().toString().trim();
            String filterType="";

            if (mmyyFilter.contains("All")){
                customAdapter.clear();
                cursor = mDatabase.rawQuery("select * from "+ dbHelper.getTable_name() +"  Order by  dateoftrans  DESC ;", null);
            }
            else if(!searchFilter.equals("") ){
                customAdapter.clear();
                switch (ftyFilter.trim()){

                           // values must be of same type of expression
                case "Source Type" :
                    cursor = mDatabase.rawQuery("Select * from "+ dbHelper.getTable_name() +" WHERE "+  dbHelper.getColumn_amountsourcetype() +"  LIKE '%" + searchFilter +"%' Order by  dateoftrans  DESC ;", null);
                    break; // break is optional

                case "Category" :
                    cursor = mDatabase.rawQuery("Select * from "+ dbHelper.getTable_name() +" WHERE "+  dbHelper.getColumn_transCategory() +"  LIKE '%" + searchFilter +"%' Order by  dateoftrans  DESC ;", null);
                    break; // break is optional
                case "Transaction Type" :
                    //filter_arrTransType(editable.toString());
                    cursor = mDatabase.rawQuery("Select * from "+ dbHelper.getTable_name() +" WHERE "+  dbHelper.getColumn_transType() +"  LIKE '%" + searchFilter +"%' Order by  dateoftrans  DESC ;", null);
                    break; // break is optional
                case "Description" :
                    cursor = mDatabase.rawQuery("Select * from "+ dbHelper.getTable_name() +" WHERE "+  dbHelper.getColumn_transDescription() +"  LIKE ' %" + searchFilter +"%' Order by  dateoftrans  DESC ;", null);

                    break; // break is optional

                // No break is needed in the default case.
                default :
                    // Statements
            }

            }
            else
            {
                customAdapter.clear();
                cursor = mDatabase.rawQuery("Select * from "+ dbHelper.getTable_name() +" WHERE "+  dbHelper.getColumn_monthYear() +"  = '" + mmyyFilter +"' Order by  dateoftrans  DESC ;", null);
            }

            System.out.println("MainActivity.onClick:"+ cursor.getCount());
            if (cursor != null) {
                _arrAmtSourceType.clear();
                _arrTransType.clear();
                _arrTransID.clear();
                _arrAmount.clear();
                _arrTransDate.clear();
                _arrCategory.clear();
                _arrDesc.clear();
                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {
                        // Get version from Cursor
                        int _id = cursor.getInt(cursor.getColumnIndex("id"));
                        String _amountsourcetype = cursor.getString(cursor.getColumnIndex("amountsourcetype"));
                        String _transType = cursor.getString(cursor.getColumnIndex("transactiontype"));
                        String _dateoftrans = cursor.getString(cursor.getColumnIndex("dateoftrans"));
                        int _transAmount =cursor.getInt(cursor.getColumnIndex("amount"));
                        String _transCategory = cursor.getString(cursor.getColumnIndex("transCategory"));
                        String _transDesc = cursor.getString(cursor.getColumnIndex("transdescription"));


                        _arrTransID.add(Integer.toString(_id));
                        _arrAmtSourceType.add(_amountsourcetype);
                        _arrTransType.add(_transType);
                        _arrAmount.add(Integer.toString( _transAmount));
                        _arrTransDate.add(_dateoftrans);
                        _arrCategory.add(_transCategory);
                        _arrDesc.add(_transDesc);

                        // move to next row
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        customAdapter = new CustomAdapter(TransactionActivity.this, _arrTransID, _arrAmtSourceType, _arrTransType, _arrAmount, _arrTransDate, _arrCategory, _arrDesc);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
   //}

}




    private void filter_SearchArray(String text,ArrayList<String> _array,String arrayName) {
        //new array list that will hold the filtered data
        ArrayList<String> arrNames = new ArrayList<>();

        //looping through existing elements
        for (String s : _array) {
            //if the existing elements contains the search input
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                arrNames.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        customAdapter.filterListarrType(arrNames,arrayName);
    }


    private void LoadMonthYear4Db()
    {

        dbHelper = new DBHelper(getApplicationContext());
        mDatabase = dbHelper.getReadableDatabase();

        String _mmyytype;
        try {
            Cursor cursor = mDatabase.rawQuery("select  DISTINCT "+ dbHelper.getColumn_monthYear() +" from "+ dbHelper.getTable_name() +"   ;", null);
            System.out.println("Transaction search .onClick:"+ cursor.getCount());
            if (cursor != null) {
                // move cursor to first row
                if (cursor.moveToFirst()) {
                    do {
                        // Get version from Cursor
                        _mmyytype = cursor.getString(cursor.getColumnIndex(dbHelper.getColumn_monthYear().toString()));
                        mMonthYearList.add(_mmyytype.toString());
                        // move to next row
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


////////////////////////////////////
}