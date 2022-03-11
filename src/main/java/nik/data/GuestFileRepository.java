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
    private  final String filePath;
    ReservationFileRepository reservationRepository = new ReservationFileRepository("./data/reservations");
    public GuestFileRepository(String filePath){ this.filePath = filePath;}
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
    //guest_id,first_name,last_name,email,phone,state

    @Override
    public List<Guest> getGuestsForHostFromReservation(Host h, Reservation r) {
        List<Reservation> reservation = reservationRepository.findByHostId(h.getiD());
        List<Guest> result = new ArrayList<>();
        for (Reservation res : reservation){
            Guest g = getGuestFromGuestId(String.valueOf(res.guestId));
            g.setGuestId(String.valueOf(res.guestId));
            result.add(g);
        }
        return result;
    }
    public Guest getGuestFromGuestId(String iD){
        return findAll().stream()
                .filter(guest -> guest.getGuestId().equalsIgnoreCase(iD))
                .findFirst()
                .orElse(null);
    }
    @Override
    public Guest getGuestByLastName(String lastName) {
        return findAll().stream()
                .filter(i -> i.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        //guest_id,first_name,last_name,email,phone,state
        result.setGuestId(fields[0]);
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail(fields[3]);
        result.setPhone(fields[4]);
        result.setState(fields[5]);
        return result;
    }


}
