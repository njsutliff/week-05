package nik.data;

import nik.models.Host;
import nik.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHostId(String iD);
    List<Reservation> getFutureReservations(Host h);
    Host getHostFromList (List<Reservation> reservationList, Host host);
    Reservation add(Reservation r) throws DataException;
    }
