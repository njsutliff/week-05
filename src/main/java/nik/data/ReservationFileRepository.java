package nik.data;

import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;


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
        if (h!=null) {
            List<Reservation> all = findByHostId(h.getiD());
            return all.stream().filter(reservation -> reservation.getStartDate().isAfter(LocalDate.now())).toList();
        }else {
            return new ArrayList<>(0);
        }
    }

    @Override
    public Reservation createReservation(Host h, Reservation r) throws DataException {

        List<Reservation> all = findByHostId(h.getiD());
        all.add(r);
        int id = all.indexOf(r);
        r.setGuestId(id-1);
        writeAll(all, r.host.getiD());
        return r;
    }


    private void writeAll(List<Reservation> reservationList, String iD) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(iD))) {

            writer.println(HEADER);

            for (Reservation reservation : reservationList) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Reservation result) {
        //id,start_date,end_date,guest_id,total
        return String.format("%s,%s,%s,%s%s",
                result.getId(),
                result.getStartDate(),
                result.getEndDate(),
                result.getGuestId(),
                result.getTotal());
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


