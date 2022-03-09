package nik.data;

import nik.models.Host;
import nik.models.Reservation;

import java.util.List;

public interface HostRepository {
    String getIdFromEmail (String email);
    public Reservation findReservationByEmail(String email);
    List<Host> getAllHosts();
}
