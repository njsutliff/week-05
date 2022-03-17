package nik.data;

import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepository implements GuestRepository {
    private final String filePath;
    ReservationFileRepository reservationRepository = new ReservationFileRepository("./data/reservations");

    public GuestFileRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * returns all Guests
     *
     * @return list of all Guests in guests.csv
     */
    public ArrayList<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./data/guests.csv"))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    /**
     * Returns a List of Guests for a Host from a Reservation
     * @param h the Host to return a list of Guests for
     * @param r the Reservation used to get Guests
     * @return List of guests.
     */
    @Override
    public List<Guest> getGuestsForHostFromReservation(Host h, Reservation r) {
        List<Reservation> reservation = reservationRepository.findByHostId(h.getiD());
        List<Guest> result = new ArrayList<>();
        for (Reservation res : reservation) {
            Guest g = getGuestFromGuestId(String.valueOf(res.guestId));
            g.setGuestId(String.valueOf(res.guestId));
            result.add(g);
        }
        return result;
    }

    /**
     * Given an Id returns a Guest.
     * @param Id a host Id
     * @return Guest found or null if not
     */
    public Guest getGuestFromGuestId(String Id) {
        return findAll().stream()
                .filter(guest -> guest.getGuestId().equalsIgnoreCase(Id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Given a Guest's last name, return the guest.
     * @param lastName e.g "Lomas"
     * @return Guest found or null if not
     */
    @Override
    public Guest getGuestByLastName(String lastName) {
        return findAll().stream()
                .filter(i -> i.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Deserializes a Guest
     * @param fields Guest data fields
     * @return a Guest
     */
    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setGuestId(fields[0]);
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail(fields[3]);
        result.setPhone(fields[4]);
        result.setState(fields[5]);
        return result;
    }


}
