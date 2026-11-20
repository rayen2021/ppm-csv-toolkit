package h13.csv;

import h13.csv.departure.JoinedDeparture;
import org.tudalgo.algoutils.student.Student;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to export given row-objects in CSV format.
 * Data is written to a {@link BufferedWriter} which is set once and cannot be changed.
 */
public class CSVWriter implements AutoCloseable {

    private BufferedWriter writer;

    /**
     * Creates a new {@link CSVWriter} that will write CSV data as text into the given writer.
     *
     * @param writer the {@link BufferedWriter} to write text data to
     */
    @DoNotTouch
    public CSVWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    /**
     * Exports the table as CSV data into the writer.
     *
     * @param rows the table as row objects
     * @throws IOException if an I/O error occurs
     */
    public void writeCSV(List<JoinedDeparture> rows) throws IOException {
        // TODO: H13.2.5
        for(JoinedDeparture departure : rows) {
            writeRow(departure);
            writer.newLine();
        }
        writer.flush();
    }

    /**
     * Writes a single row containing serialized data from one row object.
     *
     * @param row the row object to serialize and write
     * @throws IOException if an I/O error occurs
     */
    public void writeRow(JoinedDeparture row) throws IOException {
        // TODO: H13.2.5
        List<String> list = row.serialize();
        String s=list.get(0);
        for(int i=1;i<list.size();i++){
            s+=","+list.get(i);
        }
        writer.write(s);
    }

    @DoNotTouch
    @Override
    public void close() throws IOException {
        writer.close();
    }
}
