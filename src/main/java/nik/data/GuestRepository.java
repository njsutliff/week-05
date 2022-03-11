package nik.data;
import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;

import java.util.ArrayList;
import java.util.List;

public interface GuestRepository {
     ArrayList<Guest> findAll();
     List<Guest> getGuestsForHostFromReservation(Host h, Reservation r);
     Guest getGuestByLastName(String lastName);

}
