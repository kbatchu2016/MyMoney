# MyMoney

TODOLIST
Toast().setGravity and Toast only in Activity SettingsActivity.this

DashBoard view


		CSVReader reader = new CSVReader(new FileReader("emps.csv"), ',');

		List<Employee> emps = new ArrayList<Employee>();

		// read line by line
		String[] record = null;

		while ((record = reader.readNext()) != null) {
			Employee emp = new Employee();
			emp.setId(record[0]);
			emp.setName(record[1]);
			emp.setAge(record[2]);
			emp.setCountry(record[3]);
			emps.add(emp);
		}

		System.out.println(emps);
		
		reader.close();
      
