package nik.domain;

import nik.data.GuestFileRepository;
import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class GuestService {
    private final GuestFileRepository guestRepository;

    public GuestService(GuestFileRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Result<Guest> getGuestByLastName(String lastName) {
        Result<Guest> result = validateLastName(lastName);
        if (!result.isSuccess()){
            return result;
        }
        result.setPayload(guestRepository.getGuestByLastName(lastName));
        return result;

    }

    public List<Guest> getGuestsForHostFromReservation(Host h, Reservation r) {
        return guestRepository.getGuestsForHostFromReservation(h, r);
    }

    public ArrayList<Guest> findAll() {
        return guestRepository.findAll();
    }
    public Result<Guest> validateLastName(String lastName){
        Result<Guest> result = new Result<>();
        if(lastName == null){
            result.addErrorMessage("Invalid guest, no guest found.");
        }
        if (guestRepository.getGuestByLastName(lastName) == null) {
            result.addErrorMessage("No guest found.");
        }
        return result;
    }
}
