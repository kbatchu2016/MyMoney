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
import com.anychart.anychart.CoreAxesLinear;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.HoverMode;
import com.anychart.anychart.LabelsOverlapMode;
import com.anychart.anychart.Mapping;
import com.anychart.anychart.Orientation;
import com.anychart.anychart.Pie;
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


public class SummaryActivity extends AppCompatActivity {


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
                Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
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
                            // Get version from Cursor
                           // int _id = cursor.getInt(cursor.getColumnIndex("id"));
                           // String _amountsourcetype = cursor.getString(cursor.getColumnIndex("amountsourcetype"));
                           // String _transType = cursor.getString(cursor.getColumnIndex("transactiontype"));
                           // String _dateoftrans = cursor.getString(cursor.getColumnIndex("dateoftrans"));
                            int _transAmount = cursor.getInt(cursor.getColumnIndex("monthlyexpense"));
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
                            // Get version from Cursor
                            // int _id = cursor.getInt(cursor.getColumnIndex("id"));
                            // String _amountsourcetype = cursor.getString(cursor.getColumnIndex("amountsourcetype"));
                       //     String _transType = cursor1.getString(cursor1.getColumnIndex("transactiontype"));
                            // String _dateoftrans = cursor.getString(cursor.getColumnIndex("dateoftrans"));
                            int _transAmount = cursor1.getInt(cursor1.getColumnIndex("monthlyincome"));
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
            Cartesian barChart = AnyChart.bar();
            barChart.setAnimation(true);
            barChart.setPadding(10d, 20d, 5d, 20d);
            barChart.getYScale().setStackMode(ScaleStackMode.VALUE);
            barChart.getYAxis().getLabels().setFormat(
                    "function() {\n" +
                            "    return Math.abs(this.value).toLocaleString();\n" +
                            "  }");
            barChart.getYAxis(0d).setTitle("Transactions in ₹ ");
            barChart.getXAxis(0d).setOverlapMode(LabelsOverlapMode.ALLOW_OVERLAP);
            CoreAxesLinear xAxis1 = barChart.getXAxis(1d);
            xAxis1.setEnabled(true);
            xAxis1.setOrientation(Orientation.RIGHT);
            xAxis1.setOverlapMode(LabelsOverlapMode.ALLOW_OVERLAP);
            barChart.setTitle("MyMoney Summary");
            barChart.getInteractivity().setHoverMode(HoverMode.BY_X);
            barChart.getTooltip()
                    .setTitle(false)
                    .setSeparator(false)
                    .setDisplayMode(TooltipDisplayMode.SEPARATED)
                    .setPositionMode(TooltipPositionMode.POINT)
                    .setUseHtml(true)
                    .setFontSize(12d)
                    .setOffsetX(5d)
                    .setOffsetY(0d)
                    .setFormat(
                            "function() {\n" +
                                    "      return '<span style=\"color: #D9D9D9\">₹</span>' + Math.abs(this.value).toLocaleString();\n" +
                                    "    }");



            List<DataEntry> seriesData = new ArrayList<>();

            String _months;
            int _expenseamt=0,_incomeamt=0;
           /*
            for( int x = 1 ; x < _arrtransMY.size() ; x++ )
            {
                _months=  _arrtransMY.get(x)  ;
                _expenseamt= Integer.parseInt(  _arrAmountExp.get(x) ) ;
                _incomeamt= Integer.parseInt( _arrAmountInc.get(x) ) ;
               seriesData.add(new CustomDataEntry(_months, _incomeamt, _expenseamt));
            }
            */


            for( int x = 1 ; x < _arrtransMY.size() ; x++ )
            {
                _expenseamt=0;_incomeamt=0;
                _months=  _arrtransMY.get(x)  ;
                if (dictionaryIncome.containsKey(_months))
                _incomeamt=  Integer.parseInt(  dictionaryIncome.get(_months).toString() );
                if (dictionaryExpense.containsKey(_months))
                _expenseamt = Integer.parseInt(  "-"+ dictionaryExpense.get(_months).toString());
                seriesData.add(new CustomDataEntry(_months,  _expenseamt,_incomeamt));
            }



            Set set = new Set(seriesData);
            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
            Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

            SeriesBar series1 = barChart.bar(series1Data);
            series1.setName("Expense").setColor("HotPink");

            series1.getTooltip()
                    .setPosition("left")
                    .setAnchor(EnumsAnchor.RIGHT_BOTTOM);

            SeriesBar series2 = barChart.bar(series2Data);
            series2.setName("Income");
            series2.getTooltip()
                    .setPosition("right")
                    .setAnchor(EnumsAnchor.LEFT_BOTTOM);

            barChart.getLegend().setEnabled(true);
            barChart.getLegend().setInverted(true);
            barChart.getLegend().setFontSize(13d);
            barChart.getLegend().setPadding(0d, 0d, 20d, 0d);

            anyChartView.setChart(barChart);
        }
    }

}

    class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }


/////////////////



