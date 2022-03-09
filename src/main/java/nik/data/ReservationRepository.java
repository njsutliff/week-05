package nik.data;

import com.google.common.collect.ImmutableMap;
import nik.domain.Result;
import nik.models.Host;
import nik.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHostId(String iD);
    List<Reservation> getFutureReservations(Host h);
    Host getHostFromList (List<Reservation> reservationList, Host host);
    Result<Reservation> add(Reservation r);
    }
