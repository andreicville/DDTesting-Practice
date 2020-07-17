package Utilities;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


public class ParseCSVData {
	public ParseCSVData() {

	}

	public String[][] getCSVFileData(String csvFileName) throws IOException, CsvException {
		String csvFileFullPath = System.getProperty("user.dir") + File.separator + csvFileName;
		
		CSVReader myCSVReader = null;
		try {
			myCSVReader = new CSVReader(new FileReader(csvFileFullPath));
		} catch (Exception e) {
			System.out.println("|Error: Can't locate file on path:|" + csvFileFullPath + "|");
			System.out.println("|Error:|" + e);

		}
		List<String[]> allCSVData = myCSVReader.readAll();

		String[][] testData = allCSVData.stream().toArray(String[][]::new);

		System.out.println(testData);

		return testData;
	}
}
