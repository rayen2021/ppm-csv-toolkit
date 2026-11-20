package h13.csv.departure;

import h13.csv.CSVWriter;
import h13.csv.CSVParser;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class whose methods implement joining schedule and live departure data
 */
public final class DepartureJoiner {

    @DoNotTouch
    private DepartureJoiner() {
    }

    /**
     * Reads scheduled and live departure data from two files, joins them and writes the result to a third file.
     *
     * @param scheduledDeparturesPath the path from which to read scheduled departures
     * @param liveDeparturesPath      the path from which to read live departures
     * @param outputPath              the path to write the joined departures
     * @throws IOException if an I/O error occurs
     */
    public static void processFiles(Path scheduledDeparturesPath, Path liveDeparturesPath, Path outputPath) throws IOException {
        // TODO: H13.2.6

        try(BufferedWriter writer = Files.newBufferedWriter(outputPath);
            BufferedReader reader1 = Files.newBufferedReader(scheduledDeparturesPath);
            BufferedReader reader2 = Files.newBufferedReader(liveDeparturesPath)){
                List<ScheduledDeparture> scheduledDepartures = CSVParser.parse(reader1,ScheduledDeparture::deserialize);
                List<LiveDeparture> liveDepartures = CSVParser.parse(reader2,LiveDeparture::deserialize);
                CSVWriter csvWriter = new CSVWriter(writer);
                csvWriter.writeCSV(joinTables(scheduledDepartures, liveDepartures));



        }

    }

    /**
     * Joins two tables of scheduled and live departures by their IDs.
     * If a scheduled departure is present without a live departure with matching ID,
     * its actual departure time is assumed to be its planned departure time.
     *
     * @param scheduledDepartures the scheduled departure table as a list of rows
     * @param liveDepartures      the live departures table as a list of rows
     * @return the joined table of combined departure information as a list of rows
     */
    public static List<JoinedDeparture> joinTables(List<ScheduledDeparture> scheduledDepartures, List<LiveDeparture> liveDepartures) {
        // TODO: H13.2.3
        List<JoinedDeparture> joinedDepartures = new ArrayList<>();

        for(ScheduledDeparture scheduledDeparture : scheduledDepartures){




            int tripId = scheduledDeparture.tripId();
            String trainLine = scheduledDeparture.trainLine();
            String destination = scheduledDeparture.destination();
            LocalTime scheduledTime = scheduledDeparture.scheduledTime();
            LocalTime liveTime = scheduledTime;
            Duration difference = Duration.between(scheduledTime,liveTime);


            for(LiveDeparture liveDeparture : liveDepartures){
                if(scheduledDeparture.tripId() == liveDeparture.tripId()){

                    liveTime = liveDeparture.actualTime();
                    difference = Duration.between(scheduledTime, liveTime);

                }
            }
            JoinedDeparture joinedDeparture = new JoinedDeparture(tripId,trainLine,destination,scheduledTime,liveTime,difference);
            joinedDepartures.add(joinedDeparture);
        }
        return joinedDepartures;
    }
}
