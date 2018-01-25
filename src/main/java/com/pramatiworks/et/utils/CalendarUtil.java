package com.pramatiworks.et.utils;

public class CalendarUtil {
	
	public static final String[] months = {"January", "February", "March", "April" , "May" , "June", "July", 
										"August", "September", "October", "November" , "December" };
	
	public static String getMonthName(int month) {
		return months[month-1];
	}

}
