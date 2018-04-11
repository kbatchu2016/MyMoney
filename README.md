# MyMoney

TODOLIST
Toast().setGravity and Toast only in Activity SettingsActivity.this
add all columns upto index 6 to  Export the Data
DashBoard view
menuView



protected void getEndDate() {
	// TODO Auto-generated method stub
	new DatePickerDialog(ManagerCountActivity.this, new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			String month = String.valueOf(monthOfYear + 1);
			String day = String.valueOf(dayOfMonth);

			if (dayOfMonth >= 1 && dayOfMonth <= 9) {
				day = "0" + day;
			}
			if (monthOfYear >= 0 && monthOfYear <= 8) {
				month = "0" + month;
			}
			etEndDate.setText(year + "-" + month + "-" + day);

		}
	}, year, monthOfYear, dayOfMonth).show();
}

      
      
