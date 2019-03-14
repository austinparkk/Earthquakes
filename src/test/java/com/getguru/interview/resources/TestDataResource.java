package com.getguru.interview.resources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public class TestDataResource {

    DataResource dr = new DataResource();

    @Test
    public void testStringToDate() {
        try {
            assertTrue(dr.stringToDate("2017-09-06T23:07:59.630Z").toString().contains("Sep 06 23:08:00"));
            assertTrue(dr.stringToDate("2017-09-05T23:00:17.250Z").toString().contains("Sep 05 23:00:17"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testExceptions() {
        ParseException thrownParseException = assertThrows(ParseException.class,
                () -> dr.stringToDate("2017-09-W6T23:07:59.630Z"),
                "Expected stringToDate to throw ParseException but didnt");
        assertTrue(thrownParseException.getMessage().contains("Unparseable date"));

        NumberFormatException thrownNumberException = assertThrows(NumberFormatException.class,
                () -> dr.stringToDate("2017-09-16T23:07:5Y.630Z"),
                "Expected stringToDate to throw NumberFormatException but didnt");
        assertTrue(thrownNumberException.getMessage().contains("5Y.630"));
    }

    @Test
    public void testGetEarthquakes() {
        List<Earthquake> quakes = dr.getEarthquakes(Optional.of("mexico"));
        assertEquals(quakes.size(), 2);
        assertTrue(quakes.get(0).getMagnitude() > quakes.get(1).getMagnitude());

        quakes = dr.getEarthquakes(Optional.empty());
        assertEquals(quakes.size(), 50);
        // test sort descending magnitude
        for (int i = 0; i < quakes.size() - 1; i++) {
            assertTrue(quakes.get(i).getMagnitude() >= quakes.get(i + 1).getMagnitude());
        }
        // test sort ascending time
        for (int i = 0; i < quakes.size() - 1; i++) {
            if (quakes.get(i).getMagnitude() == quakes.get(i + 1).getMagnitude()) {
                assertTrue(quakes.get(i).getTime().before(quakes.get(i + 1).getTime()));
            }
        }

        quakes = dr.getEarthquakes(Optional.of("eqiwjfqwe"));
        assertEquals(quakes.size(), 0);
    }
}