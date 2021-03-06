package nik.data;

import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHostId(String iD);
    List<Reservation> getFutureReservations(Host h);
    Reservation createReservation(String iD, Reservation r) throws DataException;
    Reservation editReservation(Host h, Reservation r) throws DataException;
}
