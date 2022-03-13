package nik.domain;

import nik.data.DataException;
import nik.data.ReservationFileRepository;
import nik.models.Guest;
import nik.models.Host;
import nik.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {
    private final ReservationFileRepository reservationRepository;

    public ReservationService(ReservationFileRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findByHostId(String Id) {
        return reservationRepository.findByHostId(Id);
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
    //TODO validation not working on dates
    public Result<Reservation> validate(String iD, Reservation r) {
        Result<Reservation> result = new Result<>();
        validateNulls(r);

        if (r == null) {
            result.addErrorMessage("Reservation is null. ");
            return result;
        }
        validateFields(iD, result, r);
        return  result;
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
        if ((r.getGuestId() < 0) || r.getGuestId() >= 1001) {
            result.addErrorMessage("Not a current guest. ");
        }
        if (r.total == null) {
            result.addErrorMessage("Total amount required. ");
        }
    }

    private void validateFields(String iD, Result<Reservation> result, Reservation r) {
        List<Reservation> reservationsWithId = reservationRepository.findByHostId(iD);
        if (r.getStartDate().isAfter(r.getEndDate())) {
            result.addErrorMessage("Reservation start date cannot be after the end date. ");
        }
        for (Reservation res : reservationsWithId) {
            if (r.getStartDate().isEqual(res.getStartDate())
                    || r.getEndDate().isEqual(res.getEndDate())) {
                result.addErrorMessage
                        ("Reservation cannot begin or end on same day,host needs time to checkout");
            }

            if (res.getStartDate().isAfter(r.getStartDate())
                    && res.getEndDate().isBefore(r.getEndDate())) {
                result.addErrorMessage("Reservation starts before and is during a reservation. ");
            }
            if (r.getStartDate().isBefore(res.getEndDate())
                    && r.getEndDate().isAfter(res.getEndDate())) {
                result.addErrorMessage("Reservation ends after and is during a reservation. ");
            }
        }
        if (r.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation must be for the future. ");
        }
    }
}
