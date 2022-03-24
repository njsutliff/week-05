package nik.data;

import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
@Repository

public class ReservationFileRepository implements ReservationRepository {
    private final String directory;
    private static final String HEADER = "id,start_date,end_date,guest_id,total";
@Autowired
    public ReservationFileRepository(@Value("./data/reservations") String directory) {
        this.directory = directory;
    }

    private String getFilePath(String id) {
        return Paths.get(directory, id + ".csv").toString();
    }

    /**
     * @param iD host Id to find List from
     * @return A list of Reservations that correspond to Id.
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
        if (h != null) {
            List<Reservation> all = findByHostId(h.getiD());
            return all.stream().filter(reservation -> reservation.getStartDate().isAfter(LocalDate.now())).toList();
        } else {
            return new ArrayList<>(0);
        }
    }

    /**
     *
     * @param hostId host Id to create Reservation for
     * @param r the Reservation to create
     * @return Reservation created to ReservationService
     * @throws DataException
     */
    @Override
    public Reservation createReservation(String hostId, Reservation r) throws DataException {

        List<Reservation> all = findByHostId(hostId);
        all.add(r);
        int id = all.indexOf(r);
        r.setId(String.valueOf(id + 1));
        writeAll(all, hostId);
        return r;
    }

    /**
     * Edits a Reservation r for Host h
     * @param h Host
     * @param r Reservation
     * @return the edited Reservation
     * @throws DataException
     */
    public Reservation editReservation(Host h, Reservation r) throws DataException {
        List<Reservation> all = findByHostId(h.getiD());
        all.set(Integer.parseInt(r.getId()) - 1, r);
        writeAll(all, h.getiD());
        return r;
    }


    /**
     * Cancels a Reservation r for Host h
     * @param h Host to cancel reservation for
     * @param r Reservation to cancel
     * @return r
     * @throws DataException
     */
    public Reservation cancelReservation(Host h, Reservation r) throws DataException {
        List<Reservation> all = findByHostId(h.getiD());
        for (int i = 0; i < all.size(); i++){
            if(all.get(i).getId().equals(r.getId())){
                all.remove(i);
                writeAll(all, h.getiD());
                return r;
            }
        }
        return r;
    }

    /**
     * Writes data to file
     * @param reservationList the list to write to
     * @param Id the Id for Host to write file for
     * @throws DataException
     */
    private void writeAll(List<Reservation> reservationList, String Id) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(Id))) {

            writer.println(HEADER);

            for (Reservation reservation : reservationList) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    /**
     * Serializes a Reservation to a String. Used for writing to file.
     * @param result Reservation to write
     * @return String of the serialized Reservation.
     */
    private String serialize(Reservation result) {
        return String.format("%s,%s,%s,%s,%s",
                result.getId(),
                result.getStartDate(),
                result.getEndDate(),
                result.getGuest().getGuestId(),
                result.getTotal());
    }

    /**
     * Deserializes a line of text file to a Reservation
     * @param fields the fields of the Reservation to be returned
     * @return Reservation that was deserialized.
     */
    private Reservation deserialize(String[] fields) {
        Reservation result = new Reservation();
        result.setId(fields[0]);
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));
        Guest g = new Guest();
        g.setGuestId(fields[3]);
        result.setGuest(g);
        result.setTotal(BigDecimal.valueOf(Double.parseDouble(fields[4])));
        return result;
    }

}


