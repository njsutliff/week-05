package nik.data;

import nik.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHostId(String iD);
    }
