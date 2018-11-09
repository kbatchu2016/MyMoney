package com.myapps.kiran.mymoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesColumn;
import com.anychart.anychart.CoreAxesLinear;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.HoverMode;
import com.anychart.anychart.LabelsOverlapMode;
import com.anychart.anychart.Mapping;
import com.anychart.anychart.Orientation;
import com.anychart.anychart.Pie;
import com.anychart.anychart.Position;
import com.anychart.anychart.ScaleStackMode;
import com.anychart.anychart.SeriesBar;
import com.anychart.anychart.Set;
import com.anychart.anychart.TooltipDisplayMode;
import com.anychart.anychart.TooltipPositionMode;
import com.anychart.anychart.ValueDataEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class BalanceViewActivity extends AppCompatActivity {

    ArrayList<String> _arrTransType = new ArrayList<>();
    ArrayList<String> _arrAmountExp = new ArrayList<>();
    ArrayList<String> _arrAmountInc = new ArrayList<>();
    ArrayList<String> _arrtransMY = new ArrayList<>();
    Map dictionaryExpense = new HashMap();
    Map dictionaryIncome = new HashMap();

    // db related
    private DBHelper dbHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);

        ///////////////////////////////////////////////////////////////
        FloatingActionButton  backtoHomePage = (FloatingActionButton) findViewById(R.id.sbacktoHomePage);
        backtoHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BalanceViewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        if (dbHelper == null) {
            try {

                dbHelper = new DBHelper(getApplicationContext());
                mDatabase = dbHelper.getReadableDatabase();

                Cursor cursor = mDatabase.rawQuery("select transmonthYear, SUM(amount) as 'monthlyexpense'  from " + dbHelper.getTable_name() + " where transactiontype = 'expense'  Group  by transmonthYear Order by  transmonthYear  DESC ;", null);
                System.out.println("MainActivity.onClick:" + cursor.getCount());
                if (cursor != null) {
                    // move cursor to first row
                    if (cursor.moveToFirst()) {
                        do {
                            int _transAmount=0;
                            // Get version from Cursor
                           // int _id = cursor.getInt(cursor.getColumnIndex("id"));
                           // String _amountsourcetype = cursor.getString(cursor.getColumnIndex("amountsourcetype"));
                           // String _transType = cursor.getString(cursor.getColumnIndex("transactiontype"));
                           // String _dateoftrans = cursor.getString(cursor.getColumnIndex("dateoftrans"));
                            _transAmount = cursor.getInt(cursor.getColumnIndex("monthlyexpense"));
                           // String _transCategory = cursor.getString(cursor.getColumnIndex("transCategory"));
                            String _transmonthYear = cursor.getString(cursor.getColumnIndex("transmonthYear"));
                            // String _transDesc = cursor.getString(cursor.getColumnIndex("transdescription"));

                            dictionaryExpense.put(_transmonthYear,_transAmount);
                            _arrAmountExp.add(Integer.toString(_transAmount));
                            _arrtransMY.add(_transmonthYear);


                            // move to next row
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            //////////////////////////////////////////////////////////////

            try {

                dbHelper = new DBHelper(getApplicationContext());
                mDatabase = dbHelper.getReadableDatabase();

                Cursor cursor1 = mDatabase.rawQuery("select transmonthYear, SUM(amount) as 'monthlyincome' from " + dbHelper.getTable_name() + " where transactiontype = 'income'  Group  by transmonthYear Order by  transmonthYear  DESC ;", null);
                System.out.println("MainActivity.onClick:" + cursor1.getCount());
                if (cursor1 != null) {
                    // move cursor to first row
                    if (cursor1.moveToFirst()) {
                        do {
                            int _transAmount=0;
                            // Get version from Cursor
                            // int _id = cursor.getInt(cursor.getColumnIndex("id"));
                            // String _amountsourcetype = cursor.getString(cursor.getColumnIndex("amountsourcetype"));
                       //     String _transType = cursor1.getString(cursor1.getColumnIndex("transactiontype"));
                            // String _dateoftrans = cursor.getString(cursor.getColumnIndex("dateoftrans"));
                            _transAmount = cursor1.getInt(cursor1.getColumnIndex("monthlyincome"));
                            // String _transCategory = cursor.getString(cursor.getColumnIndex("transCategory"));
                            String _transmonthYear = cursor1.getString(cursor1.getColumnIndex("transmonthYear"));
                            // String _transDesc = cursor.getString(cursor.getColumnIndex("transdescription"));

                            dictionaryIncome.put(_transmonthYear,_transAmount);
                            _arrAmountInc.add(Integer.toString(_transAmount));
                            _arrtransMY.add(_transmonthYear);


                            // move to next row
                        } while (cursor1.moveToNext());
                    }
                    cursor1.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            ////////////////////////////////////////////////////////


            AnyChartView anyChartView = findViewById(R.id.any_chart_view);

            Cartesian cartesian = AnyChart.column();
            List<DataEntry> data = new ArrayList<>();
            for( int x = 1 ; x < _arrtransMY.size() ; x++ )
            {
               float _expenseamt=0;float _incomeamt=0;
                String _months=  _arrtransMY.get(x)  ;
                if (dictionaryIncome.containsKey(_months))
                    _incomeamt=  Integer.parseInt(  dictionaryIncome.get(_months).toString() );
                if (dictionaryExpense.containsKey(_months))
                    _expenseamt = Integer.parseInt(   dictionaryExpense.get(_months).toString());
                data.add(new ValueDataEntry(_months,  (_incomeamt - _expenseamt )));
            }


            CartesianSeriesColumn column = cartesian.column(data);

            column.getTooltip()
                    .setTitleFormat("{%X}")
                    .setPosition(Position.CENTER_BOTTOM)
                    .setAnchor(EnumsAnchor.CENTER_BOTTOM)
                    .setOffsetX(0d)
                    .setOffsetY(5d)
                    .setFormat("₹{%Value}{Months: }");

            cartesian.setAnimation(true);
            cartesian.setTitle("Total Balance");

            cartesian.getYScale().setMinimum(0d);

            cartesian.getYAxis().getLabels().setFormat("₹{%Value}{Months: }");

            cartesian.getTooltip().setPositionMode(TooltipPositionMode.POINT);
            cartesian.getInteractivity().setHoverMode(HoverMode.BY_X);

            cartesian.getXAxis().setTitle("Months");
            cartesian.getYAxis().setTitle("Balance");

            anyChartView.setChart(cartesian);
        }
        }
    }

/////////////////




