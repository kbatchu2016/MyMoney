package com.myapps.kiran.mymoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.datatype.Duration;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DBHelper extends SQLiteOpenHelper {

    private static final String database_name = "MyMoney.db";
    private SQLiteDatabase mDatabase ;
    private Context context;

    // table info & columns names - MainActitity
    private String table_name = "accountSummary";
    private String column_id = "id";
    private String column_amountsourcetype = "amountsourcetype";
    private String column_amount="amount";
    private String column_date="dateoftrans";
    private String column_transType="transactiontype";
    private String column_transCategory="transCategory";
    private String  column_transDescription ="transdescription";
    private String column_monthYear = "transmonthYear";
    private String column_paidbills ="paidbills";

    public String getColumn_monthYear() {
        return column_monthYear;
    }



    public String getColumn_transCategory() {
        return column_transCategory;
    }

    public String getColumn_transDescription() {
        return column_transDescription;
    }

    private String database_create_statement =
            "CREATE TABLE " + table_name + "("
                    + column_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + column_transType + " TEXT,"
                    + column_amountsourcetype + " TEXT,"
                    + column_date + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + column_amount + " INTEGER,"
                    + column_transCategory + " TEXT,"
                    + column_transDescription + " TEXT ,"
                    + column_monthYear + " TEXT,"
                    + column_paidbills	+" INTEGER DEFAULT  0 "
                    + ")";


    public static String getDatabase_name() {
        return database_name;
    }

    public String getColumn_id() {
        return column_id;
    }

    public String getcolumn_amountsourcetype() {
        return column_amountsourcetype;
    }

    public String getColumn_amount() {
        return column_amount;
    }

    public String getColumn_date() {
        return column_date;
    }

    public String getColumn_transType() {
        return column_transType;
    }

    public String getDatabase_create_statement() {
        return database_create_statement;
    }
    public String getTable_name() {
        return table_name;
    }
    public String getColumn_amountsourcetype(){
        return column_amountsourcetype;
    }

    public String getcolumn_paidbills() {return column_paidbills;}

    public String getTable_SoucrTypename() {
        return table_SoucrTypename;
    }


    // Table 2 - SourceTYpe
    private String table_SoucrTypename = "sourceType";
    private String stcolumn_id = "id",stcolumn_sourcetype = "sourcetypeName";
    private String tblst_create_statement =
            "CREATE TABLE " + table_SoucrTypename + "("
                    + stcolumn_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + stcolumn_sourcetype + " TEXT "
                    + ")";
    public String getStcolumn_id() {
        return stcolumn_id;
    }

    public String getStcolumn_sourcetype() {
        return stcolumn_sourcetype;
    }

    public String getTblst_create_statement() {
        return tblst_create_statement;
    }

    // Table 2 - SourceTYpe


    // Table 3 - Category
    private String table_Categoryname = "category";
    private String catcolumn_id = "id",catcolumn_Category = "categoryname";

    private String tblCat_create_statement =
            "CREATE TABLE " + table_Categoryname  + "("
                    + catcolumn_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + catcolumn_Category + " TEXT "
                    + ")";

    public String getTable_Categoryname() {
        return table_Categoryname;
    }

    public String getCatcolumn_id() {
        return catcolumn_id;
    }

    public String getCatcolumn_Category() {
        return catcolumn_Category;
    }

    public String getTblCat_create_statement() {
        return tblCat_create_statement;
    }
    // Table 3 category



// delete the Item from Summary/TransactionActivity Screen
    public void deleteByID(int position) {
     //   sqldatabase.delete(table_name, column_id , position);

        Log.i("delete Item", "delete from "+table_name+" where id=" +position);
        sqldatabase.execSQL("delete from "+table_name+" where id=" +position);

        Cursor cursor = sqldatabase.rawQuery("select * from "+ this.getTable_name() +"  order by id asc ;", null);
        System.out.println("MainActivity.onClick:"+ cursor.getCount());
    }


    public void deleteByQuery(String  query) {

try {
    Log.i("delete Item", query);
    sqldatabase.execSQL(query);
}
 catch (Exception e) {
        e.printStackTrace();
    }

    }

    private DBHelper dbHelper;
    private SQLiteDatabase sqldatabase;

    public DBHelper(Context context){
        super(context,database_name,null,1);
       // dbHelper = new DBHelper(context);
        sqldatabase = this.getWritableDatabase();

    }
    public void onCreate(SQLiteDatabase db) {
        // create all Tables
        try {

            //db.execSQL(" DROP TABLE sourceType");

            db.execSQL(database_create_statement);
            db.execSQL(tblst_create_statement);
            db.execSQL(tblCat_create_statement);
            Log.i("DBHELPER", "all data base tables are created");


        }
        catch (Exception e){
            e.printStackTrace();;
        }
    }
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}

    public boolean exportDB2CSVFile() {
        boolean exportFileStaus =false;
        boolean mExternalStorageAvailable=false,mExternalStorageWriteable=false;
        // to check the storage space is avaiable or not
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //External storage available
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
            //only internal available
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
            // no one is available
        }

        String baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() ;// + "/sdcard/Download";
        String fileName = "MyMoney.csv";

        File file = new File(baseDir, fileName);
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            //Cursor curCSV = db.rawQuery("SELECT * FROM " + table_name ,null);
            Cursor curCSV = db.rawQuery("select * from "+ table_name +"  Order by  dateoftrans ;", null);
            System.out.println("From MainActivity.onClick:"+ curCSV.getCount());
            if (curCSV != null) {
                csvWrite.writeNext(curCSV.getColumnNames());
                while (curCSV.moveToNext()) {
                    //Which column you want to exprort
                    String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2),
                            curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6),curCSV.getString(7)};
                    csvWrite.writeNext(arrStr);
                }
                csvWrite.close();
                curCSV.close();
                exportFileStaus=true;

            }
            else
            {exportFileStaus=false;}
        }
        catch(Exception sqlEx)
        {
            Log.e("Export", sqlEx.getMessage(), sqlEx);
        }
        return exportFileStaus;
    }


}///