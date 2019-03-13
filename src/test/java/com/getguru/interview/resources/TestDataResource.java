package com.getguru.interview.resources;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

public class TestDataResource {

    DataResource dr = new DataResource();

    @Test
    public void testStringToDate() {
        try {
            System.out.println(dr.stringToDate("2017-09-06T23:07:59.630Z"));
            System.out.println(dr.stringToDate("2017-09-05T23:00:17.250Z"));
            System.out.println(dr.stringToDate("2017-09-05T00:05:13.040Z"));
        } catch (Exception e) {
            System.out.println("exception caught");
        }
    }

    public void testGetEarthquakes() {
        List<Earthquake> quakes = dr.getEarthquakes(Optional.of("mexico"));
        for (Earthquake e : quakes) {
            System.out.println(e.getMagnitude() + " on " + e.getTime().toString());
        }

        System.out.println(quakes.size());
    }
}