package nik.domain;

import nik.data.GuestFileRepository;
import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;

import java.util.ArrayList;
import java.util.List;

public class GuestService {
    private final GuestFileRepository guestRepository;

    public GuestService(GuestFileRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest getGuestByLastName(String lastName) {
        return guestRepository.getGuestByLastName(lastName);
    }

    public List<Guest> getGuestsForHostFromReservation(Host h, Reservation r){
        return  guestRepository.getGuestsForHostFromReservation(h, r);
    }

    public ArrayList<Guest> findAll() {
        return guestRepository.findAll();
    }
}
