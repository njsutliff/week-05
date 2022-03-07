package nik.data;

import nik.models.Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;


public class ReservationRepository {
    private final String directory;
    private static final String HEADER = "id,first_name,last_name,state";

    public ReservationRepository(String directory) {
        this.directory = directory;
    }

    private String getFilePath(LocalDate date) {
        return Paths.get(directory, Id + ".csv").toString();
    }

    public ArrayList<Reservation> find() {

        ArrayList<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(directory))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 4) {
                    reservations.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return reservations;
    }

    private Reservation deserialize(String[] fields) {
            Reservation result = new Reservation();
            result.setId(fields[0]);
            result.setStartDate(LocalDate.parse(fields[1]));
       // result.setGuest(fields[3]);
        result.setTotal(BigDecimal.valueOf(Long.parseLong(fields[4])));
        result.setEndDate(LocalDate.parse(fields[2]));
    return result;
    }
}


