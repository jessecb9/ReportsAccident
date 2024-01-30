package accidentpack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A java project that reads accidents.csv that prints specific accident data in
 * 2021 and 2022 in major states.
 *
 * @author Jesse Cyr-Brophy
 * @version January 25, 2024
 *
 */

public class Reports {

	private static Report[] reports = new Report[1048575];

	public static void main(String[] args) {
		String path = "accidents.csv";
		String line = "";
		try {	int i = 0;
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				if (i > 0) {
					String[] rawData = line.split(",");
					Report report = new Report(rawData[0], rawData[1], dateFormatter(rawData[2], "MM/dd/yyyy H:mm"),
							dateFormatter(rawData[3], "MM/dd/yyyy H:mm"), rawData[4], rawData[5], rawData[6],
							rawData[7], rawData[8], rawData[9], rawData[10], rawData[11], rawData[12], rawData[13]);
					reports[i - 1] = report;	}
				i++;}
			br.close();
			//printReport();
			accidentsInCASev1(reports);
			averageVisbility(reports);
			mostFrequentWeather(reports);
			nightTimeAccidents("12/01/2021 0:00", "02/01/2022 23:59");
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
	}

	/**
	 * Prints the Accidents CSV report
	 *
	 * @param integer  length of report
	 * @param toString converts to print the array
	 * @return the accident report
	 */
	public static void printReport() {
		for (int i = 0; i < reports.length; i++) {
			System.out.println(reports[i].toString());
		}
		long startTime = System.nanoTime();
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		double seconds = (double) timeElapsed / 1000000000;
		System.out.printf("%s nano seconds to print report\n ", timeElapsed);
		System.out.printf("%s seconds to print report\n ", seconds);

	}

	/**
	 * Prints the number of accidents in CA with a severity of 1
	 *
	 * @param integer number of accidents that will be counted
	 * @param find    the severity level
	 * @param scan    for state of CA
	 * @return the number of accidents in CA with a severity of 1
	 */
	public static void accidentsInCASev1(Report[] reports) {
		long startTime = System.nanoTime();
		int numOfAccidents = 0;
		for (int i = 0; i < reports.length; i++) {
			if (reports[i].getSeverity().equalsIgnoreCase("1") && reports[i].getState().equalsIgnoreCase("CA")) {
				++numOfAccidents;	}
		}
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		double seconds = (double) timeElapsed / 1000000000;
		System.out.printf("%s nano seconds for task 1\n", timeElapsed);
		System.out.printf("%s seconds for task 1\n", seconds);

		System.out.printf("The number of accidents with a severity of 1 in the State of CA is: %s\n", numOfAccidents);
	}

	/**
	 * Method to print the average visibility in all the accidents
	 *
	 * @param sum the total visibility
	 * @param the average of the visibility
	 * @return the average of visibility
	 */
	public static void averageVisbility(Report[] reports) {
		double sum = 0;
		double avg = 0;
		long startTime = System.nanoTime();
		for (int i = 1; i < reports.length; i++) {
			sum += Double.parseDouble(reports[i].getVisibility());
			avg = sum / reports.length - 1;

		}
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		double seconds = (double) timeElapsed / 1000000000;
		System.out.printf("%s nano seconds for task 2\n", timeElapsed);
		System.out.printf("%s seconds for task 2\n", seconds);
		System.out.printf("The average visbility is: %s\n", avg);
	}

	/**
	 * Date formatter to convert string to specified format
	 * 
	 * @param dateTimeStr
	 * @param format
	 * @return date
	 */
	private static LocalDateTime dateFormatter(String dateTimeStr, String format) {
		if (dateTimeStr.contains(" ") && format != null) {
			String[] dateSplit = dateTimeStr.split(" ")[0].split("/");
			String month = String.format("%02d", Integer.parseInt(dateSplit[0]));
			String day = String.format("%02d", Integer.parseInt(dateSplit[1]));
			String year = String.format("%04d", Integer.parseInt(dateSplit[2]));
			String time = dateTimeStr.split(" ")[1];
			final String dateTIme = String.format("%s/%s/%s %s", month, day, year, time);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
			return LocalDateTime.parse(dateTIme, dtf);
		}
		return LocalDateTime.now();

	}
/**
 * Method to print number of accidents at night in the winter of 2021.
 * 
 * @param startDateTime
 * @param endDateTime
 * @return number of accidents
 */
	public static List<Report> nightTimeAccidents(String startDateTime, String endDateTime) {
		LocalDateTime startDate = dateFormatter(startDateTime, "MM/dd/yyyy H:mm");
		LocalDateTime endDate = dateFormatter(endDateTime, "MM/dd/yyyy H:mm");
		List<Report> filteredArray = new ArrayList<>();
		long startTime = System.nanoTime();for (int i = 0; i < reports.length; i++) {
			if (reports[i] != null) {
				if (reports[i].getStartTime().isAfter(startDate) && reports[i].getEndTime().isBefore(endDate)) {
					if (reports[i].getSunriseSunset().equalsIgnoreCase("Night")) {
						filteredArray.add(reports[i]);
					}
				}
			}
		}
		
		long endTime = System.nanoTime();
		long timeElapsed = endTime - startTime;
		double seconds = (timeElapsed) / 1000000000;
		System.out.printf("%s nanoseconds for task 4\n ", timeElapsed);
		System.out.printf("%s seconds for task 4 \n", seconds);
		System.out.printf("The number of accidents in the winter of 2021 at night %s\n", filteredArray.size());
		return filteredArray;
	}

	/**
	 * Method to print the most frequent weather condition in the report
	 *
	 * @param count     the length of each word
	 * @param find      the frequency of each word
	 * @param find      what word occurs the most
	 * @param restore   the resulting string
	 * @param frequency of the string
	 */
	public static void mostFrequentWeather(Report[] reports) {
		Map<String, Integer> frequency = new HashMap<>();
		long startTime = System.nanoTime();
		for (int i = 0; i < reports.length; i++) {
			String key = reports[i].getWeatherCondition();
			if (!frequency.containsKey(key)) {
				frequency.put(key, 1);} else {
				frequency.put(key, frequency.get(key) + 1);		}
		}
		long endTime = System.nanoTime();
		int highest = 0;
		String restore = null;
		for(Entry<String,Integer> entry: frequency.entrySet()) {
			if(entry.getValue().intValue() > highest) {
				highest = entry.getValue();
				restore = entry.getKey();}
		}long timeElapsed = endTime - startTime;
		double seconds = (double) timeElapsed / 1000000000;
		System.out.println(timeElapsed + " nano seconds for task 3");
		System.out.println(seconds + "  seconds for task 3");
		System.out.println("The most common weather condition is: " + restore);
	}
}
