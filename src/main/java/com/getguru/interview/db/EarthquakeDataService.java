package com.getguru.interview.db;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EarthquakeDataService {

  @SuppressWarnings("unused")
  private ObjectMapper _objectMapper = new ObjectMapper();

  public List<RawEarthquakeData> getEarthquakeData() {
    InputStream is = getClass().getResourceAsStream("earthquake.json");
    JsonReader reader = Json.createReader(new InputStreamReader(is));
    JsonArray earthquakeArray = reader.readArray();
    reader.close();

    return toRawEarthquakeArray(earthquakeArray);
  }

  // Takes in a JsonArray and maps each element to RawEarthquakeData object.
  // returns the resulting list of RawEarthquakeData.
  public List<RawEarthquakeData> toRawEarthquakeArray(JsonArray array) {
    List<RawEarthquakeData> earthquakes = new ArrayList<RawEarthquakeData>();

    for (JsonValue v : array) {
      String earthquakeString = v.toString();
      try {
        RawEarthquakeData earthquake = _objectMapper.readValue(earthquakeString, RawEarthquakeData.class);
        earthquakes.add(earthquake);
      } catch (Exception e) {
        System.out.println(e);
      }

    }
    return earthquakes;
  }

}