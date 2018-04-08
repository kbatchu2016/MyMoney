package com.myapps.kiran.mymoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {


    TextView tvAddAmtSouurceType, tcAddCategory;
    EditText etSourceType, etCategory;
    ImageButton ibAmtSourceType, ibCategory;

    Context context;
    private DBHelper dbHelper;

    private SQLiteDatabase mDatabase ;

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


    }//

    @Override
    protected void onStart() {
        super.onStart();


        ibAmtSourceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String amountSourceType = etSourceType.getText().toString();
                if (!amountSourceType.equals("")){
                    addSourceType(amountSourceType);
                }else {
                   // Toast.makeText( context.getApplicationContext(), " Enter SourceType to Add !!", Toast.LENGTH_LONG).show();
                }

            }
        });

        ibCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                String categoryName = etCategory.getText().toString();
                if(!categoryName.equals("")) {
                    addCategory(categoryName);
                   // MainActivity ma = new MainActivity();
                   // ma.mCategorysList.add(categoryName.toString());
                }
                else {
                 //   Toast.makeText(context.getApplicationContext(), " Enter Category to Add!!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }///

    public void addSourceType(String amountSourceType)
    {
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
                    Toast.makeText(this.context, " SourceType Added Successfully !!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this.context, "Error inserting !!!", Toast.LENGTH_LONG).show();
                }
            } else

            {
                Toast.makeText(this.context, "Database is null!!!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){}

    }

    public void addCategory(String categoryName)
    {
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
                    Toast.makeText(this.context, "Category Inserted Successfully !!!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this.context, "Error inserting !!!", Toast.LENGTH_LONG).show();
                }
            }
            else

            {
                Toast.makeText(this.context, "Database is null!!!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){}
    }


}////
