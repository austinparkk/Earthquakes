package com.getguru.interview.resources;

import com.getguru.interview.db.EarthquakeDataService;
import com.getguru.interview.db.RawEarthquakeData;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.text.SimpleDateFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
public class DataResource {

  @SuppressWarnings("unused")
  private EarthquakeDataService _dataService = new EarthquakeDataService();

  /*
   * If given a filter string, filters the list of rawEarthquakeData, converts
   * rawEarthquakeData into plain Earthquake objects, then sorts the list of
   * Earthquakes by descending magnitude and ascending time
   * 
   * @param filter an optional filter string
   * 
   * @return a filtered, sorted, list of Earthquakes
   */
  @GET
  public List<Earthquake> getEarthquakes(@QueryParam("filter") Optional<String> filter) {
    List<RawEarthquakeData> rawEarthquakeList = _dataService.getEarthquakeData();
    List<RawEarthquakeData> filteredRawList = new ArrayList<>();
    List<Earthquake> result = new ArrayList<Earthquake>();
    // if optional filter is given, filter the list
    if (filter.isPresent()) {
      for (RawEarthquakeData e : rawEarthquakeList) {
        if (e.getPlace().toLowerCase().contains(filter.get().toLowerCase())) {
          filteredRawList.add(e);
        }
      }
    } else {
      filteredRawList = rawEarthquakeList;
    }
    // convert rawEarthquakeData to Earthquake
    for (RawEarthquakeData e : filteredRawList) {
      result.add(rawToEarthquake(e));
    }

    // sort list by descending magnitude and ascending time
    Comparator<Earthquake> magComparator = Comparator.comparing(Earthquake::getMagnitude).reversed();
    Collections.sort(result, magComparator.thenComparing(Earthquake::getTime));

    return result;
  }

  // ---------- HELPER FUNCTIONS -----------------

  /*
   * convert single RawEarthquakeData to Earthquake
   * 
   * @param rawEarthquake a single rawEarthquakeData object
   * 
   * @return a single Earthquake object
   */
  public Earthquake rawToEarthquake(RawEarthquakeData rawEarthquake) {
    Earthquake earthquake = new Earthquake();
    try {
      earthquake.setTime(stringToDate(rawEarthquake.getTime()));
    } catch (Exception e) {
      System.out.println("ERROR: the Earthquake with id: " + rawEarthquake.getId()
          + " has invalid format in its time field. Its time will be set to the current time");
      earthquake.setTime(new Date());
    }

    earthquake.setLatitude(rawEarthquake.getLatitude());
    earthquake.setLongitude(rawEarthquake.getLatitude());
    earthquake.setDepth(rawEarthquake.getDepth());
    earthquake.setMagnitude(rawEarthquake.getMagnitude());
    earthquake.setMagType(rawEarthquake.getMagType());
    earthquake.setId(rawEarthquake.getId());
    earthquake.setPlace(rawEarthquake.getPlace());
    earthquake.setType(rawEarthquake.getType());

    return earthquake;
  }

  /*
   * Converts a String to type Date while rounding up the seconds to whole
   * integers
   * 
   * @param dateString the String that needs to be converted to type Date
   * 
   * @return Date of earthquake
   * 
   * @throws ParseException, NumberFormatException if time is in invalid format
   */
  public Date stringToDate(String dateString) throws ParseException, NumberFormatException {
    dateString = dateString.substring(0, dateString.length() - 1);
    String[] dateAndTime = dateString.split("T");
    String[] time = dateAndTime[1].split(":");
    String hour = time[0];
    String minute = time[1];
    int secondsRounded = (int) Math.round(Double.valueOf(time[2]));
    String formattedDate = dateAndTime[0] + " " + hour + ":" + minute + ":" + Integer.toString(secondsRounded);
    Date result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDate);
    return result;
  }

}