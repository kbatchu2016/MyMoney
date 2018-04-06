package com.myapps.kiran.mymoney;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String database_name = "MyMoney.db";
    // table info & columns names
    private String table_name = "accountSummary";
    private String column_id = "id",column_amountsourcetype = "amountsourcetype",column_amount="amount",
            column_date="dateoftrans",column_transType="transactiontype"  ;


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



    private String database_create_statement =
            "CREATE TABLE " + table_name + "("
                    + column_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + column_transType + " TEXT,"
                    + column_amountsourcetype + " TEXT,"
                    + column_date + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + column_amount + " INTEGER"
                    + ")";

    public String getTable_name() {
        return table_name;
    }
    public String getColumn_amountsourcetype(){
        return column_amountsourcetype;
    }
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
        db.execSQL(database_create_statement);
    }
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}
}