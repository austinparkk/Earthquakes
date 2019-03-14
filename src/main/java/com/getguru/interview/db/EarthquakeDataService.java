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

  /*
   * Reads the raw data from the provided ​earthquake.json file
   * 
   * @return a list of ​RawEarthquakeData​ objects
   */
  public List<RawEarthquakeData> getEarthquakeData() {
    List<RawEarthquakeData> result = new ArrayList<RawEarthquakeData>();
    InputStream is = getClass().getResourceAsStream("earthquake.json");
    JsonReader reader = Json.createReader(new InputStreamReader(is));
    try {
      JsonArray jsonEarthquakeArray = reader.readArray();
      result = jsonToRawEarthquakeArray(jsonEarthquakeArray);
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      reader.close();
    }

    return result;
  }

  /*
   * Maps each element in the input JsonArray to a RawEarthquakeData object and
   * returns a list of all resulting objects
   * 
   * @param jsonEarthquakeArray the json array to convert
   * 
   * @return the resulting list of RawEarthquakeData
   */
  public List<RawEarthquakeData> jsonToRawEarthquakeArray(JsonArray jsonEarthquakeArray) {
    List<RawEarthquakeData> rawEarthquakes = new ArrayList<RawEarthquakeData>();

    for (JsonValue jsonEarthquake : jsonEarthquakeArray) {
      String earthquakeString = jsonEarthquake.toString();
      try {
        RawEarthquakeData rawEarthquake = _objectMapper.readValue(earthquakeString, RawEarthquakeData.class);
        rawEarthquakes.add(rawEarthquake);
      } catch (Exception e) {
        System.out.println(e);
      }

    }
    return rawEarthquakes;
  }

}