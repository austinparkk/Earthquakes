package com.getguru.interview.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDataService {
    private EarthquakeDataService dataService = new EarthquakeDataService();

    @Test
    public void testGetEarthQuakeData() {
        assertEquals(dataService.getEarthquakeData().size(), 50);

    }
}