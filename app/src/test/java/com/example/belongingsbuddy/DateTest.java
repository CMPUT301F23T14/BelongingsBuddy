package com.example.belongingsbuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Calendar;

/**
 * Tests the functionality of the Date class
 */
public class DateTest {

    /**
     * Tests that all 3 Date constructors work as expected
     */
    @Test
    public void testConstructors() {
        // constructor passing in integer values
        // NOTE: currently does not check that valid integers are given (ex: user can give month 22)
        // this will be implemented for final product
        Date d1 = new Date(1, 10, 2000);
        // constructor passing in no values
        Date d2 = new Date();
        // constructor passing in string
        Date d3 = new Date("2023-11-09");
        // assert that the Dates were constructed
        assertTrue(d1 instanceof Date);
        assertTrue(d2 instanceof Date);
        assertTrue(d3 instanceof Date);
        // try to construct a Date with invalid String input
        assertThrows(IllegalArgumentException.class, () -> {
            new Date("invalid-string-format");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Date("abc");
        });
        // make sure d2 matches the current calendar date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        assertEquals(year, d2.getYear());
        assertEquals(month, d2.getMonth());
        assertEquals(day, d2.getDay());
    }

    /**
     * Tests the setter, getter, and the compareTo methods in the Date class
     */
    @Test
    public void testSettersAndCompare(){
        Date d1 = new Date(1,2,2013);
        Date d2 = new Date(20, 5, 2000);
        assertEquals(5, d2.getMonth());
        // set a new month for d2
        d2.setMonth(d1.getMonth());
        assertEquals(2, d2.getMonth());
        assertEquals(d1.getMonth(), d2.getMonth());
        // d2 > d1, because 2013 > 2000
        assertTrue(d1.compareTo(d2) > 0);
        // compare two dates with same year and month
        Date d3 = new Date(30, 6, 2023);
        Date d4 = new Date(11, 6, 2023);
        assertEquals(d3.getYear(), d4.getYear());
        assertEquals(d3.getMonth(), d4.getMonth());
        assertTrue(d3.getDay() > d4.getDay());
        assertTrue(d3.compareTo(d4) > 0);
    }

    @Test
    public void testGetString(){
        Date d1 = new Date(1,4,2019);
        assertEquals("2019-4-1", d1.getString());
        d1.setMonth(12);
        assertEquals("2019-12-1", d1.getString());
    }

}
