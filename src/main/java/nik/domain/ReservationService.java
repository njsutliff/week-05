package nik.domain;

import nik.data.DataException;
import nik.data.GuestFileRepository;
import nik.data.HostFileRepository;
import nik.data.ReservationFileRepository;
import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private final ReservationFileRepository reservationRepository;
    private final GuestFileRepository guestRepository;
    private final HostFileRepository hostRepository;

    public ReservationService(ReservationFileRepository reservationRepository,
                              GuestFileRepository guestRepository,
                              HostFileRepository hostRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    public List<Reservation> findByHostId(String Id) {
        List<Reservation> resultList = reservationRepository.findByHostId(Id);
        for (Reservation r : resultList) {
            String guestId = r.getGuest().getGuestId();
            r.setGuest(guestRepository.getGuestFromGuestId(guestId));
            r.setHost(hostRepository.getHostFromId(Id));
        }

        return resultList;
    }

    public List<Reservation> getFutureReservations(Host h) {
        return reservationRepository.getFutureReservations(h);
    }

    public Result<Reservation> createReservation(String iD, Reservation r) throws DataException {
        Result<Reservation> result = validate(iD, r);
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(reservationRepository.createReservation(iD, r));

        return result;
    }

    public Result<Reservation> editReservation(Host h, Reservation r) throws DataException {
        Result<Reservation> result = validate(h.getiD(), r);
        if (!result.isSuccess()) {
            return result;
        }
        if (reservationRepository.findByHostId(h.getiD()).contains(r)) {
            result.addErrorMessage("Cannot edit reservation");
        }
        result.setPayload(reservationRepository.editReservation(h, r));
        return result;
    }

    public boolean cancelReservation(Host h, Reservation r) throws DataException {
        if(findByHostId(h.getiD()).size()==0){
            return  false;
        }
        Result<Reservation> reservationResult = new Result<>();
        validate(h.getiD(), r);
        if (!reservationResult.isSuccess()){
            return false;
        }else {
            return reservationRepository.cancelReservation(h, r);
        }
    }

    public List<Reservation> getReservationsForGuestAndHost(Host h, Guest guest) throws DataException {
        List<Reservation> reservationsForHost = reservationRepository.findByHostId(h.getiD());
        List<Reservation> reservationsForGuest = new ArrayList<>();

        for (Reservation reservation : reservationsForHost) {
            String guestId = reservation.getGuest().getGuestId();
            reservation.setGuest(guestRepository.getGuestFromGuestId(guestId));
            if (reservation.getGuest().getGuestId().equals(guest.getGuestId())) {
                reservationsForGuest.add(reservation);
            }
        }
        return reservationsForGuest;
    }

    public Result<Reservation> validate(String iD, Reservation r) {
        Result<Reservation> result = new Result<>();
        validateNulls(r);

        if (r == null) {
            result.addErrorMessage("Reservation is null. ");
            return result;
        }
        validateFields(iD, result, r);
        return result;
    }

    private void validateNulls(Reservation r) {
        Result<Reservation> result = new Result<>();
        if (r == null) {
            result.addErrorMessage("Nothing to save. ");
        }
        if (r.getId() == null) {
            result.addErrorMessage("Id is required. ");
        }
        if (r.getStartDate() == null) {
            result.addErrorMessage("Start date is required. ");
        }
        if (r.getEndDate() == null) {
            result.addErrorMessage("End date is required. ");
        }
        // if ((r.getGuestId() < 0) || r.getGuestId() >= 1001) {
        // result.addErrorMessage("Not a current guest. ");
        //}
        if (r.total == null) {
            result.addErrorMessage("Total amount required. ");
        }
    }

    private void validateFields(String iD, Result<Reservation> result, Reservation r) {
        List<Reservation> reservationsWithId = reservationRepository.findByHostId(iD);
        LocalDate startDate = r.getStartDate();
        LocalDate endDate = r.getEndDate();
        if (startDate.isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservations must be made for the future. ");
        }
        if (startDate.isAfter(endDate)) {
            result.addErrorMessage("Reservation start date cannot be after the end date. ");
            return;
        }

        for (Reservation res : reservationsWithId) {
            LocalDate existingStartDate = res.getStartDate();
            LocalDate existingEndDate = res.getEndDate();
            if (startDate.isEqual(existingStartDate)
                    || endDate.isEqual(existingEndDate)) {
                result.addErrorMessage
                        ("Reservation cannot begin or end on same day,host needs time to checkout");
                return;
            }
            if (startDate.isBefore(existingStartDate) && endDate.isAfter(existingStartDate)) {
                result.addErrorMessage("Reservation cannot overlap an existing reservation. ");
                return;
            }

            if (startDate.isAfter(existingStartDate) && startDate.isBefore(existingEndDate)) {
                result.addErrorMessage("Reservation cannot overlap an existing reservation. ");
                return;
            }

        }
    }


}
