package com.myapps.kiran.mymoney;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<String> arrTransID;
    ArrayList<String> arrAmtSourceType;
    ArrayList<String> arrTransType;
    ArrayList<String> arrAmount;
    ArrayList<String> arrTransDate;
    ArrayList<String> arrCategory;
    ArrayList<String> arrDesc;
    Context context;


    // Constructor  with arraylist variables need to display in  the card
    public CustomAdapter(Context context,ArrayList<String> arrTransID, ArrayList<String> arrAmtSourceType, ArrayList<String> arrTransType, ArrayList<String> arrAmount, ArrayList<String> arrTransDate,ArrayList<String> arrCategory, ArrayList<String> arrDesc) {
        this.context = context;
        this.arrTransID=arrTransID;
        this.arrAmtSourceType = arrAmtSourceType;
        this.arrTransType = arrTransType;
        this.arrAmount = arrAmount;
        this.arrTransDate=arrTransDate;
        this.arrCategory=arrCategory;
        this.arrDesc=arrDesc;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_transaction, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        try {
            // set the data in items
            holder.mtransId.setText(arrTransID.get(position));
            holder.mAmtSourceType.setText(arrAmtSourceType.get(position));
            holder.mTransType.setText(arrTransType.get(position));
            holder.mAmount.setText(arrAmount.get(position));
            holder.mTransDate.setText(arrTransDate.get(position));
            holder.mCategory.setText(arrCategory.get(position));
            holder.mDesc.setText(arrDesc.get(position));
            if (arrTransType.get(position).toString().toLowerCase().equals("income")) {
              //  holder.mTransType.setTextColor(Color.GREEN);
            //    holder.mAmount.setTextColor(Color.GREEN);
            } else {
               // holder.mTransType.setTextColor(Color.RED);
                holder.mAmount.setTextColor(Color.RED);
            }
        }
        catch (Exception e){}
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, " Logn Press To Delete The Item! ", Toast.LENGTH_SHORT).show();
            }
        });

        // long Press to delete the Item
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               // Toast.makeText(context, "OnLongClick Called at position " + position, Toast.LENGTH_SHORT).show();
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.create();
                builder.setMessage("Click 'Yes/Edit' To Delete/Update The Selected Transaction Details!!! ")
                      .setTitle("Modify Item")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String amount=arrAmount.get(position);
                                // delete Item - at the Display item selected  position //
                                deleteItem( position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "   Deleted the Transaction ! " + amount , Toast.LENGTH_SHORT).show();
                            }
                        })
                       /*.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String amount=arrAmount.get(position);
                                Intent intent = new Intent(context, MainActivity.class);
                                ArrayList<String> myEditList = new ArrayList<String>();
                                //intent.putExtra("position",Integer.toString(  position));
                                intent.putExtra("amount",arrAmount.get(position));
                                intent.putExtra("AmtSourceType",arrAmtSourceType.get(position));
                                intent.putExtra("Category", arrCategory.get(  position));
                                intent.putExtra("Desc", arrDesc.get(  position));
                                intent.putExtra( "TransDate",arrTransDate.get(  position));
                                intent.putExtra("TransID", arrTransID.get(  position));
                                intent.putExtra("TransType", arrTransType.get(  position));

                                deleteItem( position);


                                context.startActivity(intent);
                                Toast.makeText(context, "   Edit the Transaction ! " + amount , Toast.LENGTH_SHORT).show();
                            }

                        }) */
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog - do nothing
                            };
                        });
                // create alert dialog
                AlertDialog alertDialog = builder.create();
                // show it
                alertDialog.show();
                ///  dialog end
                return true;
            }

        });


    }

    // delete the card Item from the list
    public void deleteItem(int position)
    {
        //removes the row
        String transid= arrTransID.get(position);
        Log.i("deleteItem",  "Delete   transid " + transid);
       // Toast.makeText(context, "Delete   transid " + transid, Toast.LENGTH_SHORT).show();
        DBHelper dbHelper= new DBHelper( this.context);
        dbHelper.deleteByID(Integer.parseInt(transid));
        arrTransID.remove(position);
        arrAmtSourceType.remove(position);
        arrAmount.remove(position);
        arrTransDate.remove(position);
        arrTransType.remove(position);
        arrCategory.remove(position);
        arrDesc.remove(position);
        notifyItemRemoved(position);
        // delete Item//
      }


    @Override
    public int getItemCount() {
        return arrAmtSourceType.size();
    }

    // initialize / design the card view item with  each control
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mtransId,mAmtSourceType, mTransType, mAmount,mTransDate,mCategory,mDesc;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            mtransId= (TextView) itemView.findViewById(R.id.transId);
            mAmtSourceType = (TextView) itemView.findViewById(R.id.amountsourcetype);
            mTransType = (TextView) itemView.findViewById(R.id.transactiontype);
            mAmount = (TextView) itemView.findViewById(R.id.amount);
            mTransDate = (TextView) itemView.findViewById(R.id.date);

            mCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            mDesc = (TextView) itemView.findViewById(R.id.tvDesc);

        }
    }



    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    public void filterListarrType(ArrayList<String> filterdNames,String arrayName) {
        switch(arrayName.toString().trim())
        {
            // case statements
            // values must be of same type of expression
            case "Source Type" :
                this.arrAmtSourceType = filterdNames;
                break; // break is optional
            case "Category" :
                this.arrCategory = filterdNames;
                break; // break is optional
            case "Transaction Type" :
                this.arrTransType = filterdNames;
                break; // break is optional
            case "Description" :
                this.arrDesc = filterdNames;
                break; // break is optional
            // No break is needed in the default case.
            default :
                // Statements
        }

        notifyDataSetChanged();
    }

    public void filterListarrTransType(ArrayList<String> filterdNames) {
        this.arrTransType = filterdNames;
        notifyDataSetChanged();
    }
    public void filterListarrAmtSourceType(ArrayList<String> filterdNames) {
        this.arrAmtSourceType = filterdNames;
        notifyDataSetChanged();
    }

    public void clear() {
        try {
            arrTransID.removeAll(arrTransID);
            arrAmtSourceType.removeAll(arrAmtSourceType);
            arrAmount.removeAll(arrAmount);
            arrTransDate.removeAll(arrTransDate);
            arrTransType.removeAll(arrTransType);
            arrCategory.removeAll(arrCategory);
            arrDesc.removeAll(arrDesc);
            notifyDataSetChanged();
        }
        catch (Exception ex)
        {
                    ex.printStackTrace();
        }

    }



///////////
}
