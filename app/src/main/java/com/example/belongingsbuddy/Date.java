package com.example.belongingsbuddy;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Date represents a date on the calendar by its year, month, and day;
 * The Class uses standard getter and setter methods
 */
public class Date implements Comparable<Date>, Serializable {
    private int day;
    private int month;
    private int year;

    /**
     * Constructor with given data
     * @param d
     * integer representing the day of the month
     * @param m
     * integer representing the month of the year
     * @param y
     * integer representing the year
     */
    public Date(int d, int m, int y){
        day = d;
        month = m;
        year = y;
    }

    /**
     * Constructor for when no parameters are passed in;
     * Creates a Date using the current date
     */
    public Date(){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Constructor for when a String is passed as a parameter.
     * String should be in yyyy-mm-dd format
     * @param string
     * String in the format "yyyy-mm-dd" |
     * where 'yyyy' represents the year |
     * 'mm' represents the month |
     * and 'dd' represents the day
     */
    protected Date (String string){
        try{
            List<String> data = Arrays.asList(string.split("-"));
            year = Integer.parseInt(data.get(0));
            month = Integer.parseInt(data.get(1));
            day = Integer.parseInt(data.get(2));
        }
        catch(Exception e){
            throw new IllegalArgumentException();
        }


    }

    /**
     * Creates a String representation of the Date
     * @return
     * Sting representing the Date
     */
    public String getString(){
        String d = Integer.toString(day);
        String m = Integer.toString(month);
        String y = Integer.toString(year);
        return String.join("-", y, m, d);
    }
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Compares this date to another Date object.
     * More recent dates are considered greater than lder dates
     *
     * @param anotherDate the object to be compared.
     * @return
     * a positive number if this is greater then anotherDate |
     * a negative number if this is less than anotherDate |
     * 0 if this is the same as anotherDate
     */
    @Override
    public int compareTo(Date anotherDate) {
        // years
        int yearComp = Integer.compare(this.year, anotherDate.year);
        if (yearComp != 0) {
            return yearComp;
        }
        // if years same, compare months
        int monthComp = Integer.compare(this.month, anotherDate.month);
        if (monthComp != 0) {
            return monthComp;
        }
        // if years and months same, compare days
        return Integer.compare(this.day, anotherDate.day);
    }
}
