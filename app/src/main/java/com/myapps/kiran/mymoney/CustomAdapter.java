package com.myapps.kiran.mymoney;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<String> arrTransID;
    ArrayList<String> arrAmtSourceType;
    ArrayList<String> arrTransType;
    ArrayList<String> arrAmount;
    ArrayList<String> arrTransDate;
    Context context;


    // Constructor  with arraylist variables need to display in  the card
    public CustomAdapter(Context context,ArrayList<String> arrTransID, ArrayList<String> arrAmtSourceType, ArrayList<String> arrTransType, ArrayList<String> arrAmount, ArrayList<String> arrTransDate) {
        this.context = context;
        this.arrTransID=arrTransID;
        this.arrAmtSourceType = arrAmtSourceType;
        this.arrTransType = arrTransType;
        this.arrAmount = arrAmount;
        this.arrTransDate=arrTransDate;

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
        // set the data in items
        holder.mtransId.setText(arrTransID.get(position));
        holder.mAmtSourceType.setText(arrAmtSourceType.get(position));
        holder.mTransType.setText(arrTransType.get(position));
        holder.mAmount.setText(arrAmount.get(position));
        holder.mTransDate.setText(arrTransDate.get(position));
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, "Logn Press To Delete ", Toast.LENGTH_SHORT).show();
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
                builder.setMessage("Click 'Yes' To Delete Selected Transaction Details ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String amount=arrAmount.get(position);
                                // delete Item - at the Display item selected  position //
                                deleteItem( position);
                                Toast.makeText(context, "Deleted the Transaction Details " + amount , Toast.LENGTH_SHORT).show();
                            }
                        })
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
    public void deleteItem(int position) { //removes the row


                        String transid= arrTransID.get(position);
                        Log.i("deleteItem",  "Delete   transid " + transid);
                        Toast.makeText(context, "Delete   transid " + transid, Toast.LENGTH_SHORT).show();
                        DBHelper dbHelper= new DBHelper( this.context);
                        dbHelper.deleteByID(Integer.parseInt(transid));
                        arrTransID.remove(position);
                        arrAmtSourceType.remove(position);
                        arrAmount.remove(position);
                        arrTransDate.remove(position);
                        arrTransType.remove(position);
                        notifyItemRemoved(position);
                        // delete Item//
                    }

    @Override
    public int getItemCount() {
        return arrAmtSourceType.size();
    }

    // initialize / design the card view item with  each control
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mtransId,mAmtSourceType, mTransType, mAmount,mTransDate;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            mtransId= (TextView) itemView.findViewById(R.id.transId);
            mAmtSourceType = (TextView) itemView.findViewById(R.id.amountsourcetype);
            mTransType = (TextView) itemView.findViewById(R.id.transactiontype);
            mAmount = (TextView) itemView.findViewById(R.id.amount);
            mTransDate = (TextView) itemView.findViewById(R.id.date);
        }



    }



}
