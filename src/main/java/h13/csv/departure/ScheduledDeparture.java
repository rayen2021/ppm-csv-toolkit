package h13.csv.departure;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import h13.csv.CSVFormatException;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

/**
 * A departure present in a schedule without real-world information of actual departure times.
 *
 * @param tripId        a unique ID identifying this departure
 * @param trainLine     the train's line/display name on the departure display
 * @param destination   the train's destination
 * @param scheduledTime the train's scheduled (planned) departure time
 */
public record ScheduledDeparture(int tripId, String trainLine, String destination, LocalTime scheduledTime) {

    @DoNotTouch
    public ScheduledDeparture {
        if (trainLine == null) {
            throw new IllegalArgumentException("trainLine must not be null");
        }
        if (destination == null) {
            throw new IllegalArgumentException("destination must not be null");
        }
        if (scheduledTime == null) {
            throw new IllegalArgumentException("scheduledTime must not be null");
        }
    }

    /**
     * Deserializes a row of strings into a {@link ScheduledDeparture} object.
     *
     * @param row a list of length 4 containing ID, train line, destination, scheduled departure time as strings
     * @return a deserialized {@link ScheduledDeparture} object.
     */
    public static ScheduledDeparture deserialize(List<String> row) {
        // TODO: H13.2.2
        if(row.size()!=4) {
            throw new CSVFormatException("Invalid row count");
        }
        int tripId;
        try{
             tripId = Integer.parseInt(row.get(0));
        }catch(NumberFormatException e){
            throw new CSVFormatException("Invalid trip id");
        }

        String trainLine = row.get(1);
        String destination = row.get(2);
        LocalTime scheduledTime;
        try {
            scheduledTime = LocalTime.parse(row.get(3));
        }catch(DateTimeParseException e){
            throw new CSVFormatException("Invalid scheduled time");
        }
        return new ScheduledDeparture(tripId, trainLine, destination, scheduledTime);
    }
}
