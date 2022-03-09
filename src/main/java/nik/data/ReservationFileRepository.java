package nik.data;

import com.google.common.collect.ImmutableMap;
import nik.models.Host;
import nik.models.Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReservationFileRepository implements  ReservationRepository {
    private final String directory;
    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private  HashMap<Reservation, Host> map = new HashMap<Reservation, Host>();
    //HostRepository hostRepository = new HostFileRepository("./data/hosts.csv");
    public ReservationFileRepository(String directory) {
        this.directory = directory;
    }

    private String getFilePath(String id) {
        return Paths.get(directory, id + ".csv").toString();
    }

    public List<Reservation> findAll() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(directory)))) {

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

    /**
     * @param iD
     * @return A list of Reservations that correspond to an Id.
     */
    public List<Reservation> findByHostId(String iD) {

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

    @Override
    public List<Reservation> getFutureReservations(Host h) {
        List<Reservation> all = findByHostId(h.getiD());
       return all.stream().filter(reservation -> reservation.getStartDate().isAfter(LocalDate.now())).toList();
    }


    /**
     * @param reservationList returned from GetByID
     * @param host
     * @return
     */
    @Override
    public Host getHostFromList(List<Reservation> reservationList, Host host) {
        for (Reservation r : reservationList) {
            if (r.host == host) {
            }
            return null;
        }
        return host;
    }
        private Reservation deserialize (String[]fields){
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


