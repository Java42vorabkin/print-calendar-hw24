package telran.application.dates;

import java.time.*;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class PrintCalendar {
private static final TextStyle WEEK_DAY_LENGTH = TextStyle.SHORT_STANDALONE;
static  DayOfWeek[] daysOfWeek = DayOfWeek.values();
private static Locale locale = Locale.forLanguageTag("en");

	public static void main(String[] args) {
		try {		
		//TODO Part for arguments processing
		//java -jar <jar file name> <month number> <year> <full name of week day (SUNDAY upper case)>
		//no arguments - current month, current year, MONDAY
		//no year, no week day - current year, MONDAY
		//no week day - MONDAY 
			// #1
			if(args.length>0 && args[0].equals("-help")) {
				printHelp();
				return;
			}
			int month = getMonth(args);
			int year = getYear(args);
			int firstDayOfWeek = getFirstDayOfWeek(args);
			/*
			// #2
			int monthYearDay[] = getMonthYearFirstDay(args);
			int month = monthYearDay[0];
			int year = monthYearDay[1];
			int firstDayOfWeek = monthYearDay[2];
			*/
			updateDaysOfWeek(firstDayOfWeek);
			printCalendar(month, year);
		} catch (NumberFormatException ex) {
			System.out.println("NumberFormatException: " + ex.getMessage() + getInputString(args));
			printHelp();
		} catch (IllegalArgumentException ex) {
			System.out.println("IllegalArgumentException: " + ex.getMessage() + getInputString(args));
			printHelp();
		} catch (DateTimeException ex) {
			System.out.println("DateTimeException: " + ex.getMessage() + getInputString(args)); 
			printHelp();
		}
		// Done
	}
	private static void printHelp() {
		System.out.println("\n\nRequired Input Format:\nMonth(1-12) Year(-5000 - 5000) fullWeekDayName(Monday - Sunday)");		
	}
	// This method returns input string
	private static String getInputString(String[] args) {
		String inputString = "\nInput: ";
		for(String arg : args) {
			inputString += arg + " ";
		}
		return inputString;
	}
	// This method updates the array daysOfWeek due to firstDayOfWeek
	private static void updateDaysOfWeek(int firstDayOfWeek) {
		//TODO reordering of static field daysOfWeek 
		//in the case of wrong week day exception should be thrown
		if(firstDayOfWeek > 1) {
//			List<DayOfWeek> tmpList = new LinkedList<>();
			List<DayOfWeek> tmpList = new ArrayList<>();
			int index = firstDayOfWeek - 1;
			for(DayOfWeek day : daysOfWeek) {
				if(index == daysOfWeek.length) {
					index = 0;
				}
				tmpList.add(daysOfWeek[index++]);
			}
			tmpList.toArray(daysOfWeek);
		} // if firstDayOfWeek==1 then nothing to do
		// Done
	}
	// #2
	private static int[] getMonthYearFirstDay(String[] args) {
		if(args.length==0) {
			return new int[] {LocalDate.now().getMonthValue(), LocalDate.now().getYear(), 1};
		}
		int month = Integer.parseInt(args[0]);
		if(args.length==1) {
			return new int[] {month, LocalDate.now().getYear(), 1};
		}
		int year = Integer.parseInt(args[1]);
		if(args.length==2) {
			return new int[] {month, year, 1};
		}
		int firstDayOfWeek = DayOfWeek.valueOf(args[2].toUpperCase()).getValue();
		return new int[] {month, year, firstDayOfWeek};
	}
	// #1
	// This method returns integer value of month or current month as default
	private static int getMonth(String[] args) throws NumberFormatException {
		return args.length > 0 ? Integer.parseInt(args[0]) : LocalDate.now().getMonthValue();
	}
	// This method returns integer value of year or current year as default
	private static int getYear(String[] args) throws NumberFormatException {
		return args.length > 1 ? Integer.parseInt(args[1]) : LocalDate.now().getYear();
	}
	// This method returns the integer value of the first day of the week or
	// 1c(Monday) as default
	private static int getFirstDayOfWeek(String[] args) throws NumberFormatException {
		return args.length > 2 ? DayOfWeek.valueOf(args[2].toUpperCase()).getValue() : 1;
	}
	
	private static void printCalendar(int month, int year) {
		printTitle(month, year);
		printWeekDays();
		printDates(month, year);
		
	}

	private static void printDates(int month, int year) {
		int firstColumn = getFirstColumn(month, year);
		printOffset(firstColumn);
		int days = getDaysNumber(month, year);
		int columnWidth = getColumnWidth();
		int line = 1;
		for (int i = 1; i <= days; i++) {
			System.out.printf("%" + columnWidth +"d", i);
			if ((line + firstColumn) % daysOfWeek.length == 0) {
				System.out.println();
				
				firstColumn = 0;
			} else {
				firstColumn++;
			}
			
			
		}
		
	}

	private static int getColumnWidth() {
		
		return (daysOfWeek[0].getDisplayName(WEEK_DAY_LENGTH, locale) + " ").length();
	}

	private static int getDaysNumber(int month, int year) {
		
		return YearMonth.of(year, month).lengthOfMonth();
	}

	private static void printOffset(int firstColumn) {
		System.out.print(" ".repeat(firstColumn * getColumnWidth()));
		
	}

	private static int getFirstColumn(int month, int year) {
		LocalDate firstDateMonth = LocalDate.of(year, month, 1);
		int firstWeekDay = firstDateMonth.getDayOfWeek().getValue();
		int firstValue = daysOfWeek[0].getValue();
		int delta = firstWeekDay -firstValue;
		
		
		
		return delta >= 0 ? delta : delta + daysOfWeek.length;
	}

	private static void printWeekDays() {
		String res = " ".repeat(getColumnWidth() / 2);
		for (int i = 0; i < daysOfWeek.length; i++) {
			res += daysOfWeek[i].getDisplayName(WEEK_DAY_LENGTH, locale) + " ";
		}
		System.out.println(res);
		
	}

	private static void printTitle(int month, int year) {
		Month monthObj = Month.of(month);
		System.out.print(" ".repeat(2 * getColumnWidth() + 1));
		System.out.printf("%s, %d\n", monthObj.getDisplayName(TextStyle.FULL_STANDALONE, locale), year);
		
	}

}
