# MyMoney

TODOLIST
Toast().setGravity and Toast only in Activity SettingsActivity.this

DashBoard view

/**
 * OpenCSV CSVReader Example, Read line by line
 * 
 * @author pankaj
 *
 */
public class OpenCSVReaderLineByLineExample {

	public static void main(String[] args) throws IOException {

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
	}

}



/**
 * OpenCSV CSVReader Example, Read all at once
 * 
 * @author pankaj
 *
 */
public class OpenCSVReaderReadAllExample {

	public static void main(String[] args) throws IOException {
		CSVReader reader = new CSVReader(new FileReader("emps.csv"), ',');

		List<Employee> emps = new ArrayList<Employee>();

		List<String[]> records = reader.readAll();

		Iterator<String[]> iterator = records.iterator();

		while (iterator.hasNext()) {
			String[] record = iterator.next();
			Employee emp = new Employee();
			emp.setId(record[0]);
			emp.setName(record[1]);
			emp.setAge(record[2]);
			emp.setCountry(record[3]);
			emps.add(emp);
		}

		System.out.println(emps);

		reader.close();

	}

}

