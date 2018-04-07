package com.myapps.kiran.mymoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    private static final String database_name = "MyMoney.db";
    private SQLiteDatabase mDatabase ;
    private Context context;

    // table info & columns names - MainActitity
    private String table_name = "accountSummary";
    private String column_id = "id",column_amountsourcetype = "amountsourcetype",column_amount="amount",
            column_date="dateoftrans",column_transType="transactiontype" , column_transCategory="transCategory" ,column_transDescription="transDescription";
    private String database_create_statement =
            "CREATE TABLE " + table_name + "("
                    + column_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + column_transType + " TEXT,"
                    + column_amountsourcetype + " TEXT,"
                    + column_date + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + column_amount + " INTEGER"
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
}