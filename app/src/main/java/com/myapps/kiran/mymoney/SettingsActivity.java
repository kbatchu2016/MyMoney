package com.myapps.kiran.mymoney;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {




    TextView tvAddAmtSouurceType, tcAddCategory;
    EditText etSourceType, etCategory;
    Spinner spSourceType, spCategory;

    ImageButton ibAmtSourceType, ibCategory;
    ImageButton ibRemoveSourceType, ibRemoveCategory;

    Context context;
    private DBHelper dbHelper;

    private SQLiteDatabase mDatabase ;

    List<String> mSourceTypesList,mCategorysList;

    ArrayAdapter<String> adapterStype,adapterCat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dbHelper = new DBHelper(getApplicationContext());
        mDatabase = dbHelper.getReadableDatabase();

        tvAddAmtSouurceType = (TextView) findViewById(R.id.tvAddSourceType);
        tcAddCategory = (TextView) findViewById(R.id.tvAddCategory);

        etSourceType = (EditText) findViewById(R.id.etAddSourceType);
        etCategory = (EditText) findViewById(R.id.etAddCategory);
        etSourceType.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etCategory.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        ibAmtSourceType = (ImageButton) findViewById(R.id.ibAddSourceType);
        ibCategory = (ImageButton) findViewById(R.id.ibCategory);


        ibRemoveSourceType = (ImageButton) findViewById(R.id.ibRemoveSourceType1);
        ibRemoveCategory = (ImageButton) findViewById(R.id.ibRemoveCat1);


        spSourceType = (Spinner) findViewById(R.id.spRemoveSourcetype);

        String[] mSourceTypeArray  = {""};//(String[]) getResources().getStringArray(R.array.amountType_array);
        mSourceTypesList = new ArrayList<>(Arrays.asList(mSourceTypeArray));
        adapterStype = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                mSourceTypesList);
        // Specify the layout to use when the list of choices appears
        adapterStype.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spSourceType.setAdapter(adapterStype);
        LoadSourceType4Db();
        adapterStype.notifyDataSetChanged();


        spCategory =(Spinner) findViewById(R.id.spRemoveCategory);
        String[] mCatArray  = {""};//(String[]) getResources().getStringArray(R.array.categorytType_array);
        mCategorysList = new ArrayList<>(Arrays.asList(mCatArray));
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterCat = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                mCategorysList);
        // Specify the layout to use when the list of choices appears
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spCategory.setAdapter(adapterCat);
        LoadCategory4Db();
        adapterCat.notifyDataSetChanged();

        //////////////////       AmtSourceType - Spinner           //////////////////



        FloatingActionButton  backtoHomePage = (FloatingActionButton) findViewById(R.id.backtoHomePage);
        backtoHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



//////////////////////  sipnner data /////////////////////





    }//

    @Override
    protected void onStart() {
        super.onStart();


        ibAmtSourceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                String amountSourceType =  (String) etSourceType.getText().toString();
                if (!amountSourceType.equals("")){
                     if(addSourceType(amountSourceType)) {
                         Toast toast = Toast.makeText(SettingsActivity.this, amountSourceType+"     -  SourceType Added !!", Toast.LENGTH_LONG);
                         toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                         toast.show();
                         //reload the SourceTypes
                         mSourceTypesList.removeAll(mSourceTypesList);
                         LoadSourceType4Db();

                     }
                }
                else {
                    Toast toast=Toast.makeText(SettingsActivity.this, " Enter SourceType to Add!!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });

        ibCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                String categoryName =   (String) etCategory.getText().toString();
                if(!categoryName.equals("")) {
                    if(addCategory(categoryName)){
                       // MainActivity ma = new MainActivity();
                       // ma.mCategorysList.add(categoryName.toString());
                        Toast toast=Toast.makeText(SettingsActivity.this, categoryName + "      - Category Added !!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER,0,0);
                        toast.show();
                        //reload the SourceTypes
                        mCategorysList.removeAll(mCategorysList);
                        LoadCategory4Db();
                    }
                }
                else {
                    Toast toast=Toast.makeText(SettingsActivity.this, " Enter Category to Add!!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });



        ibRemoveSourceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                String amountSourceType =  (String) spSourceType.getSelectedItem().toString();
                if (!amountSourceType.equals("")){

                    dbHelper= new DBHelper(getApplicationContext());
                    String table_name = dbHelper.getTable_SoucrTypename();
                    String keyColumn =dbHelper.getStcolumn_sourcetype();
                    dbHelper.deleteByQuery("DELETE FROM  "+table_name+"  where  "+keyColumn+" = '" + amountSourceType +"'" );

                    Toast toast = Toast.makeText(SettingsActivity.this,   amountSourceType + "-  SourceType Removed !!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                    toast.show();
                    //reload the SourceTypes
                    mSourceTypesList.removeAll(mSourceTypesList);
                    LoadSourceType4Db();
                }
                else {
                    Toast toast=Toast.makeText(SettingsActivity.this, " Select SourceType to Remove!!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });

        ibRemoveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                String categoryName =   (String) spCategory.getSelectedItem().toString();
                if(!categoryName.equals("")) {
                        dbHelper= new DBHelper(getApplicationContext());
                        String table_name = dbHelper.getTable_Categoryname();
                        String keyColumn =dbHelper.getCatcolumn_Category();
                        dbHelper.deleteByQuery("DELETE FROM  "+table_name+"  where  "+keyColumn+" = '" + categoryName +"'" );

                        Toast toast = Toast.makeText(SettingsActivity.this,   categoryName + "-  category Removed !!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                        toast.show();

                        //reload the SourceTypes
                        mCategorysList.removeAll(mCategorysList);
                        LoadCategory4Db();
                }
                else {
                    Toast toast=Toast.makeText(SettingsActivity.this, " Select Category to Removed!!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });

    }///

    public boolean addSourceType(String amountSourceType)
    {
        boolean addFlag=false;
        Log.i("insertData", "insertData: started");
        try {
            dbHelper = new DBHelper(getApplicationContext());
            mDatabase = dbHelper.getReadableDatabase();
            // Date dNow = new Timestamp( transDate.toString()) ;
            String table_name = dbHelper.getTable_SoucrTypename();  // "accountSummary";

            ContentValues values = new ContentValues();

            // values.put("id",1); // auto increment
            values.put(dbHelper.getStcolumn_sourcetype(), amountSourceType);


            Log.i("insertData", "insertData:values started");
            long rowId = 0;
            if (mDatabase != null) {
                rowId = mDatabase.insert(table_name, null, values);
                if (rowId != -1) {
                    addFlag=true;
                    //Toast.makeText(this.context, " SourceType Added Successfully !!!", Toast.LENGTH_LONG).show();
                } else {
                   // Toast.makeText(this.context, "Error inserting !!!", Toast.LENGTH_LONG).show();
                }
            } else

            {
              //  Toast.makeText(this.context, "Database is null!!!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){}

        return addFlag;

    }

    public boolean addCategory(String categoryName)
    {
        boolean addFlag=false;

        Log.i("insertData", "insertData: started");
        try {
            dbHelper = new DBHelper(getApplicationContext());
            mDatabase = dbHelper.getReadableDatabase();
            // Date dNow = new Timestamp( transDate.toString()) ;
            String table_name =  dbHelper.getTable_Categoryname();  // "accountSummary";

            ContentValues values = new ContentValues();

            // values.put("id",1); // auto increment
            values.put(dbHelper.getCatcolumn_Category(),categoryName);


            Log.i("insertData", "insertData:values started");
            long rowId = 0 ;
            if(mDatabase!=null)        {
                rowId = mDatabase.insert(table_name, null, values);
                if (rowId != -1)            {
                    addFlag=true;
                   // Toast.makeText(this.context, "Category Inserted Successfully !!!", Toast.LENGTH_LONG).show();
                }
                else
                {
                   // Toast.makeText(this.context, "Error inserting !!!", Toast.LENGTH_LONG).show();
                }
            }
            else

            {
               // Toast.makeText(this.context, "Database is null!!!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){}
        return  addFlag;
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
        dbHelper=null;
        mDatabase=null;
    }

    // load spinner items from Database
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

    // load spinner items from Database
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

}////


// TODO: 10/4/2018  http://www.mysamplecode.com/2012/10/android-spinner-programmatically.html 
