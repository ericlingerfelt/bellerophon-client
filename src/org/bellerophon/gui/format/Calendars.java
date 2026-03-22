/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: Calendars.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.format;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * The Class Calendars.
 *
 * @author Eric J. Lingerfelt
 */
public class Calendars{
	
	/**
	 * Gets the default calendar.
	 *
	 * @return the default calendar
	 */
	public static Calendar getDefaultCalendar(){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		return calendar;
	}
	
	/**
	 * Gets the date from calendar.
	 *
	 * @param calendar the calendar
	 * @return the date from calendar
	 */
	public static Calendar getDateFromCalendar(Calendar calendar){
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		return c;
	}
	
	/**
	 * Gets the calendar.
	 *
	 * @param string the string
	 * @return the calendar
	 */
	public static Calendar getCalendar(String string){
		if(string.equals("0000-00-00 00:00:00")){
			return Calendars.getDefaultCalendar();
		}
		Calendar calendar = Calendar.getInstance();
		String day = string.split(" ")[0];
		String time = string.split(" ")[1];
		int year = Integer.valueOf(day.split("-")[0]);
		int month = Integer.valueOf(day.split("-")[1])-1;
		int date = Integer.valueOf(day.split("-")[2]);
		int hourOfDay = Integer.valueOf(time.split(":")[0]);
		int minute = Integer.valueOf(time.split(":")[1]);
		int second = Integer.valueOf(time.split(":")[2]);
		calendar.set(year, month, date, hourOfDay, minute, second);
		return calendar;
	}
	
	/**
	 * Gets the date time string.
	 *
	 * @param calendar the calendar
	 * @return the date time string
	 */
	public static String getDateTimeString(Calendar calendar){
		return new SimpleDateFormat().format(calendar.getTime(), new StringBuffer()
												, new FieldPosition(0)).toString();
	}
	
	/**
	 * Gets the date string.
	 *
	 * @param calendar the calendar
	 * @return the date string
	 */
	public static String getDateString(Calendar calendar){
		return new SimpleDateFormat("MM/dd/yy").format(calendar.getTime(), new StringBuffer()
												, new FieldPosition(0)).toString();
	}
	
	/**
	 * Gets the formatted date string.
	 *
	 * @param calendar the calendar
	 * @return the formatted date string
	 */
	public static String getFormattedDateString(Calendar calendar){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime(), new StringBuffer()
												, new FieldPosition(0)).toString();
	}
	
	public static String getFormattedOutputDateString(Calendar calendar){
		return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(calendar.getTime(), new StringBuffer()
												, new FieldPosition(0)).toString();
	}
	
	/**
	 * Gets the timestamp string.
	 *
	 * @param calendar the calendar
	 * @return the timestamp string
	 */
	public static String getTimestampString(Calendar calendar){
		return new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime(), new StringBuffer()
												, new FieldPosition(0)).toString();
	}
	
	/**
	 * Gets the timestamp string short.
	 *
	 * @param calendar the calendar
	 * @return the timestamp string short
	 */
	public static String getTimestampStringShort(Calendar calendar){
		return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime(), new StringBuffer()
												, new FieldPosition(0)).toString();
	}
	
	/**
	 * Gets the date string.
	 *
	 * @param string the string
	 * @return the date string
	 */
	public static String getDateString(String string){
		Calendar calendar = Calendar.getInstance();
		String day = string.split(" ")[0];
		String time = string.split(" ")[1];
		int year = Integer.valueOf(day.split("-")[0]);
		int month = Integer.valueOf(day.split("-")[1])-1;
		int date = Integer.valueOf(day.split("-")[2]);
		int hourOfDay = Integer.valueOf(time.split(":")[0]);
		int minute = Integer.valueOf(time.split(":")[1]);
		int second = Integer.valueOf(time.split(":")[2]);
		calendar.set(year, month, date, hourOfDay, minute, second);
		return new SimpleDateFormat().format(calendar.getTime(), new StringBuffer()
												, new FieldPosition(0)).toString();
	}
	
}
