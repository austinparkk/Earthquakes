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

  @GET
  public List<Earthquake> getEarthquakes(@QueryParam("filter") Optional<String> filter) {
    List<RawEarthquakeData> rawList = _dataService.getEarthquakeData();
    List<RawEarthquakeData> filteredList = new ArrayList<>();
    List<Earthquake> result = new ArrayList<Earthquake>();
    // filter list if argument exists
    if (filter.isPresent()) {
      for (RawEarthquakeData e : rawList) {
        if (e.getPlace().toLowerCase().contains(filter.get().toLowerCase())) {
          filteredList.add(e);
        }
      }
    } else {
      filteredList = rawList;
    }
    // convert rawEarthquakeData to Earthquake..
    for (RawEarthquakeData e : filteredList) {
      result.add(rawToEarthquake(e));
    }

    // sort list by descending magnitude and ascending time
    Comparator<Earthquake> magComparator = Comparator.comparing(Earthquake::getMagnitude).reversed();
    Collections.sort(result, magComparator.thenComparing(Earthquake::getTime));

    return result;
  }

  // ---------- HELPER FUNCTIONS -----------------

  public Earthquake rawToEarthquake(RawEarthquakeData raw) {
    Earthquake earthquake = new Earthquake();
    try {
      earthquake.setTime(stringToDate(raw.getTime()));
    } catch (Exception e) {
      System.out.println(e);
    }

    earthquake.setLatitude(raw.getLatitude());
    earthquake.setLongitude(raw.getLatitude());
    earthquake.setDepth(raw.getDepth());
    earthquake.setMagnitude(raw.getMagnitude());
    earthquake.setMagType(raw.getMagType());
    earthquake.setId(raw.getId());
    earthquake.setPlace(raw.getPlace());
    earthquake.setType(raw.getType());

    return earthquake;
  }

  // Converts String to type Date while rounding up the seconds to whole integers.

  public Date stringToDate(String date) throws NumberFormatException, ParseException {
    date = date.substring(0, date.length() - 1);
    String[] dateAndTime = date.split("T");
    String[] time = dateAndTime[1].split(":");
    String hour = time[0];
    String minute = time[1];
    int seconds = (int) Math.round(Double.valueOf(time[2]));
    String formattedDate = dateAndTime[0] + " " + hour + ":" + minute + ":" + Integer.toString(seconds);
    Date result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDate);
    return result;
  }

}