 {
                Intent intent = getIntent();
                // get the data from the putExtra
                if(intent.getStringExtra("TransID")!=null &&   ! intent.getStringExtra("TransID").isEmpty()){
                String amount = intent.getStringExtra("amount");
                String AmtSourceType = intent.getStringExtra("AmtSourceType");
                String TransType = intent.getStringExtra("TransType");
                String Category = intent.getStringExtra("Category");
                String Desc = intent.getStringExtra("Desc");
                String TransDate = intent.getStringExtra("TransDate");
                String TransID = intent.getStringExtra("TransID");
                TransID.equals(null);
                int selectionPosition= adapterStype.getPosition(AmtSourceType);
                spAmtType.setSelection(selectionPosition);
                int selectionPosition2= adapterCat.getPosition(Category);
                spCategoryType.setSelection(selectionPosition2);
                chkIncomeValue.setChecked(false);
                chkExpenseValue.setChecked(false);
                if (TransType.equals("income")) chkIncomeValue.setChecked(true);
                if (TransType.equals("expense")) chkExpenseValue.setChecked(true);
                editDate.setText(TransDate);
                etTransAmount.setText(amount);
                etDesc.setText(Desc);
                TransID=null;
                //remove the  inten content
                intent.removeExtra("TransID");

            }
        }
	
	
	
	
	                        .setNeutralButton("Edit", new DialogInterface.OnClickListener() {
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
