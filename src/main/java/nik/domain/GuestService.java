package nik.domain;

import nik.data.GuestFileRepository;
import nik.models.Guest;

import java.util.ArrayList;

public class GuestService {
    private final GuestFileRepository guestRepository;

    public GuestService(GuestFileRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest getGuestByLastName(String lastName) {
        return guestRepository.getGuestByLastName(lastName);
    }

    public ArrayList<Guest> findAll() {
        return guestRepository.findAll();
    }
}
