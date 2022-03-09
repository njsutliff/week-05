package nik.data;

import com.google.common.collect.ImmutableMap;
import nik.models.Host;
import nik.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHostId(String iD);
    ImmutableMap<Reservation, Host> associateReservationWithHost();
    Host getHostFromList (List<Reservation> reservationList, Host host);
    }
