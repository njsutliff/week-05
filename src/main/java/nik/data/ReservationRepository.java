package nik.data;

import nik.models.Reservation;

import java.util.ArrayList;

public interface ReservationRepository {
    public ArrayList<Reservation> findByHostId(String iD);
    }
