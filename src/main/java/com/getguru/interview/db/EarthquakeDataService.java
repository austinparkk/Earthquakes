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
  // @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property =
  // “@class”)

  public List<RawEarthquakeData> getEarthquakeData() {
    /*
     * XXX HINT: data can be loaded from a local resource called `seismic.json` into
     * an InputStream with the following code InputStream is =
     * getClass().getResourceAsStream("earthquake.json");
     */

    /*
     * XXX HINT: see
     * http://www.studytrails.com/java/json/java-jackson-serialization-list/ for
     * information on how you can use ObjectMapper to deserialize the list of data
     * from the file
     */
    InputStream is = getClass().getResourceAsStream("earthquake.json");
    JsonReader reader = Json.createReader(new InputStreamReader(is));
    JsonArray earthquakeArray = reader.readArray();
    reader.close();

    return toRawEarthquakeArray(earthquakeArray);
  }

  // Takes in a JsonArray and maps each json in array to RawEarthquakeData object.
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