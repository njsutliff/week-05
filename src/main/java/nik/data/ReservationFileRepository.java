package nik.data;

import nik.models.Host;
import nik.models.Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;


public class ReservationFileRepository implements  ReservationRepository{
    private final String directory;
    private static final String HEADER = "id,start_date,end_date,guest_id,total";

    public ReservationFileRepository(String directory) {
        this.directory = directory;
    }

    private String getFilePath(String id) {
        return Paths.get(directory + id + ".csv").toString();
    }

    public ArrayList<Reservation> findByHostId(String iD) {

        ArrayList<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(iD)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    reservations.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return reservations;
    }

    private Reservation deserialize(String[] fields) {
        //id,start_date,end_date,guest_id,total

        Reservation result = new Reservation();
        result.setId(fields[0]);
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));
        result.setGuestId(Integer.parseInt(fields[3]));
        result.setTotal(BigDecimal.valueOf(Long.parseLong(fields[4])));
    return result;
    }
}


